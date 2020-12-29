package com.paigesoftware.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.paigesoftware.roomdemo.databinding.ListItemBinding
import com.paigesoftware.roomdemo.db.Subscriber

class SubscriberRecyclerViewAdapter(
    private val subscriberList: List<Subscriber>
    ): RecyclerView.Adapter<SubscriberRecyclerViewAdapter.SubscriberViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return SubscriberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriberViewHolder, position: Int) {
        holder.bind(subscriberList[position])
    }

    override fun getItemCount(): Int {
        return subscriberList.size
    }

    class SubscriberViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(subscriber: Subscriber) {
            binding.nameTextView.text = subscriber.name
            binding.emailTextView.text = subscriber.email
        }

    }

}

