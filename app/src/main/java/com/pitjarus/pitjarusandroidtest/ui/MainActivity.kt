package com.pitjarus.pitjarusandroidtest.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.pitjarus.pitjarusandroidtest.R
import com.pitjarus.pitjarusandroidtest.databinding.ActivityMainBinding
import com.pitjarus.pitjarusandroidtest.databinding.CustomActionBarLayoutBinding
import com.pitjarus.pitjarusandroidtest.ui.auth.LoginActivity
import com.pitjarus.pitjarusandroidtest.ui.store.StoreActivity
import com.pitjarus.pitjarusandroidtest.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var _binding: ActivityMainBinding
//    binding action bar
    private lateinit var _bindingActionBar: CustomActionBarLayoutBinding
    private val _viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setupUi()
        setupObserver()
    }
    @SuppressLint("SetTextI18n")
    fun setupUi() {
//        setup action bar
        _bindingActionBar = CustomActionBarLayoutBinding.inflate(layoutInflater)

        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM)
            setCustomView(_bindingActionBar.root)
            customView.layoutParams.width = androidx.appcompat.app.ActionBar.LayoutParams.MATCH_PARENT
        }

        _bindingActionBar.textTitleActionBar.text = "Main Menu"
        _bindingActionBar.btnActionBar.apply{
            setImageDrawable(resources.getDrawable(R.drawable.ic_refresh))
            setColorFilter(resources.getColor(R.color.white))
            setOnClickListener(this@MainActivity)
        }


        val date = SimpleDateFormat("MMMM yyyy").format(System.currentTimeMillis())
        _binding.textCardMonth.text = "Kunjungan pada bulan ${date}"


        _binding.apply {
            btnKunjungan.setOnClickListener(this@MainActivity)
            btnDashboard.setOnClickListener(this@MainActivity)
            btnTarget.setOnClickListener(this@MainActivity)
            btnTransmision.setOnClickListener(this@MainActivity)
            btnLogout.setOnClickListener (this@MainActivity)
        }
    }


    fun setupObserver(){
        _viewModel.isLogin.observe(this){
            if(!it){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent).also {
                    finish()
                }
            }
        }
        _viewModel.logout.observe(this){
            if(it.status == Status.SUCCESS){
                if(it.data == true){

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent).also {
                    finish()
                }
                }
            }
        }
    }

    override fun onClick(view: View?) {
        Timber.d("onClick: ${view?.id}")
        when(view?.id) {
            _bindingActionBar.btnActionBar.id -> {
                Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show()
            }
            _binding.btnKunjungan.id -> {
                val intent = Intent(this, StoreActivity::class.java)
                startActivity(intent)
            }
            _binding.btnDashboard.id -> {
                Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show()
            }
            _binding.btnTarget.id -> {
                Toast.makeText(this, "Target", Toast.LENGTH_SHORT).show()
            }
            _binding.btnTransmision.id -> {
                Toast.makeText(this, "Transmision", Toast.LENGTH_SHORT).show()
            }
            _binding.btnLogout.id -> {
                _viewModel.logout
            }
        }
    }
}