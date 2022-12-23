package com.fourshape.numbermatch.ui_views;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.numbermatch.utils.SharedData;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ViewCelebration {

    public static void start (KonfettiView konfettiView) {

        AppColor appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(konfettiView.getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        try {

            konfettiView.build()
                    .addColors(
                            konfettiView.getContext().getColor(appColor.getCelebrationBlueColor()),
                            konfettiView.getContext().getColor(appColor.getCelebrationYellowColor()),
                            konfettiView.getContext().getColor(appColor.getCelebrationPinkColor()),
                            konfettiView.getContext().getColor(appColor.getCelebrationGreenColor()),
                            konfettiView.getContext().getColor(appColor.getCelebrationViolentColor())
                    )
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 2.0f))
                    .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                    .burst(650);

        } catch (Exception e) {
            MakeLog.exception(e);
        }


    }

}
