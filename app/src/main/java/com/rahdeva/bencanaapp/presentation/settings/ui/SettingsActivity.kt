package com.rahdeva.bencanaapp.presentation.settings.ui

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.rahdeva.bencanaapp.R
import com.rahdeva.bencanaapp.databinding.ActivitySettingsBinding
import com.rahdeva.bencanaapp.presentation.settings.viewmodel.SettingsViewModel
import com.rahdeva.bencanaapp.utils.provider.ResourceProvider
import com.rahdeva.bencanaapp.utils.preferences.Settingpreferences
import com.rahdeva.bencanaapp.utils.provider.ViewModelFactory
import com.rahdeva.bencanaapp.utils.preferences.dataStore

class SettingsActivity : AppCompatActivity() {
    private val binding: ActivitySettingsBinding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private lateinit var preferences: Settingpreferences
    private lateinit var resourceProvider: ResourceProvider

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initWindow()

        preferences = Settingpreferences.getInstance(application.dataStore)
        resourceProvider = ResourceProvider(applicationContext)

        val settingsViewModel = ViewModelProvider(this, ViewModelFactory(preferences, resourceProvider)).get(
            SettingsViewModel::class.java
        )

        binding.apply {
            settingsViewModel.getThemeSettings().observe(this@SettingsActivity) { nightStateOn: Boolean ->
                if (nightStateOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchNight.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchNight.isChecked = false
                }
            }

            settingsViewModel.getNotificationSettings().observe(this@SettingsActivity) { isEnabled: Boolean ->
                switchNotification.isChecked = isEnabled
            }

            btnBack.setOnClickListener {
                onBackPressed()
            }

            switchNight.setOnCheckedChangeListener{ _: CompoundButton?, isChecked: Boolean ->
                settingsViewModel.saveThemeSettings(isChecked)
            }
            switchNotification.setOnCheckedChangeListener{ _: CompoundButton?, isChecked: Boolean ->
                if (isChecked) {
                    settingsViewModel.saveNotificationSettings(true)
                } else {
                    settingsViewModel.saveNotificationSettings(false)
                    switchNotification.isChecked = false
                    Toast.makeText(this@SettingsActivity, R.string.notification_disabled, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun initWindow(){
        window.statusBarColor = getColor(R.color.primary_background)
        // Change the item color (text, icon etc) inside the status bar
        val windowInsetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        windowInsetsController.isAppearanceLightStatusBars = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK === Configuration.UI_MODE_NIGHT_NO
    }
}