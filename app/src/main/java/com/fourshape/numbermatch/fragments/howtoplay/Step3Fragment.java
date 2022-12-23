package com.fourshape.numbermatch.fragments.howtoplay;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class Step3Fragment extends Fragment {

    private static final String TAG = "Step3Fragment";

    private View mainView;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_step3, null);

        MaterialButton gotoHomeMB;
        TextView tcTV, appTitleTV;
        KonfettiView konfettiView;
        AppColor appColor;
        FrameLayout parentFL;

        if (getContext() == null)
            return mainView;

        appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());
        tcTV = mainView.findViewById(R.id.tc_complete_tv);
        appTitleTV = mainView.findViewById(R.id.app_logo_tv);
        gotoHomeMB = mainView.findViewById(R.id.goto_to_home_mb);
        konfettiView = mainView.findViewById(R.id.viewKonfetti);
        parentFL = mainView.findViewById(R.id.parent_fl);

        parentFL.setBackgroundColor(getContext().getColor(appColor.getAppBackgroundColor()));
        appTitleTV.setTextColor(getContext().getColor(appColor.getHomeAppTitleColor()));
        tcTV.setTextColor(getContext().getColor(appColor.getTutorCompletedTextColor()));
        gotoHomeMB.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(appColor.getPrimaryBtnBackgroundColor())));
        gotoHomeMB.setTextColor(getContext().getColor(appColor.getPrimaryBtnTextColor()));

        gotoHomeMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getActivity() != null)
                    getActivity().finish();

            }
        });

        konfettiView.build()
                .addColors(
                        getContext().getColor(appColor.getCelebrationBlueColor()),
                        getContext().getColor(appColor.getCelebrationYellowColor()),
                        getContext().getColor(appColor.getCelebrationPinkColor()),
                        getContext().getColor(appColor.getCelebrationGreenColor()),
                        getContext().getColor(appColor.getCelebrationViolentColor())
                )
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 2.0f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .stream(900, 5000L);

        return mainView;

    }

}
