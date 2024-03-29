package com.app.sportsnews.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.app.sportsnews.R
import com.app.sportsnews.api.apimodels.Hit

class NewsAdapter(listener: OnAdapterItemClick) :
    DataBindingAdapter<Hit>(DiffCallBack(), listener) {
    class DiffCallBack : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.createdAt == newItem.createdAt
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.createdAt == newItem.createdAt
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_recyclerview_main

    interface OnAdapterItemClick {
        fun onItemClick(position: Int)
    }
}
