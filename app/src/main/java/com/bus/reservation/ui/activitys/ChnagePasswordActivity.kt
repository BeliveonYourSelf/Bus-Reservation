package com.bus.reservation.ui.activitys

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bus.reservation.comman.SharePrefranceUtills
import com.bus.reservation.comman.dialogs.ProgressDialog
import com.bus.reservation.databinding.ActivityChnagePasswordBinding
import com.bus.reservation.presentation.changepassword.ChangePassState
import com.bus.reservation.presentation.changepassword.ChangePasswordViewModel
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@AndroidEntryPoint
class ChnagePasswordActivity : BaseBindingActivity<ActivityChnagePasswordBinding>() {
    val changePasswordViewModel: ChangePasswordViewModel by viewModels<ChangePasswordViewModel>()
    lateinit var progressDialog: ProgressDialog
    override fun getActivityContext(): BaseActivity {
        return this@ChnagePasswordActivity
    }

    override fun setBinding(): ActivityChnagePasswordBinding {
        return ActivityChnagePasswordBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()

        progressDialog = ProgressDialog(getActivityContext())
        lifecycleScope.launch {
            changePasswordViewModel.changPasswordResponse.collect { (isLoading: Boolean, data: String?, success: Boolean): ChangePassState ->

                when {
                    isLoading -> {
                        progressDialog.show()
                        Log.e(TAG, "changePasswordViewModel loading: ", )
                    }

                    data?.isNotEmpty() == true -> {
                        Log.e(TAG, "changePasswordViewModel error ${data}: ", )
                    }

                    success -> {
                        Log.e(TAG, "changePasswordViewModel success ${data}: ", )
                        progressDialog.dismiss()
                        SharePrefranceUtills.isLogin = false
                        launchActivity(getActivityIntent<SplashActivity> { })
                        Toast.makeText(
                            getActivityContext(),
                            "Password Changed Successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

            }
        }
    }

    override fun initViewListener() {
        super.initViewListener()
        with(mBinding) {
            setClickListener(btnSavePDf, toolbar.ivBack)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        with(mBinding) {
            when (v) {
                toolbar.ivBack -> {

                }

                btnSavePDf -> {
                    if (!TextUtils.isEmpty(mBinding.edOldPass.text.toString()) &&
                        !TextUtils.isEmpty(mBinding.edNewPass.text.toString()) &&
                        !TextUtils.isEmpty(mBinding.edNewPassVeri.text.toString())
                    ) {
                        if (mBinding.edNewPass.text.toString() == mBinding.edNewPassVeri.text.toString()) changePassword() else Toast.makeText(
                            getActivityContext(),
                            "New Password not same",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        Toast.makeText(
                            getActivityContext(),
                            "Please fill all details",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

    private fun changePassword() {
        val jsonObj = JSONObject()
        jsonObj.put("currentPassword", mBinding.edOldPass.text.toString())
        jsonObj.put("newPassword", mBinding.edNewPass.text.toString())
        jsonObj.put("verifyPassword", mBinding.edNewPassVeri.text.toString())

        changePasswordViewModel.changePassword(
            jsonObj.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        )
    }

}