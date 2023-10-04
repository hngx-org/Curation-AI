package com.panther.contentai.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.panther.contentai.util.CURATOR_PREFERENCE
import com.panther.contentai.util.FIRST_LAUNCH


class CuratorSharedPreference {
    private val editor = sharedPref?.edit()

    fun updateSharedPref(state: Boolean) {
        Log.d(FIRST_LAUNCH, "updateSharedPref: $state ")
        editor?.putBoolean(FIRST_LAUNCH, state)?.apply()
    }

    fun fetchSharedPref(): Boolean {
        val state = sharedPref?.getBoolean(FIRST_LAUNCH,true)?:true
        Log.d(FIRST_LAUNCH, "getSharedPref: $state")
        return state
    }

    companion object{
        private var sharedPref:SharedPreferences? = null
        fun initSharedPref(context: Context){
            sharedPref = context.getSharedPreferences(CURATOR_PREFERENCE, Context.MODE_PRIVATE)
        }
    }

}