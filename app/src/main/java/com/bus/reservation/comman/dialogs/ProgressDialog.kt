package com.bus.reservation.comman.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.bus.reservation.databinding.DialogProgressBinding

class ProgressDialog(context: Context) : Dialog(context) {
    val binding= DialogProgressBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) // Custom layout for progress dialog
        setCancelable(false) // Disable canceling the dialog
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Transparent background
    }

    fun setMessage(message: String) {
        binding.tvProgressMessage?.text = message
    }




}
