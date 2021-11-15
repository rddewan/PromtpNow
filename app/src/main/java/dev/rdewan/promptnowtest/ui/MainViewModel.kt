package dev.rdewan.promptnowtest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rdewan.promptnowtest.data.ItemModel
import dev.rdewan.promptnowtest.util.ItemEvent
import dev.rdewan.promptnowtest.util.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*


/*
created by Richard Dewan 06/11/2021
*/

class MainViewModel : ViewModel() {
    //when checkbox is checked/unchecked add-remove item from selectedItem
    val selectedItem: MutableLiveData<MutableList<ItemModel>> = MutableLiveData(mutableListOf())

    private var _items: MutableStateFlow<LinkedList<ItemModel>> =
        MutableStateFlow(LinkedList<ItemModel>())
    val items: StateFlow<LinkedList<ItemModel>> get() = _items

    //item state can be : added, updated, deleted, error
    private val _itemState = MutableLiveData<ResultState<LinkedList<ItemModel>>>()
    val itemState: LiveData<ResultState<LinkedList<ItemModel>>> get() = _itemState

    fun setItemState(event: ItemEvent, position: Int = 0, item: ItemModel? = null) {
        /*
        perform the operation in lifecycle aware ViewModelScope
        this operation will be cancelled when ViewModel is destroyed - preventing memory leak
         */
        viewModelScope.launch {
            when (event) {
                is ItemEvent.AddItem -> {
                    //set state to loading
                    _itemState.value = ResultState.Loading
                    //inserts the specified element at the beginning of this list
                    _items.value.addFirst(item)
                    //set the live data for observers
                    selectedItem.value = selectedItem.value
                    //set state to added
                    _itemState.value = ResultState.Added(_items.value)
                }
                is ItemEvent.UpdateItem -> {
                    //set state to loading
                    _itemState.value = ResultState.Loading
                    //update the list item if the param item is not null
                    if (item != null) {
                        _items.value[position] = item
                    }
                    //set the live data for observers
                    selectedItem.value = selectedItem.value
                    //set state to updated
                    _itemState.value = ResultState.Updated
                }
                is  ItemEvent.DeleteItem -> {
                    //set state to loading
                    _itemState.value = ResultState.Loading
                    //delete the items from list - validate if the selected item is not null
                    selectedItem.value?.let {
                        _items.value.removeAll(it)
                        it.clear()
                    }
                    //set the live data for observers
                    selectedItem.value = selectedItem.value
                    //set state to deleted
                    _itemState.value = ResultState.Deleted(_items.value)
                }

                else -> Unit
            }
        }
    }
}