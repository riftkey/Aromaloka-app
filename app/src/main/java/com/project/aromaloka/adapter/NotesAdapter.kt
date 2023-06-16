package com.project.aromaloka.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.aromaloka.databinding.ItemBrandListBinding
import com.project.aromaloka.databinding.ItemNotesListBinding
import com.project.aromaloka.models.Brand
import com.project.aromaloka.models.Note

class NotesAdapter (
    private val listNote: MutableList<Note> = mutableListOf(),
    private val listener: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    fun setData(data: MutableList<Note>) {
        listNote.clear()
        listNote.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listNote[position]
        holder.bind(item)

        if (listener != null) {
            holder.itemView.setOnClickListener { listener?.invoke(item) }
        }
    }

    override fun getItemCount() = minOf(listNote.size, 6)

    class ViewHolder(private val binding: ItemNotesListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.tvNotesName.text = note.name
        }
    }
}
