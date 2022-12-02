package pjatk.smb.db

data class ShoppingList(
    var id: String,
    var name: String,
    var userId: String?
) : java.io.Serializable