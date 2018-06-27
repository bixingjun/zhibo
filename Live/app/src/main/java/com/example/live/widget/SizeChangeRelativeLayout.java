package com.example.live.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;



public class SizeChangeRelativeLayout extends RelativeLayout {

    public SizeChangeRelativeLayout(Context context) {
        super(context);
    }

    public SizeChangeRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public SizeChangeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mOnSizeChangeListener == null) {
            return;
        }
        if (h > oldh) {

            mOnSizeChangeListener.onLarge();
        } else {

            mOnSizeChangeListener.onSmall();
        }
    }

    private OnSizeChangeListener mOnSizeChangeListener;

    public void setOnSizeChangeListener(OnSizeChangeListener l) {
        mOnSizeChangeListener = l;
    }

    public interface OnSizeChangeListener {
        public void onLarge();

        public void onSmall();
    }
}
