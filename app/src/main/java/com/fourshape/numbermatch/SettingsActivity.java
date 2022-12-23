package com.fourshape.numbermatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.numbermatch.analytics.TrackScreen;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.LiveGameSettings;
import com.fourshape.numbermatch.utils.ScreenConfiguration;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private ConstraintLayout parent;
    private AppBarLayout appBarLayout;
    private MaterialToolbar materialToolbar;
    private SwitchMaterial scoreSM, timeSM, audioSM, vibrationSM, muteSM;
    private TextView scoreHelpTV, timeHelpTV;
    private MaterialDivider mDiv1;

    private AppColor appColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        ScreenConfiguration.set(this, this);
        TrackScreen.now(this);

        setContentView(R.layout.activity_settings);

        parent = findViewById(R.id.parent);
        appBarLayout = findViewById(R.id.app_bar_layout);
        materialToolbar = findViewById(R.id.material_toolbar);
        scoreSM = findViewById(R.id.show_score);
        timeSM = findViewById(R.id.show_time);
        audioSM = findViewById(R.id.game_sound);
        vibrationSM = findViewById(R.id.game_vibration);
        muteSM = findViewById(R.id.mute_ads_sound);
        mDiv1 = findViewById(R.id.m_div_1);

        scoreHelpTV = findViewById(R.id.score_switch_help_tv);
        timeHelpTV = findViewById(R.id.time_switch_help_tv);

        appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(this, SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        scoreHelpTV.setTextColor(getColor(appColor.getSwitchSuggestionTextColor()));
        timeHelpTV.setTextColor(getColor(appColor.getSwitchSuggestionTextColor()));

        parent.setBackgroundColor(getColor(this.appColor.getAppBackgroundColor()));
        appBarLayout.setBackgroundColor(getColor(this.appColor.getAppBarBackgroundColor()));
        materialToolbar.setTitleTextColor(getColor(this.appColor.getAppBarTitleTextColor()));
        materialToolbar.setNavigationIconTint(getColor(this.appColor.getAppBarIconTintColor()));
        materialToolbar.setBackgroundColor(getColor(this.appColor.getAppBarBackgroundColor()));

        int[][] states = new int[][] {
                new int[] {android.R.attr.state_checked},
                new int[] {}
        };

        int[] colors = new int[] {
                this.getColor(appColor.getSwitchOnTintColor()),
                this.getColor(appColor.getAppBarIconTintColor())
        };

        int[][] statesForTrack = new int[][] {
                new int[] {android.R.attr.state_checked},
                new int[] {}
        };

        int[] colorsForTrack = new int[] {
                this.getColor(appColor.getSwitchTrackCheckedColor()),
                this.getColor(appColor.getSwitchTrackUncheckedColor())
        };

        scoreSM.setTextColor(getColor(appColor.getSwitchTextColor()));
        timeSM.setTextColor(getColor(appColor.getSwitchTextColor()));
        audioSM.setTextColor(getColor(appColor.getSwitchTextColor()));
        vibrationSM.setTextColor(getColor(appColor.getSwitchTextColor()));
        muteSM.setTextColor(getColor(appColor.getSwitchTextColor()));

        scoreSM.setTrackTintList(new ColorStateList(statesForTrack, colorsForTrack));
        timeSM.setTrackTintList(new ColorStateList(statesForTrack, colorsForTrack));
        audioSM.setTrackTintList(new ColorStateList(statesForTrack, colorsForTrack));
        vibrationSM.setTrackTintList(new ColorStateList(statesForTrack, colorsForTrack));
        muteSM.setTrackTintList(new ColorStateList(statesForTrack, colorsForTrack));

        scoreSM.setThumbTintList(new ColorStateList(states, colors));
        timeSM.setThumbTintList(new ColorStateList(states, colors));
        audioSM.setThumbTintList(new ColorStateList(states, colors));
        vibrationSM.setThumbTintList(new ColorStateList(states, colors));
        muteSM.setThumbTintList(new ColorStateList(states, colors));

        SharedData sharedData = new SharedData(this);
        CommonSharedData commonSharedData = new CommonSharedData(this, SharedData.SHARED_PREF_TITLE);

        audioSM.setChecked(commonSharedData.getGameSoundStatus());
        scoreSM.setChecked(sharedData.getGameScoreStatus());
        timeSM.setChecked(sharedData.getGameTimeStatus());
        vibrationSM.setChecked(commonSharedData.getGameVibrationStatus());
        muteSM.setChecked(sharedData.getAdsSoundStatus());

        mDiv1.setDividerColor(getColor(appColor.getNormalDividerColor()));

        setViewsListener();

    }

    private void setViewsListener () {

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        muteSM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LiveGameSettings.ADS_SOUND_STATUS = b;
                new SharedData(compoundButton.getContext()).setAdsSoundStatus(b);
            }
        });

        scoreSM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LiveGameSettings.GAME_SCORE = b;
                new SharedData(compoundButton.getContext()).toggleGameScore(b);
            }
        });

        timeSM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LiveGameSettings.GAME_TIME = b;
                new SharedData(compoundButton.getContext()).toggleGameTime(b);
            }
        });

        audioSM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LiveGameSettings.GAME_SOUND = b;
                new CommonSharedData(compoundButton.getContext(), SharedData.SHARED_PREF_TITLE).toggleGameSound(b);
            }
        });

        vibrationSM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LiveGameSettings.GAME_VIBRATION = b;
                new CommonSharedData(compoundButton.getContext(), SharedData.SHARED_PREF_TITLE).toggleGameVibration(b);
            }
        });

    }

}