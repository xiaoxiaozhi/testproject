package com.myapplication.interfac

import android.support.v7.widget.Toolbar

interface OperationInterface {
    fun initToolbar(toolbar: Toolbar): Toolbar {
        return toolbar
    }

    fun statusVisible(): Boolean {
        return false
    }
}