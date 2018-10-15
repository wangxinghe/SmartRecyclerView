package com.mouxuejie.smartrecyclerview.kt

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.mouxuejie.smartrecyclerview.HeaderFooterRecyclerAdapter


class HeaderFooterRecyclerView_Kt : RecyclerView {
    private var mHeaderFooterRecyclerAdapter: HeaderFooterRecyclerAdapter? = null

    // 主构造函数，次构造函数
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        mHeaderFooterRecyclerAdapter = HeaderFooterRecyclerAdapter(null, null, null, this, null)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(null)
        mHeaderFooterRecyclerAdapter!!.setAdapter(adapter)
        super.setAdapter(mHeaderFooterRecyclerAdapter)
    }

    override fun swapAdapter(adapter: Adapter<*>?, removeAndRecycleExistingViews: Boolean) {
        super.swapAdapter(null, removeAndRecycleExistingViews)
        mHeaderFooterRecyclerAdapter!!.setAdapter(adapter)
        super.swapAdapter(mHeaderFooterRecyclerAdapter, removeAndRecycleExistingViews)
    }

    fun setEmptyView(v: View) {
        mHeaderFooterRecyclerAdapter!!.setEmptyView(v)
    }

    fun addHeaderView(v: View) {
        mHeaderFooterRecyclerAdapter!!.addHeaderView(v)
    }

    fun addFooterView(v: View) {
        mHeaderFooterRecyclerAdapter!!.addFooterView(v)
    }

    fun removeHeaderView(v: View) {
        mHeaderFooterRecyclerAdapter!!.removeHeaderView(v)
    }

    fun removeFooterView(v: View) {
        mHeaderFooterRecyclerAdapter!!.removeFooterView(v)
    }

    fun isEmpty(): Boolean {
        return mHeaderFooterRecyclerAdapter!!.isEmpty()
    }

    fun getItemCount(): Int {
        return mHeaderFooterRecyclerAdapter!!.getItemCount()
    }
}