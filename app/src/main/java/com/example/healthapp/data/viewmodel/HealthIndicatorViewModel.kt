package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.HealthIndicator
import com.example.healthapp.data.repository.HealthIndicatorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HealthIndicatorViewModel(private val repository: HealthIndicatorRepository) : ViewModel() {

    val allHealthIndicators = repository.allHealthIndicators

    fun insertHealthIndicator(healthIndicator: HealthIndicator) = viewModelScope.launch {
        repository.insertHealthIndicator(healthIndicator)
    }

    fun updateHealthIndicator(healthIndicator: HealthIndicator) = viewModelScope.launch {
        repository.updateHealthIndicator(healthIndicator)
    }

    fun deleteHealthIndicator(healthIndicator: HealthIndicator) = viewModelScope.launch {
        repository.deleteHealthIndicator(healthIndicator)
    }

    fun getHealthIndicatorById(indicatorID: Int) = repository.getHealthIndicatorById(indicatorID)

    fun getHealthMetricByUserId(userId: Int) = repository.getHealthMetricByUserId(userId)

    private val _healthMetrics = MutableStateFlow<List<HealthIndicator>>(emptyList())
    val healthMetrics: StateFlow<List<HealthIndicator>> = _healthMetrics.asStateFlow()

    fun loadHealthMetricsOnce(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val metrics = repository.getHealthMetricByUserIdN(userId)
            _healthMetrics.value = metrics
        }
    }

    fun getHealthMetricsByUserIdAndStore(userId: Int) {
        viewModelScope.launch {
            repository.getHealthIndicatorsAndStore(userId)
        }
    }
}

class HealthIndicatorViewModelFactory(private val healthIndicatorRepository: HealthIndicatorRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthIndicatorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HealthIndicatorViewModel(healthIndicatorRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
