package com.fourshape.numbermatch.ui_popups;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.fourshape.easythingslib.listeners.OnThemeChangeListener;
import com.fourshape.easythingslib.utils.AppThemes;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.DimPopupWindow;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.listeners.OnBoardFontSizeChangeListener;
import com.fourshape.numbermatch.puzzle_core.BoardFontSize;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;

public class PopupTheme {

    private View mainView;
    private MaterialButtonToggleGroup themeGroup, textSizeGroup;
    private MaterialButton dayMB, nightMB, smallTextMB, mediumTextMB, largeTextMB;
    private MaterialCardView parentCV;
    private PopupWindow popupWindow;
    private boolean shouldDismiss = false;

    private OnThemeChangeListener onThemeChangeListener;
    private OnBoardFontSizeChangeListener onBoardFontSizeChangeListener;

    public PopupTheme (Context context) {
        mainView = LayoutInflater.from(context).inflate(R.layout.popup_theme_change, null);
        prepare();
    }

    public PopupTheme setOnBoardFontSizeChangeListener(OnBoardFontSizeChangeListener onBoardFontSizeChangeListener) {
        this.onBoardFontSizeChangeListener = onBoardFontSizeChangeListener;
        return this;
    }

    public PopupTheme setOnThemeChangeListener(OnThemeChangeListener onThemeChangeListener) {
        this.onThemeChangeListener = onThemeChangeListener;
        return this;
    }

    public PopupTheme show (View anchorView) {

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

    private void prepare () {

        themeGroup = mainView.findViewById(R.id.material_toggle_group);
        textSizeGroup = mainView.findViewById(R.id.material_text_size_toggle_group);

        smallTextMB = mainView.findViewById(R.id.small_font_mb);
        mediumTextMB = mainView.findViewById(R.id.medium_font_mb);
        largeTextMB = mainView.findViewById(R.id.large_font_mb);
        dayMB = mainView.findViewById(R.id.day_mb);
        nightMB = mainView.findViewById(R.id.night_mb);
        parentCV = mainView.findViewById(R.id.parent_cv);

        if (themeGroup != null) {

            CommonSharedData commonSharedData = new CommonSharedData(mainView.getContext(), SharedData.SHARED_PREF_TITLE);

            int selectedTheme = commonSharedData.getAppCurrentTheme();

            if (selectedTheme == AppThemes.DAY) {
                themeGroup.check(R.id.day_mb);
            } else if (selectedTheme == AppThemes.DARK) {
                themeGroup.check(R.id.night_mb);
            } else {
                themeGroup.check(R.id.day_mb);
            }

        }

        if (textSizeGroup != null) {

            float fSize = new SharedData(mainView.getContext()).getBoardFontSize();

            if (fSize == BoardFontSize.SMALL) {
                textSizeGroup.check(R.id.small_font_mb);
            } else if (fSize == BoardFontSize.MEDIUM) {
                textSizeGroup.check(R.id.medium_font_mb);
            } else {
                textSizeGroup.check(R.id.large_font_mb);
            }

        }

        int width = FrameLayout.LayoutParams.WRAP_CONTENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false;

        popupWindow = new PopupWindow(this.mainView, width, height, focusable);

        refreshViewsColors();
        setViewsClickListener();
    }

    private void setViewsClickListener () {

        if (textSizeGroup != null) {

            textSizeGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                    if (isChecked) {

                        SharedData sharedData = new SharedData(group.getContext());
                        group.check(checkedId);

                        if (checkedId == R.id.small_font_mb) {

                            sharedData.setBoardFontSize(BoardFontSize.SMALL);

                            if (onBoardFontSizeChangeListener != null)
                                onBoardFontSizeChangeListener.onChange(BoardFontSize.SMALL);

                        } else if (checkedId == R.id.medium_font_mb) {

                            sharedData.setBoardFontSize(BoardFontSize.MEDIUM);

                            if (onBoardFontSizeChangeListener != null)
                                onBoardFontSizeChangeListener.onChange(BoardFontSize.MEDIUM);

                        } else if (checkedId == R.id.large_font_mb) {

                            sharedData.setBoardFontSize(BoardFontSize.LARGE);

                            if (onBoardFontSizeChangeListener != null)
                                onBoardFontSizeChangeListener.onChange(BoardFontSize.LARGE);

                        }

                    }

                }
            });

        }

        if (themeGroup != null) {

            themeGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                    if (isChecked) {

                        CommonSharedData commonSharedData = new CommonSharedData(mainView.getContext(), SharedData.SHARED_PREF_TITLE);
                        group.check(checkedId);

                        if (shouldDismiss) {
                            dismiss();
                        }

                        if (checkedId == R.id.day_mb) {
                            commonSharedData.setCurrentAppTheme(AppThemes.DAY);
                            if (onThemeChangeListener != null) {
                                onThemeChangeListener.onChanged();
                            }
                        } else if (checkedId == R.id.night_mb) {
                            commonSharedData.setCurrentAppTheme(AppThemes.DARK);
                            if (onThemeChangeListener != null) {
                                onThemeChangeListener.onChanged();
                            }
                        }

                        refreshViewsColors();

                    }
                }
            });

        }

    }

    public PopupTheme shouldDismiss (boolean shouldDismiss) {
        this.shouldDismiss = shouldDismiss;
        return this;
    }

    private void dismiss () {

        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
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

        dayMB.setTextColor(new ColorStateList(states, colorsForText));
        nightMB.setTextColor(new ColorStateList(states, colorsForText));
        smallTextMB.setTextColor(new ColorStateList(states, colorsForText));
        mediumTextMB.setTextColor(new ColorStateList(states, colorsForText));
        largeTextMB.setTextColor(new ColorStateList(states, colorsForText));

        dayMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        nightMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        smallTextMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        mediumTextMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        largeTextMB.setStrokeColor(new ColorStateList(states, colorsForStroke));

        dayMB.setIconTint(new ColorStateList(states, colorsForText));
        nightMB.setIconTint(new ColorStateList(states, colorsForText));
        smallTextMB.setIconTint(new ColorStateList(states, colorsForText));
        mediumTextMB.setIconTint(new ColorStateList(states, colorsForText));
        largeTextMB.setIconTint(new ColorStateList(states, colorsForText));

        dayMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        nightMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        smallTextMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        mediumTextMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        largeTextMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));

        parentCV.setCardBackgroundColor(context.getColor(appColor.getPopupCardBackgroundColor()));

    }

}
