package com.erp.salespruchase.viewmodel

import androidx.lifecycle.ViewModel
import com.erp.salespruchase.DisplaySale

class SharedViewModel : ViewModel() {
    private var _displaySale: DisplaySale? = null
    val displaySale: DisplaySale?
        get() = _displaySale

    fun setDisplaySale(data: DisplaySale?) {
        _displaySale = data
    }
}