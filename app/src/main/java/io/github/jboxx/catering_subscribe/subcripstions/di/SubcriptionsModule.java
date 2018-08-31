package io.github.jboxx.catering_subscribe.subcripstions.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.github.jboxx.catering_subscribe.customviews.calendardecorator.DisableSaturdayAndMonday;
import io.github.jboxx.catering_subscribe.customviews.calendardecorator.SelectionSquareDecorator;
import io.github.jboxx.catering_subscribe.subcripstions.SubcriptionsContract;
import io.github.jboxx.catering_subscribe.subcripstions.SubcriptionsPresenter;
import io.github.jboxx.catering_subscribe.customviews.CustomRadioGroup;

@Module
public class SubcriptionsModule {

    @Provides
    CustomRadioGroup provideCustomRadioGroup() {
        return new CustomRadioGroup();
    }

    @Provides
    SelectionSquareDecorator provideSelectionSquareDecorator(Context context) {
        return new SelectionSquareDecorator(context);
    }

    @Provides
    DisableSaturdayAndMonday provideDisableSaturdayAndMonday() {
        return new DisableSaturdayAndMonday();
    }

    @Provides
    @SubcriptionsScope
    SubcriptionsContract.Presenter providePresenter() {
        return new SubcriptionsPresenter();
    }

}
