package pjatk.smb

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import pjatk.smb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private val launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { it -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.seeLists.setOnClickListener {
            val intent = Intent(this, ShoppingListsActivity::class.java).apply {  }
            launcher.launch(intent)
        }
        binding.options.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java).apply {  }
            launcher.launch(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        auth.signOut()
    }

}