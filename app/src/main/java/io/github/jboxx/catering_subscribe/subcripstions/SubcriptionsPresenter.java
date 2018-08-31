package io.github.jboxx.catering_subscribe.subcripstions;

import android.support.annotation.Nullable;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.jboxx.catering_subscribe.pojo.SubcriptionTime;

public class SubcriptionsPresenter implements SubcriptionsContract.Presenter {

    private final String TAG = SubcriptionsPresenter.class.getSimpleName();

    private int currentStep = SubcriptionsContract.STEP_START_SUBCRIPTION;

    private SubcriptionsContract.View view;

    private Date today;
    private Locale locale;
    private SimpleDateFormat simpleDateFormat;
    private List<CalendarDay> savedDates;
    private List<SubcriptionTime> subcriptionTimeList;
    private double pricePerDay;
    private int newCount;
    private int numberOfDays;
    private boolean isCanAddMoreDays;
    private int shouldSelectDates;

    public SubcriptionsPresenter() {
        this.locale = new Locale("id");
        this.simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);
        this.today = new Date();
        this.savedDates = new ArrayList<>();
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
                "UNDEFINED",22500, false);
        subcriptionTimeList.add(subcriptionTime1);

        SubcriptionTime subcriptionTime2
                = new SubcriptionTime("10_DAYS",
                "10 Hari", "Rp 24.250/hari", 10,
                "19", 24250,false);
        subcriptionTimeList.add(subcriptionTime2);

        SubcriptionTime subcriptionTime3
                = new SubcriptionTime("5_DAYS",
                "5 Hari", "Rp 25.000/hari", 5,
                "9", 25000,false);
        subcriptionTimeList.add(subcriptionTime3);

        SubcriptionTime subcriptionTime4
                = new SubcriptionTime("CUSTOM",
                "Pilih Sendiri", "Min. 2 hari", 2,
                "4", 25000, true);
        subcriptionTimeList.add(subcriptionTime4);

        newCount = 1;
        pricePerDay = subcriptionTime1.getPricePerDay();
        numberOfDays = subcriptionTime1.getNumberOfDays();
        isCanAddMoreDays = subcriptionTime1.isAddMoreDays();
        shouldSelectDates = subcriptionTime1.getNumberOfDays();

        view.initViews();
        view.setSubcriptionTime(subcriptionTimeList);
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
    public void onClickSubcriptionTime(SubcriptionTime subcriptionTime) {
        this.isCanAddMoreDays = subcriptionTime.isAddMoreDays();
        this.numberOfDays = subcriptionTime.getNumberOfDays();
        this.shouldSelectDates = subcriptionTime.getNumberOfDays();
        this.pricePerDay = subcriptionTime.getPricePerDay();

        view.clearSelectionCalendar();
        savedDates.clear();

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTime(today);
        tomorrow.add(Calendar.DATE, 1);

        view.showTomorrowMonth(CalendarDay.from(tomorrow));

        for (int i = 0; i < shouldSelectDates;) {
            final CalendarDay current = CalendarDay.from(tomorrow);
            int dayOfWeek = current.getCalendar().get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SUNDAY && dayOfWeek != Calendar.SATURDAY) {
                view.setSelectionDate(current);
                savedDates.add(current);
                i++;
            }
            tomorrow.add(Calendar.DATE, 1);
        }

        if (!isCanAddMoreDays && shouldSelectDates == numberOfDays) {
            view.setDisabilibiltyCalendar(true);
        } else {
            view.setDisabilibiltyCalendar(false);
        }

        view.setValuePricePerDay(pricePrefix(pricePerDay));
        view.setValueSubcriptionDays(numberOfDays);
        setFirstSelectedDate();
        computeTotalPrice();
    }

    @Override
    public boolean saveDate(CalendarDay calendarDay, boolean isChecked) {
        if (isChecked) {
            savedDates.add(calendarDay);
            Collections.sort(savedDates, (o1, o2) -> {
                Calendar left = o1.getCalendar();
                Calendar right = o2.getCalendar();
                return left.compareTo(right);
            });
            setFirstSelectedDate();
            this.numberOfDays++;
            if (!isCanAddMoreDays && shouldSelectDates == numberOfDays) {
                view.setDisabilibiltyCalendar(true);
            }
        } else {
            savedDates.remove(calendarDay);
            checkIfNotSelectionAnything();
            this.numberOfDays--;
            view.setDisabilibiltyCalendar(false);
        }
        checkProperPricePerDayByNumberOfDaysThatPicked();
        view.setValueSubcriptionDays(numberOfDays);
        computeTotalPrice();
        return true;
    }

    private boolean checkProperPricePerDayByNumberOfDaysThatPicked() {
        if (isCanAddMoreDays) {
            double pricePerDayForMaxPickedDay = 0d;
            for (SubcriptionTime subcriptionTime : subcriptionTimeList) {
                if (!subcriptionTime.getMaxNumberOfDays().equals(SubcriptionTime.UNDEFINED)) {
                    if (subcriptionTime.getNumberOfDays() <= numberOfDays
                            && Integer.parseInt(subcriptionTime.getMaxNumberOfDays()) >= numberOfDays) {
                        this.pricePerDay = subcriptionTime.getPricePerDay();
                        view.setValuePricePerDay(pricePrefix(pricePerDay));
                        return true;
                    }
                } else {
                    pricePerDayForMaxPickedDay = subcriptionTime.getPricePerDay();
                }
            }
            this.pricePerDay = pricePerDayForMaxPickedDay;
            view.setValuePricePerDay(pricePrefix(pricePerDay));
            return true;
        }
        return false;
    }

    private void checkIfNotSelectionAnything() {
        if (savedDates!= null && savedDates.size() == 0) {
            view.setValueStartSubcriptionDateVisibility(false);
        } else {
            setFirstSelectedDate();
        }
    }

    @Override
    public void setFirstSelectedDate() {
        if (getFirstSelectedDate() != null) {
            Date date = getFirstSelectedDate().getTime();
            String firstSelectedDate;
            try {
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
    public Calendar getFirstSelectedDate() {
        if (savedDates != null && savedDates.size() > 0) {
            return savedDates.get(0).getCalendar();
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
        if (numberOfDays < shouldSelectDates) {
            view.snackbarErrorShouldSelectMoreDates(shouldSelectDates - numberOfDays);
            return false;
        }
        return true;
    }

    private void computeTotalPrice() {
        view.setTotalPrice(pricePrefix((pricePerDay * newCount) * numberOfDays));
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
