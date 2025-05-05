package com.freelances.projectmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Personal(
    val maNv: String = "",
    val name: String = "",
    val date: String = "",
    val sex: String = "",
    val chucVu: String = "",
    val hsl: String = "",
    val lcb: String = "",
): Parcelable