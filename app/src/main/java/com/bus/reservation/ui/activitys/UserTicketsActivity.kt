package com.bus.reservation.ui.activitys

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bus.reservation.databinding.ActivityUserTicketsBinding
import com.bus.reservation.domain.model.UserTicketDetailsData
import com.bus.reservation.presentation.user_ticket_details.UserTicketDetailsViewModel
import com.bus.reservation.ui.fragments.TicketPagerAdapter
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity
import com.google.android.material.tabs.TabLayout
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@AndroidEntryPoint
class UserTicketsActivity : BaseBindingActivity<ActivityUserTicketsBinding>() {

    val userViewModel: UserTicketDetailsViewModel by viewModels<UserTicketDetailsViewModel>()
    lateinit var ticketId  : String
    lateinit var ticketPagerAdapter: TicketPagerAdapter
    override fun getActivityContext(): BaseActivity {
        return this@UserTicketsActivity
    }

    override fun setBinding(): ActivityUserTicketsBinding {
        return ActivityUserTicketsBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        ticketId = intent.extras!!.getString("TicketId","")
        ticketPagerAdapter = TicketPagerAdapter(supportFragmentManager,lifecycle,ticketId)
        with(mBinding) {
            tabLayout.addTab(tabLayout.newTab().setText("UserCurrentTickets"))
            tabLayout.addTab(tabLayout.newTab().setText("UserAllTickets"))
            viewPager.adapter=ticketPagerAdapter

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        viewPager.currentItem=tab.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                }
            })
        }
//        getAllUserTickets()

        // TODO: getUserAllTickets
        lifecycleScope.launch {
            userViewModel.userAllTicketResponse.collect { (isLoading: Boolean, data: List<UserTicketDetailsData>?, message: String?) ->
                when {
                    isLoading -> {
                        Log.e(TAG, "userAllTicketResponse Loading: ")
                    }

                    message?.isNotEmpty() == true -> {
                        Log.e(TAG, "userAllTicketResponse message$message: ")
                    }

                    data != null -> {
//                        getUserTicket(data[0].id)
                        Log.e(
                            TAG,
                            "userAllTicketResponse data${
                                GsonBuilder().setPrettyPrinting().create().toJson(data)
                            }: "
                        )
                    }
                }

            }
        }
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
                    }
                }

            }
        }
    }


    override fun initViewListener() {
        super.initViewListener()
        with(mBinding) {
            setClickListener(toolbar.ivBack)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
         with(mBinding) {
                     when(v){
                         toolbar.ivBack ->{  mActivity.onBackPressedDispatcher.onBackPressed()}
                     }
                 }
    }

}