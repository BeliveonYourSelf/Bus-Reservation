package com.bus.reservation.ui.activitys

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.bus.reservation.R
import com.bus.reservation.comman.SharePrefranceUtills
import com.bus.reservation.comman.hideKeyboard
import com.bus.reservation.comman.isVisable
import com.bus.reservation.databinding.ActivitySearchBusListBinding
import com.bus.reservation.domain.model.BusSearchData
import com.bus.reservation.ui.adapters.BusSearchListAdapter
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SearchBusListActivity : BaseBindingActivity<ActivitySearchBusListBinding>() {
    lateinit var busSearchListAdapter: BusSearchListAdapter
    var date: String? = null
    var fromText: String? = null
    var toText: String? = null
    var dataList : List<BusSearchData>? = null
    override fun getActivityContext(): BaseActivity {
        return this@SearchBusListActivity
    }

    override fun setBinding(): ActivitySearchBusListBinding {
        return ActivitySearchBusListBinding.inflate(layoutInflater)
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
        }
    }
    override fun initView() {
        super.initView()
        getAllDataAndSetData()

    }

    private fun getAllDataAndSetData() {
        dataList = intent.getSerializableExtra("SearchData") as? List<BusSearchData>
        date = intent.extras?.getString("date", "")
        fromText = intent.extras?.getString("from", "")
        toText = intent.extras?.getString("to", "")
        mBinding.tvTommorow.text = date
        mBinding.tvFrom.text = fromText
        mBinding.tvTo.text = toText

        if(dataList?.size == 0){
                mBinding.mRecycler.isVisable(false)
                mBinding.ivNoData.isVisable(true)
            Toast.makeText(getActivityContext(), "Please select valid bus details", Toast.LENGTH_SHORT).show()
        }else{
            mBinding.mRecycler.isVisable(true)
            mBinding.ivNoData.isVisable(false)
            busSearchListAdapter = BusSearchListAdapter(getActivityContext(), dataList!!)
            mBinding.mRecycler.adapter = busSearchListAdapter
            busSearchListAdapter.onBusClick = { searchModel: BusSearchData ->
                Log.e(TAG, "onBusClick: ========> $searchModel")
                launchActivity(getActivityIntent<BusDetailsActivity> {
                    putSerializable(SharePrefranceUtills.BusModel, searchModel)
                })
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
                tvTommorow,
                ivSwap,
                toolbar.ivBack
            )
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        with(mBinding) {
            when (v) {
                toolbar.ivBack ->{ backFromCurrentScreen()}
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

                    launchActivity(getActivityIntent<MainActivity> {  })
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
                    launchActivity(getActivityIntent<SettingActivity> {  })
                }
                else -> {

                }
            }
        }
    }
}