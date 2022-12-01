package pjatk.smb

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pjatk.smb.databinding.ShoppingEntriesElementBinding
import pjatk.smb.db.ShoppingItemEntry

class ShoppingEntryAdapter(
    private val entryViewModel: ShoppingItemEntryViewModel,
    private val activity: AppCompatActivity,
    val listId: Long
) : RecyclerView.Adapter<ShoppingEntryAdapter.ViewHolder>() {

    private var entries = emptyList<ShoppingItemEntry>()
    private val launcher: ActivityResultLauncher<Intent> =
        activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val r = result.data?.getSerializableExtra(EDIT_ENTRY_RESULT)
                if (r != null) {
                    when (val entryResult =
                        r as EditShoppingEntryActivity.Companion.EditEntryResult) {
                        is EditShoppingEntryActivity.Companion.Modified -> {
                            update(entryResult.entry)
                        }
                        is EditShoppingEntryActivity.Companion.Deleted -> {
                            delete(entryResult.entry)
                        }

                    }
                }

            }
        }

    class ViewHolder(val binding: ShoppingEntriesElementBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShoppingEntriesElementBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    fun onModifyProductClick(modifiedEntry: ShoppingItemEntry) {
        val intent = EditShoppingEntryActivity.newIntent(activity, modifiedEntry, listId)
        launcher.launch(intent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.binding.isCompleteCheckbox.isChecked = entry.isComplete
        holder.binding.isCompleteCheckbox.isEnabled = false
        holder.binding.entryName.text = entry.productName
        holder.binding.entryQuantity.text = entry.quantity.toString()
        holder.binding.entryPrice.text = (entry.quantity * entry.pricePerItem).toString()
        holder.binding.editEntry.setOnClickListener {
            onModifyProductClick(entry)
        }

    }

    override fun getItemCount(): Int = entries.size

    fun update(entry: ShoppingItemEntry) {
        CoroutineScope(Dispatchers.IO).launch {
            entryViewModel.update(entry)
        }
        notifyDataSetChanged()

    }

    fun add(entry: ShoppingItemEntry) {
        CoroutineScope(Dispatchers.IO).launch {
            entryViewModel.insert(entry)
        }
        notifyDataSetChanged()
    }

    fun delete(entry: ShoppingItemEntry) {
        CoroutineScope(Dispatchers.IO).launch {
            entryViewModel.delete(entry)
            withContext(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }
    }

    fun setEntries(entries: List<ShoppingItemEntry>) {
        this.entries = entries
        notifyDataSetChanged()
    }


}
