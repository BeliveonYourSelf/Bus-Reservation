package com.bus.reservation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bus.reservation.databinding.IntroLayoutsBinding

class SliderAdapter(
    val context: Context,
    val sliderList: ArrayList<SliderData>,
) : PagerAdapter() {
    lateinit var binding: IntroLayoutsBinding
    override fun getCount(): Int {
        return sliderList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        binding = IntroLayoutsBinding.inflate(LayoutInflater.from(context), container, false)
        with(binding) {
            ivMain.setAnimation(sliderList[position].slideImage)
            tvTittle.text=sliderList[position].slideTitle
            tvDes.text=sliderList[position].slideDescription
        }
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

}
