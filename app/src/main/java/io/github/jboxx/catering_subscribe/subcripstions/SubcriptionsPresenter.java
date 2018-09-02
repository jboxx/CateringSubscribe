package io.github.jboxx.catering_subscribe.subcripstions;

import android.support.annotation.Nullable;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.jboxx.catering_subscribe.pojo.SubcriptionTime;

public class SubcriptionsPresenter implements SubcriptionsContract.Presenter {

    private final String TAG = SubcriptionsPresenter.class.getSimpleName();

    private int currentStep = SubcriptionsContract.STEP_START_SUBCRIPTION;

    private SubcriptionsContract.View view;

    private Locale locale;
    private SimpleDateFormat simpleDateFormat;
    private List<SubcriptionTime> subcriptionTimeList;
    private SubcriptionTime subcriptionTime;
    private SubcriptionTime customSubcriptionTime;
    private int newCount;
    private int numberOfDays;

    public SubcriptionsPresenter() {
        this.locale = new Locale("id");
        this.simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);
        this.subcriptionTimeList= new ArrayList<>();
    }

    @Override
    public void setView(SubcriptionsContract.View view) {
        this.view = view;
    }

    @Override
    public void onBegin() {

        SubcriptionTime subcriptionTime1
                = new SubcriptionTime("20_DAYS",
                "20 Hari", "Rp 22.500/hari", 20,
                "UNDEFINED",22500);
        subcriptionTimeList.add(subcriptionTime1);

        SubcriptionTime subcriptionTime2
                = new SubcriptionTime("10_DAYS",
                "10 Hari", "Rp 24.250/hari", 10,
                "19", 24250);
        subcriptionTimeList.add(subcriptionTime2);

        SubcriptionTime subcriptionTime3
                = new SubcriptionTime("5_DAYS",
                "5 Hari", "Rp 25.000/hari", 5,
                "9", 25000);
        subcriptionTimeList.add(subcriptionTime3);

        SubcriptionTime subcriptionTime4
                = new SubcriptionTime("CUSTOM",
                "Pilih Sendiri", "Rp 25.000/hari", 2,
                "4", 25000);
        subcriptionTimeList.add(subcriptionTime4);

        for (int i = 0; i < subcriptionTimeList.size(); i++) {
            subcriptionTimeList.get(i).setPid(i);
            if (subcriptionTimeList.get(i).getParam().equals(SubcriptionTime.CUSTOM)) {
                this.customSubcriptionTime = new SubcriptionTime(subcriptionTimeList.get(i));
                this.customSubcriptionTime.setPid(i);
                this.customSubcriptionTime.setDescSubcriptionDisplay(
                        "Min. " + subcriptionTimeList.get(i).getShouldSelectedDays() + " Hari");
            }
        }

        this.newCount = 1;

        view.initViews();
        view.setSubcriptionTime(subcriptionTimeList, 0);
        view.setValueAmountBox(newCount);
        computeTotalPrice();
    }

    @Override
    public void onChangeCounter(int newCount) {
        this.newCount = newCount;
        view.setValueAmountBox(newCount);
        computeTotalPrice();
    }

    @Override
    public void onClickNextButton() {
        if (validateCurrentState()) {
            onMoveFoward();
        }
    }

    @Override
    public void onChangeSubcriptionTime(int index, boolean isSetManually) {
        setSubcriptionTime(subcriptionTimeList.get(index), isSetManually);
    }

    private void setSubcriptionTime(SubcriptionTime subcriptionTime, boolean isSetManually) {
        this.subcriptionTime = subcriptionTime;
        if (!isSetManually) {
            this.numberOfDays = subcriptionTime.getShouldSelectedDays();

            view.clearSelectionCalendar();

            LocalDate tomorrow = LocalDate.now().plusDays(1);

            view.showTomorrowMonth(CalendarDay.from(tomorrow));
            selectDateWithoutHoliday(numberOfDays, tomorrow);

            if (!subcriptionTime.getParam().equals(SubcriptionTime.CUSTOM)) {
                view.setRadioCustomSubcriptionTime(customSubcriptionTime.getPid(),
                        customSubcriptionTime.getDaySubcriptionDisplay(), customSubcriptionTime.getDescSubcriptionDisplay());
            } else {
                view.setRadioCustomSubcriptionTime(customSubcriptionTime.getPid(),
                        numberOfDays, subcriptionTime.getDescSubcriptionDisplay());
            }
        }
        view.setValuePricePerDay(pricePrefix(subcriptionTime.getPricePerDay()));
        view.setValueSubcriptionDays(numberOfDays);
        setFirstSelectedDate();
        computeTotalPrice();
    }

    @Override
    public boolean saveDate(CalendarDay calendarDay, boolean isChecked) {
        if (isChecked) {
            this.numberOfDays++;
        } else {
            this.numberOfDays--;
            selectDay(calendarDay);
        }
        checkIfNumberSelectedDaysOnList();

        checkIfNotSelectionAnything();
        view.setValueSubcriptionDays(numberOfDays);
        computeTotalPrice();
        return true;
    }

    @Override
    public void setFirstSelectedDate() {
        if (getFirstSelectedDate() != null) {
            String firstSelectedDate;
            try {
                Date date = java.sql.Date.valueOf(getFirstSelectedDate().toString());
                firstSelectedDate = simpleDateFormat.format(date);
                view.setValueStartSubcriptionDate(firstSelectedDate);
                view.setValueStartSubcriptionDateVisibility(true);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public LocalDate getFirstSelectedDate() {
        if (view.getSelectedCalendar() != null && view.getSelectedCalendar().size() > 0) {
            return sortedCalendarList().get(0);
        }
        return null;
    }

    @Override
    public void onMoveFoward() {
        switch (currentStep) {
            case SubcriptionsContract.STEP_START_SUBCRIPTION:
                currentStep = SubcriptionsContract.STEP_DELIVERY_SUBCRIPTION;
                view.showStep(SubcriptionsContract.STEP_DELIVERY_SUBCRIPTION, true);
                break;
            case SubcriptionsContract.STEP_DELIVERY_SUBCRIPTION:
                currentStep = SubcriptionsContract.STEP_PAYMENT_SUBCRIPTION;
                view.showStep(SubcriptionsContract.STEP_PAYMENT_SUBCRIPTION, true);
                break;
            case SubcriptionsContract.STEP_PAYMENT_SUBCRIPTION:
                break;
        }
    }

    @Override
    public void onMoveBackward() {
        switch (currentStep) {
            case SubcriptionsContract.STEP_START_SUBCRIPTION:
                view.exitScreen();
                break;
            case SubcriptionsContract.STEP_DELIVERY_SUBCRIPTION:
                currentStep = SubcriptionsContract.STEP_START_SUBCRIPTION;
                view.showStep(SubcriptionsContract.STEP_START_SUBCRIPTION, false);
                break;
            case SubcriptionsContract.STEP_PAYMENT_SUBCRIPTION:
                currentStep = SubcriptionsContract.STEP_DELIVERY_SUBCRIPTION;
                view.showStep(SubcriptionsContract.STEP_DELIVERY_SUBCRIPTION, false);
                break;
        }
    }

    private boolean validateCurrentState() {
        switch (currentStep) {
            case SubcriptionsContract.STEP_START_SUBCRIPTION:
                return validateStartSubcription();
            case SubcriptionsContract.STEP_DELIVERY_SUBCRIPTION:
                return true;
            case SubcriptionsContract.STEP_PAYMENT_SUBCRIPTION:
                return true;
            default: return false;
        }
    }

    private boolean validateStartSubcription() {
        if (subcriptionTime.getParam().equals(SubcriptionTime.CUSTOM)
                && numberOfDays < subcriptionTime.getShouldSelectedDays()) {
            view.snackbarErrorShouldSelectMoreDates(subcriptionTime.getShouldSelectedDays() - numberOfDays);
            return false;
        }
        return true;
    }

    private boolean checkIfNumberSelectedDaysOnList() {
        if (!subcriptionTime.getParam().equals(SubcriptionTime.CUSTOM)) {
            // Dari bukan custom radio button
            int size = subcriptionTimeList.size();
            for (int i = 0; i < size; i++) {
                if (subcriptionTimeList.get(i).getShouldSelectedDays() == numberOfDays) {
                    // Harus pindah ke custom radio button
                    setProperPricePerDayByNumberOfDaysThatPicked();
                    view.setCheckRadioSubcriptionTime(i);
                    return true;
                }
            }
        } else {
            // Dari custom radio button
            int size = subcriptionTimeList.size();
            for (int i = 0; i < size; i++) {
                if (subcriptionTimeList.get(i).getShouldSelectedDays() == numberOfDays) {
                    // Harus pindah ke proper radio button
                    if (subcriptionTimeList.get(i).getParam().equals(SubcriptionTime.CUSTOM)) {
                        view.setRadioCustomSubcriptionTime(customSubcriptionTime.getPid(),
                                numberOfDays, subcriptionTime.getDescSubcriptionDisplay());
                    } else {
                        view.setRadioCustomSubcriptionTime(customSubcriptionTime.getPid(),
                                customSubcriptionTime.getDaySubcriptionDisplay(), customSubcriptionTime.getDescSubcriptionDisplay());
                    }
                    view.setCheckRadioSubcriptionTime(i);
                    return true;
                }
            }
        }
        setToCustomSubcriptionTime();
        return false;
    }

    private void setToCustomSubcriptionTime() {
        view.setCheckRadioSubcriptionTime(customSubcriptionTime.getPid());
        setProperPricePerDayByNumberOfDaysThatPicked();
    }

    private void selectDateWithoutHoliday(int numberOfDays, LocalDate tomorrow) {
        for (int i = 0; i < numberOfDays;) {
            final CalendarDay current = CalendarDay.from(tomorrow);
            DayOfWeek dayOfWeek = current.getDate().getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SUNDAY && dayOfWeek != DayOfWeek.SATURDAY) {
                view.setSelectionDate(current);
                i++;
            }
            tomorrow = tomorrow.plusDays(1);
        }
    }

    private void selectDay(CalendarDay deletedCalendar) {
        if (!subcriptionTime.getParam().equals(SubcriptionTime.CUSTOM)
                || numberOfDays <= customSubcriptionTime.getShouldSelectedDays()) {

            LocalDate localDate = sortedCalendarList().get(sortedCalendarList().size()-1);

            LocalDate selectDate = deletedCalendar.getDate().isAfter(localDate)
                    ? deletedCalendar.getDate().plusDays(1) : localDate.plusDays(1);

            selectDateWithoutHoliday(1, selectDate);

            numberOfDays++;

            view.setValuePricePerDay(pricePrefix(subcriptionTime.getPricePerDay()));
            view.setValueSubcriptionDays(numberOfDays);
            setFirstSelectedDate();
            computeTotalPrice();
        }
    }

    private void setProperPricePerDayByNumberOfDaysThatPicked() {
        if (subcriptionTime.getParam().equals(SubcriptionTime.CUSTOM)) {
            double pricePerDayForMaxPickedDay = customSubcriptionTime.getPricePerDay();
            String descSubcriptionDisplay = subcriptionTime.getDescSubcriptionDisplay();
            for (SubcriptionTime subcriptionTime : subcriptionTimeList) {
                if (!subcriptionTime.getMaxNumberOfDays().equals(SubcriptionTime.UNDEFINED)) {
                    if (subcriptionTime.getShouldSelectedDays() <= numberOfDays
                            && Integer.parseInt(subcriptionTime.getMaxNumberOfDays()) >= numberOfDays) {
                        view.setRadioCustomSubcriptionTime(customSubcriptionTime.getPid(),
                                numberOfDays, subcriptionTime.getDescSubcriptionDisplay());
                        this.subcriptionTime.setPricePerDay(subcriptionTime.getPricePerDay());
                        view.setValuePricePerDay(pricePrefix(this.subcriptionTime.getPricePerDay()));
                        return;
                    }
                } else {
                    pricePerDayForMaxPickedDay = subcriptionTime.getPricePerDay();
                    descSubcriptionDisplay = subcriptionTime.getDescSubcriptionDisplay();
                }
            }
            this.subcriptionTime.setPricePerDay(pricePerDayForMaxPickedDay);
            view.setRadioCustomSubcriptionTime(customSubcriptionTime.getPid(),
                    numberOfDays, descSubcriptionDisplay);
            view.setValuePricePerDay(pricePrefix(subcriptionTime.getPricePerDay()));
            return;
        }
        return;
    }

    private void checkIfNotSelectionAnything() {
        if (view.getSelectedCalendar()!= null && view.getSelectedCalendar().size() == 0) {
            view.setValueStartSubcriptionDateVisibility(false);
        } else {
            setFirstSelectedDate();
        }
    }

    private List<LocalDate> sortedCalendarList() {
        List<CalendarDay> calendarDayList = new ArrayList<>(view.getSelectedCalendar());
        List<LocalDate> sortedCalendar = new ArrayList<>();
        for (CalendarDay calendarDay : calendarDayList) {
            sortedCalendar.add(calendarDay.getDate());
        }
        Collections.sort(sortedCalendar);
        return sortedCalendar;
    }

    private void computeTotalPrice() {
        view.setTotalPrice(pricePrefix((subcriptionTime.getPricePerDay() * newCount) * numberOfDays));
    }

    private String pricePrefix(double price) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        format.setMaximumFractionDigits(0);
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) format).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("Rp ");
        ((DecimalFormat) format).setDecimalFormatSymbols(decimalFormatSymbols);

        return format.format(price);
    }
}
