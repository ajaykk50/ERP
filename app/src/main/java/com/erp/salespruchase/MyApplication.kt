package com.erp.salespruchase

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}