package com.mouxuejie.smartrecyclerview.kt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.mouxuejie.smartrecyclerview.R
import com.pnikosis.materialishprogress.ProgressWheel

// 类继承，构造函数继承
class LoadMoreFooter_Kt(context : Context, recyclerView: HeaderFooterRecyclerView_Kt, onLoadMoreListener: OnLoadMoreListener)
    : AbstractLoadMoreFooter_Kt(recyclerView, onLoadMoreListener) {

    // 成员变量
    private var mProgressWheel : ProgressWheel
    private var mTvText : TextView

    init {
        var footerView = LayoutInflater.from(context).inflate(R.layout.footer_load_more, null)
        mProgressWheel = footerView.findViewById(R.id.progress_wheel)
        mTvText = footerView.findViewById(R.id.tv_text)
        recyclerView.addFooterView(footerView)
    }

    // 函数复写，选择语句when
    override fun setState(@STATE state: Int) {
        super.setState(state)
        when (state) {
            STATE_DISABLE -> {
                mProgressWheel.visibility = View.INVISIBLE
                mProgressWheel.stopSpinning()
                mTvText.visibility = View.INVISIBLE
                mTvText.text = null
                mTvText.isClickable = false
            }
            STATE_READY -> {
                mProgressWheel.visibility = View.INVISIBLE
                mProgressWheel.stopSpinning()
                mTvText.visibility = View.VISIBLE
                mTvText.text = null
                mTvText.isClickable = true
            }
            STATE_LOADING -> {
                mProgressWheel.visibility = View.VISIBLE
                mProgressWheel.spin()
                mTvText.visibility = View.INVISIBLE
                mTvText.text = null
                mTvText.isClickable = false
            }
            STATE_FINISHED -> {
                mProgressWheel.visibility = View.INVISIBLE
                mProgressWheel.stopSpinning()
                mTvText.visibility = View.VISIBLE
                mTvText.setText(R.string.load_more_finished)
                mTvText.isClickable = false
            }
            STATE_FAILED -> {
                mProgressWheel.visibility = View.INVISIBLE
                mProgressWheel.stopSpinning()
                mTvText.visibility = View.VISIBLE
                mTvText.setText(R.string.load_more_failed)
                mTvText.isClickable = true
            }
            else -> throw AssertionError("Unknown load more state.")
        }
    }
}