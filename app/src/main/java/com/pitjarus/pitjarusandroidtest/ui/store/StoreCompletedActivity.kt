package com.pitjarus.pitjarusandroidtest.ui.store

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.pitjarus.pitjarusandroidtest.adapter.DashboardItemAdapter
import com.pitjarus.pitjarusandroidtest.data.model.Store
import com.pitjarus.pitjarusandroidtest.databinding.ActivityStoreCompletedBinding
import com.pitjarus.pitjarusandroidtest.databinding.CustomActionBarLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreCompletedActivity : AppCompatActivity(),View.OnClickListener, DashboardItemAdapter.OnItemClickListener {

    private lateinit var  _binding: ActivityStoreCompletedBinding
    private lateinit var _bindingActionBar: CustomActionBarLayoutBinding
    private  val _viewmodel: StoreViewModel by viewModels()

    lateinit var _store : Store
    lateinit var  _adapter: DashboardItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoreCompletedBinding.inflate(layoutInflater)

        setContentView(_binding.root)

        //        get inetnt
        _store = intent.getParcelableExtra("store")!!

        setupUi()
        setupObserver()
    }
    fun setupUi(){
        _binding.textInformation.isSelected = true
        _bindingActionBar = CustomActionBarLayoutBinding.inflate(layoutInflater)
        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
            customView = _bindingActionBar.root
            customView.layoutParams.width = androidx.appcompat.app.ActionBar.LayoutParams.MATCH_PARENT
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        _bindingActionBar.textTitleActionBar.text = "Main Menu"
        _bindingActionBar.btnActionBar.apply{
            visibility = android.view.View.GONE
        }

        _binding.apply {
            textStoreName.text = _store.name
            textStoreCode.text = _store.code
        }

        if(!_store.visitThisMonth.isNullOrEmpty()){
            _binding.textPerfectStoreSuccessCompleted.visibility = View.VISIBLE
            _binding.textPerfectStoreDangerCompleted.visibility = View.GONE
        }else{
            _binding.textPerfectStoreSuccessCompleted.visibility = View.GONE
            _binding.textPerfectStoreDangerCompleted.visibility = View.VISIBLE
        }

        _binding.apply {
            btnInformation.setOnClickListener(this@StoreCompletedActivity)
            btnProdukCheck.setOnClickListener(this@StoreCompletedActivity)
            btnSKUPromo.setOnClickListener(this@StoreCompletedActivity)
            btnRingkasanOOS.setOnClickListener(this@StoreCompletedActivity)
            btnStoreInvestment.setOnClickListener(this@StoreCompletedActivity)
            buttonSelesai.setOnClickListener(this@StoreCompletedActivity)
        }
        setupRecycleView()

        _viewmodel.getStore(_store.id)

    }

    fun setupRecycleView(){
        _adapter = DashboardItemAdapter(this)
//        grid horizontal layout
        _binding.recyclerView.apply {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@StoreCompletedActivity, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = _adapter
        }
        _adapter.setDashboardItemList(listOf("OSA", "NPD"));
    }
    fun setupObserver(){
        _viewmodel.store.observe(this){
            if(it.status == com.pitjarus.pitjarusandroidtest.utils.Status.SUCCESS){
                _binding.apply {
                    textStoreName.text = _store.name
                    textStoreCode.text = _store.code
                }

                if(!_store.visitThisMonth.isNullOrEmpty()){
                    _binding.textPerfectStoreSuccessCompleted.visibility = View.VISIBLE
                    _binding.textPerfectStoreDangerCompleted.visibility = View.GONE
                }else{
                    _binding.textPerfectStoreSuccessCompleted.visibility = View.GONE
                    _binding.textPerfectStoreDangerCompleted.visibility = View.VISIBLE
                }
                _store = it.data!!
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View?) {
        when(view?.id){
            _binding.btnInformation.id -> Toast.makeText(this, "Information", Toast.LENGTH_SHORT).show()
            _binding.btnProdukCheck.id -> Toast.makeText(this, "Produk Check", Toast.LENGTH_SHORT).show()
            _binding.btnSKUPromo.id -> Toast.makeText(this, "SKU Promo", Toast.LENGTH_SHORT).show()
            _binding.btnRingkasanOOS.id -> Toast.makeText(this, "Ringkasan OOS", Toast.LENGTH_SHORT).show()
            _binding.btnStoreInvestment.id -> Toast.makeText(this, "Store Investment", Toast.LENGTH_SHORT).show()
            _binding.buttonSelesai.id -> {
                Toast.makeText(this, "Selesai", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }

        }
    }

//    adapter
    override fun onItemClick(position: Int) {
    }

}