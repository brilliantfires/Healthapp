package com.example.healthapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.DisplayCard
import com.example.healthapp.data.repository.DisplayCardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DisplayCardViewModel(private val repository: DisplayCardRepository) : ViewModel() {
    val allCards: LiveData<List<DisplayCard>> = repository.allCards.asLiveData()
    val displayedCards: LiveData<List<DisplayCard>> = repository.displayedCards.asLiveData()

    fun insert(displayCard: DisplayCard) = viewModelScope.launch {
        repository.insert(displayCard)
    }

    fun update(displayCard: DisplayCard) = viewModelScope.launch {
        repository.update(displayCard)
    }

    fun delete(displayCard: DisplayCard) = viewModelScope.launch {
        repository.delete(displayCard)
    }

    fun updateCardDisplayStatus(cardId: Int, isDisplayed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            // Flow不可以直接使用copy来进行修改
            val updatedCard =
                repository.getDisplayedCardByCardId(cardId).copy(isDisplayed = isDisplayed)
            repository.update(updatedCard)
        }
    }


}

class DisplayCardViewModelFactory(private val displayCardRepository: DisplayCardRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DisplayCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DisplayCardViewModel(displayCardRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
