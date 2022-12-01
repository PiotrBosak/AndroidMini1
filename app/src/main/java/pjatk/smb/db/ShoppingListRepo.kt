package pjatk.smb.db

class ShoppingListRepo(private val dao: ShoppingListDao) {

    val lists = dao.getLists()

    suspend fun insert(list: ShoppingList) = dao.insert(list)
    suspend fun update(list: ShoppingList) = dao.update(list)
    suspend fun delete(list: ShoppingList) = dao.delete(list)
    suspend fun deleteAll() = dao.deleteAll()
}