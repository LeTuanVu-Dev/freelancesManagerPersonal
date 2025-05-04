package com.freelances.projectmanager.di

import android.content.Context
import android.content.SharedPreferences
import com.freelances.projectmanager.utils.helper.PersonalHelper
import com.freelances.projectmanager.utils.preference.AppSharedPreference
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(AppSharedPreference.PREF_NAME, Context.MODE_PRIVATE)
    }
    singleOf(::AppSharedPreference)
    singleOf(::PersonalHelper)
}