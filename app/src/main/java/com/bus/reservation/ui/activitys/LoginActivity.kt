package com.bus.reservation.ui.activitys

import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bus.reservation.comman.SharePrefranceUtills
import com.bus.reservation.comman.dialogs.ProgressDialog
import com.bus.reservation.comman.isVisable
import com.bus.reservation.databinding.ActivityLoginBinding
import com.bus.reservation.domain.model.LoginUser
import com.bus.reservation.presentation.user_login.UserViewModel
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseBindingActivity<ActivityLoginBinding>() {
    val loginViewModel: UserViewModel by viewModels<UserViewModel>()
    lateinit var progressDialog: ProgressDialog
    override fun getActivityContext(): BaseActivity {
        return this@LoginActivity
    }

    override fun setBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        with(mBinding) {
            progressDialog = ProgressDialog(getActivityContext())

//            materialCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
//                if (isChecked) {
//                } else {
//
//                }
//            }
            lifecycleScope.launch {
                loginViewModel.loginResponse.collect { response ->
                    if (response.isLoading) progressDialog.show() else progressDialog.dismiss()

                    response.data?.let {
                        Log.e(
                            TAG,
                            "Login Success  : =====> ${
                                GsonBuilder().setPrettyPrinting().create()
                                    .toJson(response.data)
                            }"
                        )
                        SharePrefranceUtills.userName = it.firstName + it.lastName
                        SharePrefranceUtills.oAuthToken = it.token
                        SharePrefranceUtills.isLogin = true
                        Log.e(TAG, "userName: ---->${SharePrefranceUtills.userName}")
                        launchActivity(getActivityIntent<MainActivity> { })
                    }
                    if (response.error?.isNotEmpty() == true) {
                        Log.e(TAG, "Login error : =====> ${response.error}")
                        Toast.makeText(
                            getActivityContext(),
                            "${response.error}",
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
            setClickListener(btnLogin, tvCreateAccount, ivShowPassword, ivHidePassword)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        with(mBinding) {
            when (v) {
                btnLogin -> {

                    if (checkValidation()) {
                        val user = LoginUser(
                            edEmail.text.toString().trim(),
                            edPassword.text.toString().trim()
                        )
                        loginViewModel.setupLogin(user)


                    } else {
                        Toast.makeText(
                            getActivityContext(),
                            "Please fill details",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                tvCreateAccount -> {
                    launchActivity(getActivityIntent<SignUpActivity> { })
                }

                ivShowPassword -> {
                    ivHidePassword.isVisable(true)
                    ivShowPassword.isVisable(false)
                    edPassword.transformationMethod = HideReturnsTransformationMethod()
                }

                ivHidePassword -> {
                    ivShowPassword.isVisable(true)
                    ivHidePassword.isVisable(false)
                    edPassword.transformationMethod = PasswordTransformationMethod()
                }

                else -> {}
            }
        }
    }

    private fun checkValidation(): Boolean {
        with(mBinding) {
            if (!TextUtils.isEmpty(edEmail.text.toString()) &&
                !TextUtils.isEmpty(edPassword.text.toString())
            ) {
                return true
            } else {
                return false
            }
        }
    }
}