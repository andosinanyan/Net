package com.example.net.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.net.databinding.ImageSliderItemViewBinding

class ImageSlider : RecyclerView.Adapter<ImageSlider.ViewHolder>() {

    private val imagesList = mutableListOf<String>()

    class ViewHolder(val binding: ImageSliderItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ImageSliderItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.apply {
                imgIntro.load(imagesList[position])
            }
        }
    }

    override fun getItemCount() = imagesList.size

    fun setImagesData(imagesList: List<String>) {
        this.imagesList.clear()
        this.imagesList.addAll(imagesList)
        notifyItemRangeChanged(0, imagesList.size)
        //#FF3B30
        //#39C118
    }
}