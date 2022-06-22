package com.irakliy.presentation.main

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget
import com.irakliy.R
import com.irakliy.databinding.RecyclerItemBinding
import com.irakliy.domain.MovieModel

class MovieAdapter : ListAdapter<MovieModel, MovieAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position)
        with(holder) {
            binding.titleTextView.text = model.title
            binding.subtitleTextView.text = model.subtitle
            binding.supportingTextView.text = model.supportingText
            Glide.with(binding.root)
                .asBitmap()
                .load(model.imgUrl)
                .placeholder(R.drawable.placeholder_vertical)
                .into(object : ImageViewTarget<Bitmap>(holder.binding.imageView) {
                    override fun setResource(resource: Bitmap?) {
                        resource?.let {
                            Palette.from(resource)
                                .maximumColorCount(PALETTE_MAXIMUM_COLOR_COUNT)
                                .generate { palette ->
                                    palette?.mutedSwatch?.let {
                                        holder.binding.root.setCardBackgroundColor(it.rgb)
                                        holder.binding.titleTextView.setTextColor(it.titleTextColor)
                                        holder.binding.subtitleTextView.setTextColor(it.bodyTextColor)
                                        holder.binding.supportingTextView.setTextColor(it.bodyTextColor)
                                    }
                                }
                        }
                        holder.binding.imageView.setImageBitmap(resource)
                    }
                })
        }
    }


    override fun submitList(list: List<MovieModel>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    object DiffCallback : DiffUtil.ItemCallback<MovieModel>() {
        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(val binding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private companion object {
        const val PALETTE_MAXIMUM_COLOR_COUNT = 24
    }

}