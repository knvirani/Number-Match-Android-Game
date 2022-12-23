package com.fourshape.numbermatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.numbermatch.analytics.TrackScreen;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.page_adapters.StatisticsAdapter;
import com.fourshape.numbermatch.utils.ScreenConfiguration;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class StatisticsActivity extends AppCompatActivity {

    private ConstraintLayout parentCL;
    private AppBarLayout appBarLayout;
    private MaterialToolbar materialToolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private AppColor appColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        ScreenConfiguration.set(this, this);
        TrackScreen.now(this);

        setContentView(R.layout.activity_statistics);

        parentCL = findViewById(R.id.parent_cl);
        appBarLayout = findViewById(R.id.app_bar_layout);
        materialToolbar = findViewById(R.id.material_toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        appColor = new AppColor();

        refreshViewColors();

        tabLayout.addTab(tabLayout.newTab().setText("Easy"));
        tabLayout.addTab(tabLayout.newTab().setText("Medium"));
        tabLayout.addTab(tabLayout.newTab().setText("Hard"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        final StatisticsAdapter statisticsAdapter = new StatisticsAdapter(this.getSupportFragmentManager(), getLifecycle(), tabLayout.getTabCount());

        viewPager.setAdapter(statisticsAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position), true);
            }
        });


    }

    private void refreshViewColors () {

        appColor.setTheme(new CommonSharedData(this, SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        parentCL.setBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        appBarLayout.setBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        materialToolbar.setBackgroundColor(this.getColor(appColor.getAppBarBackgroundColor()));
        materialToolbar.setTitleTextColor(this.getColor(appColor.getAppBarTitleTextColor()));
        materialToolbar.setNavigationIconTint(this.getColor(appColor.getAppBarIconTintColor()));

        tabLayout.setBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        tabLayout.setTabTextColors(ColorStateList.valueOf(this.getColor(this.appColor.getStatsTabTextColor())));
        tabLayout.setSelectedTabIndicatorColor(this.getColor(this.appColor.getStatsTabIndicatorColor()));

    }

}