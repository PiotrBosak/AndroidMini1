package pjatk.smb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import pjatk.smb.databinding.ActivityEditListBinding
import pjatk.smb.db.ShoppingList

const val EDIT_LIST = "pjatk.smb.edit_list"
const val EDIT_LIST_RESULT = "pjatk.smb.edit_list_result"

class EditShoppingListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditListBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var isDeleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = getPreferences(Context.MODE_PRIVATE)
        editor = sp.edit()

        val list = intent.getSerializableExtra(EDIT_LIST) as ShoppingList
        binding.editListName.text = list.name
        binding.modifyProduct.setOnClickListener {
            val newList = ShoppingList(name = binding.editListNewName.text.toString())
            binding.editListName.text = newList.name
            if (!isDeleted) {
                val data = Intent().apply {
                    putExtra(
                        EDIT_LIST_RESULT,
                        Modified(newList)
                    )
                }
                setResult(Activity.RESULT_OK, data)
                onBackPressed()
            }
        }
        binding.deleteProduct.setOnClickListener {
            val data = Intent().apply {
                putExtra(EDIT_LIST_RESULT, Deleted(list))
            }
            setResult(Activity.RESULT_OK, data)
            isDeleted = true
        }
    }


    companion object {
        sealed interface EditListResult : java.io.Serializable
        data class Modified(val list: ShoppingList) : EditListResult
        data class Deleted(val list: ShoppingList) : EditListResult

        fun newIntent(packageContext: Context, list: ShoppingList): Intent {
            return Intent(packageContext, EditShoppingListActivity::class.java).apply {
                putExtra(EDIT_LIST, list)
            }
        }
    }
}