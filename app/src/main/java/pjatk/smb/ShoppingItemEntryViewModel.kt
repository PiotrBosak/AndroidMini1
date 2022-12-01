package pjatk.smb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import pjatk.smb.db.ShoppingItemEntry
import pjatk.smb.db.ShoppingItemEntryRepo
import pjatk.smb.db.ShoppingListDB

class ShoppingItemEntryViewModel(application: Application, private val listId: Long) : AndroidViewModel(application) {

    private val repo: ShoppingItemEntryRepo
    val entries: LiveData<List<ShoppingItemEntry>>

    init {
        repo = ShoppingItemEntryRepo(
            ShoppingListDB.getDatabase(application.applicationContext).shoppingEntryDao()
        )
        entries = repo.getEntriesById(listId)
    }



    suspend fun getEntryById(id: Long) = repo.getEntryById(id)
    suspend fun insert(entry: ShoppingItemEntry): Long = repo.insert(entry)
    suspend fun update(entry: ShoppingItemEntry) = repo.update(entry)
    suspend fun delete(entry: ShoppingItemEntry) = repo.delete(entry)
}