package pjatk.smb.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.*

@Entity
@Serializable
data class Store(
    @PrimaryKey(autoGenerate = false)
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var description: String,
    var radius: Double,
    var latitude: Double,
    var longitude: Double,
) : java.io.Serializable
