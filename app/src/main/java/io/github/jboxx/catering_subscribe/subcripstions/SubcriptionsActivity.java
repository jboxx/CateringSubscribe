package io.github.jboxx.catering_subscribe.subcripstions;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.jboxx.catering_subscribe.BaseApplication;
import io.github.jboxx.catering_subscribe.R;
import io.github.jboxx.catering_subscribe.customviews.CircleView;
import io.github.jboxx.catering_subscribe.customviews.CounterView;
import io.github.jboxx.catering_subscribe.customviews.CustomRadioGroup;
import io.github.jboxx.catering_subscribe.customviews.calendardecorator.DisableSaturdayAndMonday;
import io.github.jboxx.catering_subscribe.customviews.calendardecorator.SelectionSquareDecorator;
import io.github.jboxx.catering_subscribe.pojo.SubcriptionTime;
import io.github.jboxx.catering_subscribe.utils.UiUtils;

public class SubcriptionsActivity extends AppCompatActivity implements SubcriptionsContract.View,
        OnDateSelectedListener {

    private final String TAG = SubcriptionsActivity.class.getSimpleName();

    public static void launch(Context context) {
        Log.d(SubcriptionsActivity.class.getSimpleName(), "launch SubcriptionsActivity");
        Intent intent = new Intent(context, SubcriptionsActivity.class);
        context.startActivity(intent);
    }

    @Inject
    UiUtils uiUtils;
    @Inject
    Resources resources;
    @Inject
    SubcriptionsContract.Presenter subcriptionsPresenter;
    @Inject
    CustomRadioGroup customRadioGroup;
    @Inject
    SelectionSquareDecorator selectionSquareDecorator;
    @Inject
    DisableSaturdayAndMonday disableSaturdayAndMonday;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rvOne)
    CircleView rvOne;
    @BindView(R.id.txtStep1Label)
    TextView txtStep1Label;
    @BindView(R.id.rvTwo)
    CircleView rvTwo;
    @BindView(R.id.txtStep2Label)
    TextView txtStep2Label;
    @BindView(R.id.rvThree)
    CircleView rvThree;
    @BindView(R.id.txtStep3Label)
    TextView txtStep3Label;

    @BindView(R.id.viewFlipper)
    ViewFlipper viewFlipper;

    @BindView(R.id.counterView)
    CounterView counterView;
    @BindView(R.id.containerRadioSubcriptionTime)
    GridLayout containerRadioSubcriptionTime;
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;

    @BindView(R.id.valuePricePerDay)
    TextView valuePricePerDay;
    @BindView(R.id.valueAmountBox)
    TextView valueAmountBox;
    @BindView(R.id.valueSubcriptionDays)
    TextView valueSubcriptionDays;
    @BindView(R.id.valueStartSubcriptionDate)
    TextView valueStartSubcriptionDate;
    @BindView(R.id.valueTotal)
    TextView valueTotal;

    public SubcriptionsActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_subcriptions);
        ButterKnife.bind(this);
        ((BaseApplication) getApplication()).createSubcriptionsComponent().inject(this);

        subcriptionsPresenter.setView(this);
        subcriptionsPresenter.onBegin();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                subcriptionsPresenter.onMoveBackward();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        subcriptionsPresenter.onMoveBackward();
    }

    @Override
    public void initViews() {
        uiUtils.setupToolbar(R.string.start_subscriptions, R.id.toolbar, this);
        ViewCompat.setElevation(appbar, resources.getDimensionPixelSize(R.dimen.elevation_app_bar));

        rvTwo.setCircleActive(false);
        rvThree.setCircleActive(false);

        counterView.setListener(newCount -> subcriptionsPresenter.onChangeCounter(newCount));

        calendarView.setOnDateChangedListener(this);
        calendarView.addDecorators(
                selectionSquareDecorator,
                disableSaturdayAndMonday
        );
        Calendar minimumCalendar = Calendar.getInstance();
        minimumCalendar.set(minimumCalendar.get(Calendar.YEAR),
                minimumCalendar.get(Calendar.MONTH),
                minimumCalendar.get(Calendar.DATE) + 1);
        Calendar maxmimumCalendar = Calendar.getInstance();
        maxmimumCalendar.set(maxmimumCalendar.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        calendarView.state().edit()
                .setMinimumDate(minimumCalendar)
                .setMaximumDate(maxmimumCalendar)
                .commit();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
        subcriptionsPresenter.saveDate(calendarDay, b);
        calendarView.invalidateDecorators();
    }

    @Override
    public void setSelectionDate(CalendarDay calendarDay) {
        calendarView.setDateSelected(calendarDay, true);
        calendarView.invalidateDecorators();
    }

    @Override
    public void setDisabilibiltyCalendar(boolean isDisable) {
        disableSaturdayAndMonday.addDatesNotDisable(isDisable, calendarView.getSelectedDates());
        calendarView.invalidateDecorators();
    }

    @Override
    public void clearSelectionCalendar() {
        calendarView.clearSelection();
    }

    @Override
    public void snackbarErrorShouldSelectMoreDates(int i) {
        Snackbar.make(coordinatorLayout, getString(R.string.should_select_more_dates, String.valueOf(i)), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void exitScreen() {
        finish();
    }

    @Override
    public void showTomorrowMonth(CalendarDay calendarDay) {
        calendarView.setCurrentDate(calendarDay, true);
    }

    @Override
    public void showStep(int step, boolean isAnimForwards) {
        if (isAnimForwards) {
            viewFlipper.setInAnimation(this, R.anim.slide_in_right);
            viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
        } else {
            viewFlipper.setInAnimation(this, R.anim.slide_in_left);
            viewFlipper.setOutAnimation(this, R.anim.slide_out_right);
        }

        viewFlipper.setDisplayedChild(step);

        switch (step){
            case SubcriptionsContract.STEP_START_SUBCRIPTION:
                rvTwo.setCircleActive(false);
                txtStep2Label.setTextColor(ResourcesCompat.getColor(resources, R.color.grey_300, getTheme()));
                break;
            case SubcriptionsContract.STEP_DELIVERY_SUBCRIPTION:
                rvTwo.setCircleActive(true);
                rvThree.setCircleActive(false);
                txtStep2Label.setTextColor(ResourcesCompat.getColor(resources, R.color.default_text_color, getTheme()));
                txtStep3Label.setTextColor(ResourcesCompat.getColor(resources, R.color.grey_300, getTheme()));
                break;
            case SubcriptionsContract.STEP_PAYMENT_SUBCRIPTION:
                rvThree.setCircleActive(true);
                txtStep3Label.setTextColor(ResourcesCompat.getColor(resources, R.color.default_text_color, getTheme()));

                break;
        }
    }

    @Override
    public void setSubcriptionTime(List<SubcriptionTime> subcriptionTimeList) {
        if (subcriptionTimeList.size() > 0) {
            containerRadioSubcriptionTime.removeAllViews();

            for (int x = 0; x < subcriptionTimeList.size(); x++) {
                LinearLayout view = (LinearLayout) LayoutInflater.from(this)
                        .inflate(R.layout.view_radio_subcription_time, null, false);

                RadioButton lblRadioButton = view.findViewById(R.id.lblRadioButton);
                TextView descRadioButton = view.findViewById(R.id.descRadioButton);

                lblRadioButton.setTag(subcriptionTimeList.get(x));
                lblRadioButton.setText(subcriptionTimeList.get(x).getDaySubcriptionDisplay());
                descRadioButton.setText(subcriptionTimeList.get(x).getDescSubcriptionDisplay());

                view.setId(uiUtils.generateViewId());
                view.setTag(subcriptionTimeList.get(x));
                GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(
                        GridLayout.UNDEFINED,GridLayout.FILL,1f),
                        GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));
                containerRadioSubcriptionTime.addView(view, param);
            }

            customRadioGroup.setView(containerRadioSubcriptionTime);
            customRadioGroup.setOnButtonCheckedListener(button -> {
                subcriptionsPresenter.onClickSubcriptionTime((SubcriptionTime) button.getTag());
                highlightSelectedSubcriptionTime(button);
            });
            customRadioGroup.setChecked(0);
        }
    }

    @Override
    public void setValuePricePerDay(String value) {
        valuePricePerDay.setText(value);
    }

    @Override
    public void setValueStartSubcriptionDate(String value) {
        valueStartSubcriptionDate.setText(getString(R.string.start_subcription_date_tag, value));
    }

    @Override
    public void setValueStartSubcriptionDateVisibility(boolean isVisible) {
        valueStartSubcriptionDate.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setValueSubcriptionDays(int numberOfDays) {
        valueSubcriptionDays.setText(getString(R.string.tag_days, String.valueOf(numberOfDays)));
    }

    @Override
    public void setValueAmountBox(int newCount) {
        valueAmountBox.setText(getString(R.string.tag_box, String.valueOf(newCount)));
    }

    @Override
    public void setTotalPrice(String totalPrice) {
        valueTotal.setText(totalPrice);
    }

    @OnClick(R.id.btnNext)
    void onClickNextButton() {
        subcriptionsPresenter.onClickNextButton();
    }

    private void highlightSelectedSubcriptionTime(CompoundButton button) {
        int rows = containerRadioSubcriptionTime.getChildCount();
        for (int x = 0; x < rows; x++) {
            LinearLayout containerRadioButton = (LinearLayout) containerRadioSubcriptionTime.getChildAt(x);
            if (containerRadioButton.getTag().equals(button.getTag())) {
                containerRadioButton.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_radiobutton));
                TextView lblRadioButton =
                        containerRadioSubcriptionTime.getChildAt(x).findViewById(R.id.lblRadioButton);
                lblRadioButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                TextView descRadioButton =
                        containerRadioSubcriptionTime.getChildAt(x).findViewById(R.id.descRadioButton);
                descRadioButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            } else {
                containerRadioButton.setBackground(ContextCompat.getDrawable(this, R.drawable.regular_state_radiobutton));
                TextView lblRadioButton =
                        containerRadioSubcriptionTime.getChildAt(x).findViewById(R.id.lblRadioButton);
                lblRadioButton.setTextColor(ContextCompat.getColor(this, R.color.default_text_color));
                TextView descRadioButton =
                        containerRadioSubcriptionTime.getChildAt(x).findViewById(R.id.descRadioButton);
                descRadioButton.setTextColor(ContextCompat.getColor(this, R.color.default_text_color));
            }
        }
    }
}
