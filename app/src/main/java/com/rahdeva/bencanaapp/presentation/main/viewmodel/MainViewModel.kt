package com.rahdeva.bencanaapp.presentation.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rahdeva.bencanaapp.data.DisasterRepository
import com.rahdeva.bencanaapp.data.model.DisasterItems
import com.rahdeva.bencanaapp.utils.DataResults
import com.rahdeva.bencanaapp.utils.DisasterUtils
import com.rahdeva.bencanaapp.utils.provider.ResourceProvider
import com.rahdeva.bencanaapp.utils.preferences.Settingpreferences
import kotlinx.coroutines.launch

class MainViewModel(
    private val preferences: Settingpreferences,
    resourceProvider: ResourceProvider
) : ViewModel() {

    private val disasterRepository: DisasterRepository = DisasterRepository()

    private val disasterUtils = DisasterUtils(resourceProvider)
    private val _disasterItemsArray = MutableLiveData<List<DisasterItems?>?>()
    val disasterItemsArray: LiveData<List<DisasterItems?>?> = _disasterItemsArray

    private val _filter = MutableLiveData<String>()
    val filter: LiveData<String> = _filter

    private val _cityId = MutableLiveData<String>()
    val cityId: LiveData<String> = _cityId

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isWarned = MutableLiveData<Boolean>()
    val isWarned: LiveData<Boolean> = _isWarned

    private val _warningText = MutableLiveData<String>()
    val warningText: LiveData<String> = _warningText


    init {
        getRecentDisaster()
    }

    fun getThemeSettings() : LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }
    fun getNotificationSettings(): LiveData<Boolean> {
        return preferences.getNotificationSetting().asLiveData()
    }
    fun saveThemeSettings(darkModeState: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(darkModeState)
        }
    }

    private fun setWarn(state: Boolean, msg: String) {
        _isWarned.value = state
        _warningText.value = msg
    }

    fun getCityId(): String {
        return cityId.value ?: ""
    }

    //TODO: 1. change error into Toast
    //TODO: 2. Settings page, suggestion list,

    fun getRecentDisaster() {
        if (getFilter().isNotEmpty() && getCityId().isNotEmpty()) {
            getDisasterByLocationAndType(getCityId(), getFilter())
        } else if (getFilter().isNotEmpty() && getCityId().isEmpty())  {
            getDisasterByType(getFilter())
        } else if (getFilter().isEmpty() && getCityId().isNotEmpty()) {
            getDisasterByLocation(getCityId())
        } else {
            getDisaster()
        }
    }

    private fun getDisaster(){
        _isLoading.postValue(true)
        viewModelScope.launch {
            disasterRepository.getDisaster().apply {
                when(this){
                    is DataResults.Success -> {
                        _disasterItemsArray.value = this.data
                        _isLoading.postValue(false)
                    }
                    is DataResults.Error -> {
                        // TODO: change this into toast or dialog for a better user experience
                        setWarn(true, this.error)
                        _isLoading.postValue(false) }
                }
            }
        }
    }

    private fun getDisasterByLocation(query: String){
        _isLoading.postValue(true)
        viewModelScope.launch {
            disasterRepository.searchDisaster(query).apply {
                when(this){
                    is DataResults.Success -> {
                        _disasterItemsArray.value = this.data
                        _isLoading.postValue(false)
                    }
                    is DataResults.Error -> {
                        setWarn(true, this.error)
                        _isLoading.postValue(false)
                    }
                }
            }
        }
    }

    private fun getDisasterByType(disasterType: String){
        _isLoading.postValue(true)
        viewModelScope.launch {
            disasterRepository.getFilterDisaster(disasterType).apply {
                when(this){
                    is DataResults.Success -> {
                        _disasterItemsArray.value = this.data
                        _isLoading.postValue(false)
                    }
                    is DataResults.Error -> {
                        setWarn(true, this.error)
                        _isLoading.postValue(false)
                    }
                }
            }
        }
    }

    private fun getDisasterByLocationAndType(loc: String, type: String){
        _isLoading.postValue(true)
        viewModelScope.launch {
            disasterRepository.getDisasterByLocationAndType(loc, type).apply {
                when(this){
                    is DataResults.Success -> {
                        _disasterItemsArray.value = this.data
                        _isLoading.postValue(false)
                    }
                    is DataResults.Error -> {
                        setWarn(true, this.error)
                        _isLoading.postValue(false)
                    }
                }
            }
        }
    }

    fun setFilter(type: String) {
        this._filter.value = type
        getRecentDisaster()
    }

    fun getFilter(): String {
        return filter.value ?: ""
    }

    fun setLocation(location: String) {
        _cityId.value = disasterUtils.getRegionCode(location)
    }
}