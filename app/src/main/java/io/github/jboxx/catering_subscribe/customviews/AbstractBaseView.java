package io.github.jboxx.catering_subscribe.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public abstract class AbstractBaseView extends View {

    public static String TAG = AbstractBaseView.class.getSimpleName();

    public AbstractBaseView(Context context) {
        super(context);
    }

    public AbstractBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        logSpec(MeasureSpec.getMode(widthMeasureSpec));
        setMeasuredDimension(getImprovedDefaultWidth(widthMeasureSpec), getImprovedDefaultHeight(heightMeasureSpec));
    }

    private void logSpec(int specMode) {
        if (specMode == MeasureSpec.UNSPECIFIED) {
            Log.d(TAG, "mode: unspecified");
            return;
        }
        if (specMode == MeasureSpec.AT_MOST) {
            Log.d(TAG, "mode: at most");
            return;
        }
        if (specMode == MeasureSpec.EXACTLY) {
            Log.d(TAG, "mode: exact");
            return;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw called");
    }

    @Override
    protected void onRestoreInstanceState(Parcelable p) {
        Log.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(p);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(TAG, "onSaveInstanceState");
        Parcelable p = super.onSaveInstanceState();
        return p;
    }

    private int getImprovedDefaultHeight(int measureSpec) {
        // int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                return hGetMaximumHeight();
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.AT_MOST:
                return hGetMinimumHeight();
        }
        return specSize;
    }

    private int getImprovedDefaultWidth(int measureSpec) {
        // int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                return hGetMaximumWidth();
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.AT_MOST:
                return hGetMinimumWidth();
        }
        // you shouldn't come here
        return specSize;
    }

    // Override these methods to provide a maximum size
    // "h" stands for hook pattern
    abstract protected int hGetMaximumHeight();

    abstract protected int hGetMaximumWidth();

    // For minimum height use the View's methods
    protected int hGetMinimumHeight() {
        return this.getSuggestedMinimumHeight();
    }

    protected int hGetMinimumWidth() {
        return this.getSuggestedMinimumWidth();
    }
}
