package com.etcetera.cabtest

import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.etcetera.cabtest.databinding.ItemSimpleBinding

class SimpleAdapter(private val items: MutableList<Alarm>, private val listener: Event) : RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>() {
    private var actionStatus = false

    override fun getItemCount(): Int = items.size

    fun setActionMode(status: Boolean) {
        actionStatus = status
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.binding.textName.text = items[position].id.toString()
        holder.binding.iCheck.visibility = if (actionStatus) View.VISIBLE else View.GONE
        holder.binding.iCheck.isChecked = items[position].selected
        holder.binding.ivEnable.setImageDrawable(ContextCompat.getDrawable(holder.binding.root.context, if (items[position].status) R.drawable.ic_alarm_dark else R.drawable.ic_alarm ))
        holder.binding.iCheck.setOnClickListener {
            if (actionStatus) {
                items[position].selected = !items[position].selected
            }
        }
        holder.binding.root.setOnClickListener {
            if (actionStatus) {
                holder.binding.iCheck.isChecked = !items[position].selected
                items[position].selected = !items[position].selected
            } else {
                listener.onViewItem(position)
            }
        }
        holder.binding.ivEnable.setOnClickListener {
            if (!actionStatus) {
                holder.binding.ivEnable.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.binding.root.context,
                        if (items[position].status) R.drawable.ic_alarm else R.drawable.ic_alarm_dark
                    )
                )
                listener.onToggleAlarm(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val binding = DataBindingUtil.inflate<ItemSimpleBinding>(LayoutInflater.from(parent.context), R.layout.item_simple, parent, false)
        return SimpleViewHolder(binding)
    }

    inner class SimpleViewHolder(val binding: ItemSimpleBinding) : RecyclerView.ViewHolder(binding.root)

    interface Event {
        fun onToggleAlarm(position: Int)
        fun onViewItem(position: Int)
    }
}