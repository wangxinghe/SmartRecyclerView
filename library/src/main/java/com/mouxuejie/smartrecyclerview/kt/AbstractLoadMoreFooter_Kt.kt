package com.mouxuejie.smartrecyclerview.kt

import android.support.annotation.IntDef
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

// 构造函数，成员变量
abstract class AbstractLoadMoreFooter_Kt(recyclerView: HeaderFooterRecyclerView_Kt, private var mOnLoadMoreListener: OnLoadMoreListener) {

    // 常量
    companion object {
        const val STATE_DISABLE = 0
        const val STATE_READY = 1
        const val STATE_LOADING = 2
        const val STATE_FINISHED = 3
        const val STATE_FAILED = 4
    }

    // 注解
    @IntDef(STATE_DISABLE.toLong(), STATE_READY.toLong(), STATE_LOADING.toLong(), STATE_FINISHED.toLong(), STATE_FAILED.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class STATE

    @STATE
    private var mState = STATE_DISABLE

    // 接口
    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    // 构造函数，匿名类
    init {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    tryToLoadMore(recyclerView as HeaderFooterRecyclerView_Kt)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                tryToLoadMore(recyclerView as HeaderFooterRecyclerView_Kt)
            }
        })
    }

    // 函数
    private fun tryToLoadMore(recyclerView: HeaderFooterRecyclerView_Kt) {
        val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        if (lastVisibleItemPosition == recyclerView.getItemCount() - 1 && isReadyLoadMore(recyclerView)) {
            mState = STATE_LOADING
            mOnLoadMoreListener!!.onLoadMore()
        }
    }

    private fun isReadyLoadMore(recyclerView: HeaderFooterRecyclerView_Kt) : Boolean {
        return !recyclerView.isEmpty() && (mState == STATE_READY || mState == STATE_FAILED)
    }

    open fun setState(@STATE state: Int) {
        mState = state
    }

}