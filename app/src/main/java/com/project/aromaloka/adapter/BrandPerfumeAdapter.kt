package com.project.aromaloka.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.aromaloka.databinding.ItemHomeListBinding
import com.project.aromaloka.models.Perfume

class BrandPerfumeAdapter (
    private val listPerfume: MutableList<Perfume> = mutableListOf(),
    private val listener: (Perfume) -> Unit
) :
    RecyclerView.Adapter<BrandPerfumeAdapter.ViewHolder>() {

    fun setData(data: MutableList<Perfume>) {
        listPerfume.clear()
        listPerfume.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPerfume[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{
            listener(item)
        }
    }

    override fun getItemCount() = listPerfume.size

    class ViewHolder(private val binding: ItemHomeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(perfume: Perfume) {
            binding.apply {
                Glide.with(itemView)
                    .load(perfume.variant_image_url)
                    .into(binding.ivImagePerfume)
                tvVariant.text = perfume.variant
                tvCreatedBy.visibility = View.INVISIBLE
            }
        }
    }

}