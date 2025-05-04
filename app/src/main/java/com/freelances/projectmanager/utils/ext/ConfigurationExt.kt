package com.freelances.projectmanager.utils.ext

import android.content.Context
import android.util.Log
import android.view.View
import java.util.UUID

fun Context.isRTL(): Boolean {
    return resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
}

fun generateUniqueId(): String = UUID.randomUUID().toString()

