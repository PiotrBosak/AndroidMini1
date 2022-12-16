package pjatk.smb

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import pjatk.smb.databinding.ActivityStoresBinding
import pjatk.smb.db.Store

class StoreAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoresBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var geoClient: GeofencingClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        geoClient = LocationServices.getGeofencingClient(this)
        Log.d("Store", "Entered")
        binding = ActivityStoresBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = getPreferences(Context.MODE_PRIVATE)
        editor = sp.edit()

        val storeModel = StoreViewModel(application)
        val adapter = StoreAdapter(storeModel)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.addStore.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ), 1
                )
            }


            fusedLocationClient.lastLocation
                .addOnSuccessListener {
                    val name = binding.storeName.text.toString()
                    val description = binding.storeDescription.text.toString()
                    val radius = binding.storeRadius.text.toString().toDouble()
                    val latitude = it.latitude
                    val longitude = it.longitude
                    val store = Store(
                        name = name,
                        description = description,
                        radius = radius,
                        latitude = latitude,
                        longitude = longitude
                    )

                    adapter.add(store)
                    addGeofence(it, store)
                }
        }
        binding.rv1.layoutManager = LinearLayoutManager(this)
        binding.rv1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.rv1.adapter = adapter
        storeModel.stores.observe(this, Observer {
            it.let {
                adapter.setStores(it)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun addGeofence(loc: Location, store: Store) {
        val geofence = Geofence.Builder()
            .setRequestId(store.name)
            .setCircularRegion(loc.latitude, loc.longitude, store.radius.toFloat())
            .setExpirationDuration(30 * 60 * 1000)
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT
            )
            .build()
        val request = GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
        val geoPi = PendingIntent.getBroadcast(
            this,
            1,
            Intent(this, GeoReceiver::class.java).apply {
                putExtra("storeName", store.name)
            },
            PendingIntent.FLAG_MUTABLE
        )

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ), 1
            )
        }
        geoClient.addGeofences(request, geoPi)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Geofence with id: ${geofence.requestId} was added!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i("geofence123", "Geofence with id: ${geofence.requestId} was added!")
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Geofence error!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("geofence123", "Geofence with id: ${geofence.requestId} was NOT added!")
                Log.e("geofence123", it.message.toString())
            }
    }
}