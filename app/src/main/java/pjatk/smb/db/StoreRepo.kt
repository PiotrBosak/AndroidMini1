package pjatk.smb.db

class StoreRepo(private val dao: StoreDao) {
    suspend fun getEntryById(id: Long) = dao.getStoreById(id)
    val stores = dao.getAllStores()
    suspend fun getStores() = dao.getAllStores()
    suspend fun insert(store: Store): Long = dao.insert(store)
    suspend fun update(store: Store) = dao.update(store)
    suspend fun delete(store: Store) = dao.delete(store)

}
