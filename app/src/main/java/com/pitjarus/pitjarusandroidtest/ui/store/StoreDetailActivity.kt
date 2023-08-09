package com.pitjarus.pitjarusandroidtest.ui.store

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.common.util.concurrent.ListenableFuture
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.pitjarus.pitjarusandroidtest.R
import com.pitjarus.pitjarusandroidtest.data.model.Store
import com.pitjarus.pitjarusandroidtest.databinding.ActivityStoreDetailBinding
import com.pitjarus.pitjarusandroidtest.databinding.CameraBottomSheetBinding
import com.pitjarus.pitjarusandroidtest.databinding.CustomActionBarLayoutBinding
import com.pitjarus.pitjarusandroidtest.utils.BitmapHelper
import dagger.hilt.android.AndroidEntryPoint
import mumayank.com.airlocationlibrary.AirLocation
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class StoreDetailActivity : AppCompatActivity() {

    private lateinit var  _binding: ActivityStoreDetailBinding
    private lateinit var _bindingActionBar: CustomActionBarLayoutBinding
    lateinit var _bindingCameraBottomSheetBinding: CameraBottomSheetBinding
    private  val _viewmodel: StoreViewModel by viewModels()

    lateinit var _store : Store
    private var _airLoc: AirLocation? = null
    private var _location: LatLng? = null

    private lateinit var cameraSheetBehavior: BottomSheetBehavior<*>
    lateinit var imageUri:String
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(_binding.root)
//        get inetnt
        _store = intent.getParcelableExtra("store")!!
        setupUi()
        setupObserver()
    }
    fun setupUi(){
        _bindingActionBar = CustomActionBarLayoutBinding.inflate(layoutInflater)
        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
            customView = _bindingActionBar.root
            customView.layoutParams.width = androidx.appcompat.app.ActionBar.LayoutParams.MATCH_PARENT
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        _bindingActionBar.textTitleActionBar.text = "Verifikasi Store"
        _bindingActionBar.btnActionBar.apply{
           visibility = android.view.View.GONE
        }
        _binding.apply {
            textStoreName.text = _store.name
            textStoreAddress.text = _store.address
        }

        if(!_store.lastVisit.isNullOrEmpty()){
            _binding.textStoreDateLastVisit.text = _store.lastVisit
        }else
            _binding.textStoreDateLastVisit.text = "-"
        checkDistance(_store.distance!!)
        _binding.buttonReset.setOnClickListener {
            checkLocation()
            _viewmodel.getStore(_store.id!!)
            Toast.makeText(this, "Lokasi Berhasil Diupdate", Toast.LENGTH_SHORT).show()
        }

        _binding.btnMyLocation.setOnClickListener {
            checkLocation()
            _viewmodel.getStore(_store.id!!)
            Toast.makeText(this, "Lokasi Berhasil Diupdate", Toast.LENGTH_SHORT).show()
        }
        _binding.btnNavigation.setOnClickListener{
//            intent to google maps
            val intent = android.content.Intent(
                android.content.Intent.ACTION_VIEW,
                android.net.Uri.parse("http://maps.google.com/maps?daddr=${_store.latitude},${_store.longitude}")
            )
            startActivity(intent)
        }
        _binding.btnCamera.setOnClickListener {
            if (!checkDistance(_store.distance!!)) {
                cameraBottomSheet().show()
            }else{
                Toast.makeText(this, "Lokasi Belum Sesuai", Toast.LENGTH_SHORT).show()
            }
        }
        _binding.buttonVisit.setOnClickListener {
            if (!checkDistance(_store.distance!!)) {
                cameraBottomSheet().show()
            }else{
                Toast.makeText(this, "Lokasi Belum Sesuai", Toast.LENGTH_SHORT).show()
            }
        }
        _binding.buttonNoVisit.setOnClickListener {
            onBackPressed()
        }
    }

    fun setupObserver(){
        _viewmodel.store.observe(this){
            if(it.status == com.pitjarus.pitjarusandroidtest.utils.Status.SUCCESS){
                _store = it.data!!
                _binding.apply {
                    textStoreName.text = _store.name
                    textStoreAddress.text = _store.address
                }
                checkDistance(_store.distance!!)

            }
        }
    }

    fun checkDistance(distance:Double):Boolean= if(distance!! > 100 || distance <=0.1) {
        _binding.textUserLocation.apply {
            text = "Lokasi Belum Sesuai"
            setTextColor(Color.YELLOW)
        }
        true
    }
    else {
        _binding.textUserLocation.apply {
            text = "Lokasi Sudah Sesuai"
            setTextColor(Color.GREEN)
        }
        false
    }


    private fun checkLocation() = runWithPermissions(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
    ) {
        _airLoc = AirLocation(this, true,  true, object : AirLocation.Callbacks {

            override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                Timber.d("Location Failed: ${locationFailedEnum.name}")
            }

            override fun onSuccess(location: Location) {
                Timber.d("Location Success: ${location.latitude} ${location.longitude}")
                _viewmodel.setMyLocation( location.latitude, location.longitude)
                _location = LatLng(location.latitude, location.longitude)
            }
        })
    }

    private fun checkCamera() = runWithPermissions(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_MEDIA_IMAGES ,
        android.Manifest.permission.READ_MEDIA_VIDEO,
        android.Manifest.permission.READ_MEDIA_AUDIO,
    ){
        Timber.d("Camera Success")
        _bindingCameraBottomSheetBinding.previewCamera.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            startCamera()
        }

        startCamera()
    }

    fun cameraBottomSheet(): BottomSheetDialog {
        val dialog = BottomSheetDialog(this)
        _bindingCameraBottomSheetBinding = CameraBottomSheetBinding.inflate(layoutInflater, _binding.root, false)
        dialog.setContentView(_bindingCameraBottomSheetBinding.root)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()
        checkCamera()
        cameraSheetBehavior = (dialog as BottomSheetDialog).behavior
        cameraSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        cameraSheetBehavior.isHideable = true
        _bindingCameraBottomSheetBinding.buttonTakeCamera.setOnClickListener {
            takePhoto()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                animateFlash()
                dialog.dismiss()
            }
        }
        return dialog
    }

    private fun startCamera() {
        _bindingCameraBottomSheetBinding.apply {
            previewCamera.visibility = View.VISIBLE
            buttonTakeCamera.visibility = View.VISIBLE
        }
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(_bindingCameraBottomSheetBinding.previewCamera.createSurfaceProvider())
        }
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun animateFlash() {
        _binding.root.postDelayed({
            _binding.root.foreground = ColorDrawable(Color.WHITE)
            _binding.root.postDelayed({
                _binding.root.foreground = null
            }, 50)
        }, 100)
    }
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val name = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Timber.e("Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    imageUri = output.savedUri.toString()
                    Timber.d("Photo capture succeeded: $imageUri")
                    _viewmodel.setVisited(_store.id!!)
                    val intent = Intent(baseContext, StoreCompletedActivity::class.java)
                    intent.putExtra("imageUri", imageUri)
                    intent.putExtra("store", _store)
                    startActivity(intent).also {
                        finish()
                    }
                }
            }
        )
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}