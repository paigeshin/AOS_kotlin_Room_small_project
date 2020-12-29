package com.paigesoftware.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.paigesoftware.roomdemo.databinding.ListItemBinding
import com.paigesoftware.roomdemo.db.Subscriber

class SubscriberRecyclerViewAdapter(
    private val clickListener: (Subscriber) -> Unit
    ): RecyclerView.Adapter<SubscriberRecyclerViewAdapter.SubscriberViewHolder>() {

    private val subscriberList: ArrayList<Subscriber> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return SubscriberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriberViewHolder, position: Int) {
        holder.bind(subscriberList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return subscriberList.size
    }

    fun setList(subscribers: List<Subscriber>) {
        subscriberList.clear()
        subscriberList.addAll(subscribers)
    }

    //view가 아니라 binding을 넘겨준다.
    class SubscriberViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> Unit) {
//        fun bind(subscriber: Subscriber) {
            binding.nameTextView.text = subscriber.name
            binding.emailTextView.text = subscriber.email
            binding.listItemLayout.setOnClickListener {
                clickListener(subscriber)
            }
        }

    }

}

