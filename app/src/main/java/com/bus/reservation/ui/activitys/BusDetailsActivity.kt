package com.bus.reservation.ui.activitys

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.bus.reservation.R
import com.bus.reservation.comman.SharePrefranceUtills
import com.bus.reservation.comman.dialogs.ProgressDialog
import com.bus.reservation.comman.sharePreranceManager
import com.bus.reservation.databinding.ActivityBusDetailsBinding
import com.bus.reservation.domain.model.BusSearchData
import com.bus.reservation.domain.model.OpenTicketData
import com.bus.reservation.domain.model.SeatModel
import com.bus.reservation.presentation.book_ticket.TicketState
import com.bus.reservation.presentation.book_ticket.TicketViewModel
import com.bus.reservation.ui.adapters.TicketAdapter
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@AndroidEntryPoint
class BusDetailsActivity : BaseBindingActivity<ActivityBusDetailsBinding>() {
    var dataModel: BusSearchData? = null
    val ticketViewModel: TicketViewModel by viewModels<TicketViewModel>()
    lateinit var progressDialog: ProgressDialog;
    lateinit var tickerAdapter: TicketAdapter
    lateinit var dataList: MutableList<SeatModel>
    override fun getActivityContext(): BaseActivity {
        return this@BusDetailsActivity
    }

    override fun setBinding(): ActivityBusDetailsBinding {
        return ActivityBusDetailsBinding.inflate(layoutInflater)
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

            dataList = mutableListOf()
            dataList = generateSeats()

            val listdata = sharePreranceManager.getStringValue(dataModel?.id,"")
            Log.e(TAG, "SharePrefranceUtills.seatList: ------> $listdata")
            if (listdata?.isNotEmpty() == true) {
                Log.e(TAG, "isNotEmpty$listdata")
                dataList = Gson().fromJson(listdata, object : TypeToken<ArrayList<SeatModel>>() {}.type) as MutableList<SeatModel>
                tickerAdapter = TicketAdapter(getActivityContext(), dataList)
                mBinding.mRecyclerSeat.adapter = tickerAdapter

            } else {
                tickerAdapter = TicketAdapter(getActivityContext(), dataList)
                mBinding.mRecyclerSeat.adapter = tickerAdapter
                sharePreranceManager.setStringValue(dataModel?.id,Gson().toJson(dataList))
            }
            tickerAdapter.onSeatClicker = {
                Log.e(TAG, "on seat clicked ========>${it}")
                launchActivityForResult(getActivityIntent<BookSeatActivity> {
                    putSerializable(SharePrefranceUtills.BookModel, it)
                }) { number, intent ->

                }
//                launchActivity(getActivityIntent<BookSeatActivity> {
//                    putSerializable(SharePrefranceUtills.BookModel, it)
//                putString("Date", mBinding.tvTommorow.text.toString())
//                putString("BusName", mBinding.tvTravesName.text.toString())
//                putString("BusId", dataModel?.id)
//                })
            }
        }


    }

    private fun generateSeats(): MutableList<SeatModel> {
        return (1..14).map { seatNumber ->
            SeatModel(
                isBooked = false,
                dataModel!!.id,
                dataModel!!.travelsname,
                dataModel!!.traveldate,
                dataModel!!.departure,
                seatNumber.toString(),
                dataModel!!.fare.toString()
            )
        }.toMutableList()
    }

    override fun initView() {
        super.initView()
        progressDialog = ProgressDialog(getActivityContext())
        with(mBinding) {
            toolbar.tvProfileName.text = SharePrefranceUtills.userName
        }
        dataModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getSerializable(SharePrefranceUtills.BusModel, BusSearchData::class.java)
        } else {
            intent.extras?.getSerializable(SharePrefranceUtills.BusModel) as BusSearchData?
        }

        Log.e(TAG, " BusDetailsActivity Details Received dataModel: =========> $dataModel")
        if (dataModel != null) {
            with(mBinding) {
                tvFrom.text = dataModel!!.from
                tvTo.text = dataModel!!.to
                tvTravesName.text = dataModel!!.travelsname
                tvTommorow.text = dataModel!!.traveldate
                tvTime.text = dataModel!!.departure
                tvSeatLeft.text = dataModel!!.availability.toString() + " Seats Left"
                tvFar.text = "Price : " + dataModel!!.fare.toString()
            }
        }


//        getAllOpenTickets()

    }

    private fun getAllOpenTickets() {
        ticketViewModel.getAllOpenTicket()
        ticketViewModel.getAllCloseTickets()
        // TODO: getAllOpenTickets
        lifecycleScope.launch {
            ticketViewModel.openTicketResponse.collect { (isLoading: Boolean, data: List<OpenTicketData>?, message: String?): TicketState ->
                when {
                    isLoading -> {
                        progressDialog.show()
                        Log.e(TAG, "getAllOpenTickets: Loading....")
                    }

                    message?.isNotEmpty() == true -> {
                        progressDialog.dismiss()
                        Log.e(TAG, "getAllOpenTickets: Loading....$message")
                    }

                    data != null -> {
                        progressDialog.dismiss()
                        dataList = mutableListOf()
//                        dataList.addAll(data.filter { it.bus == dataModel?.id })

                        Log.e(
                            TAG,
                            "getAllOpenTickets: data....${
                                GsonBuilder().setPrettyPrinting().create().toJson(data)
                            }",
                        )
                    }
                }
            }

        }

        // TODO: getAllCloseTickets
        lifecycleScope.launch {
            ticketViewModel.closeTicketResponse.collect { (isLoading: Boolean, data: List<OpenTicketData>?, message: String?) ->
                when {
                    isLoading -> {
                        Log.e(TAG, "closeTicketResponse: Loading....")
                    }

                    data != null -> {
//                        dataList.addAll(data.filter { it.bus == dataModel?.id })
                        Log.e(
                            TAG,
                            "closeTicketResponse: data....${
                                GsonBuilder().setPrettyPrinting().create().toJson(data)
                            }",
                        )

                        // TODO: Setting Adapters
                        withContext(Dispatchers.Main) {
                            tickerAdapter = TicketAdapter(getActivityContext(), dataList)
                            mBinding.mRecyclerSeat.adapter = tickerAdapter

                            tickerAdapter.onSeatClicker = {
                                Log.e(TAG, "on seat clicked ========>${it}")
                                launchActivity(getActivityIntent<BookSeatActivity> {
                                    putSerializable(SharePrefranceUtills.BookModel, it)
                                    putString(
                                        SharePrefranceUtills.BusName,
                                        mBinding.tvTravesName.text.toString()
                                    )
                                })
                            }
                        }
                    }

                    message?.isNotEmpty() == true -> {
                        Log.e(TAG, "closeTicketResponse: message  ${message}")
                    }
                }

            }
        }


    }

    override fun initViewListener() {
        super.initViewListener()
        with(mBinding) {
            setClickListener(
                ivHomeMenu,
                ivBooking,
                ivSetting,
                ivSwap,
                toolbar.ivBack
            )
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        with(mBinding) {
            when (v) {
                toolbar.ivBack -> {
                    backFromCurrentScreen()
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
                    launchActivity(getActivityIntent<MainActivity> { })
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

            }
        }
    }
}