package pjatk.smb.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal
import java.util.UUID

@Entity(
    foreignKeys = [ForeignKey(
        entity = ShoppingList::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("shoppingListId"),
        onDelete = ForeignKey.CASCADE
    )]
)

@Serializable
data class ShoppingItemEntry(
    @PrimaryKey(autoGenerate = false)
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val productName: String,
    val quantity: Int,
    val pricePerItem: Double,
    val isComplete: Boolean,
    val shoppingListId: Int
) : java.io.Serializable

object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}