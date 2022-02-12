package com.example.appcv.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcv.databinding.EventRvLayoutBinding
import com.example.appcv.model.Event
import io.realm.RealmResults


class EventsAdapter(
    private val context: Context, private val eventsList: RealmResults<Event>
) : RecyclerView
.Adapter<EventsAdapter.EventViewHolder>() {

    class EventViewHolder(
        val binding: EventRvLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventRvLayoutBinding.inflate(inflater, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.binding.eventTitle.text = eventsList[position]!!.title
        holder.binding.eventDescription.text = eventsList[position]!!.description
        holder.binding.eventTime.text = eventsList[position]!!.getTimeRange()
    }

    override fun getItemCount() = eventsList.size
}