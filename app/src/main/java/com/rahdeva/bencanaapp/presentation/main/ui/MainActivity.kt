package com.rahdeva.bencanaapp.presentation.main.ui

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rahdeva.bencanaapp.R
import com.rahdeva.bencanaapp.data.model.DisasterEnum
import com.rahdeva.bencanaapp.data.model.DisasterType
import com.rahdeva.bencanaapp.data.model.DisasterItems
import com.rahdeva.bencanaapp.databinding.ActivityMainBinding
import com.rahdeva.bencanaapp.presentation.main.adapter.DisasterAdapter
import com.rahdeva.bencanaapp.presentation.main.adapter.FilterDisasterAdapter
import com.rahdeva.bencanaapp.presentation.main.viewmodel.MainViewModel
import com.rahdeva.bencanaapp.presentation.settings.ui.SettingsActivity
import com.rahdeva.bencanaapp.utils.BitmapUtils
import com.rahdeva.bencanaapp.utils.DisasterUtils
import com.rahdeva.bencanaapp.utils.notification.NotificationWorker
import com.rahdeva.bencanaapp.utils.provider.ResourceProvider
import com.rahdeva.bencanaapp.utils.preferences.Settingpreferences
import com.rahdeva.bencanaapp.utils.provider.ViewModelFactory
import com.rahdeva.bencanaapp.utils.preferences.dataStore
import com.rahdeva.bencanaapp.utils.extension.makeGone
import com.rahdeva.bencanaapp.utils.extension.makeInvisible
import com.rahdeva.bencanaapp.utils.extension.makeVisible
import java.util.concurrent.TimeUnit

//TODO: clear item list if error occured, internet/non-api error should use toast
//TODO: use mock data to create a notification

class MainActivity : AppCompatActivity() {

    private val listFilterDisasterAdapter: FilterDisasterAdapter by lazy { FilterDisasterAdapter(this) }
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var viewModel: MainViewModel
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var workManager: WorkManager

    private lateinit var mBottomSheetLayout: LinearLayout
    private lateinit var mSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var rvDisasterList: RecyclerView
    private lateinit var btnCloseBottomSheet: ImageView
    private lateinit var warningLayout: ConstraintLayout
    private lateinit var imgWarning: ImageView
    private lateinit var tvWarning: TextView

    private lateinit var preferences: Settingpreferences
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var disasterUtils: DisasterUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initWindow()
        setFilterDisaster()

