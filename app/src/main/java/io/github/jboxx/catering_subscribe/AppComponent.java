package io.github.jboxx.catering_subscribe;

import javax.inject.Singleton;

import dagger.Component;
import io.github.jboxx.catering_subscribe.subcripstions.di.SubcriptionsComponent;
import io.github.jboxx.catering_subscribe.subcripstions.di.SubcriptionsModule;

@Singleton
@Component(modules = {
        AppModule.class})
public interface AppComponent {

    SubcriptionsComponent plus(SubcriptionsModule subcriptionsModule);

}
