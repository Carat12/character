package com.example.character.ui.episode

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.character.data.model.Episode
import com.example.character.databinding.ItemEpisodeBinding
import com.example.character.ui.AdapterListener

class EpisodeAdapter(var mContext: Context): RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    private lateinit var listener: AdapterListener
    private var mList: ArrayList<Episode> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemEpisodeBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position], position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(var itemBinding: ItemEpisodeBinding): RecyclerView.ViewHolder(itemBinding.root){
        fun bind(episode: Episode, position: Int) {
            itemBinding.episode = episode
            itemBinding.root.setOnClickListener {
                listener.onItemClicked(it, position)
            }
        }
    }

    fun setAdapterListener(listener: AdapterListener){
        this.listener = listener
    }

    fun getItemData(position: Int): Episode{
        return mList[position]
    }

    fun setData(t: ArrayList<Episode>) {
        mList = t
        notifyDataSetChanged()
    }
}