package com.fourshape.numbermatch.fragments.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.puzzle_core.GameLevel;
import com.fourshape.numbermatch.puzzle_core.structure.Achievement;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class MediumLevelAchievementsFragment extends Fragment {

    private View mainView;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_game_level_achievements, null);

        Achievement achievement;

        if (new SharedData(getContext()).getAchievementJson(GameLevel.MEDIUM) == null) {
            achievement = new Achievement();
        } else {
            achievement = new Achievement(new Gson().fromJson(new SharedData(getContext()).getAchievementJson(GameLevel.MEDIUM), Achievement.class));
        }

        if (achievement == null) {
            achievement = new Achievement();
        }

        achievement.setView(mainView.findViewById(R.id.dynamic_views_container));

        mainView.findViewById(R.id.dynamic_views_container).setVisibility(View.VISIBLE);

        return mainView;
    }

}
