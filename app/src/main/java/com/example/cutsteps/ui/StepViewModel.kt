package com.example.cutsteps.ui

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cutsteps.data.StepRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StepViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StepRepository(application)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _permissionGranted = MutableStateFlow(checkPermission())
    val permissionGranted: StateFlow<Boolean> = _permissionGranted.asStateFlow()

    init {
        refresh()
        viewModelScope.launch {
            repository.ticks.collect { refresh() }
        }
        if (_permissionGranted.value) {
            repository.start()
        }
    }

    fun refresh() {
        val week = repository.lastSevenDays()
        _uiState.value = HomeUiState(
            todaySteps = week.lastOrNull()?.steps ?: 0L,
            week = week,
            hasSensor = repository.hasSensor()
        )
    }

    fun onPermissionResult(granted: Boolean) {
        _permissionGranted.value = granted
        if (granted) {
            repository.start()
            refresh()
        }
    }

    override fun onCleared() {
        repository.stop()
        super.onCleared()
    }

    private fun checkPermission(): Boolean {
        val app = getApplication<Application>()
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ||
            app.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) ==
            PackageManager.PERMISSION_GRANTED
    }
}

data class HomeUiState(
    val todaySteps: Long = 0L,
    val week: List<StepRepository.DaySteps> = emptyList(),
    val hasSensor: Boolean = true
)
