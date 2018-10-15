package com.mouxuejie.smartrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HeaderFooterRecyclerAdapter extends RecyclerView.Adapter {

    private static final int BASE_HEADER_VIEW_TYPE = -1 << 10;
    private static final int BASE_FOOTER_VIEW_TYPE = -1 << 11;
    private static final int BASE_EMPTY_VIEW_TYPE = BASE_FOOTER_VIEW_TYPE - 1;

    private int mLatestHeaderViewType = BASE_HEADER_VIEW_TYPE;
    private int mLatestFooterViewType = BASE_FOOTER_VIEW_TYPE;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    // These two ArrayList are assumed to NOT be null.
    // They are indeed created when declared in ListView and then shared.
    private ArrayList<FixedViewInfo> mHeaderViewInfos;
    private ArrayList<FixedViewInfo> mFooterViewInfos;

    private FixedViewInfo mEmptyViewInfo;

    /**
     * A class that represents a fixed view in a list, for example a header at the top
     * or a footer at the bottom.
     */
    public class FixedViewInfo {
        /** The view to add to the list */
        public View view;
        public int viewType;
        /** The data backing the view. */
        public Object data;
    }

    private RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemMoved(fromPosition, toPosition);
        }
    };

    public HeaderFooterRecyclerAdapter(ArrayList<FixedViewInfo> headerViewInfos,
                                 ArrayList<FixedViewInfo> footerViewInfos, FixedViewInfo emptyViewInfo,
                                 RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        if (headerViewInfos == null) {
            mHeaderViewInfos = new ArrayList<FixedViewInfo>();
        } else {
            mHeaderViewInfos = headerViewInfos;
        }

        if (footerViewInfos == null) {
            mFooterViewInfos = new ArrayList<FixedViewInfo>();
        } else {
            mFooterViewInfos = footerViewInfos;
        }

        mEmptyViewInfo = emptyViewInfo;

        mRecyclerView = recyclerView;
        setAdapter(adapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isEmptyViewType(viewType)) {
            return createEmptyViewHolder();
        } else if (isHeaderViewType(viewType)) {
            return createHeaderViewHolder(viewType);
        } else if (isFooterViewType(viewType)) {
            return createFooterViewHolder(viewType);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= getHeadersCount() && position < getHeadersCount() + getItemCount()) {
            mAdapter.onBindViewHolder(holder, position - getHeadersCount());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmpty()) {
            return BASE_EMPTY_VIEW_TYPE;
        } else if (isHeaderView(position)) {
            return mHeaderViewInfos.get(position).viewType;
        } else if (isFooterView(position)) {
            return mFooterViewInfos.get(position - getHeadersCount() - mAdapter.getItemCount()).viewType;
        }
        return mAdapter.getItemViewType(position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) {
            return 1;
        } else {
            return getFootersCount() + getHeadersCount() + mAdapter.getItemCount();
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
            mAdapter.onDetachedFromRecyclerView(mRecyclerView);
        }
        mAdapter = adapter;
        if (adapter != null) {
            mAdapter.registerAdapterDataObserver(mAdapterDataObserver);
            mAdapter.onAttachedToRecyclerView(mRecyclerView);
        }
    }

    public void setEmptyView(View v) {
        final FixedViewInfo fixedViewInfo = new FixedViewInfo();
        fixedViewInfo.view = v;
        fixedViewInfo.viewType = BASE_EMPTY_VIEW_TYPE;
        mEmptyViewInfo = fixedViewInfo;
    }

    public void addHeaderView(View v) {
        if (null == v) {
            throw new IllegalArgumentException("the view to add must not be null");
        }
        if (containsHeaderView(v)) {
            return;
        }

        final FixedViewInfo fixedViewInfo = new FixedViewInfo();
        fixedViewInfo.view = v;
        fixedViewInfo.viewType = mLatestHeaderViewType++;
        mHeaderViewInfos.add(fixedViewInfo);

        notifyItemInserted(getHeadersCount() - 1);
    }

    public void addFooterView(View v) {
        if (null == v) {
            throw new IllegalArgumentException("the view to add must not be null");
        }
        if (containsFooterView(v)) {
            return;
        }

        final FixedViewInfo fixedViewInfo = new FixedViewInfo();
        fixedViewInfo.view = v;
        fixedViewInfo.viewType = mLatestFooterViewType++;
        mFooterViewInfos.add(fixedViewInfo);

        notifyItemInserted(getItemCount() - 1);
    }

    public void removeHeaderView(View v) {
        final int position = getHeaderViewPosition(v);
        if (position >= 0) {
            mHeaderViewInfos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeFooterView(View v) {
        final int position = getFooterViewPosition(v);
        if (position >= 0) {
            mFooterViewInfos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public int getHeadersCount() {
        return mHeaderViewInfos.size();
    }

    public int getFootersCount() {
        return mFooterViewInfos.size();
    }

    public boolean isEmpty() {
        return mAdapter == null || mAdapter.getItemCount() <= 0;
    }

    private boolean containsHeaderView(View v) {
        for (int i = 1; i < mHeaderViewInfos.size(); i++) {
            FixedViewInfo info = mHeaderViewInfos.get(i);
            if (info.view == v) {
                return true;
            }
        }
        return false;
    }

    private boolean containsFooterView(View v) {
        for (int i = 0; i < mFooterViewInfos.size(); i++) {
            FixedViewInfo info = mFooterViewInfos.get(i);
            if (info.view == v) {
                return true;
            }
        }
        return false;
    }

    private View getHeaderView(int viewType) {
        for (int i = 0; i < mHeaderViewInfos.size(); i++) {
            FixedViewInfo info = mHeaderViewInfos.get(i);
            if (info.viewType == viewType) {
                return info.view;
            }
        }
        return null;
    }

    private View getFooterView(int viewType) {
        for (int i = 0; i < mFooterViewInfos.size(); i++) {
            FixedViewInfo info = mFooterViewInfos.get(i);
            if (info.viewType == viewType) {
                return info.view;
            }
        }
        return null;
    }

    private int getHeaderViewPosition(View v) {
        for (int i = 0; i < mHeaderViewInfos.size(); i++) {
            final FixedViewInfo fixedViewInfo = mHeaderViewInfos.get(i);
            if (fixedViewInfo.view == v) {
                return i;
            }
        }
        return -1;
    }

    private int getFooterViewPosition(View v) {
        for (int i = 0; i < mFooterViewInfos.size(); i++) {
            final FixedViewInfo fixedViewInfo = mFooterViewInfos.get(i);
            if (fixedViewInfo.view == v) {
                return i;
            }
        }
        return -1;
    }

    private boolean isHeaderView(int position) {
        return position >= 0 && position <= mHeaderViewInfos.size() - 1;
    }

    private boolean isFooterView(int position) {
        return position >= getHeadersCount() + mAdapter.getItemCount()
                && position <= getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
    }

    private boolean isEmptyViewType(int viewType) {
        return viewType == BASE_EMPTY_VIEW_TYPE;
    }

    private boolean isHeaderViewType(int viewType) {
        return viewType >= BASE_HEADER_VIEW_TYPE && viewType < 0;
    }

    private boolean isFooterViewType(int viewType) {
        return viewType >= BASE_FOOTER_VIEW_TYPE && viewType < BASE_HEADER_VIEW_TYPE;
    }

    private RecyclerView.ViewHolder createEmptyViewHolder() {
        View emptyView = mEmptyViewInfo.view;
        return new RecyclerView.ViewHolder(emptyView){};
    }

    private RecyclerView.ViewHolder createHeaderViewHolder(int viewType) {
        View headerView = getHeaderView(viewType);
        return new RecyclerView.ViewHolder(headerView){};
    }

    private RecyclerView.ViewHolder createFooterViewHolder(int viewType) {
        View footerView = getFooterView(viewType);
        return new RecyclerView.ViewHolder(footerView){};
    }

}
