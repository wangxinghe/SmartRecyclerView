package com.mouxuejie.smartrecyclerview.kt

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlin.collections.ArrayList

// 范型
class HeaderFooterRecyclerAdapter_Kt(headerViewInfos: ArrayList<FixedViewInfo>,
                                     footerViewInfos: ArrayList<FixedViewInfo>,
                                     emptyViewInfo: FixedViewInfo,
                                     recyclerView: RecyclerView,
                                     adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val BASE_HEADER_VIEW_TYPE = -1 shl 10
        private val BASE_FOOTER_VIEW_TYPE = -1 shl 11
        private val BASE_EMPTY_VIEW_TYPE = BASE_FOOTER_VIEW_TYPE - 1
    }

    private var mLatestHeaderViewType = BASE_HEADER_VIEW_TYPE
    private var mLatestFooterViewType = BASE_FOOTER_VIEW_TYPE

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    private lateinit var mHeaderViewInfos: ArrayList<FixedViewInfo>
    private lateinit var mFooterViewInfos: ArrayList<FixedViewInfo>
    private lateinit var mEmptyViewInfo: FixedViewInfo

    open class FixedViewInfo {
        /** The view to add to the list  */
        var view: View? = null
        var viewType: Int = 0
        // Object类型
        var data: Any? = null
    }

    private var mAdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    init {
        if (headerViewInfos == null) {
            mHeaderViewInfos = ArrayList()
        } else {
            mHeaderViewInfos = headerViewInfos
        }

        if (footerViewInfos == null) {
            mFooterViewInfos = ArrayList()
        } else {
            mFooterViewInfos = footerViewInfos
        }

        mEmptyViewInfo = emptyViewInfo

        mRecyclerView = recyclerView
        setAdapter(adapter)
    }

    // if 可以替换成 when，也可以不替换
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return when {
            isEmptyViewType(viewType) -> createEmptyViewHolder()
            isHeaderViewType(viewType) -> createHeaderViewHolder(viewType)
            isFooterViewType(viewType) -> createFooterViewHolder(viewType)
            else -> mAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position >= getHeadersCount() && position < getHeadersCount() + itemCount) {
            mAdapter.onBindViewHolder(holder, position - getHeadersCount())
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isEmpty() -> BASE_EMPTY_VIEW_TYPE
            isHeaderView(position) -> mHeaderViewInfos.get(position).viewType
            isFooterView(position) -> mFooterViewInfos.get(position - getHeadersCount() - mAdapter.getItemCount()).viewType
            else -> mAdapter.getItemViewType(position - getHeadersCount())
        }
    }

    override fun getItemCount(): Int {
        return when {
            isEmpty() -> 1
            else -> getFootersCount() + getHeadersCount() + mAdapter.itemCount
        }
    }

    private fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        mAdapter?.unregisterAdapterDataObserver(mAdapterDataObserver)
        mAdapter?.onDetachedFromRecyclerView(mRecyclerView)

        mAdapter = adapter

        mAdapter.registerAdapterDataObserver(mAdapterDataObserver)
        mAdapter.onAttachedToRecyclerView(mRecyclerView)
    }

    private fun setEmptyView(v: View) {
        val fixedViewInfo = FixedViewInfo()
        fixedViewInfo.view = v
        fixedViewInfo.viewType = BASE_EMPTY_VIEW_TYPE
        mEmptyViewInfo = fixedViewInfo
    }

    private fun addHeaderView(v: View) {
        if (null == v) {
            throw IllegalArgumentException("the view to add must not be null")
        }
        if (containsHeaderView(v)) {
            return
        }

        val fixedViewInfo = FixedViewInfo()
        fixedViewInfo.view = v
        fixedViewInfo.viewType = mLatestHeaderViewType++
        mHeaderViewInfos.add(fixedViewInfo)

        notifyItemInserted(getHeadersCount() - 1)
    }

    private fun addFooterView(v: View) {
        if (null == v) {
            throw IllegalArgumentException("the view to add must not be null")
        }
        if (containsFooterView(v)) {
            return
        }

        val fixedViewInfo = FixedViewInfo()
        fixedViewInfo.view = v
        fixedViewInfo.viewType = mLatestFooterViewType++
        mFooterViewInfos.add(fixedViewInfo)

        notifyItemInserted(itemCount - 1)
    }

    private fun removeHeaderView(v: View) {
        val position = getHeaderViewPosition(v)
        if (position >= 0) {
            mHeaderViewInfos.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun removeFooterView(v: View) {
        val position = getFooterViewPosition(v)
        if (position >= 0) {
            mFooterViewInfos.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    public fun getHeadersCount() : Int {
        return mHeaderViewInfos.size
    }

    public fun getFootersCount() : Int {
        return mFooterViewInfos.size
    }

    public fun isEmpty() : Boolean {
        return mAdapter == null || mAdapter.itemCount <= 0
    }

    // for循环集合遍历
    private fun containsHeaderView(v: View) : Boolean {
        for (i in mHeaderViewInfos!!.indices) {
            var info = mHeaderViewInfos!![i]
            if (info.view == v) {
                return true
            }
        }
        return false
    }

    private fun containsFooterView(v: View) : Boolean {
        for (i in mFooterViewInfos!!.indices) {
            var info = mFooterViewInfos!![i]
            if (info.view == v) {
                return true
            }
        }
        return false
    }

    private fun getHeaderView(viewType: Int) : View? {
        for (i in mHeaderViewInfos.indices) {
            val info = mHeaderViewInfos[i]
            if (info.viewType == viewType) {
                return info.view
            }
        }
        return null
    }

    private fun getFooterView(viewType: Int) : View? {
        for (i in mFooterViewInfos.indices) {
            val info = mFooterViewInfos[i]
            if (info.viewType == viewType) {
                return info.view
            }
        }
        return null
    }

    private fun getHeaderViewPosition(v: View) : Int {
        for (i in mHeaderViewInfos.indices) {
            val fixedViewInfo = mHeaderViewInfos[i]
            if (fixedViewInfo.view === v) {
                return i
            }
        }
        return -1
    }

    private fun getFooterViewPosition(v: View) : Int {
        for (i in mFooterViewInfos.indices) {
            val fixedViewInfo = mFooterViewInfos[i]
            if (fixedViewInfo.view === v) {
                return i
            }
        }
        return -1
    }

    private fun isHeaderView(position: Int) : Boolean{
        return position >= 0 && position <= mHeaderViewInfos.size - 1
    }

    private fun isFooterView(position: Int) : Boolean {
        return position >= getHeadersCount() + mAdapter.itemCount && position <= getHeadersCount() + getFootersCount() + mAdapter.itemCount
    }

    private fun isEmptyViewType(viewType: Int) : Boolean {
        return viewType == BASE_EMPTY_VIEW_TYPE
    }

    private fun isHeaderViewType(viewType: Int) : Boolean {
        return viewType in BASE_HEADER_VIEW_TYPE..-1
    }

    private fun isFooterViewType(viewType: Int) : Boolean {
        return viewType in BASE_FOOTER_VIEW_TYPE..(BASE_HEADER_VIEW_TYPE - 1)
    }

    private fun createEmptyViewHolder() : RecyclerView.ViewHolder {
        var emptyView = mEmptyViewInfo.view
        return object : RecyclerView.ViewHolder(emptyView!!){}
    }

    private fun createHeaderViewHolder(viewType: Int) : RecyclerView.ViewHolder {
        val headerView = getHeaderView(viewType)
        return object : RecyclerView.ViewHolder(headerView!!) {}
    }

    private fun createFooterViewHolder(viewType: Int) : RecyclerView.ViewHolder {
        val footerView = getFooterView(viewType)
        return object : RecyclerView.ViewHolder(footerView!!) {}
    }
}