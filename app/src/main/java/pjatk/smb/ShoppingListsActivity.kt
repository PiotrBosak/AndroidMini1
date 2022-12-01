package pjatk.smb

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import pjatk.smb.databinding.ActivityShoppingListsBinding
import pjatk.smb.db.ShoppingList

class ShoppingListsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingListsBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ShoppingList", "Entered")
        binding = ActivityShoppingListsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = getPreferences(Context.MODE_PRIVATE)
        editor = sp.edit()

        val listsModel = ShoppingListViewModel(application)
        val adapter = ShoppingListAdapter(listsModel, this)

        binding.addList.setOnClickListener {
           val list = ShoppingList(name = binding.newList.text.toString())
            adapter.add(list)
        }
        binding.rv1.layoutManager = LinearLayoutManager(this)
        binding.rv1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.rv1.adapter = adapter
        listsModel.lists.observe(this, Observer {
            it.let {
                adapter.setLists(it)
            }
        })
    }
}