package com.mouxuejie.smartrecyclerview;

import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    @LoadMoreFooter.STATE
    private int mState = STATE_DISABLED;

    public LoadMoreFooter.OnLoadMoreListener mOnLoadMoreListener;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public AbstractLoadMoreFooter(HeaderFooterRecyclerView recyclerView, LoadMoreFooter.OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("wxh", "onScrollStateChanged");
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    tryToLoadMore((HeaderFooterRecyclerView) recyclerView);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("wxh", "onScrolled");
                tryToLoadMore((HeaderFooterRecyclerView) recyclerView);
            }
        });
    }

    public void setState(@LoadMoreFooter.STATE int state) {
        mState = state;
    }

    @LoadMoreFooter.STATE
    public int getState() {
        return mState;
    }

    private void tryToLoadMore(HeaderFooterRecyclerView recyclerView) {
        final int lastVisibleItemPosition = ((LinearLayoutManager)
                recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        if (lastVisibleItemPosition == recyclerView.getItemCount() - 1 && isReadyLoadMore(recyclerView)) {
            setState(STATE_LOADING);
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onLoadMore();
            }
        }
    }

    private boolean isReadyLoadMore(HeaderFooterRecyclerView recyclerView) {
        return !recyclerView.isEmpty() && (mState == STATE_READY || mState == STATE_FAILED);
    }

}
