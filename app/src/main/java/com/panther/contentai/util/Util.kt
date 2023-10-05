package com.panther.contentai.util


const val CURATOR_PREFERENCE = "Events shared preference"
const val FIRST_LAUNCH = "Application First Launch"

fun String?.isValid(msg:String):String {
    return try {
        if (this.isNullOrEmpty()) {
            msg
        } else {
            return this

        }
    } catch (e: Exception) {
        msg
    } catch (e: NullPointerException) {
        msg
    }
}
