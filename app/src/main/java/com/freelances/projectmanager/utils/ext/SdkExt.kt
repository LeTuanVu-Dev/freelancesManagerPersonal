package com.freelances.`21A100100413_NgoAnhTuan_05052025`.utils.ext

import android.os.Build

fun getCurrentSdkVersion(): Int {
    return Build.VERSION.SDK_INT
}

val isAPI26OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

val isAPI28OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

val isAPI29OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

val isAPI30OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

val isAPI31OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

val isAPI33OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

val isAPI34OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
