package com.fourshape.numbermatch.page_adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fourshape.numbermatch.fragments.stats.EasyLevelStatsFragment;
import com.fourshape.numbermatch.fragments.stats.HardLevelStatsFragment;
import com.fourshape.numbermatch.fragments.stats.MediumLevelStatsFragment;

import org.jetbrains.annotations.NotNull;

public class StatisticsAdapter extends FragmentStateAdapter {

    private int totalTabs;

    public StatisticsAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle, int totalTabs) {
        super(fragmentManager, lifecycle);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new EasyLevelStatsFragment();

            case 1:
                return new MediumLevelStatsFragment();

            case 2:
                return new HardLevelStatsFragment();

        }

        return new EasyLevelStatsFragment();

    }



    @Override
    public int getItemCount() {
        return totalTabs;
    }

}
