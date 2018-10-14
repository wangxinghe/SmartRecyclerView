package com.mouxuejie.smartrecyclerview;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class AbstractLoadMoreFooter {
    // 不允许加载
    public static final int STATE_DISABLED = 0;
    // 准备加载
    public static final int STATE_READY = 1;
    // 加载中
    public static final int STATE_LOADING = 2;
    // 加载完成
    public static final int STATE_FINISHED = 3;
    // 加载失败
    public static final int STATE_FAILED = 4;

    @IntDef({STATE_DISABLED, STATE_READY, STATE_LOADING, STATE_FINISHED, STATE_FAILED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATE {}

    @STATE
    private int mState;

    public OnLoadMoreListener mOnLoadMoreListener;

    public interface OnLoadMoreListener {
        void loadMore();
    }

    public AbstractLoadMoreFooter(RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    loadMore();
                }
            }
        });
    }

    public void setState(@STATE int state) {
        mState = state;
    }

    @STATE
    public int getState() {
        return mState;
    }

    private void loadMore() {
        if (mState == STATE_READY || mState == STATE_FAILED) {
            setState(STATE_LOADING);
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.loadMore();
            }
        }
    }
}
