package io.github.jboxx.catering_subscribe.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.jboxx.catering_subscribe.R;

public class CounterView extends LinearLayout {

    public static final int MAX_INFINITE = -1;

    private int count;
    private int maxCount = MAX_INFINITE;
    private String prefix = "";

    private TextView txtCounter;
    private Button btnMinus;
    private Button btnPlus;
    private boolean isPlusEnabled;
    private boolean isMinusEnabled;
    private OnCountChangedListener listener;

    public CounterView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CounterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public CounterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CounterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        TypedArray aTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CounterView, defStyleAttr, 0);

        prefix = aTypedArray.getString(R.styleable.CounterView_prefix);

        aTypedArray.recycle();

        count = 1;

        setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.END);

        txtCounter = new TextView(context);
        txtCounter.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_rounded_stroke_orange));
        txtCounter.setGravity(Gravity.CENTER);
        txtCounter.setTypeface(null, Typeface.BOLD);
        txtCounter.setText(String.valueOf(count) + " " + prefix);
        txtCounter.setTextSize(16f);
        txtCounter.setTextColor(ContextCompat.getColor(context, R.color.default_text_color));
        txtCounter.setPadding(context.getResources().getDimensionPixelSize(R.dimen.margin_8dp),
                context.getResources().getDimensionPixelSize(R.dimen.margin_14dp),
                context.getResources().getDimensionPixelSize(R.dimen.margin_8dp),
                context.getResources().getDimensionPixelSize(R.dimen.margin_14dp));
        txtCounter.setLayoutParams(new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        addView(txtCounter);

        btnMinus = new Button(context);
        btnMinus.setText("-");
        btnMinus.setTextSize(18f);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(
                context.getResources().getDimensionPixelSize(R.dimen.btn_counter_width),
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(context.getResources().getDimensionPixelSize(R.dimen.margin_8dp),
                0, context.getResources().getDimensionPixelSize(R.dimen.margin_2dp), 0);
        btnMinus.setLayoutParams(layoutParams);
        btnMinus.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        btnMinus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_counter_left_state));
        btnMinus.setOnClickListener(v -> {
            if (isMinusEnabled){
                --count;
                if(listener != null){
                    listener.onCountChanged(count);
                }
                redrawCounter(count);
                disableButtonsIfNeeded();
            }
        });
        addView(btnMinus);

        btnPlus = new Button(context);
        btnPlus.setText("+");
        btnPlus.setTextSize(18f);
        btnPlus.setLayoutParams(new LinearLayout.LayoutParams(
                context.getResources().getDimensionPixelSize(R.dimen.btn_counter_width),
                ViewGroup.LayoutParams.MATCH_PARENT));
        btnPlus.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        btnPlus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_counter_right_state));
        btnPlus.setOnClickListener(v -> {
            if(isPlusEnabled){
                ++count;
                if(listener != null){
                    listener.onCountChanged(count);
                }
                redrawCounter(count);
                disableButtonsIfNeeded();
            }
        });
        addView(btnPlus);
        disableButtonsIfNeeded();
    }

    private void disableButtonsIfNeeded() {

        if(maxCount == MAX_INFINITE){
            isPlusEnabled = true;
        } else if((count + 1) > maxCount){
            isPlusEnabled = false;
        } else {
            isPlusEnabled = true;
        }

        if((count - 1) > 0) {
            isMinusEnabled = true;
        } else {
            isMinusEnabled = false;
        }
        setDisabilityLeftCounterButton();
        setDisabilityRightCounterButton();
    }

    private void setDisabilityLeftCounterButton() {
        btnMinus.setEnabled(isMinusEnabled);
    }

    private void setDisabilityRightCounterButton() {
        btnPlus.setEnabled(isPlusEnabled);
    }

    private void redrawCounter(int count) {
        txtCounter.setText(String.valueOf(count) + " " + prefix);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count){
        setCount(count, true);
    }

    public void setCount(int count, boolean shouldFireListeners){

        if(count < 0){
            return;
        }

        if(maxCount == MAX_INFINITE){
            this.count = count;
            if (listener != null && shouldFireListeners) {
                listener.onCountChanged(this.count);
            }
        } else {
            if (count < maxCount) {
                this.count = count;
                if (listener != null && shouldFireListeners) {
                    listener.onCountChanged(this.count);
                }
            } else {
                this.count = maxCount;
            }
        }
        redrawCounter(this.count);
        disableButtonsIfNeeded();
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        if(count > maxCount && maxCount != MAX_INFINITE){
            count = maxCount;
        }
        if (count == 0 && maxCount > 0){
            count = 1;
        }
        redrawCounter(this.count);
        disableButtonsIfNeeded();
    }

    public void setListener(OnCountChangedListener listener) {
        this.listener = listener;
    }

    public interface OnCountChangedListener {
        void onCountChanged(int newCount);
    }
}
