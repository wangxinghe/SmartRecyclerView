package com.mouxuejie.smartrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class HeaderFooterRecyclerView extends RecyclerView {
    private HeaderFooterRecyclerAdapter mHeaderFooterRecyclerAdapter;

    public HeaderFooterRecyclerView(Context context) {
        super(context);
        init();
    }

    public HeaderFooterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderFooterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mHeaderFooterRecyclerAdapter = new HeaderFooterRecyclerAdapter(null,
                null, null, this, null);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(null);
        mHeaderFooterRecyclerAdapter.setAdapter(adapter);
        super.setAdapter(mHeaderFooterRecyclerAdapter);
    }

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        super.swapAdapter(null, removeAndRecycleExistingViews);
        mHeaderFooterRecyclerAdapter.setAdapter(adapter);
        super.swapAdapter(adapter, removeAndRecycleExistingViews);
    }

    public void setEmptyView(View v) {
        mHeaderFooterRecyclerAdapter.setEmptyView(v);
    }

    public void addHeaderView(View v) {
        mHeaderFooterRecyclerAdapter.addHeaderView(v);
    }

    public void addFooterView(View v) {
        mHeaderFooterRecyclerAdapter.addFooterView(v);
    }

    public void removeHeaderView(View v) {
        mHeaderFooterRecyclerAdapter.removeHeaderView(v);
    }

    public void removeFooterView(View v) {
        mHeaderFooterRecyclerAdapter.removeFooterView(v);
    }

    public boolean isEmpty() {
        return mHeaderFooterRecyclerAdapter.isEmpty();
    }

    public int getItemCount() {
        return mHeaderFooterRecyclerAdapter.getItemCount();
    }

}
