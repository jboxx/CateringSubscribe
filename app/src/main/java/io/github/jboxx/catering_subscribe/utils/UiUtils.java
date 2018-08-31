package io.github.jboxx.catering_subscribe.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.concurrent.atomic.AtomicInteger;

import io.github.jboxx.catering_subscribe.R;

public class UiUtils {

    private final AtomicInteger sNextGeneratedId;

    public UiUtils() {
        sNextGeneratedId = new AtomicInteger(1);
    }

    public void showKeyBoard(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void showKeyBoard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideKeyboard(Context context, View view) {
        if(view != null){
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setupToolbar(@IdRes int toolbarId, AppCompatActivity activity){
        setupToolbar(0, toolbarId, activity, null);
    }

    public void setupToolbar(@IdRes int toolbarId,
                             AppCompatActivity activity, ColorDrawable colorDrawable){
        setupToolbar(0, toolbarId, activity, colorDrawable);
    }

    public final void setupToolbar(@StringRes int title, @IdRes int toolbarId,
                                   AppCompatActivity activity) {
        setupToolbar(title, toolbarId, activity, R.drawable.ic_back_dark, null);
    }

    public final void setupToolbar(@StringRes int title, @IdRes int toolbarId,
                                   AppCompatActivity activity, ColorDrawable colorDrawable) {
        setupToolbar(title, toolbarId, activity, R.drawable.ic_back_dark, colorDrawable);
    }

    public final void setupToolbar(@StringRes int title,
                                   @IdRes int toolbarId,
                                   AppCompatActivity activity,
                                   @DrawableRes int navigationIcon) {
        setupToolbar(title, toolbarId, activity, navigationIcon, null);
    }

    public final void setupToolbar(@StringRes int title,
                                   @IdRes int toolbarId,
                                   AppCompatActivity activity,
                                   @DrawableRes int navigationIcon,
                                   ColorDrawable colorDrawable) {
        Toolbar toolbar = ((Toolbar) activity.findViewById(toolbarId));
        toolbar.setTitle((title == 0) ? "" : activity.getString(title));
        activity.setSupportActionBar(toolbar);
        if (navigationIcon != 0){
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(navigationIcon);
        }
        if (title == 0) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if (colorDrawable != null) {
            activity.getSupportActionBar().setBackgroundDrawable(colorDrawable);
        }
    }

    public float dpFromPx(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    public float pxFromDp(float dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }

    @SuppressLint("NewApi")
    public int generateViewId() {
        if (Build.VERSION.SDK_INT < 17) {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }

}
