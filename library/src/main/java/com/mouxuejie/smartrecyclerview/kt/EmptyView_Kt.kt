package com.mouxuejie.smartrecyclerview.kt

import android.content.Context
import android.support.annotation.IntDef
import android.view.LayoutInflater
import android.widget.TextView
import com.mouxuejie.smartrecyclerview.R

class EmptyView_Kt(context: Context, recyclerView: HeaderFooterRecyclerView_Kt, private var mOnReloadListener: OnReloadListener?) {

    companion object {
        const val EMPTY_BY_NETWORK_UNAVAILABLE = 0
        const val EMPTY_BY_DATA = 1
    }

    @IntDef(EMPTY_BY_NETWORK_UNAVAILABLE.toLong(), EMPTY_BY_DATA.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class STATE

    lateinit var mEmptyTv: TextView

    interface OnReloadListener {
        fun onReload()
    }

    init {
        var emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, recyclerView, false)
        mEmptyTv = emptyView.findViewById(R.id.empty_tv)
        recyclerView.setEmptyView(emptyView)
    }

    open fun setState(@STATE state: Int) {
        when (state) {
            EMPTY_BY_NETWORK_UNAVAILABLE -> {
                mEmptyTv.text = "网络异常，请刷新重试"
            }
            EMPTY_BY_DATA -> {
                mEmptyTv.text = "喔唷，暂无数据"
            }
        }
    }
}