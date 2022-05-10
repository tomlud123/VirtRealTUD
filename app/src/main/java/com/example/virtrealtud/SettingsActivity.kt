package com.example.virtrealtud

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    companion object {
        const val DEFAULT_BUFFER_SIZE = 3f
        const val DEFAULT_SENSITIVITY = 10f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setViewListeners()
        updateViews()
    }

    private fun setViewListeners(){
        sampleRateSlider.addOnChangeListener { slider, value, fromUser ->
//            Toast.makeText(this, value.toString(), Toast.LENGTH_LONG).show()
            val sharedPref = getSharedPreferences("settings", 0)
            val editor = sharedPref.edit()
            editor.putFloat("buffer size", value)
            editor.apply()
        }

        sensivitySlider.addOnChangeListener { slider, value, fromUser ->
            val sharedPref = getSharedPreferences("settings", 0)
            val editor = sharedPref.edit()
            editor.putFloat("sensitivity", value)
            editor.apply()
        }

        leftHand.setOnCheckedChangeListener { buttonView, isChecked ->
            val sharedPref = getSharedPreferences("settings", 0)
            val editor = sharedPref.edit()
            editor.putBoolean("left hand", isChecked)
            editor.apply()
        }
    }

    private fun updateViews() {
        val sharedPref = getSharedPreferences("settings", 0)
        var bufferSize = sharedPref.getFloat("buffer size", DEFAULT_BUFFER_SIZE)
        sampleRateSlider.value = bufferSize
        var sensitivity = sharedPref.getFloat("sensitivity", DEFAULT_SENSITIVITY)
        sensivitySlider.value = sensitivity
        var leftHandOn = sharedPref.getBoolean("left hand", false)
        leftHand.isChecked = leftHandOn
    }


    }