package io.github.jboxx.catering_subscribe.customviews.calendardecorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

public class DisableSaturdayAndMonday implements DayViewDecorator {

    private HashSet<CalendarDay> dates = new HashSet<>();

    public DisableSaturdayAndMonday() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        int dayOfWeek = calendarDay.getCalendar().get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY
                || dayOfWeek == Calendar.SATURDAY) {
            return true;
        } else if (dates.size() > 0 && !dates.contains(calendarDay)) {
            return true;
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
        dayViewFacade.setDaysDisabled(true);
    }

    public void addDatesNotDisable(boolean isDisable, Collection<CalendarDay> list) {
        dates.clear();
        if (isDisable) {
            dates.addAll(list);
        }
    }
}
