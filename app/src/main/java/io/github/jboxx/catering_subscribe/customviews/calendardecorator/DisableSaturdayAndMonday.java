package io.github.jboxx.catering_subscribe.customviews.calendardecorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.DayOfWeek;

import java.util.Collection;
import java.util.HashSet;

public class DisableSaturdayAndMonday implements DayViewDecorator {

    private HashSet<CalendarDay> dates = new HashSet<>();

    public DisableSaturdayAndMonday() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        DayOfWeek dayOfWeek = calendarDay.getDate().getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SUNDAY
                || dayOfWeek == DayOfWeek.SATURDAY) {
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

    public void addDatesNotDisable(Collection<CalendarDay> list) {
        dates.clear();
        dates.addAll(list);
    }
}
