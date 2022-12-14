package pjatk.smb

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import pjatk.smb.databinding.ActivitySigninBinding


class SigninSignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySigninBinding

    private val launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { it -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.buttonLogin.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            signIn(email, password)
        }
        binding.buttonSignup.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            signUp(email, password)
        }
    }

    private fun signIn(email: String, password: String) {

        aux(email, password) { email, password -> auth.signInWithEmailAndPassword(email, password) }
    }

    private fun signUp(email: String, password: String) {

        aux(email, password) { email, password ->
            auth.createUserWithEmailAndPassword(
                email,
                password
            )
        }
    }

    private fun aux(email: String, password: String, f: (String, String) -> Task<AuthResult>) {
        if (!validateForm()) {
            return
        }
        f(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java).apply { }
                    launcher.launch(intent)
                } else {
                    Toast.makeText(
                        this, "Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.etEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.error = "Required."
            valid = false
        } else {
            binding.etEmail.error = null
        }

        val password = binding.etPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.etPassword.error = "Required."
            valid = false
        } else {
            binding.etPassword.error = null
        }

        return valid
    }
}