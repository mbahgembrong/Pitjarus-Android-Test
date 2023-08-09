package com.pitjarus.pitjarusandroidtest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pitjarus.pitjarusandroidtest.data.model.Store
import com.pitjarus.pitjarusandroidtest.databinding.StoreItemBinding
import com.pitjarus.pitjarusandroidtest.utils.distaneConverter
import timber.log.Timber
import javax.inject.Inject

class StoreItemAdapter(
    private val listener:OnItemClickListener,

) : RecyclerView.Adapter<StoreItemAdapter.StoreItemViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(store: Store, position: Int)
        abstract fun setOnLocationChangeListener(any: Any)
    }

    private val storeList = ArrayList<Store>()

    fun setStoreList(storeList: List<Store>) {
        this.storeList.clear()
        this.storeList.addAll(storeList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreItemViewHolder {
        val binding = StoreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreItemViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = storeList.size

    override fun onBindViewHolder(holder: StoreItemViewHolder, position: Int) = holder.bind(storeList[position])
class StoreItemViewHolder(private val  binding: StoreItemBinding, private val listener: StoreItemAdapter.OnItemClickListener) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {


    private lateinit var store: Store

    init {
        binding.root.setOnClickListener(this)
    }

    fun bind(store: Store) {
        this.store = store
        binding.textTokoName.text = store.name
        if (!store.visitThisMonth.isNullOrEmpty()){
            binding.textPerfectStore.visibility = View.VISIBLE
        }else
            binding.textPerfectStore.visibility = View.GONE
        Timber.d("store distance ${store.distance}")
        if(store.distance != 0.0){
            binding.textJarak.apply {
                text = "${distaneConverter(store.distance!!)})}"
                visibility = View.VISIBLE
            }
            binding.imageLocation.visibility = View.VISIBLE
        }else{
            binding.textJarak.visibility = View.INVISIBLE
            binding.imageLocation.visibility = View.INVISIBLE
        }
    }

    override fun onClick(view: View?) {
        listener.onItemClick(store, adapterPosition)
    }
    }
}


