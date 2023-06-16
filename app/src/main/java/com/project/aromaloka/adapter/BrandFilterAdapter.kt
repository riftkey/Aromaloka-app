package com.project.aromaloka.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.project.aromaloka.R
import com.project.aromaloka.databinding.ItemBrandListBinding
import com.project.aromaloka.models.Brand
import com.project.aromaloka.models.Note

@RequiresApi(Build.VERSION_CODES.M)
class BrandFilterAdapter(
    private val context: Context,
    private val listBrand: MutableList<Brand> = mutableListOf(),
//    private val brandList: ArrayList<Brand>
) : RecyclerView.Adapter<BrandFilterAdapter.ViewHolder>() {

    val listItemClicked = mutableListOf<String>()

    fun setData(data: MutableList<Brand>) {
        listBrand.clear()
        listBrand.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemBrandListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listBrand[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            item.isClicked = !item.isClicked
            if (item.isClicked) {
                listItemClicked.add(item.name)
            } else {
                listItemClicked.remove(item.name)
            }
            holder.setViewToColor(item.isClicked)
        }
    }

    override fun getItemCount() = listBrand.size

    class ViewHolder(private var itemBinding: ItemBrandListBinding, var context: Context) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(brand: Brand) {
            setViewToColor(brand.isClicked)
            itemBinding.tvBrandName.text = brand.name
            val cleanedBrandName = brand.name?.replace(" ", "")?.replace("&", "")?.replace(".", "")?.replace("'", "")?.toLowerCase()
            val brandLogoResId = itemView.resources.getIdentifier(cleanedBrandName, "drawable", itemView.context.packageName)
            if (brandLogoResId != 0) {
                itemBinding.ivBrand.setImageResource(brandLogoResId)
            } else {
                itemBinding.ivBrand.setImageResource(R.drawable.fonce)
            }
        }

        fun setViewToColor(isClicked: Boolean) {
            if (isClicked) {
                itemBinding.layoutContent.setBackgroundColor(context.getColor(R.color.primaryColorPink))
            } else {
                itemBinding.layoutContent.setBackgroundColor(context.getColor(R.color.primaryColorGreen))
            }
        }
    }

}