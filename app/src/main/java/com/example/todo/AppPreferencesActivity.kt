/*package com.example.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AppPreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_preferences)
    }
}*/
/*package com.example.todo

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class AppPreferencesActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_preferences)

        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        val switchNotifications = findViewById<Switch>(R.id.switchNotifications)
        val switchDarkMode = findViewById<Switch>(R.id.switchDarkMode)

        // Load saved values
        switchNotifications.isChecked = prefs.getBoolean("notifications", true)
        switchDarkMode.isChecked = prefs.getBoolean("dark_mode", false)

        // Save on change
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notifications", isChecked).apply()
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
        }
    }
}*/
package com.example.todo

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AppPreferencesActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_preferences)

        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        val switchNotifications = findViewById<Switch>(R.id.switchNotifications)
        val switchDarkMode = findViewById<Switch>(R.id.switchDarkMode)
        val switchSound = findViewById<Switch>(R.id.switchSound)
        val switchAutoSync = findViewById<Switch>(R.id.switchAutoSync)
        val btnClearCache = findViewById<Button>(R.id.btnClearCache)

        // Load saved values
        switchNotifications.isChecked = prefs.getBoolean("notifications", true)
        switchDarkMode.isChecked = prefs.getBoolean("dark_mode", false)
        switchSound.isChecked = prefs.getBoolean("sound", true)
        switchAutoSync.isChecked = prefs.getBoolean("auto_sync", true)

        // Save preferences
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notifications", isChecked).apply()
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            Toast.makeText(this, "Restart app to apply theme", Toast.LENGTH_SHORT).show()
        }

        switchSound.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sound", isChecked).apply()
        }

        switchAutoSync.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("auto_sync", isChecked).apply()
        }

        // Clear Cache
        btnClearCache.setOnClickListener {
            cacheDir.deleteRecursively()
            Toast.makeText(this, "Cache cleared", Toast.LENGTH_SHORT).show()
        }
    }
}