        initObjects()
        setObservable()
        setSearchFeature()
        observeBottomSheetBehaviour()
    }

    private fun initObjects() {
        preferences = Settingpreferences.getInstance(application.dataStore)
        resourceProvider = ResourceProvider(applicationContext)
        viewModel = ViewModelProvider(this, ViewModelFactory(preferences, resourceProvider))[MainViewModel::class.java]
        disasterUtils = DisasterUtils(resourceProvider)
        mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment

        rvDisasterList = findViewById(R.id.rv_disaster_list)
        rvDisasterList.layoutManager = LinearLayoutManager(this)
        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout)
        btnCloseBottomSheet = findViewById(R.id.btn_back)

        warningLayout = findViewById(R.id.warning_layout)
        tvWarning = findViewById(R.id.tv_warning)
        imgWarning = findViewById(R.id.img_warning)

        mSheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout)
        mSheetBehavior.peekHeight = 600
    }

    /*
   *  Set the app layout to fullscreen
   * */
    private fun initWindow(){
        if (Build.VERSION.SDK_INT in 21..29) {
            window.statusBarColor = Color.TRANSPARENT
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        } else if (Build.VERSION.SDK_INT >= 30) {
            window.statusBarColor = Color.TRANSPARENT
            // Making status bar overlaps with the activity
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        // Change the item color (text, icon etc) inside the status bar
        val windowInsetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        windowInsetsController.isAppearanceLightStatusBars = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK === Configuration.UI_MODE_NIGHT_NO
    }

    /*
    * Initialize list for filter disaster
    * */
    private fun setFilterDisaster(){
        val value = DisasterEnum.values().map { it }
        binding.apply {
            rvDisasterFilter.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            rvDisasterFilter.adapter = listFilterDisasterAdapter
            listFilterDisasterAdapter.setItems(value)
        }

        // Using Kotlin Lambda to expose callback from adapter so we can handle the UI logic in Activity
        listFilterDisasterAdapter.overdueCallback {
            pickYourDisaster(it)
        }
    }

    private fun pickYourDisaster(value: DisasterEnum){
        when(value.type){
            DisasterType.EARTHQUAKE -> {
                viewModel.setFilter(value.type)
            }
            DisasterType.FLOOD -> {
                viewModel.setFilter(value.type)
            }
            DisasterType.WIND -> {
                viewModel.setFilter(value.type)
            }
            DisasterType.VOLCANO -> {
                viewModel.setFilter(value.type)
            }
            DisasterType.HAZE -> {
                viewModel.setFilter(value.type)
            }
            DisasterType.FIRE -> {
                viewModel.setFilter(value.type)
            }
            else -> {
                viewModel.setFilter("")
            }
        }
    }

    private fun setObservable() {
        viewModel.apply {
            // ViewModel func to get theme
            getThemeSettings().observe(this@MainActivity) { nightState ->
                if (nightState) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                // ViewModel func to save theme
                saveThemeSettings(nightState)
            }

            // ViewModel func to get get notification
            getNotificationSettings().observe(this@MainActivity) { notificationIsEnabled ->
                if (notificationIsEnabled) {
                    workManager = WorkManager.getInstance(this@MainActivity)
                    setPeriodNotification()
                }
            }

            disasterItemsArray.observe(this@MainActivity) {
                mapFragment.getMapAsync { maps ->
                    maps.clear()
                }
                setDisasterData(it)
                mSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }

            isLoading.observe(this@MainActivity){
                loadingState(it)
            }
            isWarned.observe(this@MainActivity){
                errorState(it)
            }
            warningText.observe(this@MainActivity){
                tvWarning.text = it
                imgWarning.setImageDrawable(disasterUtils.getWarningImage(it))
            }
        }
    }

    private fun loadingState(isLoading: Boolean){
        binding.apply {
            if(isLoading){
                clLoading.makeVisible()
                errorState(false)
                mSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    clLoading.makeGone()
                    mSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                }, 1000)
            }
        }
    }

    private fun errorState(isError: Boolean){
        binding.apply {
            if(isError){
                rvDisasterList.makeGone()
                warningLayout.makeVisible()
            }
            else {
                rvDisasterList.makeVisible()
                warningLayout.makeGone()
            }
        }
    }

    // This line will fire a notification for every 15 minutes
    private fun setPeriodNotification() {
        val periodicNotificationRequest = PeriodicWorkRequest.Builder(
            NotificationWorker::class.java,
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).build()

        workManager.enqueue(periodicNotificationRequest)
    }

    private fun observeBottomSheetBehaviour(){
        mSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        btnCloseBottomSheet.makeVisible()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        btnCloseBottomSheet.makeInvisible()
                    }
                    else -> {
                        btnCloseBottomSheet.makeInvisible()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }

        })
        btnCloseBottomSheet.setOnClickListener {
            mSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSearchFeature() {
        val cityNamesArray = resources.getStringArray(R.array.city_names)
        val searchAdapter = ArrayAdapter(this, R.layout.item_suggestion_city, cityNamesArray)

        binding.apply {
            // Make suggestion disappear at first cycle
            listSearchSuggestion.adapter = searchAdapter
            listSearchSuggestion.makeGone()
            listSearchSuggestion.setOnItemClickListener{ _, _, position, _ ->
                val selectedItem = searchAdapter.getItem(position) as String
                searchMain.setQuery(selectedItem, false)
                viewModel.setLocation(selectedItem)
                searchMain.clearFocus()
                viewModel.getRecentDisaster()
            }

            // Listener to search view
            searchMain.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.setLocation(query ?: "")
                    searchMain.clearFocus()
                    viewModel.getRecentDisaster()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrBlank()) {
                        viewModel.setLocation("")
                        viewModel.getRecentDisaster()
                    }
                    searchAdapter.filter.filter(newText)
                    return true
                }
            })

            // State listener to any View related to search feature
            searchMain.setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    cvListSearchSuggestion.makeVisible()
                    listSearchSuggestion.makeVisible()
                    btnSettings.makeGone()
                    mBottomSheetLayout.makeGone()
                    mSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    searchMain.clearFocus()

                    cvListSearchSuggestion.makeGone()
                    listSearchSuggestion.makeGone()
                    btnSettings.makeVisible()
                    btnSettings.makeVisible()
                    mBottomSheetLayout.makeVisible()
                    mSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                }
            }
        }
        // -- END of binding

        rvDisasterList.addOnItemTouchListener(object: RecyclerView.OnItemTouchListener {
            private val gestureDetector = GestureDetector(rvDisasterList.context, object: GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
            })

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val childView = rv.findChildViewUnder(e.x, e.y)
                if (childView != null && gestureDetector.onTouchEvent(e)) {
                    childView.callOnClick()
                    mSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    return true
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                if (gestureDetector.onTouchEvent(e)) {
                    btnCloseBottomSheet.makeInvisible()
                }
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {  }
        })
    }

    private fun setDisasterData(listDisasterItems: List<DisasterItems?>?) {
        val list = ArrayList<DisasterItems>()
        if (listDisasterItems != null) {
            for (disaster in listDisasterItems) {
                list.add(
                    DisasterItems(
                        disaster?.coordinates,
                        disaster?.type,
                        disaster?.disasterProperty
                    )
                )
            }
        }

        rvDisasterList.adapter = DisasterAdapter(list, mapFragment, mSheetBehavior, resourceProvider)
        setMapPoints(list)
    }

    private fun setMapPoints(list: ArrayList<DisasterItems>) {
        var lastLoc = LatLng(-0.7893, 113.9213)
        val boundBuilder = LatLngBounds.Builder()
        for (item in list) {
            val latitude = String.format("%.2f", item.coordinates?.get(0)?:0.0).toDouble()
            val longitude = String.format("%.2f", item.coordinates?.get(1)?:0.0).toDouble()
            val location = LatLng(longitude, latitude)
            val vectorDrawable = VectorDrawableCompat.create(
                resources,
                disasterUtils.getMarkerIcon(item.disasterProperty?.disasterType),
                null
            )

            val markerTitle = disasterUtils.getDisasterType(item.disasterProperty?.disasterType)
            val markerSubtitle = disasterUtils.getRegionString(item.disasterProperty?.tags?.instanceRegionCode ?:"")


            val bitmap = BitmapUtils().vectorToBitmap(vectorDrawable)

            mapFragment.getMapAsync { googleMap ->
                try {
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title(markerTitle)
                            .snippet(markerSubtitle)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    )
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
            boundBuilder.include(location)
        }
        mapFragment.getMapAsync {
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, 5f))
        }
    }


}