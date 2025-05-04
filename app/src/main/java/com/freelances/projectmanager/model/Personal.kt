package com.freelances.projectmanager.model

import android.os.Parcelable
import com.freelances.projectmanager.utils.ext.generateUniqueId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Personal(
    val idPersonal: String = generateUniqueId(),
    val name: String = "",
    val date: String = "",
    val sex: String = "",
): Parcelable