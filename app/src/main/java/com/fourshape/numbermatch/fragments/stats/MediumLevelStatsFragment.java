package com.fourshape.numbermatch.fragments.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.game_db.FetchStats;
import com.fourshape.numbermatch.listeners.OnStatsDataFetchListener;
import com.fourshape.numbermatch.puzzle_core.GameLevel;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.jetbrains.annotations.NotNull;

public class MediumLevelStatsFragment extends Fragment {

    private View mainView;
    private static final int STAT_ID = GameLevel.MEDIUM;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_game_level_stats, null);

        CircularProgressIndicator progressIndicator = mainView.findViewById(R.id.progress_circular);
        if (progressIndicator.getVisibility() != View.VISIBLE)
            progressIndicator.setVisibility(View.VISIBLE);

        if (getContext() == null)
            return mainView;

        FetchStats fetchStats = new FetchStats(getContext(), mainView.findViewById(R.id.dynamic_views_container), STAT_ID);
        fetchStats.setOnStatsDataFetchListener(new OnStatsDataFetchListener() {
            @Override
            public void onFetched() {
                progressIndicator.setVisibility(View.GONE);
                fetchStats.fitData();
                mainView.findViewById(R.id.dynamic_views_container).setVisibility(View.VISIBLE);
            }
        });

        fetchStats.fetchData();

        return mainView;
    }

}
