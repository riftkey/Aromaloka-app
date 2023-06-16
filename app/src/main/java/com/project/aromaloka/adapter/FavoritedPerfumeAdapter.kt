package com.project.aromaloka.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.aromaloka.databinding.ItemFavoritedPerfumeListBinding
import com.project.aromaloka.databinding.ItemHomeListBinding
import com.project.aromaloka.models.Perfume

class FavoritedPerfumeAdapter(
    private val listPerfume: MutableList<Perfume> = mutableListOf(),
    private val listener: (Perfume) -> Unit,
    private val removeListener: (Perfume) -> Unit
) : RecyclerView.Adapter<FavoritedPerfumeAdapter.ViewHolder>() {

    fun setData(data: MutableList<Perfume>) {
        listPerfume.clear()
        listPerfume.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemFavoritedPerfumeListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPerfume[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{
            listener(item)
        }
    }

    override fun getItemCount() = listPerfume.size

    inner class ViewHolder(private val binding: ItemFavoritedPerfumeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(perfume: Perfume) {
            binding.apply {
                Glide.with(itemView)
                    .load(perfume.variant_image_url)
                    .into(binding.ivPerfume)
                tvPerfumeName.text = perfume.variant
                tvPerfumeBrand.text = "by ${perfume.brand}"
                tvPerfumeGender.text = perfume.gender
                tvPerfumeRating.text = perfume.rating
                tvPerfumeSize.text = perfume.size
                tvPerfumeIngredients.text = "${perfume.top_notes1}, ${perfume.mid_notes1}, ${perfume.base_notes1}"

                btnDelete.setOnClickListener {
                    removeListener(perfume)
                }
            }
        }
    }
}