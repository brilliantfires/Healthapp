package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.DisplayCard
import kotlinx.coroutines.flow.Flow

@Dao
interface DisplayCardDao {
    @Query("SELECT * FROM displayCards")
    fun getAllCards(): Flow<List<DisplayCard>>

    @Query("SELECT * FROM displayCards WHERE isDisplayed = 1")
    fun getDisplayedCards(): Flow<List<DisplayCard>>

    // 按照cardId来选择对应的项
    @Query("SELECT * FROM displayCards WHERE id=:cardId")
    fun getDisplayedCardByCardId(cardId: Int): DisplayCard

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(displayCard: DisplayCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(displayCards: List<DisplayCard>)

    @Update
    suspend fun updateCard(displayCard: DisplayCard)

    @Delete
    suspend fun deleteCard(displayCard: DisplayCard)
}
