package com.yjiang45.project3.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.yjiang45.project3.database.Items
import com.yjiang45.project3.database.ItemsRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    init {
        ItemsRepository.initialize(application)
    }

    private val itemRepository = ItemsRepository.get()
    val items = itemRepository.getAllItems()
    val unCheckedItems = itemRepository.getUnchecked()

    fun insert(items: Items) {
        itemRepository.insert(items)
    }

    fun deleteWord(items: Items) {
        itemRepository.deleteItem(items)
    }

    fun updateItem(items: Items){
        itemRepository.updateItem(items)
    }

    fun deleteAll(){
        itemRepository.deleteAll()
    }
}
