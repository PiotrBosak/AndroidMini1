package pjatk.smb

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pjatk.smb.databinding.ShoppingListsElementBinding
import pjatk.smb.db.ShoppingItemEntry
import pjatk.smb.db.ShoppingList

class ShoppingListAdapter(
    private val listViewModel: ShoppingListViewModel,
    private val activity: ShoppingListsActivity
) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {
    private val editListLauncher: ActivityResultLauncher<Intent> =
        activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val r = result.data?.getSerializableExtra(EDIT_LIST_RESULT)
                if (r != null) {
                    when (val entryResult =
                        r as EditShoppingListActivity.Companion.EditListResult) {
                        is EditShoppingListActivity.Companion.Modified -> {
                            update(entryResult.list)
                        }
                        is EditShoppingListActivity.Companion.Deleted -> {
                            delete(entryResult.list)
                        }

                    }
                }

            }
        }
    private val entriesLauncher: ActivityResultLauncher<Intent> =
        activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { it -> }
    private var lists = emptyList<ShoppingList>()

    class ViewHolder(val binding: ShoppingListsElementBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShoppingListsElementBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.listName.text = lists[position].name
        holder.binding.seeList.setOnClickListener {
            val intent = ShoppingEntriesActivity.newIntent(activity, lists[position].id)
            entriesLauncher.launch(intent)
        }
        holder.binding.editList.setOnClickListener {
            val intent = EditShoppingListActivity.newIntent(activity, lists[position])
            editListLauncher.launch(intent)
        }

    }

    override fun getItemCount(): Int = lists.size

    fun update(list: ShoppingList) {
        CoroutineScope(Dispatchers.IO).launch {
            listViewModel.update(list)
        }
        notifyDataSetChanged()

    }

    fun add(list: ShoppingList) {
        CoroutineScope(Dispatchers.IO).launch {
            listViewModel.insert(list)
        }
        notifyDataSetChanged()
    }

    fun delete(list: ShoppingList) {
        CoroutineScope(Dispatchers.IO).launch {
            listViewModel.delete(list)
            withContext(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }
    }

    fun setLists(shoppingLists: List<ShoppingList>) {
        lists = shoppingLists
        notifyDataSetChanged()
    }
}