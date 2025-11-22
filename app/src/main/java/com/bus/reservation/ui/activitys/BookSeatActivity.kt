package com.bus.reservation.ui.activitys

import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.bus.reservation.R
import com.bus.reservation.comman.SharePrefranceUtills
import com.bus.reservation.comman.dialogs.ProgressDialog
import com.bus.reservation.comman.sharePreranceManager
import com.bus.reservation.data.model.BookSearData
import com.bus.reservation.databinding.ActivityBookSeatBinding
import com.bus.reservation.domain.model.SeatModel
import com.bus.reservation.presentation.book_ticket.BookState
import com.bus.reservation.presentation.book_ticket.TicketViewModel
import com.bus.reservation.worker.PostNotificationWorkManger
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class BookSeatActivity : BaseBindingActivity<ActivityBookSeatBinding>() {
    val ticketViewModel: TicketViewModel by viewModels<TicketViewModel>()
    var bookModel: SeatModel? = null
    var bookingDate: String? = null
    var busId: String? = null
    lateinit var progressDialog: ProgressDialog
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    @Inject
    lateinit var postNotificationWorkManger: PostNotificationWorkManger

    override fun getActivityContext(): BaseActivity {
        return this@BookSeatActivity
    }

    override fun setBinding(): ActivityBookSeatBinding {
        return ActivityBookSeatBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        super.initView()
        progressDialog = ProgressDialog(getActivityContext())

//        mBinding.tvTravellesName.text = intent.extras!!.getString("BusName", "")
//        mBinding.edSitNumber.setText(intent.extras!!.getInt("SeatNo", 0).toString())
//        mBinding.edDate.setText(intent.extras!!.getString("Date", "").toString())
//        mBinding.edStartTime.setText("09:00:00 AM")
//        mBinding.edEndTime.setText("12:00:00 PM")
//        busId = intent.extras!!.getString("BusId", "").toString()


        bookModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getSerializable(SharePrefranceUtills.BookModel, SeatModel::class.java)
        } else {
            intent.extras?.getSerializable(SharePrefranceUtills.BookModel) as SeatModel?
        }

//        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
//        bookingDate = ZonedDateTime.parse(bookModel!!.traveldate).toLocalDate().format(formatter)
        with(mBinding) {
            busId = bookModel?.busId
            tvTravellesName.text = bookModel!!.travellessname
            edSitNumber.setText("Seat no : ${bookModel!!.seatNo}")
            edPrice.setText("Price : ${bookModel!!.fare}")
            edDate.setText("Date : ${bookModel!!.traveldate}")
            edStartTime.setText("Start Time : ${bookModel!!.departure}")
            edEndTime.setText("End Time : 12:00:00 AM")
        }

        lifecycleScope.launch {
            ticketViewModel.bookTicketResponse.collect { (isLoading: Boolean, data: BookSearData?, message: String?): BookState ->
                when {
                    isLoading -> {
                        progressDialog.show()
                        Log.e(TAG, "bookTicketResponse: Loading....")
                    }

                    message?.isNotEmpty() == true -> {
                        progressDialog.dismiss()
                        Log.e(TAG, "bookTicketResponse: message....$message")
                        Toast.makeText(
                            getActivityContext(),
                            "Seat  Already Booked ",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }

                    data != null -> {
                        progressDialog.dismiss()
                        launchActivity(getActivityIntent<UserTicketsActivity> {
                            putString("TicketId", data.id)
                        })
                        val type: Type = object : TypeToken<ArrayList<SeatModel?>?>() {}.type
                        val arrayListPrefData = Gson().fromJson(
                            sharePreranceManager.getStringValue(
                                bookModel?.busId,
                                ""
                            ), type
                        ) as List<SeatModel>
                        val modelFound = arrayListPrefData.find(fun(it: SeatModel): Boolean {
                            return it.seatNo == data.seatNumber.toString()
                        })
                        if (modelFound != null) {
                            modelFound.isBooked = true
                        }

                        sharePreranceManager.setStringValue(
                            bookModel?.busId,
                            Gson().toJson(arrayListPrefData)
                        )


//                        launchActivity(getActivityIntent<YourBookingDetailsActivity> {
//                            putSerializable(SharePrefranceUtills.SeatSuccess, data)
//                            putString(
//                                SharePrefranceUtills.BusName,
//                                mBinding.tvTravellesName.text.toString()
//                            )
//                            putString(
//                                SharePrefranceUtills.USERNAME,
//                                mBinding.edfirstName.text.toString()
//                            )
//                            putString(
//                                SharePrefranceUtills.PHONE,
//                                mBinding.edphoneNumber.text.toString()
//                            )
//                            putString(SharePrefranceUtills.EMAIL, mBinding.edEmail.text.toString())
//                            putString(SharePrefranceUtills.DATE, mBinding.edDate.text.toString())
//                            putString(
//                                SharePrefranceUtills.TIME,
//                                mBinding.edStartTime.text.toString() + " " + mBinding.edEndTime.text.toString()
//                            )
//                        })
                        if (SharePrefranceUtills.isNotificationOn) {
                            Log.e(
                                TAG,
                                "initView: -------->${SharePrefranceUtills.isNotificationOn}",
                            )
                            postNotificationWorkManger.postNotification()
                        }

                        mActivity.finish()
                        Log.e(
                            TAG,
                            "bookTicketResponse: data....${
                                GsonBuilder().setPrettyPrinting().create().toJson(data)
                            }",
                        )
                    }
                }
            }
        }
//        bookingTicket()
    }


    private fun bookingTicket() {
        SharePrefranceUtills.fullName = mBinding.edfirstName.text.toString()
        SharePrefranceUtills.phoneNUmber = mBinding.edphoneNumber.text.toString()
        SharePrefranceUtills.emailAddress = mBinding.edEmail.text.toString()
        val bookObject = JSONObject()
        bookObject.put("seat_number", bookModel!!.seatNo)
        bookObject.put("date", bookModel!!.traveldate)
        bookObject.put("price", bookModel!!.fare)
        bookObject.put("is_booked", true)
        bookObject.put("bus", bookModel!!.busId)

        Log.e(TAG, "json------> ${GsonBuilder().setPrettyPrinting().create().toJson(bookObject)} ")

        val requestBody =
            bookObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        ticketViewModel.bookTicket(requestBody)


    }

    override fun initViewListener() {
        super.initViewListener()
        with(mBinding) {
            setClickListener(btnMain, toolbar.ivBack)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        with(mBinding) {
            when (v) {
                toolbar.ivBack -> {
                    backFromCurrentScreen()
                }

                btnMain -> {
                    if (TextUtils.isEmpty(edfirstName.text.toString()) &&
                        TextUtils.isEmpty(edphoneNumber.text.toString()) &&
                        TextUtils.isEmpty(edphoneNumber2.text.toString()) &&
                        TextUtils.isEmpty(edEmail.text.toString())
                    ) {
                        Toast.makeText(
                            getActivityContext(),
                            "Please fill all details",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    if (isValidString(edEmail.text.toString())) {
                        showBookingConfirmationDialog()
                    }else{
                        Toast.makeText(getActivityContext(), "Please Enter Valid Email", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showBookingConfirmationDialog() {
        MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
            .setTitle("Confirm Booking")
            .setMessage("Are you sure you want to book the bus seat?")
            .setPositiveButton("Yes") { dialog, which ->
                // Handle the action when user confirms the booking
                bookingTicket()
            }
            .setNegativeButton("No") { dialog, which ->
                // Handle the action when user cancels the booking
                dialog.dismiss()
            }
            .setCancelable(false) // Makes the dialog non-cancelable by tapping outside
            .show()

    }

    fun isValidString(str: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }
}