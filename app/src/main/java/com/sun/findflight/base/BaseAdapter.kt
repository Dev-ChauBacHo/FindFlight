package com.sun.findflight.base

import androidx.recyclerview.widget.RecyclerView
import com.sun.findflight.utils.setFadeAnimation

abstract class BaseAdapter<T, V : BaseViewHolder<T>>(
    private val items: MutableList<T>
) : RecyclerView.Adapter<V>() {

    override fun onBindViewHolder(holder: V, position: Int) {
        getItem(position)?.let { holder.bindData(it) }
        holder.itemView.setFadeAnimation()
    }

    override fun getItemCount() = items.size

    private fun getItem(position: Int): T? =
        if (position in 0 until itemCount) items[position] else null

    open fun updateData(newData: MutableList<T>) {
        items.apply {
            clear()
            addAll(newData)
        }
        notifyDataSetChanged()
    }
    /*
        TODO add a method to update only image (origin, des) of each item flight
     */
}
