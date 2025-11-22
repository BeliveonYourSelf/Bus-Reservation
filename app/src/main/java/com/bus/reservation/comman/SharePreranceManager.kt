package com.bus.reservation.comman

import android.content.Context
import com.example.day6task.base.BaseApplication

val sharePreranceManager: SharePreranceManager
    get() = SharePreranceManager(BaseApplication.AppContext.context)


class SharePreranceManager(context: Context) {
    private val mPref = context.getSharedPreferences(PERFNAME, Context.MODE_PRIVATE)

    companion object {
        const val PERFNAME = "bus_pref"
    }

    fun setStringValue(key: String?, default: String?) {
        mPref.edit().putString(key, default).apply()
    }
    fun getStringValue(key: String?,default: String?) : String?{
        return mPref.getString(key,default)
    }

    fun setBooleanValue(key: String?, default: Boolean){
        mPref.edit().putBoolean(key,default).apply()
    }
    fun getBooleanValue(key: String?,default: Boolean) : Boolean{
        return mPref.getBoolean(key,default)
    }
}