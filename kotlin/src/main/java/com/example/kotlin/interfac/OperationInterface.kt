package com.myapplication.interfac

import androidx.appcompat.widget.Toolbar


interface OperationInterface {
    /**
     * 初始化toolbar
     */
    fun initToolbar(toolbar: Toolbar) {

    }

    /**
     * 状态栏是覆盖在内容上方，默认不覆盖
     */
    fun statusVisible(): Boolean {
        return true
    }

    /**
     * 是否全屏，默认不全屏
     */
    fun fullScreen(): Boolean {
        return false
    }
}