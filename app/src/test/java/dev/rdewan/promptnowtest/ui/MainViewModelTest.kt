package dev.rdewan.promptnowtest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import dev.rdewan.promptnowtest.data.ItemModel
import dev.rdewan.promptnowtest.util.CoroutinesTestRule
import dev.rdewan.promptnowtest.util.ItemEvent
import dev.rdewan.promptnowtest.util.ResultState
import dev.rdewan.promptnowtest.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest


import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var itemModel: ItemModel
    private lateinit var updateModel: ItemModel
    private lateinit var itemModelList: MutableList<ItemModel>

    @Rule
    @JvmField
    val instantTaskExecutorRules = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var coroutinesTestRule = CoroutinesTestRule()


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        viewModel = MainViewModel()

        itemModel = ItemModel("Item 1", false)
        updateModel = ItemModel("Item 1", true)
        itemModelList = mutableListOf(
            ItemModel("Item 1", false),
            ItemModel("Item 3", false),
            ItemModel("Item 3", false),
        )
    }

    /*
    TEST add item
    1. add item to list
    2. assert items value contains itemModel
    3. assert itemState is set to ResultState.Added(itemModel)
     */
    @ExperimentalCoroutinesApi
    @Test
    fun test_add_item() = coroutinesTestRule.testDispatcher.runBlockingTest {
        //set add state
        viewModel.setItemState(ItemEvent.AddItem,item = itemModel)

        //assert flow data contain itemModel
        assertThat(viewModel.items.value.contains(itemModel))
        //assert live data state is ResultState.Added(itemModel)
        assertThat(viewModel.itemState.getOrAwaitValue().equals(ResultState.Added(itemModel)))
    }

    /*
    TEST update event
    1. add item to list
    2. add item to selected list
    3. send update event
    4. assert selectedItem contains updateModel
    5. assert itemState is set to ResultState.Updated
    6. assert item status value is update to true
     */
    @ExperimentalCoroutinesApi
    @Test
    fun test_update_item() = coroutinesTestRule.testDispatcher.runBlockingTest {
        //set add state
        viewModel.setItemState(ItemEvent.AddItem,item = itemModel)
        //set selected item
        viewModel.selectedItem.value?.add(updateModel)
        //set update state
        viewModel.setItemState(ItemEvent.UpdateItem,0, item = updateModel)


        //assert livedata selectedItem contain updateModel
        assertThat(viewModel.selectedItem.getOrAwaitValue().contains(updateModel))
        //assert live data state is Updated
        Truth.assertThat(viewModel.itemState.getOrAwaitValue() == ResultState.Updated)
        //assert item status value is update to true
        assertThat(viewModel.items.value.first.status)

    }

    /*
    TEST delete event
    1. add item to list
    2. add item to selected list
    3. send delete event
    4. assert selectedItem contains updateModel
    5. assert itemState is set to ResultState.Deleted(viewModel.items)
    6. assert items size is set to 0 after delete
     */
    @ExperimentalCoroutinesApi
    @Test
    fun test_delete_item() = coroutinesTestRule.testDispatcher.runBlockingTest {
        //set add state
        viewModel.setItemState(ItemEvent.AddItem,item = itemModel)
        //set selected item
        viewModel.selectedItem.value?.add(updateModel)
        //set update state
        viewModel.setItemState(ItemEvent.DeleteItem)

        //assert livedata selectedItem contain updateModel
        assertThat(viewModel.selectedItem.getOrAwaitValue().contains(updateModel))
        //assert live data state is deleted
        Truth.assertThat(viewModel.itemState.getOrAwaitValue() == ResultState.Deleted(viewModel.items))
        //assert the item was deleted and list is set to 0
        Truth.assertThat(viewModel.items.value.size == 0)

    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {

    }
}