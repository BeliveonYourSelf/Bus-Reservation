package com.bus.reservation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bus.reservation.comman.isVisable
import com.bus.reservation.databinding.ItemLoaderBinding

class LoaderAdapter : LoadStateAdapter<LoaderAdapter.LoaderHolder>() {

    inner class LoaderHolder(val binding: ItemLoaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.progress.isVisable(loadState is LoadState.Loading)
        }
    }

    override fun onBindViewHolder(holder: LoaderHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderHolder {
        return LoaderHolder(
            ItemLoaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}