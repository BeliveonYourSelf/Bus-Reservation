package com.bus.reservation.ui.activitys

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Build
import android.security.keystore.UserNotAuthenticatedException
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.bus.reservation.R
import com.bus.reservation.comman.SharePrefranceUtills
import com.bus.reservation.comman.dialogs.ProgressDialog
import com.bus.reservation.comman.hideKeyboard
import com.bus.reservation.comman.isVisable
import com.bus.reservation.databinding.ActivityMainBinding
import com.bus.reservation.domain.model.BusListData
import com.bus.reservation.domain.model.BusSearchData
import com.bus.reservation.presentation.bus_list.BusSearchState
import com.bus.reservation.presentation.bus_list.BusViewModel
import com.bus.reservation.ui.adapters.BusSearchListAdapter
import com.bus.reservation.ui.adapters.BuslistPagingAdapter
import com.bus.reservation.ui.adapters.LoaderAdapter
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@AndroidEntryPoint
class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    private val busViewModel: BusViewModel by viewModels<BusViewModel>()
    private var isSwap = false
    private var selectedDate = ""
    private var toText = ""
    private var fromText = ""
    lateinit var progressDialog: ProgressDialog
    lateinit var busSearchListAdapter: BusSearchListAdapter
    lateinit var buslistPagingAdapter: BuslistPagingAdapter

    override fun getActivityContext(): BaseActivity {
        return this@MainActivity
    }

    override fun setBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        with(mBinding) {
            toolbar.tvProfileName.text = SharePrefranceUtills.userName
            Log.e(TAG, "SharePrefranceUtills.profile ---> ${SharePrefranceUtills.profile}")
            try {

                val file = File(SharePrefranceUtills.profile)
                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    toolbar.ivProfile.setImageBitmap(bitmap)
                } else {
                    Log.e("ProfileImage", "Image file not found")
                }
            } catch (e: SecurityException) {
                Log.e("ProfileImage", "Permission issue: ${e.message}")
                e.printStackTrace()
            }
            toolbar.ivBack.isVisable(false)

            ivHomeMenu.imageTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    R.color.black,
                    resources.newTheme()
                )
            )
            ivBooking.imageTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    resources.newTheme()
                )
            )
            ivSetting.imageTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    resources.newTheme()
                )
            )
            ivHomeMenu.setBackgroundResource(R.drawable.bg_selected_menu)
            ivBooking.setBackgroundResource(R.drawable.bg_unselected_menu)
            ivSetting.setBackgroundResource(R.drawable.bg_unselected_menu)


        }
    }


    override fun initView() {
        super.initView()

        // TODO: RecyclerPagingAdapter
        buslistPagingAdapter = BuslistPagingAdapter()
        mBinding.mRecyclerAllBusList.adapter = buslistPagingAdapter.withLoadStateHeaderAndFooter(header = LoaderAdapter(), footer = LoaderAdapter())

        progressDialog = ProgressDialog(getActivityContext())
//        getAllBusList()

        lifecycleScope.launch {
            busViewModel.pagingState.collect { (isLoading: Boolean, message: String?, data: PagingData<BusListData>?) ->
                when {
                    isLoading -> {
                        progressDialog.show()
                    }

                    message?.isNotEmpty() == true -> {
                        progressDialog.show()
                        Log.e(TAG, "pagingData error ==> $message: ")
                    }
                }

            }

        }
        // TODO: getttingAllBus Data Using Paging3 Library
        lifecycleScope.launch {
            busViewModel.busPagerdata.collectLatest { pagingData: PagingData<BusListData> ->
                mBinding.mRecyclerAllBusList.visibility = View.VISIBLE
                buslistPagingAdapter.submitData(lifecycle, pagingData)
            }
        }
        lifecycleScope.launch {
            buslistPagingAdapter.loadStateFlow.collect { loadStates: CombinedLoadStates ->
                when (val refreshState = loadStates.refresh) {
                    is LoadState.Loading -> {
                        Log.e(TAG, "LoadState.Loading", )
                        progressDialog.show()
                    }

                    is LoadState.NotLoading -> {
                        Log.e(TAG, "LoadState.NotLoading", )
                        progressDialog.dismiss()
                    }
                    is LoadState.Error -> {
                        Log.e(TAG, "LoadState.Error ->: ", )
                        progressDialog.dismiss()

                        val exception = refreshState.error
                        if (exception is UserNotAuthenticatedException) {
                            handleUnauthorizedError()
                            Log.e(TAG, "UserNotAuthenticatedException.Error ->: ", )
                        } else if (exception is HttpException) {
                            Log.e(TAG, "HttpException.Error ->: ", )
                            handleUnauthorizedError()
                        } else {
                            Log.e(TAG, "handleGenericError.Error ->: ", )
                            handleGenericError(exception)
                        }
                    }
                    // Handle other states like LoadState.Loading if needed

                }
            }
        }


        // TODO: GetAllBusCollector
        lifecycleScope.launch {
            busViewModel.busListResponse.collect { response ->
                when {
                    response.isLoading -> {
                        progressDialog.show()
                        Log.e(
                            TAG, "getAllBusList isLoading:"
                        )

                    }

                    response.data != null -> {
                        progressDialog.dismiss()
                        Log.e(
                            TAG,
                            "getAllBusList Success: ${
                                GsonBuilder().setPrettyPrinting().create().toJson(response.data)
                            }",
                        )
                    }

                    response.message?.isNotEmpty() == true -> {
                        progressDialog.dismiss()
                        Log.e(TAG, "busListResponse Error ${response.message} ")
                    }
                }

            }
        }
        // TODO: SearchBusCollector
        lifecycleScope.launch {
            busViewModel.searchBusResponse.collect { (isLoading: Boolean, data: List<BusSearchData>?, message: String?): BusSearchState ->
                when {
                    isLoading -> {
                        progressDialog.show()
                        Log.e(TAG, "searchBusToFrom: isLoading ===>  $message")
                    }

                    data != null -> {
                        progressDialog.dismiss()
                        Log.e(
                            TAG,
                            "searchBusToFrom: data ===>  ${
                                GsonBuilder().setPrettyPrinting().create().toJson(data)
                            }",
                        )

                        launchActivity(getActivityIntent<SearchBusListActivity> {
                            putString("from", fromText)
                            putString("to", toText)
                            putString("date", selectedDate)
                            putSerializable("SearchData", ArrayList(data))
                        })

                    }

                    message?.isNotEmpty() == true -> {
                        progressDialog.dismiss()
                        Toast.makeText(
                            getActivityContext(),
                            "Something went wrong $message",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, "searchBusToFrom: error ===>  $message")
                        if (message.equals("HTTP 401 Unauthorized")) {
                            SharePrefranceUtills.isLogin = false
                            launchActivity(getActivityIntent<LoginActivity> { })
                        }
                    }
                }

            }
        }

    }

    private fun handleUnauthorizedError() {
        Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun handleGenericError(exception: Throwable) {
        Toast.makeText(this, "An error occurred: ${exception.message}", Toast.LENGTH_SHORT)
            .show()
    }

    override fun initViewListener() {
        super.initViewListener()
        with(mBinding) {
            setClickListener(
                ivHomeMenu,
                ivBooking,
                ivSetting,
                btnSearchBus,
                tvToday,
                tvTommorow,
                tvOther,
                ivSwap
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View) {
        super.onClick(v)
        with(mBinding) {
            when (v) {
                tvOther -> {
                    selectedDate = ""
                    tvTommorow.setBackgroundResource(R.drawable.bg_white_stroke)
                    tvTommorow.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
                    tvToday.setBackgroundResource(R.drawable.bg_white_stroke)
                    tvToday.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
                    showDatePicker()
                }

                btnSearchBus -> {
                    getActivityContext().hideKeyboard(btnSearchBus)
                    if (TextUtils.isEmpty(edTo.text.toString()) && TextUtils.isEmpty(edFrom.text.toString())) {
                        Toast.makeText(
                            getActivityContext(),
                            "Please Select Destination",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    if (TextUtils.isEmpty(selectedDate)) {
                        Toast.makeText(
                            getActivityContext(),
                            "Please Select Date",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    toText = edTo.text.toString().trim()
                    fromText = edFrom.text.toString().trim()
                    searchBusToFrom(fromText, toText, selectedDate)
                }

                ivHomeMenu -> {
                    ivHomeMenu.imageTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.black,
                            resources.newTheme()
                        )
                    )
                    ivBooking.imageTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.white,
                            resources.newTheme()
                        )
                    )
                    ivSetting.imageTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.white,
                            resources.newTheme()
                        )
                    )
                    ivHomeMenu.setBackgroundResource(R.drawable.bg_selected_menu)
                    ivBooking.setBackgroundResource(R.drawable.bg_unselected_menu)
                    ivSetting.setBackgroundResource(R.drawable.bg_unselected_menu)
                }

                ivBooking -> {
                    ivHomeMenu.imageTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.white,
                            resources.newTheme()
                        )
                    )
                    ivSetting.imageTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.white,
                            resources.newTheme()
                        )
                    )
                    ivBooking.imageTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.black,
                            resources.newTheme()
                        )
                    )
                    ivBooking.setBackgroundResource(R.drawable.bg_selected_menu)
                    ivHomeMenu.setBackgroundResource(R.drawable.bg_unselected_menu)
                    ivSetting.setBackgroundResource(R.drawable.bg_unselected_menu)


                }

                ivSetting -> {
                    ivSetting.imageTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.black,
                            resources.newTheme()
                        )
                    )
                    ivHomeMenu.imageTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.white,
                            resources.newTheme()
                        )
                    )
                    ivBooking.imageTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.white,
                            resources.newTheme()
                        )
                    )
                    ivSetting.setBackgroundResource(R.drawable.bg_selected_menu)
                    ivBooking.setBackgroundResource(R.drawable.bg_unselected_menu)
                    ivHomeMenu.setBackgroundResource(R.drawable.bg_unselected_menu)

                    launchActivity(getActivityIntent<SettingActivity> { })
                }

                ivSwap -> {
                    isSwap = !isSwap
                    if (isSwap) {
                        if (!TextUtils.isEmpty(edTo.text.toString()) && !TextUtils.isEmpty(edFrom.text.toString())) {
                            val toText = edTo.text.toString()
                            val fromText = edFrom.text.toString()
                            edFrom.setText(toText)
                            edTo.setText(fromText)
                            edTo.setSelection(edTo.text.toString().length)
                            edFrom.setSelection(edFrom.text.toString().length)
                        } else {
                            Toast.makeText(
                                getActivityContext(),
                                "Please enter destination",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        val toText = edTo.text.toString()
                        val fromText = edFrom.text.toString()
                        edFrom.setText(toText)
                        edTo.setText(fromText)
                        edTo.setSelection(edTo.text.toString().length)
                        edFrom.setSelection(edFrom.text.toString().length)
                    }
                }

                tvToday -> {
                    tvToday.setBackgroundResource(R.drawable.bg_main_stroke)
                    tvToday.setTextColor(resources.getColor(R.color.black, resources.newTheme()))
                    tvTommorow.setBackgroundResource(R.drawable.bg_white_stroke)
                    tvTommorow.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
                    val todayDate = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                    val todaysDate = todayDate.format(formatter)
                    selectedDate = todaysDate
                    mBinding.tvSelectedDate.text=selectedDate
                    Log.e(TAG, "Today========> $todaysDate ")
                }

                tvTommorow -> {
                    tvTommorow.setBackgroundResource(R.drawable.bg_main_stroke)
                    tvTommorow.setTextColor(resources.getColor(R.color.black, resources.newTheme()))
                    tvToday.setBackgroundResource(R.drawable.bg_white_stroke)
                    tvToday.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
                    val tomorrowData = LocalDateTime.now().plusDays(1)
                    val tomorrowformatter =
                        DateTimeFormatter.ofPattern("yyyy/MM/dd").format(tomorrowData)
                    selectedDate = tomorrowformatter
                    mBinding.tvSelectedDate.text=selectedDate
                    Log.e(TAG, "Tomorrow========> $tomorrowformatter ")
                }


                else -> {

                }
            }
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().apply {
            val constraintsBuilder = CalendarConstraints.Builder().setValidator(
                DateValidatorPointForward.now()
            ).build()
            setCalendarConstraints(constraintsBuilder)
            setTitleText("Select Date")
            setTheme(R.style.ThemeOverlay_App_DatePicker)
        }.build()

        datePicker.show(supportFragmentManager, "")

        datePicker.addOnPositiveButtonClickListener { materialDate ->
            val dateFormatter = SimpleDateFormat("yyyy/MM/dd")
            val date = dateFormatter.format(Date(materialDate))
            this.selectedDate = date.toString()
            mBinding.tvSelectedDate.text=selectedDate
        }
        datePicker.addOnNegativeButtonClickListener {
            selectedDate = ""
            Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
        }
        datePicker.addOnCancelListener {
            selectedDate = ""
            Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
        }
    }

    // TODO: Search Bus Method
    private fun searchBusToFrom(from: String, to: String, traveldate: String) {
        val jsonObjcect = JSONObject()
        jsonObjcect.put("from", from)
        jsonObjcect.put("to", to)
        jsonObjcect.put("traveldate", traveldate)
        jsonObjcect.put("per_page", 50)
        jsonObjcect.put("page_no", 1)

        val requestBody =
            jsonObjcect.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        busViewModel.searchBus(requestBody)
    }

}