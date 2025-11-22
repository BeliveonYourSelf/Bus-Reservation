package com.bus.reservation.ui.activitys

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bus.reservation.comman.SharePrefranceUtills
import com.bus.reservation.comman.dialogs.ProgressDialog
import com.bus.reservation.data.model.BookSearData
import com.bus.reservation.databinding.ActivityYourBookingDetailsBinding
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class YourBookingDetailsActivity : BaseBindingActivity<ActivityYourBookingDetailsBinding>() {
    lateinit var progressDialog: ProgressDialog
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                saveViewAsPDF()
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                Log.e(TAG, "uri--------->: $uri")


                SharePrefranceUtills.profile = uri.toString()
                mBinding.toolbar.ivProfile.setImageURI(it)

                val inputStream = getActivityContext().contentResolver.openInputStream(uri)
                val file = File(filesDir, "profile_image.jpg") // Save to internal storage
                val outputStream = FileOutputStream(file)
                SharePrefranceUtills.profile = file.absolutePath
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
            } ?: run {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }


    var bookSeatData: BookSearData? = null
    private var username : String ?= null
    private var phone : String ?= null
    private var email : String ?= null
    private var date : String ?= null
    private var time : String ?= null
    private var traveleseName : String ?= null
    override fun getActivityContext(): BaseActivity {
        return this@YourBookingDetailsActivity
    }

    override fun setBinding(): ActivityYourBookingDetailsBinding {
        return ActivityYourBookingDetailsBinding.inflate(layoutInflater)
    }


    override fun initView() {
        super.initView()
        getAllTicketDetails()


    }

    @SuppressLint("NewApi")
    private fun getAllTicketDetails() {
        bookSeatData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getSerializable(
                SharePrefranceUtills.SeatSuccess,
                BookSearData::class.java
            )
        } else {
            intent.extras?.getSerializable(SharePrefranceUtills.SeatSuccess) as BookSearData?

        }
        Log.e(TAG, "Booked Bus Details Received bookSeatData: =========> $bookSeatData")

        traveleseName = intent.extras?.getString(SharePrefranceUtills.BusName,"")
        username = intent.extras?.getString(SharePrefranceUtills.USERNAME,"")
        phone = intent.extras?.getString(SharePrefranceUtills.PHONE,"")
        email = intent.extras?.getString(SharePrefranceUtills.EMAIL,"")
        date = intent.extras?.getString(SharePrefranceUtills.DATE,"")
        time = intent.extras?.getString(SharePrefranceUtills.TIME,"")


        with(mBinding) {
            tvTravellesName.text=traveleseName
            tvUserNameValue.text=username
            tvPhoneValue.text=phone
            tvEmailValue.text=email
            tvSeatValue.text= bookSeatData?.seatNumber.toString()
            tvPriceValue.text=bookSeatData?.price
            tvDateValue.text= ZonedDateTime.parse(bookSeatData?.date).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            tvTimeValue.text=time

        }
    }

    override fun initViewListener() {
        super.initViewListener()
        with(mBinding) {
            setClickListener(toolbar.ivBack,btnSavePDf)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
         with(mBinding) {
                     when(v){
                         toolbar.ivBack ->{backFromCurrentScreen() }
                         btnSavePDf -> {
                             progressDialog = ProgressDialog(getActivityContext())
                             Log.e(TAG, "onClick: btnSavePDf")
                             if (Build.VERSION.SDK_INT >= 32) {
                                 progressDialog.show()
                                 saveViewAsPDF()
                             } else {
                                 requestPermissionLauncher.launch(
                                     Manifest.permission.WRITE_EXTERNAL_STORAGE
                                 )
                             }
                         }
                     }
                 }
    }
    private fun saveViewAsPDF() {
        Toast.makeText(getActivityContext(), "Save As PDF", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            val view = mBinding.cvTravelerInfo
            val displayMatrices = DisplayMetrics()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                display.getRealMetrics(displayMatrices)
                getActivityContext().display.getMetrics(displayMatrices)
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

            val savedFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + File.separator + "RedBus")
            val saveFileName = "example.pdf"
            if (!savedFolder.exists()) {
                savedFolder.mkdirs()
            }
            val finalFolder = File(savedFolder, saveFileName)
            val fos = FileOutputStream(finalFolder)

            try {

            } catch (e: FileNotFoundException) {

            }
            pdfDocument.writeTo(fos)
            pdfDocument.close()
            fos.close()
            progressDialog.dismiss()
            Toast.makeText(getActivityContext(), "Pdf Saved Successfully in Documents/RedBus", Toast.LENGTH_SHORT).show()
            backFromCurrentScreen()
        }

    }
}