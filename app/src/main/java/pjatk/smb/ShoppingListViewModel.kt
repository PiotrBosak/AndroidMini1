package pjatk.smb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import pjatk.smb.db.ShoppingList
import pjatk.smb.db.ShoppingListDB
import pjatk.smb.db.ShoppingListRepo

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: ShoppingListRepo
    val lists: MutableLiveData<HashMap<String, ShoppingList>>
    var firebaseDatabase: FirebaseDatabase

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
        repo = ShoppingListRepo(firebaseDatabase)
        lists = repo.lists
    }

     suspend fun insert(list: ShoppingList) = repo.insert(list)
     suspend fun update(list: ShoppingList) = repo.update(list)
     suspend fun delete(list: ShoppingList) = repo.delete(list)
     suspend fun deleteAll() = repo.deleteAll()
}