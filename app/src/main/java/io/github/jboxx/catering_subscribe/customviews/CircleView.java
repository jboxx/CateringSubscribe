package io.github.jboxx.catering_subscribe.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import io.github.jboxx.catering_subscribe.R;

public class CircleView extends AbstractBaseView {

    private int circleRadius = 18;
    private int strokeColor = 0xFFFD9714;
    private int strokeWidth = 8;
    private int fillColor = 0X00FFFFFF;
    private int circleGap = 2;

    public CircleView(Context context) {
        super(context);

        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

        TypedArray aTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);

        strokeColor = aTypedArray.getColor(R.styleable.CircleView_strokeColor, strokeColor);
        strokeWidth = aTypedArray.getDimensionPixelSize(R.styleable.CircleView_strokeWidth, strokeWidth);
        fillColor = aTypedArray.getColor(R.styleable.CircleView_fillColor, fillColor);
        circleRadius = aTypedArray.getDimensionPixelSize(R.styleable.CircleView_circleRadius, circleRadius);
        circleGap = aTypedArray.getDimensionPixelSize(R.styleable.CircleView_circleGap, circleGap);

        aTypedArray.recycle();

    }

    private void init() {
        this.setMinimumHeight(circleRadius * 2 + strokeWidth);
        this.setMinimumWidth(circleRadius * 2 + strokeWidth);
        this.setPadding(0, 0, 0, 0);
        this.setSaveEnabled(true);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        super.onDraw(canvas);

        int w = this.getWidth();
        int h = this.getHeight();

        int ox = w/2;
        int oy = h/2;

        canvas.drawCircle(ox, oy, circleRadius, getStroke());
        canvas.drawCircle(ox, oy, circleRadius - circleGap, getFill());
    }

    @Override
    protected int hGetMaximumHeight() {
        return circleRadius * 2 + strokeWidth;
    }

    @Override
    protected int hGetMaximumWidth() {
        return circleRadius * 2 + strokeWidth;
    }

    private Paint getStroke() {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(strokeWidth);
        p.setColor(strokeColor);
        p.setStyle(Paint.Style.STROKE);
        return p;
    }

    private Paint getFill() {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(fillColor);
        p.setStyle(Paint.Style.FILL);
        return p;
    }

    public void setCircleActive(boolean isActive) {
        this.strokeColor = isActive ? 0xFFFD9714 : 0xFFE0E0E0;
        invalidate();
    }

}
