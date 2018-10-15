package com.mouxuejie.smartrecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import com.mouxuejie.smartrecyclerview.R.*;

public class LoadMoreFooter extends AbstractLoadMoreFooter {

//    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

//    @BindView(id.tv_text)
    TextView tvText;

    public LoadMoreFooter(Context context, HeaderFooterRecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener) {
        super(recyclerView, onLoadMoreListener);
        View footerView = LayoutInflater.from(context).inflate(layout.footer_load_more, null);
//        ButterKnife.bind(this, footerView);
        progressWheel = footerView.findViewById(R.id.progress_wheel);
        tvText = footerView.findViewById(R.id.tv_text);
        recyclerView.addFooterView(footerView);
    }

    @Override
    public void setState(@STATE int state) {
        super.setState(state);
        switch (state) {
            case STATE_DISABLED:
                progressWheel.setVisibility(View.INVISIBLE);
                progressWheel.stopSpinning();
                tvText.setVisibility(View.INVISIBLE);
                tvText.setText(null);
                tvText.setClickable(false);
                break;
            case STATE_LOADING:
                progressWheel.setVisibility(View.VISIBLE);
                progressWheel.spin();
                tvText.setVisibility(View.INVISIBLE);
                tvText.setText(null);
                tvText.setClickable(false);
                break;
            case STATE_FINISHED:
                progressWheel.setVisibility(View.INVISIBLE);
                progressWheel.stopSpinning();
                tvText.setVisibility(View.VISIBLE);
                tvText.setText(R.string.load_more_finished);
                tvText.setClickable(false);
                break;
            case STATE_READY:
                progressWheel.setVisibility(View.INVISIBLE);
                progressWheel.stopSpinning();
                tvText.setVisibility(View.VISIBLE);
                tvText.setText(null);
                tvText.setClickable(true);
                break;
            case STATE_FAILED:
                progressWheel.setVisibility(View.INVISIBLE);
                progressWheel.stopSpinning();
                tvText.setVisibility(View.VISIBLE);
                tvText.setText(R.string.load_more_failed);
                tvText.setClickable(true);
                break;
            default:
                throw new AssertionError("Unknown load more state.");
        }
    }

}
