package com.bus.reservation.ui.fragments;

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bus.reservation.databinding.UserAllticketFragmentBinding
import com.bus.reservation.domain.model.UserTicketDetailsData
import com.bus.reservation.presentation.user_ticket_details.UserTicketDetailsViewModel
import com.bus.reservation.ui.adapters.AllTicketAdapter
import com.example.myapplication.base.BaseBindingFragment
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Collections

@AndroidEntryPoint
public class AllTicketFragment : BaseBindingFragment<UserAllticketFragmentBinding>() {

    val userViewModel: UserTicketDetailsViewModel by viewModels<UserTicketDetailsViewModel>()

    lateinit var allTicketAdapter: AllTicketAdapter
    override fun setBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
    ): UserAllticketFragmentBinding {
        return UserAllticketFragmentBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        getAllUserTickets()

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
                        Collections.reverse(data)
                        allTicketAdapter = AllTicketAdapter(requireContext(),data )
                        mBinding.mRecycler.adapter=allTicketAdapter
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
    }

    override fun initViewListener() {
        super.initViewListener()
    }

    override fun onClick(v: View) {
        super.onClick(v)
    }

    private fun getAllUserTickets() {
        userViewModel.getUserAllTicket()
    }
}