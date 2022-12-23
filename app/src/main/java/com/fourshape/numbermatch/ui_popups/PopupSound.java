package com.fourshape.numbermatch.ui_popups;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.DimPopupWindow;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.LiveGameSettings;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;

public class PopupSound {

    private View mainView;
    private MaterialButtonToggleGroup materialButtonToggleGroup;
    private MaterialButton soundOnMB, soundOffMB;
    private MaterialCardView parentCV;
    private PopupWindow popupWindow;

    public PopupSound (Context context) {
        mainView = LayoutInflater.from(context).inflate(R.layout.popup_sound_change, null);
        prepare();
    }

    private void prepare () {

        materialButtonToggleGroup = mainView.findViewById(R.id.material_toggle_group);
        soundOnMB = mainView.findViewById(R.id.sound_on_mb);
        soundOffMB = mainView.findViewById(R.id.sound_off_mb);
        parentCV = mainView.findViewById(R.id.parent_cv);

        if (materialButtonToggleGroup != null) {

            CommonSharedData commonSharedData = new CommonSharedData(mainView.getContext(), SharedData.SHARED_PREF_TITLE);

            boolean soundStatus = commonSharedData.getGameSoundStatus();

            if (soundStatus) {
                materialButtonToggleGroup.check(R.id.sound_on_mb);
            } else {
                materialButtonToggleGroup.check(R.id.sound_off_mb);
            }

        }

        int width = FrameLayout.LayoutParams.WRAP_CONTENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false;

        popupWindow = new PopupWindow(this.mainView, width, height, focusable);

        refreshViewsColors();
        setViewsClickListener();

    }

    public PopupSound show (View anchorView) {

        if (this.mainView == null)
            return this;

        if (this.popupWindow == null)
            return this;

        if (this.popupWindow.isShowing())
            return this;

        this.popupWindow.showAsDropDown(anchorView);
        this.popupWindow.showAtLocation(this.mainView, Gravity.NO_GRAVITY, 0, 0);
        DimPopupWindow.lightDimBehind(this.popupWindow);

        return this;

    }

    private void setViewsClickListener () {

        if (materialButtonToggleGroup != null) {

            materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                    if (isChecked) {

                        CommonSharedData commonSharedData = new CommonSharedData(mainView.getContext(), SharedData.SHARED_PREF_TITLE);
                        refreshViewsColors();
                        group.check(checkedId);

                        if (checkedId == R.id.sound_on_mb) {
                            commonSharedData.toggleGameSound(true);
                            LiveGameSettings.GAME_SOUND = true;
                        } else if (checkedId == R.id.sound_off_mb) {
                            commonSharedData.toggleGameSound(false);
                            LiveGameSettings.GAME_SOUND = false;
                        }

                    }
                }
            });

        }

    }

    private void refreshViewsColors () {

        Context context = mainView.getContext();

        if (context == null)
            return;

        AppColor appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(context, SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        int[][] states = new int[][] {
                new int[] {android.R.attr.state_checked},
                new int[] {}
        };

        int[] colorsForBg = new int[] {
                context.getColor(appColor.getButtonStateCheckedBackgroundColor()),
                context.getColor(appColor.getButtonStateDefaultBackgroundColor())
        };

        int[] colorsForText = new int[] {
                context.getColor(appColor.getButtonStateCheckedTextColor()),
                context.getColor(appColor.getButtonStateDefaultTextColor())
        };

        int[] colorsForStroke = new int[] {
                context.getColor(appColor.getButtonStateCheckedBackgroundColor()),
                context.getColor(appColor.getButtonStateDefaultTextColor())
        };

        soundOnMB.setTextColor(new ColorStateList(states, colorsForText));
        soundOffMB.setTextColor(new ColorStateList(states, colorsForText));

        soundOnMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        soundOffMB.setStrokeColor(new ColorStateList(states, colorsForStroke));

        soundOnMB.setIconTint(new ColorStateList(states, colorsForText));
        soundOffMB.setIconTint(new ColorStateList(states, colorsForText));

        soundOnMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        soundOffMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));

        parentCV.setCardBackgroundColor(context.getColor(appColor.getPopupCardBackgroundColor()));

    }


}
