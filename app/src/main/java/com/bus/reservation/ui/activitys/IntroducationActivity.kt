package com.bus.reservation.ui.activitys

import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bus.reservation.R
import com.bus.reservation.comman.SharePrefranceUtills
import com.bus.reservation.comman.isVisable
import com.bus.reservation.databinding.ActivityIntroducationBinding
import com.bus.reservation.ui.adapters.SliderAdapter
import com.bus.reservation.ui.adapters.SliderData
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity

class IntroducationActivity : BaseBindingActivity<ActivityIntroducationBinding>() {
    lateinit var dataList: MutableList<SliderData>
    lateinit var sliderAdapter: SliderAdapter
    override fun getActivityContext(): BaseActivity {
        return this@IntroducationActivity
    }

    override fun setBinding(): ActivityIntroducationBinding {
        return ActivityIntroducationBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        dataList = mutableListOf(
            SliderData(
                "India's No. 1 Online Bus Ticket Booking App",
                "Online bus ticket booking keeps you away from the long queues of the offline ticket counters.",
                R.raw.bus
            ),
            SliderData(
                "All Buses of India is Avilable",
                "You can view plenty of buses and choose an appropriate bus for your travel considering the amenities, reviews, ratings and bus images available.",
                R.raw.bus
            ),
            SliderData(
                "Easy and Simple to Book Tickets",
                "Your tickets can be booked at a reasonable price with multiple payment options.",
                R.raw.ticket
            ),
        )
        sliderAdapter = SliderAdapter(getActivityContext(), ArrayList(dataList))
        mBinding.viewPagers.adapter = sliderAdapter
        mBinding.viewPagers.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == dataList.size-1) {
                    mBinding.tvNext.isVisable(true)
                }else{
                    mBinding.tvNext.isVisable(false)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        mBinding.tvNext.setOnClickListener {
            SharePrefranceUtills.isIntroComplete=true
            launchActivity(getActivityIntent<LoginActivity> {  })
        }
    }

    override fun initViewListener() {
        super.initViewListener()
    }

    override fun onClick(v: View) {
        super.onClick(v)
    }
}