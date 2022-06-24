package com.example.virtrealtud

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {
    companion object {
        const val DEFAULT_BUFFER_SIZE = 3f
        const val DEFAULT_SENSITIVITY = 10f
        const val DEFAULT_SAMPLE = R.raw.electr
        const val DEFAULT_SAMPLE_NAME = "Electro"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onResume() {
        super.onResume()
        setViewListeners()
        updateViews()
    }

    private fun setViewListeners() {
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

        sampleSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val sharedPref = getSharedPreferences("settings", 0)
                val editor = sharedPref.edit()
                val sampleName = sampleSpinner.getSelectedItem().toString()
                var sample = DEFAULT_SAMPLE
                if (sampleName == "Hi-Hat") {
                    sample = R.raw.hihat
                } else if (sampleName == "Rattle") {
                    sample = R.raw.rattle
                }
                editor.putInt("sample", sample)
                editor.putString("sampleName", sampleName)
                editor.apply()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {//do nothing
            }
        })


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
        var sample = sharedPref.getString("sampleName", DEFAULT_SAMPLE_NAME)
        for (i in 0 until sampleSpinner.count) {
            if (sampleSpinner.getItemAtPosition(i).equals(sample)) {
                sampleSpinner.setSelection(i)
                break
            }
        }
    }
}
