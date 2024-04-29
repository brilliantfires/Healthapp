package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.DisplayCardDao
import com.example.healthapp.data.entity.DisplayCard
import kotlinx.coroutines.flow.Flow

class DisplayCardRepository(private val displayCardDao: DisplayCardDao) {
    val allCards: Flow<List<DisplayCard>> = displayCardDao.getAllCards()
    suspend fun getAllCards(): List<DisplayCard> {
        return displayCardDao.getAllCardsN()
    }

    val displayedCards: Flow<List<DisplayCard>> = displayCardDao.getDisplayedCards()

    suspend fun insert(displayCard: DisplayCard) {
        displayCardDao.insertCard(displayCard)
    }

    suspend fun update(displayCard: DisplayCard) {
        displayCardDao.updateCard(displayCard)
    }

    suspend fun delete(displayCard: DisplayCard) {
        displayCardDao.deleteCard(displayCard)
    }

    suspend fun getDisplayedCardByCardId(cardId: Int): DisplayCard {
        return displayCardDao.getDisplayedCardByCardId(cardId)
    }
}
