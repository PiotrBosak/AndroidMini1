package pjatk.smb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import pjatk.smb.db.ShoppingItemEntry
import pjatk.smb.db.ShoppingItemEntryRepo

class ShoppingItemEntryViewModel(application: Application, private val listId: String) : AndroidViewModel(application) {

    private val repo: ShoppingItemEntryRepo
    var firebaseDatabase: FirebaseDatabase
    val entries: MutableLiveData<HashMap<String, ShoppingItemEntry>>

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
        repo = ShoppingItemEntryRepo(firebaseDatabase,listId)
        entries = repo.entries
    }

    suspend fun insert(entry: ShoppingItemEntry): Unit = repo.insert(entry)
    suspend fun update(entry: ShoppingItemEntry) = repo.update(entry)
    suspend fun delete(entry: ShoppingItemEntry) = repo.delete(entry)
}