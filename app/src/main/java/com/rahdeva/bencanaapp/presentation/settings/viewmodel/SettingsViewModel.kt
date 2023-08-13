package com.rahdeva.bencanaapp.presentation.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rahdeva.bencanaapp.utils.preferences.Settingpreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: Settingpreferences) : ViewModel() {

    fun getThemeSettings() : LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }
    fun saveThemeSettings(darkModeState: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(darkModeState)
        }
    }
    fun getNotificationSettings(): LiveData<Boolean> {
        return preferences.getNotificationSetting().asLiveData()
    }
    fun saveNotificationSettings(notificationIsEnabled: Boolean) {
        viewModelScope.launch {
            preferences.saveNotificationSetting(notificationIsEnabled)
        }
    }
}