package com.mouxuejie.smartrecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

public class LoadMoreFooter extends AbstractLoadMoreFooter {

//    @BindView(R2.id.progress_wheel)
    ProgressWheel mProgressWheel;

//    @BindView(R2.id.tv_text)
    TextView mTvText;

    public LoadMoreFooter(Context context, HeaderFooterRecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener) {
        super(recyclerView, onLoadMoreListener);
        View footerView = LayoutInflater.from(context).inflate(R.layout.footer_load_more, null);
//        ButterKnife.bind(this, footerView);
        mProgressWheel = footerView.findViewById(R.id.progress_wheel);
        mTvText = footerView.findViewById(R.id.tv_text);
        recyclerView.addFooterView(footerView);
    }

    public void setState(@STATE int state) {
        super.setState(state);
        switch (state) {
            case STATE_DISABLED:
                mProgressWheel.setVisibility(View.INVISIBLE);
                mProgressWheel.stopSpinning();
                mTvText.setVisibility(View.INVISIBLE);
                mTvText.setText(null);
                mTvText.setClickable(false);
                break;
            case STATE_LOADING:
                mProgressWheel.setVisibility(View.VISIBLE);
                mProgressWheel.spin();
                mTvText.setVisibility(View.INVISIBLE);
                mTvText.setText(null);
                mTvText.setClickable(false);
                break;
            case STATE_FINISHED:
                mProgressWheel.setVisibility(View.INVISIBLE);
                mProgressWheel.stopSpinning();
                mTvText.setVisibility(View.VISIBLE);
                mTvText.setText(R.string.load_more_finished);
                mTvText.setClickable(false);
                break;
            case STATE_READY:
                mProgressWheel.setVisibility(View.INVISIBLE);
                mProgressWheel.stopSpinning();
                mTvText.setVisibility(View.VISIBLE);
                mTvText.setText(null);
                mTvText.setClickable(true);
                break;
            case STATE_FAILED:
                mProgressWheel.setVisibility(View.INVISIBLE);
                mProgressWheel.stopSpinning();
                mTvText.setVisibility(View.VISIBLE);
                mTvText.setText(R.string.load_more_failed);
                mTvText.setClickable(true);
                break;
            default:
                throw new AssertionError("Unknown load more state.");
        }
    }

}
