package com.bus.reservation.ui.activitys

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.bus.reservation.comman.SharePrefranceUtills
import com.bus.reservation.databinding.ActivitySplashBinding
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.base.BaseBindingActivity

class SplashActivity : BaseBindingActivity<ActivitySplashBinding>() {
    override fun getActivityContext(): BaseActivity {
        return this@SplashActivity
    }

    override fun setBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            if (ActivityCompat.checkSelfPermission(getActivityContext(),Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivityContext(), arrayOf(Manifest.permission.POST_NOTIFICATIONS),100)
            }
        }


    }

    override fun onResume() {
        super.onResume()
        Handler(mainLooper).postDelayed({
            if(SharePrefranceUtills.isIntroComplete)
            {
                if (SharePrefranceUtills.isLogin) {
                    launchActivity(getActivityIntent<MainActivity> { })
                } else {
                    launchActivity(getActivityIntent<LoginActivity> { })
                }
            }else{
                launchActivity(getActivityIntent<IntroducationActivity> {  })
            }

        }, 3000)
    }
}