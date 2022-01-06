package com.darothub.weatherapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.darothub.weatherapp.data.database.Climate
import com.darothub.weatherapp.databinding.ForecastItemLayoutBinding

class DataAdapter : RecyclerView.Adapter<DataViewHolder>() {
    private var dataList: List<Climate> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ForecastItemLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = dataList[position]
        holder.bindTo(item)
        holder.setIsRecyclable(false)
    }
    override fun getItemCount() = dataList.size

    fun setData(data: List<Climate>) {
        val dataDiffUtils = DataDiffUtils(dataList, data)
        val result = DiffUtil.calculateDiff(dataDiffUtils)
        dataList = data.take(5)
        result.dispatchUpdatesTo(this)
    }
}

class DataDiffUtils(
    private val oldList: List<Climate>,
    private val newList: List<Climate>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].dt == newList[newItemPosition].dt
    }
}
