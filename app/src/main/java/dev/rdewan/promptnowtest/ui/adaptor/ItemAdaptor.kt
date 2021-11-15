package dev.rdewan.promptnowtest.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.rdewan.promptnowtest.data.ItemModel
import dev.rdewan.promptnowtest.databinding.RvItemBinding


/*
created by Richard Dewan 06/11/2021
*/

class ItemAdaptor(
    private val cbChecked: (position: Int, model: ItemModel) -> Unit,
    private val cbUnChecked: (position: Int, model: ItemModel) -> Unit
) : RecyclerView.Adapter<ItemAdaptor.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, cbChecked, cbUnChecked)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        asyncListDiffer.currentList[position]?.let {
            holder.onBind(it)
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    fun updateAdaptor(position:Int) {
        notifyItemChanged(position)
    }


    /*
    kotlin nested class which extends RecyclerView.ViewHolder
     */
    class ItemViewHolder(
        private val binding: RvItemBinding,
        private val cbChecked: (position: Int, model: ItemModel) -> Unit,
        private val cbUnChecked: (position: Int, model: ItemModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: ItemModel) {
            binding.lbItem.text = data.item
            binding.cbItem.isChecked = data.status

            binding.cbItem.setOnCheckedChangeListener { compoundButton, _ ->
                if (compoundButton.isChecked) {
                    cbChecked(adapterPosition, data)
                } else {
                    cbUnChecked(adapterPosition, data)
                }

            }
        }
    }

    /*
    DiffUtil.ItemCallback
     */
    private val diffUtil = object : DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem.item == newItem.item
        }

        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    /*
    Pass a new List to the AdapterHelper. Adapter updates will be computed on a background thread.
     */
    fun submitData(newItems: List<ItemModel>) {
        asyncListDiffer.submitList(newItems)
    }

}