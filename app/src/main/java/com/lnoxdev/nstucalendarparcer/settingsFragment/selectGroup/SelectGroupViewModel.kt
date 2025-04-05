package com.lnoxdev.nstucalendarparcer.settingsFragment.selectGroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnoxdev.data.InternetException
import com.lnoxdev.data.ParseException
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.data.neti.netiFindGroupDataSource.NetiFindGroupRepository
import com.lnoxdev.nstucalendarparcer.models.UiExceptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectGroupViewModel @Inject constructor(
    private val netiFindGroupRepository: NetiFindGroupRepository,
    private val settingsManager: SettingsManager,
) : ViewModel() {

    private val _groups = MutableStateFlow<List<String>?>(null)
    val groups: StateFlow<List<String>?> = _groups

    private val _exception = MutableStateFlow<UiExceptions?>(null)
    val exception: StateFlow<UiExceptions?> = _exception

    init {
        search("")
    }

    fun search(searchText: String) {
        viewModelScope.launch {
            try {
                val result = netiFindGroupRepository.findGroup(searchText)
                _groups.value = result
            } catch (e: InternetException) {
                _exception.value = UiExceptions.INTERNET
            } catch (e: ParseException) {
                _exception.value = UiExceptions.PARSE
            } catch (e: Exception) {
                _exception.value = UiExceptions.UNKNOWN
            }
        }
    }

    fun selectGroup(group: String) {
        settingsManager.changeGroup(group)
    }

}