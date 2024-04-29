package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.DailyActivity
import com.example.healthapp.data.repository.DailyActivityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class DailyActivityViewModel(private val repository: DailyActivityRepository) :
    ViewModel() {

    // 假设你的DailyActivityRepository有一个获取所有DailyActivity的Flow属性
    val allDailyActivities = repository.allDailyActivities

    // 插入DailyActivity
    fun insertDailyActivity(dailyActivity: DailyActivity) = viewModelScope.launch {
        repository.insertDailyActivity(dailyActivity)
    }

    // 更新DailyActivity
    fun updateDailyActivity(dailyActivity: DailyActivity) = viewModelScope.launch {
        repository.updateDailyActivity(dailyActivity)
    }

    // 删除DailyActivity
    fun deleteDailyActivity(dailyActivity: DailyActivity) = viewModelScope.launch {
        repository.deleteDailyActivity(dailyActivity)
    }

    // 根据ID获取DailyActivity
    fun getDailyActivityById(activityId: Int): Flow<DailyActivity> {
        return repository.getDailyActivityById(activityId)
    }

    fun getActivitiesByUserId(userId: Int): Flow<List<DailyActivity>> {
        return repository.getActivitiesByUserId(userId)
    }

    // 如果已经存在该日期的表项，就更新步数信息，如果不存在，就创建一个新的表项，插入数据
    fun addOrUpdateSteps(userId: Int, date: LocalDateTime, steps: Int) {
        viewModelScope.launch {
            // 使用 firstOrNull() 从 Flow 中获取单个数据或在无数据时返回 null
            val dailyActivity = repository.getDailyActivityByIdAndDate(userId, date).firstOrNull()

            if (dailyActivity != null) {
                // 如果存在记录，更新步数
                val updatedActivity = dailyActivity.copy(steps = steps)
                repository.updateDailyActivity(updatedActivity)
            } else {
                // 如果不存在，创建新的记录并插入
                val newDailyActivity = DailyActivity(
                    userID = userId,
                    date = date,
                    steps = steps
                )
                repository.insertDailyActivity(newDailyActivity)
            }
        }
    }

    // 如果已经存在该日期的表项，就更新消耗能量信息，如果不存在，就创建一个新的表项，插入数据
    fun addOrUpdateEnergy(userId: Int, date: LocalDateTime, energy: Double) {
        viewModelScope.launch {
            // 使用 firstOrNull() 从 Flow 中获取单个数据或在无数据时返回 null
            val dailyActivity = repository.getDailyActivityByIdAndDate(userId, date).firstOrNull()

            if (dailyActivity != null) {
                // 如果存在记录，更新消耗能量
                val updatedActivity = dailyActivity.copy(energyExpenditure = energy)
                repository.updateDailyActivity(updatedActivity)
            } else {
                // 如果不存在，创建新的记录并插入
                val newDailyActivity = DailyActivity(
                    userID = userId,
                    date = date,
                    energyExpenditure = energy
                )
                repository.insertDailyActivity(newDailyActivity)
            }
        }
    }

    // 如果已经存在该日期的表项，就更走路距离，如果不存在，就创建一个新的表项，插入数据
    fun addOrUpdateWalkDistance(userId: Int, date: LocalDateTime, distance: Double) {
        viewModelScope.launch {
            // 使用 firstOrNull() 从 Flow 中获取单个数据或在无数据时返回 null
            val dailyActivity = repository.getDailyActivityByIdAndDate(userId, date).firstOrNull()

            if (dailyActivity != null) {
                // 如果存在记录，更新消耗能量
                val updatedActivity = dailyActivity.copy(walkingDistance = distance)
                repository.updateDailyActivity(updatedActivity)
            } else {
                // 如果不存在，创建新的记录并插入
                val newDailyActivity = DailyActivity(
                    userID = userId,
                    date = date,
                    walkingDistance = distance
                )
                repository.insertDailyActivity(newDailyActivity)
            }
        }
    }

    // 如果已经存在该日期的表项，就更运动时间——就说是跑步时间吧，如果不存在，就创建一个新的表项，插入数据
    fun addOrUpdateExerciseDuration(userId: Int, date: LocalDateTime, duration: String) {
        viewModelScope.launch {
            // 使用 firstOrNull() 从 Flow 中获取单个数据或在无数据时返回 null
            val dailyActivity = repository.getDailyActivityByIdAndDate(userId, date).firstOrNull()

            if (dailyActivity != null) {
                // 如果存在记录，更新消耗能量
                val updatedActivity = dailyActivity.copy(exerciseDuration = duration)
                repository.updateDailyActivity(updatedActivity)
            } else {
                // 如果不存在，创建新的记录并插入
                val newDailyActivity = DailyActivity(
                    userID = userId,
                    date = date,
                    exerciseDuration = duration
                )
                repository.insertDailyActivity(newDailyActivity)
            }
        }
    }

    // 如果已经存在该日期的表项，就更跑步离，如果不存在，就创建一个新的表项，插入数据
    fun addOrUpdateRunningDistance(userId: Int, date: LocalDateTime, distance: Double) {
        viewModelScope.launch {
            // 使用 firstOrNull() 从 Flow 中获取单个数据或在无数据时返回 null
            val dailyActivity = repository.getDailyActivityByIdAndDate(userId, date).firstOrNull()

            if (dailyActivity != null) {
                // 如果存在记录，更新消耗能量
                val updatedActivity = dailyActivity.copy(runningDistance = distance)
                repository.updateDailyActivity(updatedActivity)
            } else {
                // 如果不存在，创建新的记录并插入
                val newDailyActivity = DailyActivity(
                    userID = userId,
                    date = date,
                    runningDistance = distance
                )
                repository.insertDailyActivity(newDailyActivity)
            }
        }
    }

    // 如果已经存在该日期的表项，就更跑步离，如果不存在，就创建一个新的表项，插入数据
    fun addOrUpdateFloorsClimbed(userId: Int, date: LocalDateTime, floor: Int) {
        viewModelScope.launch {
            // 使用 firstOrNull() 从 Flow 中获取单个数据或在无数据时返回 null
            val dailyActivity = repository.getDailyActivityByIdAndDate(userId, date).firstOrNull()

            if (dailyActivity != null) {
                // 如果存在记录，更新消耗能量
                val updatedActivity = dailyActivity.copy(floorsClimbed = floor)
                repository.updateDailyActivity(updatedActivity)
            } else {
                // 如果不存在，创建新的记录并插入
                val newDailyActivity = DailyActivity(
                    userID = userId,
                    date = date,
                    floorsClimbed = floor
                )
                repository.insertDailyActivity(newDailyActivity)
            }
        }
    }

    // 添加一个函数，用于获取特定用户ID最近一天的步数信息
    fun getLatestActivityByUserId(userId: Int): Flow<DailyActivity?> {
        // 使用map转换Flow<List<DailyActivity>>为Flow<DailyActivity?>，只取最近的一条记录
        return repository.getActivitiesByUserId(userId).map { activities ->
            activities.firstOrNull()
        }
    }

    // 查找最近7条数据
    fun getLastSevenActivities(userId: Int): Flow<List<DailyActivity>> {
        return repository.getLastSevenActivities(userId)
    }

    // 该方法可以通过设置相应的日期，来实现获取对应的日期范围的数据，比如最近一年和最近1个月的数据
    // 获取用户从特定日期开始的活动数据
    fun getActivitiesFrom(startDate: LocalDateTime, userId: Int): Flow<List<DailyActivity>> {
        return repository.getActivitiesFrom(startDate, userId)
    }

    // 获取最近一个月的数据
    fun getLastMonthActivities(userId: Int): Flow<List<DailyActivity>> {
        val startDate = LocalDate.now().minusMonths(1).atStartOfDay()
        return repository.getActivitiesFrom(startDate, userId)
    }

    // 获取最近一年的数据
    fun getLastYearActivities(userId: Int): Flow<List<DailyActivity>> {
        val startDate = LocalDate.now().minusYears(1).atStartOfDay()
        return repository.getActivitiesFrom(startDate, userId)
    }

    // 计算每个月的平均步数
    fun getMonthlyAverageSteps(lastYearActivities: List<DailyActivity>): List<Int> {
        // 首先，按每个活动的月份进行分组
        val activitiesByMonth = lastYearActivities.groupBy {
            it.date.withDayOfMonth(1)  // 将日期转换为该月的第一天
        }

        // 然后，为每个月份计算平均步数
        val monthlyAverages = activitiesByMonth.map { (month, activities) ->
            // 计算这个月所有活动步数的总和
            val totalSteps = activities.sumOf { it.steps ?: 0 }

            // 如果这个月有活动记录，计算平均步数，否则为0
            val averageSteps = if (activities.isNotEmpty()) totalSteps / activities.size else 0

            // 返回计算得到的平均步数
            averageSteps
        }

        return monthlyAverages
    }


    private val _dailyActivities = MutableStateFlow<List<DailyActivity>>(emptyList())
    val dailyActivities: StateFlow<List<DailyActivity>> = _dailyActivities.asStateFlow()

    fun loadDailyActivitiesOnce(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val activities = repository.getAllActivitiesByUserIdN(userId)
            _dailyActivities.value = activities
        }
    }

    fun getDailyActivitiesByUserIdAndStore(userId: Int) {
        viewModelScope.launch {
            repository.getDailyActivitiesAndStore(userId)
        }
    }
}


class DailyActivityViewModelFactory(private val dailyActivityRepository: DailyActivityRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DailyActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DailyActivityViewModel(dailyActivityRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
