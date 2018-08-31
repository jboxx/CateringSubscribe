package io.github.jboxx.catering_subscribe;

import android.app.Application;
import android.os.StrictMode;

import io.github.jboxx.catering_subscribe.subcripstions.di.SubcriptionsComponent;
import io.github.jboxx.catering_subscribe.subcripstions.di.SubcriptionsModule;

public class BaseApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.enableDefaults();
        appComponent = createAppComponent();
    }

    private AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public SubcriptionsComponent createSubcriptionsComponent() {
        return appComponent.plus(new SubcriptionsModule());
    }
}
