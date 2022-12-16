package pjatk.smb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import pjatk.smb.db.*

class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: StoreRepo
    val stores: LiveData<List<Store>>

    init {
        repo = StoreRepo(
            ShoppingListDB.getDatabase(application.applicationContext).storeDao()
        )
        stores = repo.stores
    }

    suspend fun getStores() = repo.getStores()
    suspend fun insert(store: Store) = repo.insert(store)
    suspend fun update(store: Store) = repo.update(store)
    suspend fun delete(store: Store) = repo.delete(store)
}