package com.app.sportsnews.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sportsnews.BR

abstract class DataBindingAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    listener: NewsAdapter.OnAdapterItemClick
) :
    ListAdapter<T, DataBindingAdapter.DataBindingViewHolder<T>>(diffCallback) {
    private val onItemClickListener = listener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    class DataBindingViewHolder<T>(
        private val binding: ViewDataBinding,
        val listener: NewsAdapter.OnAdapterItemClick
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
