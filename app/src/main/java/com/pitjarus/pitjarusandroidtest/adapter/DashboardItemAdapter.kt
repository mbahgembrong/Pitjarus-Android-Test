package com.pitjarus.pitjarusandroidtest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pitjarus.pitjarusandroidtest.databinding.DashboardStoreItemBinding
import com.pitjarus.pitjarusandroidtest.ui.store.StoreCompletedActivity

class DashboardItemAdapter(
    private val listener: OnItemClickListener,
):RecyclerView.Adapter<DashboardItemAdapter.DashboardItemViewHolder>(){
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    private val dashboardItemList = ArrayList<String>()

    fun setDashboardItemList(dashboardItemList: List<String>){
        this.dashboardItemList.clear()
        this.dashboardItemList.addAll(dashboardItemList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardItemViewHolder {
        val binding = DashboardStoreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashboardItemViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = dashboardItemList.size

    override fun onBindViewHolder(holder: DashboardItemViewHolder, position: Int) = holder.bind(dashboardItemList[position])

    class DashboardItemViewHolder(private val binding: DashboardStoreItemBinding, private val listener: OnItemClickListener):RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(dashboardItem: String){

        }

        override fun onClick(p0: View?) {
            if (p0 != null) {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}