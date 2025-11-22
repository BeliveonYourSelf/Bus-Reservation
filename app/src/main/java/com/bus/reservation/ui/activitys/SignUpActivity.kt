package com.bus.reservation.ui.activitys

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.bus.reservation.R
import com.bus.reservation.comman.SharePrefranceUtills
import com.bus.reservation.comman.dialogs.ProgressDialog
import com.bus.reservation.comman.isVisable
import com.bus.reservation.databinding.ActivitySignUpBinding
import com.bus.reservation.presentation.user_login.UserRegisterState
import com.bus.reservation.presentation.user_login.UserViewModel
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class SignUpActivity : BaseBindingActivity<ActivitySignUpBinding>() {
    private val userViewModel: UserViewModel by viewModels<UserViewModel>()
    private lateinit var progressDialog: ProgressDialog
    var selectedCountryItem: String? = ""
    var selectedStateItem: String? = ""
    var selectedCityItem: String? = ""
    var selectedGender: String? = ""

    val fristname = MutableStateFlow("")
    val lastname = MutableStateFlow("")
    val birthdate = MutableStateFlow("")
    val city = MutableStateFlow("")
    val gender = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val state = MutableStateFlow("")
    val country = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")


    private var errorMessage: String? = null
    private val isFormValid = combine(
        fristname,
        lastname,
        birthdate,
        city,
        gender,
        phone,
        state,
        country,
        email,
        password
    ) { values: Array<String> ->

        var nameisValid = false
        var lastnameisValid = false
        var birthdateisValid = false
        var emailisValid = false
        var phoneisValid = false
        var passwordisValid = false
        var genderisValid = false
        var countryvalid = false
        var statevalid = false
        var cityvalid = false

        values.forEach {
            when (it) {
                mBinding.edfirstName.text.toString() -> {
                    nameisValid = it.isNotEmpty()
                    Log.e(TAG, "firsname ${nameisValid}: ")
                }

                mBinding.edlastName.text.toString() -> {
                    lastnameisValid = it.isNotEmpty()
                    Log.e(TAG, "lastname ${lastnameisValid}: ")
                }

                mBinding.edemail.text.toString() -> {
                    emailisValid = Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    if (!emailisValid) mBinding.edemail.error = "Please enter valid email"
                    Log.e(TAG, "email ${lastnameisValid}: ")
                }

                mBinding.edphoneNumber.text.toString() -> {
                    phoneisValid = it.length >= 10
                    if (!phoneisValid) mBinding.edphoneNumber.error = "Please enter 10 Digit Phone"
                    Log.e(TAG, "phone ${phoneisValid}: ")
                }

                mBinding.edpassword.text.toString() -> {
                    passwordisValid = it.length >= 6
                    if (!passwordisValid) mBinding.edpassword.error =
                        "Please enter 6 Digit password"
                    Log.e(TAG, "password ${passwordisValid}: ")
                }
                mBinding.edbirthdate.text.toString() -> {
                    birthdateisValid = it.isNotEmpty()
                    Log.e(TAG, "birthdate ${birthdateisValid}: ")
                }


                selectedGender -> {
                    genderisValid = it.isNotEmpty()
                    Log.e(TAG, "password ${genderisValid}: ")
                }

                selectedCityItem -> {
                    cityvalid = it.isNotEmpty()
                    Log.e(TAG, "cityvalid ${cityvalid}: ")
                }

                selectedStateItem -> {
                    statevalid = it.isNotEmpty()
                    Log.e(TAG, "state ${statevalid}: ")
                }

                selectedCountryItem -> {
                    countryvalid = it.isNotEmpty()
                    Log.e(TAG, "country ${countryvalid}: ")
                }
            }
        }
        Log.e(
            TAG,
            "name-->${nameisValid},\n " +
                    "lastnameisValid-->${lastnameisValid},\n" +
                    "emailisValid-->${emailisValid},\n" +
                    "phoneisValid--${phoneisValid},\n" +
                    "genderisValid--${genderisValid},\n" +
                    "countryvalid--${countryvalid},\n" +
                    "statevalid--${statevalid},\n" +
                    "cityvalid--${cityvalid},\n" +
                    "birthdateisValid--${birthdateisValid},\n" +
                    "passwordisValid--${passwordisValid}"

        )
        nameisValid and
                lastnameisValid and
                emailisValid and
                phoneisValid and
                genderisValid and
                countryvalid and
                statevalid and
                cityvalid and
                birthdateisValid and
                passwordisValid

    }

    override fun getActivityContext(): BaseActivity {
        return this@SignUpActivity
    }

    override fun setBinding(): ActivitySignUpBinding {
        return ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()

        with(mBinding) {
            edfirstName.doOnTextChanged { text, start, before, count ->
                fristname.value = text.toString()
            }
            edlastName.doOnTextChanged { text, start, before, count ->
                lastname.value = text.toString()
            }
            edbirthdate.doOnTextChanged { text, start, before, count ->
                birthdate.value = text.toString()
            }
            edemail.doOnTextChanged { text, start, before, count ->
                email.value = text.toString()
            }
            edphoneNumber.doOnTextChanged { text, start, before, count ->
                phone.value = text.toString()
            }
            edpassword.doOnTextChanged { text, start, before, count ->
                password.value = text.toString()
            }

        }

        lifecycleScope.launch {
            isFormValid.collect {
                if (it) mBinding.btnSingUP.isVisable(true) else mBinding.btnSingUP.isVisable(false)
            }
        }
        mBinding.ivDatepicker.setOnClickListener {
            showDatePicker()
        }


        val genderAdapter = ArrayAdapter(
            getActivityContext(),
            R.layout.dropdown_item,
            resources.getStringArray(R.array.genders)
        )
        val countryadapter = ArrayAdapter(
            getActivityContext(),
            R.layout.dropdown_item,
            resources.getStringArray(R.array.country)
        )
        val stateadapter = ArrayAdapter(
            getActivityContext(),
            R.layout.dropdown_item,
            resources.getStringArray(R.array.state)
        )
        val cityadapter = ArrayAdapter(
            getActivityContext(),
            R.layout.dropdown_item,
            resources.getStringArray(R.array.coty)
        )
        mBinding.genderPicker.setAdapter(genderAdapter)
        mBinding.countryPicker.setAdapter(countryadapter)
        mBinding.statePicker.setAdapter(stateadapter)
        mBinding.cityPicker.setAdapter(cityadapter)


        mBinding.genderPicker.setOnItemClickListener { parent, view, position, id ->
            selectedGender = parent!!.getItemAtPosition(position) as String
            gender.value = selectedGender.toString()
        }

        mBinding.countryPicker.setOnItemClickListener { parent, view, position, id ->
            selectedCountryItem = parent!!.getItemAtPosition(position) as String
            country.value = selectedCountryItem.toString()
        }
        mBinding.statePicker.setOnItemClickListener { parent, view, position, id ->
            selectedStateItem = parent!!.getItemAtPosition(position) as String
            state.value = selectedStateItem.toString()
        }
        mBinding.cityPicker.setOnItemClickListener { parent, view, position, id ->
            selectedCityItem = parent!!.getItemAtPosition(position) as String
            city.value = selectedCityItem.toString()
        }

        lifecycleScope.launch {
            userViewModel.userRegisterResponse.collect { response: UserRegisterState ->
                if (response.isLoading) {
                    withContext(Dispatchers.Main) {
                        progressDialog.show()
                    }
                }
                if (response.error?.isNotEmpty() == true) {
                    Log.e(TAG, "SignUpActivity error : =====> ${response.error}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            getActivityContext(),
                            "${response.error}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        progressDialog.dismiss()
                    }
                }
                if (response.data != null) {
                    withContext(Dispatchers.Main) {
                        progressDialog.dismiss()
                        launchActivity(getActivityIntent<LoginActivity> { })
                    }
                    SharePrefranceUtills.oAuthToken = response.data.user.token
                    Log.e(
                        TAG,
                        "SignUpActivity response Success: =====> ${
                            GsonBuilder().setPrettyPrinting().create()
                                .toJson(response.data)
                        }"
                    )
                    Log.e(
                        TAG,
                        "SignUpActivity: =====> ${SharePrefranceUtills.oAuthToken}"
                    )
                }
            }
        }
    }

    override fun initViewListener() {
        super.initViewListener()
        setClickListener(mBinding.btnSingUP,mBinding.tvAlreadyHaveAcc)

    }


    override fun onClick(v: View) {
        super.onClick(v)
        with(mBinding) {
            when (v) {
                tvAlreadyHaveAcc->{launchActivity(getActivityIntent<LoginActivity> {  })}
                btnSingUP -> {
                    progressDialog = ProgressDialog(getActivityContext())
                    if (checkValidation() && checkValidEmail() && checkPasswordLength()) {
                        val jsonObject = JSONObject()
                        jsonObject.put("first_name", edfirstName.text.toString().trim())
                        jsonObject.put("last_name", edlastName.text.toString().trim())
                        jsonObject.put("birthdate", edbirthdate.text.toString().trim())
                        jsonObject.put("city", selectedCityItem)
                        jsonObject.put("gender", selectedGender)
                        jsonObject.put("phone", edphoneNumber.text.toString().trim())
                        jsonObject.put("state", selectedStateItem)
                        jsonObject.put("country", selectedCountryItem)
                        jsonObject.put("email", edemail.text.toString().trim())
                        jsonObject.put("password", edpassword.text.toString().trim())

                        val requestBody = jsonObject.toString()
                            .toRequestBody("application/json; charset=utf-8".toMediaType())
                        userViewModel.registerUser(requestBody)
                        Log.e(
                            TAG,
                            "JSONObject: -------->${
                                GsonBuilder().setPrettyPrinting().create()
                                    .toJson(jsonObject.toString())
                            }",
                        )


                    }

                }

                else -> {

                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        with(mBinding) {
            if (!TextUtils.isEmpty(edfirstName.text.toString()) &&
                !TextUtils.isEmpty(edlastName.text.toString()) &&
                !TextUtils.isEmpty(edphoneNumber.text.toString()) &&
                !TextUtils.isEmpty(edbirthdate.text.toString()) &&
                selectedCountryItem!!.isNotEmpty() &&
                selectedStateItem!!.isNotEmpty() &&
                selectedCityItem!!.isNotEmpty()&&
                selectedGender!!.isNotEmpty()

            ) {
                return true
            } else {
                Toast.makeText(getActivityContext(), "Please fill all details", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
        }
    }

    private fun checkValidEmail(): Boolean {
        if (TextUtils.isEmpty(mBinding.edemail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(
                mBinding.edemail.text.toString()
            ).matches()
        ) {
            mBinding.edemail.error = "Please enter a valid email address"
            return false
        } else {
            return true
        }
    }

    private fun checkPasswordLength(): Boolean {
        if (mBinding.edpassword.text.isEmpty() || mBinding.edpassword.text.length < 6) {
            mBinding.edpassword.error = "Password must be at least 6 characters"
            mBinding.edpassword.requestFocus()
            return false
        } else {
            return true
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().apply {
            setTitleText("Select Date")
            setTheme(R.style.ThemeOverlay_App_DatePicker)
        }.build()

        datePicker.show(supportFragmentManager, "")

        datePicker.addOnPositiveButtonClickListener { materialDate ->
            val dateFormatter = SimpleDateFormat("yyyy/MM/dd")
            val date = dateFormatter.format(Date(materialDate))
            mBinding.edbirthdate.setText(date)
        }
        datePicker.addOnNegativeButtonClickListener {
            Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
        }
        datePicker.addOnCancelListener {
            Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
        }
    }
}