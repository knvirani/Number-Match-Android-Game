package com.fourshape.numbermatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import com.fourshape.numbermatch.daily_game_view.SessionPathView;
import com.fourshape.numbermatch.daily_game_view.structure.SessionCell;
import com.fourshape.numbermatch.listeners.OnDailyGameCellTapListener;
import com.fourshape.numbermatch.utils.MakeLog;

import java.util.List;
import java.util.Random;

import nl.dionsegijn.konfetti.Confetti;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";
    private SessionPathView sessionPathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        sessionPathView = findViewById(R.id.session_path_view);
        sessionPathView.setOnDailyGameCellTapListener(onDailyGameCellTapListener);

    }

    private final OnDailyGameCellTapListener onDailyGameCellTapListener = new OnDailyGameCellTapListener() {
        @Override
        public void onTap(SessionCell sessionCell) {
            MakeLog.info(TAG, "On Cell Tap");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sessionPathView.enableCellTap();
                    MakeLog.info(TAG, "Enable Cell Tap");
                }
            }, 1000);
        }

        @Override
        public void onEnableTap() {

        }
    };

}