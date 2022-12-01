package pjatk.smb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import pjatk.smb.db.ShoppingList
import pjatk.smb.db.ShoppingListDB
import pjatk.smb.db.ShoppingListRepo

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: ShoppingListRepo
    val lists: LiveData<List<ShoppingList>>

    init {
        repo = ShoppingListRepo(
            ShoppingListDB.getDatabase(application.applicationContext).shoppingListDao()
        )
        lists = repo.lists
    }

     suspend fun insert(list: ShoppingList) = repo.insert(list)
     suspend fun update(list: ShoppingList) = repo.update(list)
     suspend fun delete(list: ShoppingList) = repo.delete(list)
     suspend fun deleteAll() = repo.deleteAll()
}