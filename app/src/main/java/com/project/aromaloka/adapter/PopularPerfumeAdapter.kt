package com.project.aromaloka.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.aromaloka.databinding.ItemHomeListBinding
import com.project.aromaloka.databinding.ItemHomePopularListBinding
import com.project.aromaloka.models.Perfume
import com.project.aromaloka.ui.detail.PerfumeDetailActivity

class PopularPerfumeAdapter(
    private val listPerfume: MutableList<Perfume> = mutableListOf(),
    private val listener: (Perfume) -> Unit
) : RecyclerView.Adapter<PopularPerfumeAdapter.ViewHolder>() {

    fun setData(data: MutableList<Perfume>) {
        listPerfume.clear()
        listPerfume.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomePopularListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPerfume[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount() = minOf(listPerfume.size, 20)

    class ViewHolder(private val binding: ItemHomePopularListBinding) :
        RecyclerView.ViewHolder(binding.root) {



        fun bind(perfume: Perfume) {
            binding.apply {
                Glide.with(itemView)
                    .load(perfume.variant_image_url)
                    .into(binding.ivImagePerfume)
                tvVariant.text = perfume.variant
                tvCreatedBy.text = "by ${perfume.brand}"
            }
        }
    }
}

