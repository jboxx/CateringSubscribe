package io.github.jboxx.catering_subscribe;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.jboxx.catering_subscribe.subcripstions.SubcriptionsActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setTitle(getString(R.string.home));
        setSupportActionBar(toolbar);
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.elevation_app_bar));
    }

    @OnClick(R.id.btnStartSubcriptions)
    void onClickStartSubcriptions() {
        SubcriptionsActivity.launch(this);
    }
}
