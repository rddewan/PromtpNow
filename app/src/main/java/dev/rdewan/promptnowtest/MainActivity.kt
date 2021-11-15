package dev.rdewan.promptnowtest

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.widget.textChanges
import dev.rdewan.promptnowtest.data.ItemModel
import dev.rdewan.promptnowtest.databinding.ActivityMainBinding
import dev.rdewan.promptnowtest.ui.MainViewModel
import dev.rdewan.promptnowtest.ui.adaptor.ItemAdaptor
import dev.rdewan.promptnowtest.util.Const
import dev.rdewan.promptnowtest.util.ItemEvent
import dev.rdewan.promptnowtest.util.ResultState
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    private lateinit var itemAdaptor: ItemAdaptor
    private var positionList: MutableList<Int> = mutableListOf<Int>()

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    //checkbox checked listener
    private val cbChecked: (position: Int, model: ItemModel) -> Unit = { position, data ->
        //add item to selected item list
        viewModel.selectedItem.value?.add(ItemModel(data.item, true))
        positionList.add(position)

        val updateItem = ItemModel(data.item, true)
        //sent update item event
        viewModel.setItemState(ItemEvent.UpdateItem,position,updateItem)

    }

    //checkbox uncheck listener
    private val cbUnChecked: (position: Int, model: ItemModel) -> Unit = { position, data ->
        //remove item from selected item list
        viewModel.selectedItem.value?.remove(ItemModel(data.item, true))
        positionList.remove(position)

        val updateItem = ItemModel(data.item, false)
        //sent update item event
        viewModel.setItemState(ItemEvent.UpdateItem,position,updateItem)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        //recyclerview
        setupRecyclerView()

        //start collecting flow item
        collectItem()

        //text change listener
        itemTextChangeListener()

        //Event observer
        eventObservers()

        //button click
        setupButtonClick()

    }

    private fun setupRecyclerView() {
        itemAdaptor = ItemAdaptor(cbChecked, cbUnChecked)

        binding.rvItems.apply {
            layoutManager = linearLayoutManager
            adapter = itemAdaptor
        }
    }

    private fun setupButtonClick() {
        binding.btnAdd.setOnClickListener {
            val newItem = ItemModel(
                item = binding.txtItem.text.toString(),
                status = false
            )

            viewModel.setItemState(ItemEvent.AddItem,item = newItem)
        }

        binding.btnDelete.setOnClickListener {
            viewModel.setItemState(ItemEvent.DeleteItem)
        }
    }

    /*
    collect item from flow in lifecycle aware manner
    flow collection will pause and resume based on lifecycle state
    min state when it can start collecting the flow is Lifecycle.State.STARTED
     */
    private fun collectItem() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collectLatest {
                    itemAdaptor.submitData(it)
                    binding.rvItems.post {
                        binding.rvItems.scrollToPosition(0)
                    }
                }
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun eventObservers() {
        viewModel.itemState.observe(this, { resultState ->
            when (resultState) {
                is ResultState.Loading -> {
                    //show progress bar
                    Log.d(Const.TAG, "Loading State")
                }
                is ResultState.Updated -> {
                    Log.d(Const.TAG, "Updating State")
                }
                is ResultState.Deleted -> {
                    Log.d(Const.TAG, "Deleted State")
                    binding.rvItems.post {
                        positionList.forEach {
                            itemAdaptor.notifyItemRemoved(it)
                        }
                        itemAdaptor.notifyDataSetChanged()
                        binding.rvItems.scrollToPosition(0)
                        positionList.clear()
                    }

                }
                is ResultState.Added -> {
                    Log.d(Const.TAG, "Added State")
                    clearText()
                    binding.rvItems.post {
                        itemAdaptor.notifyItemInserted(0)
                        binding.rvItems.scrollToPosition(0)
                    }
                }
                is ResultState.Error -> {
                    //show error
                    Log.d(Const.TAG, "Error State")
                }
                else -> Unit
            }
        })

        /*
        delete button state based on item selection
         */
        viewModel.selectedItem.observe(this, {
            if (it.isNotEmpty()) {
                binding.btnDelete.isEnabled = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.btnDelete.setBackgroundColor(
                        resources.getColor(
                            R.color.btn_active,
                            null
                        )
                    )
                } else {
                    binding.btnDelete.setBackgroundColor(resources.getColor(R.color.btn_active))
                }
            } else {
                binding.btnDelete.isEnabled = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.btnDelete.setBackgroundColor(
                        resources.getColor(
                            R.color.btn_inactive,
                            null
                        )
                    )
                } else {
                    binding.btnDelete.setBackgroundColor(resources.getColor(R.color.btn_inactive))
                }
            }
        })
    }

    private fun clearText() {
        binding.txtItem.text?.clear()
    }

    /*
    RxJava Binding for text change
     */
    private fun itemTextChangeListener() {
        compositeDisposable.add(
            binding.txtItem.textChanges().subscribe {
                if (it.isNotEmpty()) {
                    binding.btnAdd.isEnabled = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.btnAdd.setBackgroundColor(
                            resources.getColor(
                                R.color.btn_active,
                                null
                            )
                        )
                    } else {
                        binding.btnAdd.setBackgroundColor(resources.getColor(R.color.btn_active))
                    }
                } else {
                    binding.btnAdd.isEnabled = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.btnAdd.setBackgroundColor(
                            resources.getColor(
                                R.color.btn_inactive,
                                null
                            )
                        )
                    } else {
                        binding.btnAdd.setBackgroundColor(resources.getColor(R.color.btn_inactive))
                    }
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        //clear the binding to prevent memory leak
        _binding = null
    }
}