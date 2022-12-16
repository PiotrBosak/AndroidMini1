package pjatk.smb

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import pjatk.smb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { it -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.seeLists.setOnClickListener {
            val intent = Intent(this, ShoppingListsActivity::class.java).apply {  }
            launcher.launch(intent)
        }
        binding.addStores.setOnClickListener {
            val intent = Intent(this, StoreAddActivity::class.java).apply {  }
            launcher.launch(intent)
        }
        binding.seeStores.setOnClickListener {
            val intent = Intent(this, StoreMapActivity::class.java).apply {  }
            launcher.launch(intent)
        }
        binding.options.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java).apply {  }
            launcher.launch(intent)
        }
    }
}