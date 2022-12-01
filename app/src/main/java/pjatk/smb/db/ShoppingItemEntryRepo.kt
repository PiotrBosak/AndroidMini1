package pjatk.smb.db

class ShoppingItemEntryRepo(private val dao: ShoppingItemEntryDao) {
   suspend fun getEntryById(id: Long) = dao.getEntryById(id)
   fun getEntriesById(listId: Long) = dao.getEntriesForList(listId)
   suspend fun insert(entry: ShoppingItemEntry): Long = dao.insert(entry)
   suspend fun update(entry: ShoppingItemEntry) = dao.update(entry)
   suspend fun delete(entry: ShoppingItemEntry) = dao.delete(entry)

}