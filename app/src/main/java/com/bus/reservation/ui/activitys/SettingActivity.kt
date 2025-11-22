package com.bus.reservation.ui.activitys

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.bus.reservation.R
import com.bus.reservation.comman.SharePrefranceUtills
import com.bus.reservation.databinding.ActivitySettingBinding
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class SettingActivity : BaseBindingActivity<ActivitySettingBinding>() {

    private val requestPermissionLauncher =
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
                mBinding.ivProfileLogo.setImageURI(it)
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

    var pageHeight = 1120
    var pageWidth = 792
    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap
    override fun getActivityContext(): BaseActivity {
        return this@SettingActivity
    }

    override fun setBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        with(mBinding) {
            try {

                val file = File(SharePrefranceUtills.profile)
                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    toolbar.ivProfile.setImageBitmap(bitmap)
                    ivProfileLogo.setImageBitmap(bitmap)
                } else {
                    Log.e("ProfileImage", "Image file not found")
                }
            } catch (e: SecurityException) {
                Log.e("ProfileImage", "Permission issue: ${e.message}")
                e.printStackTrace()
            }
            toolbar.tvProfileName.text = SharePrefranceUtills.userName
            edfirstName.setText(SharePrefranceUtills.fullName)
            edphoneNumber.setText(SharePrefranceUtills.phoneNUmber)
            edEmail.setText(SharePrefranceUtills.emailAddress)
            toolbar.tvProfileName.text = SharePrefranceUtills.userName

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
            ivBooking.setBackgroundResource(R.drawable.bg_unselected_menu)
            ivHomeMenu.setBackgroundResource(R.drawable.bg_unselected_menu)
            ivSetting.setBackgroundResource(R.drawable.bg_selected_menu)
        }
    }

    override fun initView() {
        super.initView()

        with(mBinding) {
            notiSwitch.isChecked = SharePrefranceUtills.isNotificationOn
            notiSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                SharePrefranceUtills.isNotificationOn = isChecked
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
                toolbar.ivBack,
                btnSavePDf,
                ivProfileLogo,
                icEditprofile,
                tvChangePassword
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onClick(v: View) {
        super.onClick(v)
        with(mBinding) {
            when (v) {
                toolbar.ivBack -> {
                    backFromCurrentScreen()
                }
                tvChangePassword->{launchActivity(getActivityIntent<ChnagePasswordActivity> {  })}

                icEditprofile -> {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        Log.e(TAG, "onClick: TIRAMISU")
                        if (ActivityCompat.checkSelfPermission(
                                getActivityContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                getActivityContext(),
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                101
                            )
                            Log.e(TAG, "onClick: TIRAMISU")
                        } else {
                            Log.e(TAG, "onClick: TIRAMISU PICK")
                            pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    } else {
                        if (ActivityCompat.checkSelfPermission(
                                getActivityContext(),
                                Manifest.permission.READ_MEDIA_IMAGES
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                getActivityContext(),
                                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                                101
                            )
                            Log.e(TAG, "onClick: ELSE")
                        } else {
                            pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            Log.e(TAG, "onClick: PICK")
                        }
                    }
                }

                btnSavePDf -> {
                    Log.e(TAG, "onClick: btnSavePDf")
                    if (Build.VERSION.SDK_INT >= 32) {
//                        saving()
                        saveViewAsPDF()
                    } else {
                        requestPermissionLauncher.launch(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    }
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
//                    launchActivity(getActivityIntent<SearchBusListActivity> { })
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

                }

                else -> {

                }
            }
        }
    }

    private fun saveViewAsPDF() {
        Toast.makeText(getActivityContext(), "Save As PDF", Toast.LENGTH_SHORT).show()
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

        try {
            val fos = FileOutputStream(finalFolder)
            pdfDocument.writeTo(fos)
            pdfDocument.close()
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "saveViewAsPDF: ---> excepation ${e.message}", )
            e.printStackTrace()
        }

    }

    fun saving() {
        try {

            mBinding.cvTravelerInfo.isDrawingCacheEnabled = true
            val bitmap: Bitmap = mBinding.cvTravelerInfo.drawingCache

            val file: File
            val f: File

            // Check if external storage is available
            file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "RedBus"
            )
            if (!file.exists()) {
                file.mkdirs()
            }

            f = File(file.absolutePath + File.separator + "filename.png")
            FileOutputStream(f).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}