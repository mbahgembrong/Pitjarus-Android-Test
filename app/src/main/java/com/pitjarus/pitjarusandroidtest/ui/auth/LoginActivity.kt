package com.pitjarus.pitjarusandroidtest.ui.auth

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.pitjarus.pitjarusandroidtest.ui.MainActivity
import com.pitjarus.pitjarusandroidtest.databinding.ActivityLoginBinding
import com.pitjarus.pitjarusandroidtest.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding

    private   val _viewModel : AuthViewModel by viewModels()

    private lateinit var _progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setupUI()
        setupObserver()
        checkPermission()
    }

    private fun setupUI() {
        supportActionBar?.hide()
        _progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        _progressBar.isIndeterminate = true
        _progressBar.visibility = ProgressBar.GONE
        _binding.buttonLogin.setOnClickListener {
            val username = _binding.inputUsername.text.toString().trim()
            val password = _binding.inputPassword.text.toString().trim()
            if (username.isEmpty()) {
                _binding.inputUsername.error = "Username is required"
                _binding.inputUsername.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                _binding.inputPassword.error = "Password is required"
                _binding.inputPassword.requestFocus()
                return@setOnClickListener
            }
            _viewModel.login(username,password)
        }
    }
    fun setupObserver() {
        _viewModel.loginResponse.observe(this) {
            Timber.d("Status: ${it.toString()}")
            when(it.status) {
                Status.SUCCESS -> {
                    _progressBar.visibility = ProgressBar.GONE
                    if(it.data!!.stores.isNullOrEmpty()){
                        Toast.makeText(this, it.data.message, Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent).also { finish() }
                    }
                    Timber.d("Success")
                }
                Status.ERROR -> {
                    _progressBar.visibility = ProgressBar.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    Timber.d("Error")
                }
                Status.LOADING -> {
                    _progressBar.visibility = ProgressBar.VISIBLE
                    Timber.d("Loading")
                }
            }
        }
    }

    private fun checkPermission() = runWithPermissions(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
    ) {

    }
}

