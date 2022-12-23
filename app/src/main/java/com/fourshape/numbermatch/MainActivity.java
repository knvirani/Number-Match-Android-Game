package com.fourshape.numbermatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fourshape.easythingslib.fragments.GujMCQAppsListFragment;
import com.fourshape.easythingslib.listeners.OnThemeChangeListener;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.MakeLog;
import com.fourshape.easythingslib.utils.ShareAppLink;
import com.fourshape.numbermatch.analytics.TrackScreen;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.fragments.CurrentFragment;
import com.fourshape.numbermatch.fragments.DailyFragment;
import com.fourshape.numbermatch.fragments.FragmentTitle;
import com.fourshape.numbermatch.fragments.HomeFragment;
import com.fourshape.numbermatch.listeners.OnFragmentChangeListener;
import com.fourshape.numbermatch.ui_popups.PopupSound;
import com.fourshape.numbermatch.ui_popups.PopupTheme;
import com.fourshape.numbermatch.ui_popups.PopupThemeControl;
import com.fourshape.numbermatch.utils.PlaySound;
import com.fourshape.numbermatch.utils.ScreenConfiguration;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ConstraintLayout parent;
    private LinearLayoutCompat appBarView;
    private MaterialCardView shareCV, statsCV, soundCV, themeCV, optionsCV, howtoplayCV, achievementsCV;
    private ImageView shareIV, statsIV, soundIV, themeIV, optionsIV, howtoplayIV, achievementsIV;

    private BottomNavigationView bottomNavigationView;
    private int fragmentContainerId;
    private View bottomNavigationDividerView;

    private AppColor appColor;

    private AudioAttributes audioAttributes;
    private SoundPool soundPool;
    private int[] soundId = new int[4];

    private HomeFragment homeFragment;
    private DailyFragment dailyFragment;
    private GujMCQAppsListFragment gujMCQAppsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        ScreenConfiguration.set(this, this);

        setContentView(R.layout.activity_main);

        TrackScreen.now(this);

        parent = findViewById(R.id.parent);
        appBarView = findViewById(R.id.app_bar_layout);

        shareCV = findViewById(R.id.share_app_link_cv);
        statsCV = findViewById(R.id.stats_cv);
        soundCV = findViewById(R.id.sound_control_cv);
        themeCV = findViewById(R.id.palette_cv);
        optionsCV = findViewById(R.id.options_cv);
        howtoplayCV = findViewById(R.id.howtoplay_cv);
        achievementsCV = findViewById(R.id.achievements_cv);

        shareIV = findViewById(R.id.share_app_link_iv);
        statsIV = findViewById(R.id.stats_iv);
        soundIV = findViewById(R.id.sound_control_iv);
        themeIV = findViewById(R.id.palette_iv);
        optionsIV = findViewById(R.id.options_iv);
        howtoplayIV = findViewById(R.id.howtoplay_iv);
        achievementsIV = findViewById(R.id.achievements_iv);

        appColor = new AppColor();

        bottomNavigationDividerView = findViewById(R.id.bottom_nav_divider_view);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        //bottomNavigationView.setVisibility(View.GONE);
        //appBarView.setVisibility(View.GONE);

        fragmentContainerId = findViewById(R.id.fragment_container).getId();

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(8)
                .setAudioAttributes(audioAttributes)
                .build();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        soundId[0] = soundPool.load(this, R.raw.ui_click, 1);

        refreshViewsColor();
        setViewsClickListener();

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshViewsColor();

        if (!new CommonSharedData(this, SharedData.SHARED_PREF_TITLE).getPolicyStatus()) {
            startActivity(new Intent(this, PolicyActivity.class));
        }

    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(fragmentContainerId);

        if (fragment != null) {

            if (!(fragment instanceof HomeFragment)) {
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
            } else {
                super.onBackPressed();
            }

        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (soundPool != null)
            soundPool.release();
    }

    private void setViewsClickListener () {

        achievementsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound.now(soundPool, soundId[0]);
                view.getContext().startActivity(new Intent(view.getContext(), AchievementsActivity.class));
            }
        });

        shareCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound.now(soundPool, soundId[0]);
                ShareAppLink.share(view);
            }
        });

        soundCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound.now(soundPool, soundId[0]);
                new PopupSound(view.getContext()).show(view);
            }
        });

        themeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound.now(soundPool, soundId[0]);
                new PopupThemeControl(getWeakContext(view.getContext())).setOnThemeChangeListener(onThemeChangeListener).setThemeOnly().show(view);
            }
        });

        optionsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound.now(soundPool, soundId[0]);
                view.getContext().startActivity(new Intent(view.getContext(), OptionsActivity.class));
            }
        });

        statsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound.now(soundPool, soundId[0]);
                view.getContext().startActivity(new Intent(view.getContext(), StatisticsActivity.class));
            }
        });

        howtoplayCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound.now(soundPool, soundId[0]);
                view.getContext().startActivity(new Intent(view.getContext(), HowToPlayActivity.class));
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {

                    if (homeFragment == null)
                        homeFragment = new HomeFragment().setOnFragmentChangeListener(onFragmentChangeListener);

                    CurrentFragment.FRAGMENT = FragmentTitle.HOME_FRAGMENT;
                    getSupportFragmentManager().beginTransaction().replace(fragmentContainerId, homeFragment).commit();

                    return true;

                } else if (itemId == R.id.nav_daily) {

                    if (dailyFragment == null)
                        dailyFragment = new DailyFragment();

                    CurrentFragment.FRAGMENT = FragmentTitle.DAILY_FRAGMENT;
                    getSupportFragmentManager().beginTransaction().replace(fragmentContainerId, dailyFragment).commit();

                    return true;

                } else if (itemId == R.id.nav_our_apps) {

                    if (gujMCQAppsListFragment == null)
                        gujMCQAppsListFragment = new GujMCQAppsListFragment();

                    CurrentFragment.FRAGMENT = FragmentTitle.OUR_APPS_FRAGMENT;
                    getSupportFragmentManager().beginTransaction().replace(fragmentContainerId, gujMCQAppsListFragment).commit();
                    MakeLog.info(TAG, "OurAppsFragment");

                    return true;

                } else {
                    return false;
                }

            }
        });


    }

    private Context getWeakContext (Context context) {
        return new WeakReference<Context>(context).get();
    }

    private void refreshViewsColor () {

        appColor.setTheme(new CommonSharedData(this, SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        if (parent != null) {
            parent.setBackgroundColor(this.getColor(this.appColor.getAppBackgroundColor()));
        }

        if (appBarView != null) {
            appBarView.setBackgroundColor(getColor(appColor.getAppBarBackgroundColor()));
        }

        if (shareCV != null) {
            shareCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }
        if (soundCV != null) {
            soundCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }
        if (themeCV != null) {
            themeCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }
        if (optionsCV != null) {
            optionsCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }
        if (statsCV != null) {
            statsCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }
        if (howtoplayCV != null) {
            howtoplayCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }
        if (achievementsCV != null) {
            achievementsCV.setCardBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        }

        if (shareIV != null) {
            shareIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }
        if (soundIV != null) {
            soundIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }
        if (themeIV != null) {
            themeIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }
        if (optionsIV != null) {
            optionsIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }
        if (statsIV != null) {
            statsIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }
        if (howtoplayIV != null) {
            howtoplayIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }
        if (achievementsIV != null) {
            achievementsIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBarIconTintColor())));
        }

        if (bottomNavigationDividerView != null) {
            bottomNavigationDividerView.setBackgroundColor(this.getColor(appColor.getBottomNavigationBarDividerBackgroundColor()));
        }

        if (bottomNavigationView != null) {

            int[][] states = new int[][] {
                    new int[] {android.R.attr.state_checked},
                    new int[] {}
            };

            int[] colors = new int[] {
                    this.getColor(appColor.getBottomNavigationItemActiveTintColor()),
                    this.getColor(appColor.getAppBarIconTintColor())
            };

            bottomNavigationView.setBackgroundTintList(ColorStateList.valueOf(this.getColor(appColor.getAppBackgroundColor())));
            bottomNavigationView.setItemTextColor(new ColorStateList(states, colors));
            bottomNavigationView.setItemIconTintList(new ColorStateList(states, colors));

        }

        if (CurrentFragment.FRAGMENT != null) {
            switch (CurrentFragment.FRAGMENT) {
                case FragmentTitle.DAILY_FRAGMENT:
                    if (bottomNavigationView != null)
                        bottomNavigationView.setSelectedItemId(R.id.nav_daily);
                    break;
                case FragmentTitle.OUR_APPS_FRAGMENT:
                    if (bottomNavigationView != null)
                        bottomNavigationView.setSelectedItemId(R.id.nav_our_apps);
                    break;
                case FragmentTitle.HOME_FRAGMENT:
                default:
                    if (bottomNavigationView != null)
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                    break;
            }
        } else {
            if (bottomNavigationView != null)
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

    }

    private final OnFragmentChangeListener onFragmentChangeListener = new OnFragmentChangeListener() {
        @Override
        public void onDailyFragmentLaunch() {
            if (bottomNavigationView != null) {
                bottomNavigationView.setSelectedItemId(R.id.nav_daily);
            }
        }
    };

    private final OnThemeChangeListener onThemeChangeListener = new OnThemeChangeListener() {
        @Override
        public void onChanged() {
            refreshViewsColor();
            if (CurrentFragment.FRAGMENT != null) {
                if (CurrentFragment.FRAGMENT.equals(FragmentTitle.DAILY_FRAGMENT)) {
                    if (dailyFragment != null) {
                        dailyFragment.refreshViewsColor();
                    }
                } else if (CurrentFragment.FRAGMENT.equals(FragmentTitle.HOME_FRAGMENT)) {
                    if (homeFragment != null) {
                        homeFragment.refreshViewsColor();
                    }
                }
            }
        }
    };

}