package com.ponomarenko.livestreaming.extensions

import android.content.Context
import android.util.Log
import android.widget.Toast

fun String.loge() {
    Log.e(javaClass.simpleName, "Error: $this")
}

fun String.logd() {
    Log.d(javaClass.simpleName, "Debug: $this")
}

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()