package pjatk.smb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import pjatk.smb.db.Store

class GeoReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofenceEvent = GeofencingEvent.fromIntent(intent)
        for (geofence in geofenceEvent.triggeringGeofences)
            if (geofence != null) {
                if (geofenceEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    Toast.makeText(
                        context,
                        "Geofence for store : ${geofence.requestId} was entered!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else if (geofenceEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            } else {
                Log.i("geofence123", geofenceEvent.geofenceTransition.toString())
                Log.i("geofence123", geofenceEvent.errorCode.toString())
            }
    }
}
