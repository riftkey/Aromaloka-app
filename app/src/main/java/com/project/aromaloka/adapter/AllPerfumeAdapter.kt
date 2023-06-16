package com.project.aromaloka.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.aromaloka.R
import com.project.aromaloka.databinding.ItemPerfumeListBinding
import com.project.aromaloka.models.Perfume


class AllPerfumeAdapter (
    private val context: Context,
    private val listPerfume: MutableList<Perfume> = mutableListOf(),
    private val listener: (Perfume) -> Unit
) :
    RecyclerView.Adapter<AllPerfumeAdapter.ViewHolder>() {

    fun setData(data: MutableList<Perfume>) {
        listPerfume.clear()
        listPerfume.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPerfumeListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val perfume = listPerfume[position]
        holder.bind(perfume)

        holder.itemView.setOnClickListener {
            perfume.isClicked = !perfume.isClicked
            holder.setViewToColor(perfume.isClicked)
            listener.invoke(perfume)
        }
    }

    override fun getItemCount() = listPerfume.size

    inner class ViewHolder(private val binding: ItemPerfumeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(perfume: Perfume) {
            binding.apply {
                Glide.with(itemView)
                    .load(perfume.variant_image_url)
                    .into(binding.ivBrand)
                tvPerfumeName.text = perfume.variant
                setViewToColor(perfume.isClicked)
            }
        }

        fun setViewToColor(isClicked: Boolean) {
            if (isClicked) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.primaryColorPink))
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }
        }
    }
}
