package pjatk.smb

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import pjatk.smb.databinding.ActivityMapBinding
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import pjatk.smb.db.Store

class StoreMapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var locationManager: LocationManager

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storeModel = StoreViewModel(application)
        val adapter = StoreAdapter(storeModel)
        binding.mapView.also {
            it.getMapboxMap().setCamera(CameraOptions.Builder().zoom(0.0).build())
            it.getMapboxMap().loadStyleUri(
                Style.MAPBOX_STREETS
            ) {
            }
        }

        storeModel.stores.observe(this, Observer {
            it.let {
                adapter.setStores(it)
                it.forEach(this::addAnnotationToMap)
            }
        })

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

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun addAnnotationToMap(store: Store){
        val pointAnnotationManager = binding.mapView.annotations.createPointAnnotationManager()
        val marker = BitmapFactory.decodeResource(resources, R.drawable.red_marker)
        val scaledMarker = Bitmap.createScaledBitmap(marker, (marker.width*0.3).toInt(), (marker.height*0.3).toInt(), true)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),1)
            return
        }

            val paOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(store.longitude, store.latitude))
                .withIconImage(scaledMarker)
                .withTextAnchor(TextAnchor.TOP)
                .withTextField(store.name)

            val point = pointAnnotationManager.create(paOptions)
            Log.i("geo",point.point.toString())

    }
}