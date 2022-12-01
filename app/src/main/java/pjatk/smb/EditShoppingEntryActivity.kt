package pjatk.smb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pjatk.smb.databinding.ActivityEditEntryBinding
import pjatk.smb.db.ShoppingItemEntry

const val EDIT_ENTRY = "pjatk.smb.edit_entry"
const val EDIT_ENTRY_RESULT = "pjatk.smb.edit_entry_result"

class EditShoppingEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditEntryBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var isDeleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val entry = intent.getSerializableExtra(EDIT_ENTRY) as ShoppingItemEntry
        setProductInfo(entry)
        binding.modifyProduct.setOnClickListener {
            val quantity = if (binding.editEntryNewQuantity.text.toString() != "")
                Integer.parseInt(
                    binding.editEntryNewQuantity.text.toString()
                )
            else entry.quantity
            val name = if (binding.editEntryNewProductName.text.toString() != "")
                binding.editEntryNewProductName.text.toString()
            else entry.productName
            val price =
                if (binding.editEntryNewPrice.text.toString() != "")
                    binding.editEntryNewPrice.text.toString().toDouble()
                else entry.pricePerItem
            val newEntry = entry.copy(
                productName = name,
                quantity = quantity,
                pricePerItem = price,
                isComplete = binding.editIsCompleted.isChecked
            )
            setProductInfo(newEntry)
            if (!isDeleted) {
                val data = Intent().apply {
                    putExtra(EDIT_ENTRY_RESULT, Modified(newEntry))
                    putExtra("productName", newEntry.productName)
                }
                setResult(Activity.RESULT_OK, data)
                onBackPressed()
            }
        }
        binding.deleteProduct.setOnClickListener {
            val data = Intent().apply {
                putExtra(EDIT_ENTRY_RESULT, Deleted(entry))
            }
            setResult(Activity.RESULT_OK, data)
            isDeleted = true
            onBackPressed()
        }

    }

    fun setProductInfo(entry: ShoppingItemEntry) {
        if (!isDeleted) {
            binding.editEntryProductName.text = entry.productName
            binding.editEntryPrice.text = entry.pricePerItem.toString()
            binding.editEntryQuantity.text = entry.quantity.toString()
        }
    }

    companion object {
        sealed interface EditEntryResult : java.io.Serializable
        data class Modified(val entry: ShoppingItemEntry) : EditEntryResult
        data class Deleted(val entry: ShoppingItemEntry) : EditEntryResult

        fun newIntent(packageContext: Context, entry: ShoppingItemEntry, listId: Long): Intent {
            return Intent(packageContext, EditShoppingEntryActivity::class.java).apply {
                putExtra(EDIT_ENTRY, entry)
                putExtra(LIST_ID, listId)

            }
        }
    }
}