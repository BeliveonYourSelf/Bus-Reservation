package com.bus.reservation.ui.fragments;

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bus.reservation.comman.dialogs.ProgressDialog
import com.bus.reservation.databinding.UserTicketFragmentBinding
import com.bus.reservation.domain.model.UserTicketDetailsData
import com.bus.reservation.presentation.user_ticket_details.UserTicketDetailsViewModel
import com.example.myapplication.base.BaseBindingFragment
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class UserTicketFragment(private val ticketId: String) :
    BaseBindingFragment<UserTicketFragmentBinding>() {
    val userViewModel: UserTicketDetailsViewModel by viewModels<UserTicketDetailsViewModel>()
    lateinit var progressDialog: ProgressDialog
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                saveViewAsPDF()
            }
        }

    override fun setBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
    ): UserTicketFragmentBinding {
        return UserTicketFragmentBinding.inflate(layoutInflater)
    }

    @SuppressLint("NewApi")
    override fun initView() {
        super.initView()
        getUserTicket(ticket_id = ticketId)
        lifecycleScope.launch {
            userViewModel.userTicketResponse.collect { (isLoading: Boolean, data: List<UserTicketDetailsData>?, message: String?) ->
                when {
                    isLoading -> {
                        Log.e(TAG, "userTicketResponse Loading: ")
                    }

                    message?.isNotEmpty() == true -> {
                        Log.e(TAG, "userTicketResponse message$message: ")
                    }

                    data != null -> {
                        Log.e(
                            TAG,
                            "userTicketResponse message${
                                GsonBuilder().setPrettyPrinting().create().toJson(data)
                            }: "
                        )
                        with(mBinding) {
                            tvTravellesNameReal.text=data[0].bus[0].travelsname
                            tvSeatValue.text= data[0].seatNumber.toString()
                            tvPriceValue.text=data[0].price
                            tvFromValue.text=data[0].bus[0].from
                            tvToValue.text=data[0].bus[0].to
                            tvDateValue.text=ZonedDateTime.parse(data[0].date).toLocalDate().format(
                                DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                            tvTimeValue.text="9:00:00 PM"
                            tvNameValues.text=data[0].user[0].firstName+" "+ data[0].user[0].lastName
                            tvPhoneValue.text=data[0].user[0].phone
                            tvEmailValue.text=data[0].user[0].email
                        }
                    }
                }

            }
        }
    }

    override fun initViewListener() {
        super.initViewListener()
        with(mBinding) {
            setClickListener(toolbar.ivBack, btnSavePDf)
        }
    }


    override fun onClick(v: View) {
        super.onClick(v)
        with(mBinding) {
            when (v) {
                toolbar.ivBack -> {   requireActivity().onBackPressedDispatcher.onBackPressed()}
                btnSavePDf -> {
                    progressDialog = ProgressDialog(requireActivity())

                    if (Build.VERSION.SDK_INT >= 32) {
                        Log.e(TAG, "onClick: btnSavePDf")
                        progressDialog.show()
                        saveViewAsPDF()
                    } else {
                        Log.e(TAG, "onClick: requestPermissionLauncher")
                        requestPermissionLauncher.launch(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    }
                }

                else -> {}
            }
        }
    }


    // TODO: getUserTicket  Method
    private fun getUserTicket(ticket_id: String) {
        val jsonObjcect = JSONObject()
        jsonObjcect.put("ticket_id", ticket_id)
        val requestBody =
            jsonObjcect.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        userViewModel.getUserTicket(requestBody)
    }

    private fun saveViewAsPDF() {

        CoroutineScope(Dispatchers.IO).launch {
            val view = mBinding.rooLayout
            val displayMatrices = DisplayMetrics()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity?.display?.getRealMetrics(displayMatrices)
                activity?.display?.getMetrics(displayMatrices)
            }

            view.measure(
                View.MeasureSpec.makeMeasureSpec(
                    displayMatrices.widthPixels,
                    View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                    displayMatrices.heightPixels,
                    View.MeasureSpec.EXACTLY
                ),
            )

            var pdfDocument = PdfDocument()
            val intWidth = view.measuredWidth
            val intHeight = view.measuredHeight
            view.layout(0, 0, displayMatrices.widthPixels, displayMatrices.heightPixels)

            var myPageInfo = PdfDocument.PageInfo.Builder(intWidth, intHeight, 1).create()
            var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

            var canvas: Canvas = myPage.canvas
            view.draw(canvas)
            pdfDocument.finishPage(myPage)

            val savedFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + File.separator + "MyBus")
            val rawFileName  = "Ticket/${mBinding.tvTravellesNameReal.text}-${mBinding.tvDateValue.text}-Seat${mBinding.tvSeatValue.text}.pdf"
            val sanitizedFileName = rawFileName.replace("/", "-")
            if (!savedFolder.exists()) {
                savedFolder.mkdirs()
            }
            val finalResultPDFile = File(savedFolder, sanitizedFileName)

            try {
                val fos = FileOutputStream(finalResultPDFile)
                pdfDocument.writeTo(fos)
                pdfDocument.close()
                fos.close()
            } catch (e: FileNotFoundException) {
                Log.e(TAG, "saveViewAsPDF: ---> excepation ${e.message}")
                e.printStackTrace()
            }


            withContext(Dispatchers.Main){
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Pdf Saved Successfully in Documents/MyBus",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }

    }


}