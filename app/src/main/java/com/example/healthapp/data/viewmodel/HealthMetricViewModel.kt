package com.example.healthapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.HealthMetric
import com.example.healthapp.data.repository.HealthMetricRepository
import kotlinx.coroutines.launch

class HealthMetricViewModel(private val healthMetricRepository: HealthMetricRepository) : ViewModel() {

    fun getHealthMetricById(metricID: Int): LiveData<HealthMetric> = healthMetricRepository.getHealthMetricById(metricID).asLiveData()

    val allHealthMetrics: LiveData<List<HealthMetric>> = healthMetricRepository.getAllHealthMetrics().asLiveData()

    fun insertHealthMetric(healthMetric: HealthMetric) {
        viewModelScope.launch {
            healthMetricRepository.insertHealthMetric(healthMetric)
        }
    }

    fun insertAllHealthMetrics(metrics: List<HealthMetric>) {
        viewModelScope.launch {
            healthMetricRepository.insertAllHealthMetrics(metrics)
        }
    }

    fun updateHealthMetric(healthMetric: HealthMetric) {
        viewModelScope.launch {
            healthMetricRepository.updateHealthMetric(healthMetric)
        }
    }

    fun deleteHealthMetric(healthMetric: HealthMetric) {
        viewModelScope.launch {
            healthMetricRepository.deleteHealthMetric(healthMetric)
        }
    }
}
class HealthMetricViewModelFactory(private val healthMetricRepository: HealthMetricRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthMetricViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HealthMetricViewModel(healthMetricRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
