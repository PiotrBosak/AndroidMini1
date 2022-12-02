package pjatk.smb.db

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingItemEntry(
    var id: String,
    var productName: String,
    var quantity: Long,
    var pricePerItem: Long,
    val completed: Long,
    var shoppingListId: String
) : java.io.Serializable
