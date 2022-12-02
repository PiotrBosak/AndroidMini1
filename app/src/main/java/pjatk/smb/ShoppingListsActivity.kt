package pjatk.smb

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import pjatk.smb.databinding.ActivityShoppingListsBinding
import pjatk.smb.db.ShoppingList
import java.util.UUID

class ShoppingListsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingListsBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ShoppingList", "Entered")
        binding = ActivityShoppingListsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = getPreferences(Context.MODE_PRIVATE)
        editor = sp.edit()

        val listsModel = ShoppingListViewModel(application)
        val adapter = ShoppingListAdapter(listsModel, this)

        auth = FirebaseAuth.getInstance()
        binding.addList.setOnClickListener {
           val list = ShoppingList(id = "new",name = binding.newList.text.toString(),if(!binding.isShared.isChecked) auth.currentUser?.uid else null)
            adapter.add(list)
        }
        binding.rv1.layoutManager = LinearLayoutManager(this)
        binding.rv1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.rv1.adapter = adapter
        listsModel.lists.observe(this, Observer {
            it.let {
                adapter.setLists(it.values.toList())
            }
        })
    }
}