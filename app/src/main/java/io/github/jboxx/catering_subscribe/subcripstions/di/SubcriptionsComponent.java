package io.github.jboxx.catering_subscribe.subcripstions.di;

import dagger.Subcomponent;
import io.github.jboxx.catering_subscribe.subcripstions.SubcriptionsActivity;

@SubcriptionsScope
@Subcomponent(modules = {SubcriptionsModule.class})
public interface SubcriptionsComponent {

    void inject(SubcriptionsActivity target);

}
