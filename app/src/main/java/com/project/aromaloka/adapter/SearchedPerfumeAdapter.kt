package com.project.aromaloka.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.aromaloka.databinding.ItemSearchedPerfumeBinding
import com.project.aromaloka.models.Perfume

class SearchedPerfumeAdapter(
    private val listPerfume: MutableList<Perfume> = mutableListOf(),
    var listener: (Perfume) -> Unit
) : RecyclerView.Adapter<SearchedPerfumeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchedPerfumeBinding.inflate(
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
            listener.invoke(item)
        }
    }

    override fun getItemCount() = listPerfume.size

    fun setData(data: MutableList<Perfume>) {
        listPerfume.clear()
        listPerfume.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemSearchedPerfumeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(perfume: Perfume) {

            binding.apply {
                Glide.with(itemView)
                    .load(perfume.variant_image_url)
                    .into(binding.ivPerfume)
                tvPerfumeName.text = perfume.variant
                tvPerfumeBrand.text = "by ${perfume.brand}"
                tvPerfumeGender.text = perfume.gender
                tvPerfumePrice.text = "Rp${perfume.price}"
                tvPerfumeRating.text = perfume.rating
                tvPerfumeSize.text = perfume.size
                tvPerfumeIngredients.text = "${perfume.top_notes1}, ${perfume.mid_notes1}, ${perfume.base_notes3}"
            }
        }
    }
}
