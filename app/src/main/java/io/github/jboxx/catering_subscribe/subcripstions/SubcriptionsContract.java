package io.github.jboxx.catering_subscribe.subcripstions;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;

import java.util.List;

import io.github.jboxx.catering_subscribe.pojo.SubcriptionTime;

public interface SubcriptionsContract {

    int STEP_START_SUBCRIPTION = 0;
    int STEP_DELIVERY_SUBCRIPTION = 1;
    int STEP_PAYMENT_SUBCRIPTION = 2;

    interface View {
        void initViews();

        void showStep(int step, boolean isAnimForwards);
        void exitScreen();

        void clearSelectionCalendar();

        void setRadioCustomSubcriptionTime(int index, String daySubcriptionDisplay,
                                           String descSubcriptionDisplay);
        void setRadioCustomSubcriptionTime(int index, int daySubcription,
                                           String descSubcriptionDisplay);
        void setCheckRadioSubcriptionTime(int index);
        void setSubcriptionTime(List<SubcriptionTime> subcriptionTimeList, int defaultIndex);
        void setSelectionDate(CalendarDay calendarDay);
        void setDisabilibiltyCalendar(List<CalendarDay> calendarDayList);
        void setValuePricePerDay(String value);
        void setValueAmountBox(int newCount);
        void setValueStartSubcriptionDate(String value);
        void setValueStartSubcriptionDateVisibility(boolean isVisible);
        void setValueSubcriptionDays(int numberOfDays);
        void setTotalPrice(String totalPrice);

        void snackbarErrorShouldSelectMoreDates(int i);

        void showTomorrowMonth(CalendarDay calendarDay);

        List<CalendarDay> getSelectedCalendar();
    }

    interface Presenter {

        void setView(SubcriptionsContract.View view);
        void onBegin();
        void onChangeCounter(int newCount);
        void onChangeSubcriptionTime(int index, boolean isSetManually);
        void onClickNextButton();

        boolean saveDate(CalendarDay calendarDay, boolean isChecked);
        void setFirstSelectedDate();

        void onMoveFoward();
        void onMoveBackward();

        LocalDate getFirstSelectedDate();
    }
}
