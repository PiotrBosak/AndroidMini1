package pjatk.smb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import pjatk.smb.db.ShoppingItemEntry

class EditProductBroadcastReceiver(private val onEntryReceived: androidx.arch.core.util.Function<ShoppingItemEntry, Unit>) :
    BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null) {
            val entry = Json.decodeFromString<ShoppingItemEntry>(intent.getStringExtra("entry") ?: "")
            onEntryReceived.apply(entry)
        }
    }
}