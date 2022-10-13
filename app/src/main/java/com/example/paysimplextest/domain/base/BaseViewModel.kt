package com.example.paysimplextest.domain.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel() : ViewModel() {
    protected var TAG: String = this.javaClass.simpleName
    var MSG: String = ""
}