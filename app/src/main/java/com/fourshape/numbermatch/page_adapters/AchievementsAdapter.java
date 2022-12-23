package com.fourshape.numbermatch.page_adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fourshape.numbermatch.fragments.achievements.EasyLevelAchievementsFragment;
import com.fourshape.numbermatch.fragments.achievements.HardLevelAchievementsFragment;
import com.fourshape.numbermatch.fragments.achievements.MediumLevelAchievementsFragment;

import org.jetbrains.annotations.NotNull;

public class AchievementsAdapter extends FragmentStateAdapter  {

    private int totalTabs;

    public AchievementsAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle, int totalTabs) {
        super(fragmentManager, lifecycle);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new EasyLevelAchievementsFragment();

            case 1:
                return new MediumLevelAchievementsFragment();

            case 2:
                return new HardLevelAchievementsFragment();

        }

        return new EasyLevelAchievementsFragment();

    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
