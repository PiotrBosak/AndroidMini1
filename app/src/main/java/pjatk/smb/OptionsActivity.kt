package pjatk.smb

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pjatk.smb.databinding.ActivityOptionsBinding

const val CHANGED_BACKGRHOUND_COLOR = "changed_text_color"
const val CHANGED_TEXT_SIZE = "changed_text_size"

class OptionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOptionsBinding
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = getPreferences(Context.MODE_PRIVATE)
        editor = sp.edit()
        setOptions()
        binding.changeTextColor.setOnClickListener {
            val isChangedTextSize = sp.getBoolean(CHANGED_TEXT_SIZE, false)
            if (!isChangedTextSize) {
                editor.putBoolean(CHANGED_TEXT_SIZE, true).commit()
            }
            else {
                editor.putBoolean(CHANGED_TEXT_SIZE, false).commit()
                }
        }
        binding.changeBackgroundColor.setOnClickListener {
            val isColorChanged = sp.getBoolean(CHANGED_BACKGRHOUND_COLOR, false)
            if (!isColorChanged) {
                editor.putBoolean(CHANGED_BACKGRHOUND_COLOR, true).commit()
            } else {
                editor.putBoolean(CHANGED_BACKGRHOUND_COLOR, false).commit()
            }
        }
    }

    private fun setOptions() {
        val isChangedBackgroundColor = sp.getBoolean(CHANGED_BACKGRHOUND_COLOR, false)
        val backgroundColor =
            if (isChangedBackgroundColor) R.color.black else androidx.appcompat.R.color.material_deep_teal_500
        val isChangedTextSize = sp.getBoolean(CHANGED_TEXT_SIZE, false)
        val textSize = if (isChangedTextSize)  12F else 18F


        binding.changeBackgroundColor.setBackgroundColor(getResources().getColor(backgroundColor))
        binding.changeTextColor.setTextSize(textSize)
    }
}