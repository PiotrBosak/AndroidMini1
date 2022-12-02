package pjatk.smb.db

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ShoppingListRepo(private val firebaseDatabase: FirebaseDatabase) {

    val lists: MutableLiveData<HashMap<String, ShoppingList>> =
        MutableLiveData<HashMap<String, ShoppingList>>().also {
            it.value = HashMap<String, ShoppingList>()
        }
    private lateinit var auth: FirebaseAuth

    init {
        auth = FirebaseAuth.getInstance()
        aux("lists")
        aux("lists/${auth.currentUser?.uid?.replace("-", "")}")

    }
    private fun aux(path: String) {
        firebaseDatabase.getReference(path)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val list = ShoppingList(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        userId = snapshot.child("userId").value as String?,
                    )
                    lists.value?.put(list.id, list)
                    lists.postValue(lists.value)
                }

                override fun onChildChanged(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    val list = ShoppingList(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        userId = snapshot.child("userId").value as String?,
                    )
                    lists.value?.set(list.id, list)
                    lists.postValue(lists.value)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val list = ShoppingList(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        userId = snapshot.child("userId").value as String?,
                    )
                    lists.value?.remove(list.id)
                    lists.postValue(lists.value)
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    //TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    //TODO("Not yet implemented")
                }

            })
    }

    private fun getPath(list: ShoppingList): String {
        val isShared = list.userId.isNullOrBlank()
        return if(isShared)
            "lists"
        else
            "lists/${auth.currentUser?.uid?.replace("-","")}"
    }

    suspend fun insert(list: ShoppingList) {
        val path = getPath(list)
        firebaseDatabase.getReference(path).push().also {
            list.id = it.ref.key!!
            it.setValue(list)
        }
    }

    suspend fun update(list: ShoppingList) {
        val path = getPath(list)
        var ref = firebaseDatabase.getReference(path)
        ref.child("name").setValue(list.name)
        ref.child("userId").setValue(list.userId)
    }

    suspend fun delete(list: ShoppingList)  {
        val path = getPath(list)
        firebaseDatabase.getReference(path).removeValue()
    }

    suspend fun deleteAll() = firebaseDatabase.getReference("lists").removeValue()
}