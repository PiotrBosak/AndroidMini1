package pjatk.smb.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ShoppingItemEntryRepo(private val firebaseDatabase: FirebaseDatabase,private val listId: String) {

    val entries: MutableLiveData<HashMap<String, ShoppingItemEntry>> =
        MutableLiveData<HashMap<String, ShoppingItemEntry>>().also {
            it.value = HashMap<String, ShoppingItemEntry>()
        }

    init {
        firebaseDatabase.getReference("entries/${listId}")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val entry = ShoppingItemEntry(
                        id = snapshot.ref.key as String,
                        productName = snapshot.child("productName").value as String,
                        quantity = snapshot.child("quantity").value as Long,
                        pricePerItem = snapshot.child("pricePerItem").value as Long,
                        completed = snapshot.child("completed").value as Long,
                        shoppingListId = snapshot.child("shoppingListId").value as String,
                    )
                    entries.value?.put(entry.id, entry)
                    entries.postValue(entries.value)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val entry = ShoppingItemEntry(
                        id = snapshot.ref.key as String,
                        productName = snapshot.child("productName").value as String,
                        quantity = snapshot.child("quantity").value as Long,
                        pricePerItem = snapshot.child("pricePerItem").value as Long,
                        completed = snapshot.child("completed").value as Long,
                        shoppingListId = snapshot.child("shoppingListId").value as String,
                    )
                    entries.value?.set(entry.id, entry)
                        entries.postValue(entries.value)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val entry = ShoppingItemEntry(
                        id = snapshot.ref.key as String,
                        productName = snapshot.child("productName").value as String,
                        quantity = snapshot.child("quantity").value as Long,
                        pricePerItem = snapshot.child("pricePerItem").value as Long,
                        completed = snapshot.child("completed").value as Long,
                        shoppingListId = snapshot.child("shoppingListId").value as String,
                    )
                    Log.i("removed", entry.toString())
                    entries.value?.remove(entry.id)
                    entries.postValue(entries.value)
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    //TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    //TODO("Not yet implemented")
                }

            })
    }

    suspend fun insert(entry: ShoppingItemEntry) {
        firebaseDatabase.getReference("entries/${listId}").push().also {
            entry.id = it.ref.key!!
            it.setValue(entry)
        }
    }

    suspend fun update(entry: ShoppingItemEntry) {
        var ref = firebaseDatabase.getReference("entries/${listId}/${entry.id}")
        ref.child("productName").setValue(entry.productName)
        ref.child("quantity").setValue(entry.quantity)
        ref.child("pricePerItem").setValue(entry.pricePerItem)
        ref.child("completed").setValue(entry.completed)
        ref.child("shoppingListId").setValue(entry.shoppingListId)
    }

    suspend fun delete(entry: ShoppingItemEntry) =
        firebaseDatabase.getReference("entries/${listId}/${entry.id}").removeValue()

    suspend fun deleteAll() = firebaseDatabase.getReference("entries/${listId}").removeValue()
}