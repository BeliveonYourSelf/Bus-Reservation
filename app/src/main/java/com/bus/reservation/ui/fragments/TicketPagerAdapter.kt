package com.bus.reservation.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TicketPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val ticketid: String,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                UserTicketFragment(ticketid)
            }

            1 -> {
                AllTicketFragment()
            }

            else -> {
                throw IllegalStateException("Unexpected position $position")
            }
        }
    }
}