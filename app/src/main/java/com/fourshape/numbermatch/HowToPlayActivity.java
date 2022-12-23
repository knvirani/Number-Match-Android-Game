package com.fourshape.numbermatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.numbermatch.analytics.TrackScreen;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.fragments.howtoplay.Step1Fragment;
import com.fourshape.numbermatch.utils.ScreenConfiguration;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.appbar.MaterialToolbar;

public class HowToPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ScreenConfiguration.set(this, this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_how_to_play);

        TrackScreen.now(this);

        MaterialToolbar materialToolbar = ((MaterialToolbar)findViewById(R.id.material_toolbar));

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        AppColor appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(this, SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        materialToolbar.setNavigationIconTint(this.getColor(appColor.getAppBarIconTintColor()));
        materialToolbar.setTitleTextColor(this.getColor(appColor.getAppBarTitleTextColor()));
        materialToolbar.setBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));

        int fragmentContainerId = findViewById(R.id.fragment_container).getId();

        getSupportFragmentManager().beginTransaction().replace(fragmentContainerId, new Step1Fragment()).commit();

    }
}