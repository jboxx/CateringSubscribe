package io.github.jboxx.catering_subscribe.customviews.calendardecorator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import io.github.jboxx.catering_subscribe.R;

public class SelectionSquareDecorator implements DayViewDecorator {

    private final Drawable drawableSelected;
    private final Drawable drawable;

    public SelectionSquareDecorator(Context context) {
        drawable = ContextCompat.getDrawable(context, R.drawable.calendar_date_bg);
        drawableSelected = ContextCompat.getDrawable(context, R.drawable.calendar_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
        view.setSelectionDrawable(drawableSelected);
    }
}
