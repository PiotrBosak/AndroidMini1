package pjatk.smb

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pjatk.smb.databinding.ActivityShoppingEntriesBinding
import pjatk.smb.db.ShoppingItemEntry

const val LIST_ID = "pjatk.smb.list_id"


class ShoppingEntriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingEntriesBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var br: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingEntriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = getPreferences(Context.MODE_PRIVATE)
        editor = sp.edit()

        val listId: String = intent.getStringExtra(LIST_ID) ?: ""
        val entriesModel = ShoppingItemEntryViewModel(application, listId)
        val entriesAdapter = ShoppingEntryAdapter(entriesModel, this, listId)

        binding.addProduct.setOnClickListener {
            val entry = ShoppingItemEntry(
                id = "new",
                productName = binding.newProductName.text.toString(),
                quantity = binding.newQuantity.text.toString().toLong(),
                pricePerItem = binding.newPrice.text.toString().toLong(),
                shoppingListId = listId,
                completed = 0
            )
            entriesAdapter.add(entry)
            val intent1 = Intent().apply {
                putExtra("entry", Json.encodeToString(entry))
                setAction("pjatk.smb.NewShoppingEntry")
            }
            sendBroadcast(intent1)
        }

        br = EditProductBroadcastReceiver { modifiedEntry -> entriesAdapter.onModifyProductClick(modifiedEntry) }
        val filter = IntentFilter("pjatk.smb.ModifyShoppingEntry")
        registerReceiver(br, filter)

        binding.rv1.layoutManager = LinearLayoutManager(this)
        binding.rv1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.rv1.adapter = entriesAdapter

        entriesModel.entries.observe(this, Observer {
            it.let {
                entriesAdapter.setEntries(it.values.toList())
            }
        })

    }

    companion object {
        fun newIntent(packageContext: Context, listId: String): Intent {
            return Intent(packageContext, ShoppingEntriesActivity::class.java).apply {
                putExtra(LIST_ID, listId)
            }
        }
    }
}