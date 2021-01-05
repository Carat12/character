package com.example.character.ui.character

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.character.R
import com.example.character.data.model.Character
import com.example.character.databinding.ItemCharacterBinding
import com.example.character.ui.AdapterListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_character.view.*

class CharacterAdapter(var mContext: Context): RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    private lateinit var listener: AdapterListener
    private var mList: ArrayList<Character> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemCharacterBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position], position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(var itemBinding: ItemCharacterBinding): RecyclerView.ViewHolder(itemBinding.root){
        fun bind(character: Character, position: Int) {
            itemBinding.character = character
            Picasso
                .get()
                .load(character.image)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(itemBinding.root.img_view)
            itemBinding.root.setOnClickListener {
                listener.onItemClicked(it, position)
            }
        }
    }

    fun setListener(listener: AdapterListener){
        this.listener = listener
    }

    fun setData(t: ArrayList<Character>) {
        mList = t
        notifyDataSetChanged()
    }

    fun getItemData(position: Int): Character {
        return mList[position]
    }

    fun addData(t: Character) {
        mList.add(t)
        notifyDataSetChanged()
    }
}