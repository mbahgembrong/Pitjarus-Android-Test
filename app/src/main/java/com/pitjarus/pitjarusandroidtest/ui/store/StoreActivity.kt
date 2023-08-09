package com.pitjarus.pitjarusandroidtest.ui.store

import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.pitjarus.pitjarusandroidtest.R
import com.pitjarus.pitjarusandroidtest.adapter.StoreItemAdapter
import com.pitjarus.pitjarusandroidtest.data.model.Store
import com.pitjarus.pitjarusandroidtest.databinding.ActivityStoreBinding
import com.pitjarus.pitjarusandroidtest.databinding.CustomActionBarLayoutBinding
import com.pitjarus.pitjarusandroidtest.utils.BitmapHelper
import com.pitjarus.pitjarusandroidtest.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import mumayank.com.airlocationlibrary.AirLocation
import timber.log.Timber
import java.text.SimpleDateFormat

@AndroidEntryPoint
class StoreActivity : AppCompatActivity(), StoreItemAdapter.OnItemClickListener, OnMapReadyCallback {

    private lateinit var _binding: ActivityStoreBinding
    private lateinit var _bindingActionBar: CustomActionBarLayoutBinding
    private val _viewmodel: StoreViewModel by viewModels()
    private lateinit var _adapter : StoreItemAdapter

    private lateinit var _progressbar :ProgressBar

    private var _airLoc: AirLocation? = null
    private var _location: LatLng? = null
    private var _gMap: GoogleMap? = null
    private lateinit var _mapFragment: SupportMapFragment
    private var _stores : List<Store>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setupUi()

        setupObserver()
    }

    fun setupUi(){
//        setup action bar
        _bindingActionBar = CustomActionBarLayoutBinding.inflate(layoutInflater)
        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
            customView = _bindingActionBar.root
            customView.layoutParams.width = androidx.appcompat.app.ActionBar.LayoutParams.MATCH_PARENT
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        _bindingActionBar.textTitleActionBar.text = "List Store"
        _bindingActionBar.btnActionBar.apply{
            setImageDrawable(resources.getDrawable(R.drawable.ic_book))
        }

        _progressbar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        _progressbar.isIndeterminate = true
        _progressbar.visibility = ProgressBar.GONE

        val date = SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis())

        _binding.textKunjungan.text = "List Kunjungan ${date}"

        _binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                _viewmodel.search(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                _viewmodel.search(newText!!)
                return true
            }
        })
        setupRecyclerView()
        setupMap()
    }

    fun setupRecyclerView() {
        _adapter = StoreItemAdapter(this)
        _binding.rvStore.layoutManager = LinearLayoutManager(this)
        _binding.rvStore.adapter = _adapter

    }

    fun setupMap(){
        _mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        _mapFragment.getMapAsync(this)
    }
     fun setupObserver(){
         _viewmodel.stores.observe(this) {
             when (it.status) {
                 Status.SUCCESS -> {
                        _progressbar.visibility = ProgressBar.GONE
                     Timber.d("Status: ${it.data.toString()}")
                     if(!it.data.isNullOrEmpty()) {
                         _adapter.setStoreList(it.data)
                         _stores = it.data
                         markerStoreMap(it.data, _gMap)

                     }
                 }
                 Status.ERROR -> {
                     _progressbar.visibility = ProgressBar.GONE
                     Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                 }
                 Status.LOADING -> {
                        _progressbar.apply {
                            visibility = ProgressBar.VISIBLE
                            isIndeterminate = true
                        }
                 }
             }
         }
     }

    fun markerStoreMap (stores:List<Store>, googleMap: GoogleMap?){
        if(googleMap != null){
            for (store in stores){
                if(store.latitude != null && store.longitude != null){
                    val latlng = LatLng(store.latitude!!, store.longitude!!)
                    googleMap.addMarker(
                        MarkerOptions().title(store.name).position(latlng)
                    )
                }
            }

        }
    }

    private fun checkPermission() = runWithPermissions(
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
                _gMap?.apply {
                    clear()
                    addMarker(
                        MarkerOptions().position(_location!!).title("Your Location").icon(
                            myLocationIcon
                        )
                    )
                    val color = Color.parseColor("#A5E3FA").and(0x65FFFFFF)
                    addCircle(
                        CircleOptions().center(_location).radius(100.0).fillColor(color).strokeColor(color)
                    )
                    moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(_location, 15f))
                }
            }
        })

    }
    private val myLocationIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.blue)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_my_location, color)
    }

    override fun onItemClick(store: Store, position: Int) {
            val intent = Intent(this, StoreDetailActivity::class.java)
            intent.putExtra("store", store)
            startActivity(intent)
    }

    override fun setOnLocationChangeListener(any: Any) {
        checkPermission()

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if(googleMap != null) {
            _gMap = googleMap
            checkPermission()
            _viewmodel.search("")
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}