package com.project.aromaloka.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.aromaloka.R
import com.project.aromaloka.databinding.ItemBrandListBinding
import com.project.aromaloka.models.Brand


class BrandAdapter(
    private val listBrand: MutableList<Brand> = mutableListOf(),
    private val listener: (Brand) -> Unit
) : RecyclerView.Adapter<BrandAdapter.ViewHolder>() {

    fun setData(data: MutableList<Brand>) {
        listBrand.clear()
        listBrand.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBrandListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listBrand[position]
        holder.bind(item)

        if (listener != null) {
            holder.itemView.setOnClickListener { listener?.invoke(item) }
        }
    }

    override fun getItemCount() = minOf(listBrand.size, 6)

    class ViewHolder(private val binding: ItemBrandListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brand: Brand) {
            binding.tvBrandName.text = brand.name
            val cleanedBrandName = brand.name?.replace(" ", "")?.replace("&", "")?.replace(".", "")?.replace("'", "")?.toLowerCase()
            val brandLogoResId = itemView.resources.getIdentifier(cleanedBrandName, "drawable", itemView.context.packageName)
            if (brandLogoResId != 0) {
                binding.ivBrand.setImageResource(brandLogoResId)
            } else {
                binding.ivBrand.setImageResource(R.drawable.fonce)
            }
        }
    }

}
