package com.mouxuejie.smartrecyclerview;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EmptyView {

    // 网络异常引起的空数据
    public static final int EMPTY_BY_NETWORK_UNAVAILABLE = 0;
    // 数据为空
    public static final int EMPTY_BY_DATA = 1;

    @IntDef({EMPTY_BY_NETWORK_UNAVAILABLE, EMPTY_BY_DATA})
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATE {}

    private TextView mEmptyTv;

    private OnReloadListener mOnReloadListener;

    public interface OnReloadListener {
        void onReload();
    }

    public EmptyView(Context context, HeaderFooterRecyclerView recyclerView, OnReloadListener onReloadListener) {
        View emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, recyclerView, false);
        mEmptyTv = emptyView.findViewById(R.id.empty_tv);
        recyclerView.setEmptyView(emptyView);
        mOnReloadListener = onReloadListener;
    }

    public void setState(@STATE int state) {
        switch (state) {
            case EMPTY_BY_NETWORK_UNAVAILABLE:
                mEmptyTv.setText("网络异常，请刷新重试");
                break;
            case EMPTY_BY_DATA:
                mEmptyTv.setText("喔唷，暂无数据");
                break;
        }
    }
}
