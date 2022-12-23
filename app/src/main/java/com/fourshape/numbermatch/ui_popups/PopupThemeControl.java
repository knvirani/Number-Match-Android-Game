package com.fourshape.numbermatch.ui_popups;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.fourshape.easythingslib.listeners.OnDialogDismissListener;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.listeners.OnBoardFontSizeChangeListener;
import com.fourshape.numbermatch.puzzle_core.BoardFontSize;
import com.fourshape.numbermatch.utils.DimPopupWindow;
import com.fourshape.numbermatch.utils.LiveGameSettings;
import com.fourshape.numbermatch.utils.SharedData;
import com.fourshape.easythingslib.listeners.OnThemeChangeListener;
import com.fourshape.easythingslib.utils.AppThemes;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;

public class PopupThemeControl {

    private View mainView;
    private MaterialButtonToggleGroup themeGroup, textSizeGroup, scoreGroup, timeGroup, soundsGroup, vibrationGroup;
    private MaterialButton dayMB, nightMB, smallTextMB, mediumTextMB, largeTextMB, showScoreMB, hideScoreMB, showTimeMB, hideTimeMB,  soundsOnMB, soundsOffMB, vibOnMB, vibOffMB;
    private View dividerView;
    private MaterialCardView parentCV;
    private PopupWindow popupWindow;
    private boolean shouldDismiss = false;
    private TextView textSizeTV, timeTV, scoreTV, soundTV, vibrationTV;
    private LinearLayoutCompat extraLL;

    private OnThemeChangeListener onThemeChangeListener;
    private OnBoardFontSizeChangeListener onBoardFontSizeChangeListener;
    private OnDialogDismissListener onDialogDismissListener;

    public PopupThemeControl (Context context) {
        mainView = LayoutInflater.from(context).inflate(R.layout.popup_view_theme_control, null);
        prepare();
    }

    public PopupThemeControl setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
        return this;
    }

    public PopupThemeControl setOnBoardFontSizeChangeListener(OnBoardFontSizeChangeListener onBoardFontSizeChangeListener) {
        this.onBoardFontSizeChangeListener = onBoardFontSizeChangeListener;
        return this;
    }

    public PopupThemeControl setOnThemeChangeListener(OnThemeChangeListener onThemeChangeListener) {
        this.onThemeChangeListener = onThemeChangeListener;
        return this;
    }

    public PopupThemeControl show (View anchorView) {

        if (this.mainView == null)
            return this;

        if (this.popupWindow == null)
            return this;

        if (this.popupWindow.isShowing())
            return this;

        this.popupWindow.showAsDropDown(anchorView);
        this.popupWindow.showAtLocation(this.mainView, Gravity.NO_GRAVITY, 0, 0);
        DimPopupWindow.dimBehindWithFactor(this.popupWindow, 0.5f);

        return this;

    }

    private void prepare () {

        extraLL = mainView.findViewById(R.id.extra_ll);

        themeGroup = mainView.findViewById(R.id.material_toggle_group);
        textSizeGroup = mainView.findViewById(R.id.material_text_size_toggle_group);
        scoreGroup = mainView.findViewById(R.id.material_toggle_group_score);
        timeGroup = mainView.findViewById(R.id.material_toggle_group_time);
        soundsGroup = mainView.findViewById(R.id.material_toggle_group_sound);
        vibrationGroup = mainView.findViewById(R.id.material_toggle_group_vibration);

        smallTextMB = mainView.findViewById(R.id.small_font_mb);
        mediumTextMB = mainView.findViewById(R.id.medium_font_mb);
        largeTextMB = mainView.findViewById(R.id.large_font_mb);
        dayMB = mainView.findViewById(R.id.day_mb);
        nightMB = mainView.findViewById(R.id.night_mb);

        showScoreMB = mainView.findViewById(R.id.score_show_mb);
        hideScoreMB = mainView.findViewById(R.id.score_hide_mb);
        showTimeMB = mainView.findViewById(R.id.time_show_mb);
        hideTimeMB = mainView.findViewById(R.id.time_hide_mb);
        soundsOnMB = mainView.findViewById(R.id.sound_show_mb);
        soundsOffMB = mainView.findViewById(R.id.sound_hide_mb);
        vibOnMB = mainView.findViewById(R.id.vibration_show_mb);
        vibOffMB = mainView.findViewById(R.id.vibration_hide_mb);

        scoreTV = mainView.findViewById(R.id.score_tv);
        timeTV = mainView.findViewById(R.id.time_tv);
        textSizeTV = mainView.findViewById(R.id.board_text_size_tv);
        soundTV = mainView.findViewById(R.id.sound_tv);
        vibrationTV = mainView.findViewById(R.id.vibration_tv);

        dividerView = mainView.findViewById(R.id.divider_view_1);

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

        SharedData sharedData = new SharedData(mainView.getContext());

        if (textSizeGroup != null) {

            float fSize = sharedData.getBoardFontSize();

            if (fSize == BoardFontSize.SMALL) {
                textSizeGroup.check(R.id.small_font_mb);
            } else if (fSize == BoardFontSize.MEDIUM) {
                textSizeGroup.check(R.id.medium_font_mb);
            } else {
                textSizeGroup.check(R.id.large_font_mb);
            }

        }

        if (scoreGroup != null) {
            if (sharedData.getGameScoreStatus()) {
                scoreGroup.check(R.id.score_show_mb);
            } else {
                scoreGroup.check(R.id.score_hide_mb);
            }
        }

        if (timeGroup != null) {
            if (sharedData.getGameTimeStatus()) {
                timeGroup.check(R.id.time_show_mb);
            } else {
                timeGroup.check(R.id.time_hide_mb);
            }
        }

        if (soundsGroup != null) {


            if (new CommonSharedData(mainView.getContext(), SharedData.SHARED_PREF_TITLE).getGameSoundStatus()) {
                soundsGroup.check(R.id.sound_show_mb);
            } else {
                soundsGroup.check(R.id.sound_hide_mb);
            }
        }

        if (vibrationGroup != null) {
            if (new CommonSharedData(mainView.getContext(), SharedData.SHARED_PREF_TITLE).getGameVibrationStatus()) {
                vibrationGroup.check(R.id.vibration_show_mb);
            } else {
                vibrationGroup.check(R.id.vibration_hide_mb);
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

                        LiveGameSettings.update(group.getContext());

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


        if (soundsGroup != null) {
            soundsGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                    if (isChecked) {
                        group.check(checkedId);
                        if (checkedId == R.id.sound_show_mb) {
                            new CommonSharedData(group.getContext(), SharedData.SHARED_PREF_TITLE).toggleGameSound(true);
                            LiveGameSettings.update(group.getContext());
                            if (onThemeChangeListener != null) {
                                onThemeChangeListener.onChanged();
                            }
                        } else if (checkedId == R.id.sound_hide_mb) {
                            new CommonSharedData(group.getContext(), SharedData.SHARED_PREF_TITLE).toggleGameSound(false);
                            LiveGameSettings.update(group.getContext());
                            if (onThemeChangeListener != null) {
                                onThemeChangeListener.onChanged();
                            }
                        }
                    }

                }
            });
        }

        if (scoreGroup != null) {
            scoreGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                    if (isChecked) {
                        group.check(checkedId);
                        if (checkedId == R.id.score_show_mb) {
                            new SharedData(group.getContext()).toggleGameScore(true);
                            LiveGameSettings.update(group.getContext());
                            if (onThemeChangeListener != null) {
                                onThemeChangeListener.onChanged();
                            }
                        } else if (checkedId == R.id.score_hide_mb) {
                            new SharedData(group.getContext()).toggleGameScore(false);
                            LiveGameSettings.update(group.getContext());
                            if (onThemeChangeListener != null) {
                                onThemeChangeListener.onChanged();
                            }
                        }
                    }

                }
            });
        }

        if (timeGroup != null) {
            timeGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                    if (isChecked) {
                        group.check(checkedId);
                        if (checkedId == R.id.time_show_mb) {
                            new SharedData(group.getContext()).toggleGameTime(true);
                            LiveGameSettings.update(group.getContext());
                            if (onThemeChangeListener != null) {
                                onThemeChangeListener.onChanged();
                            }
                        } else if (checkedId == R.id.time_hide_mb) {
                            new SharedData(group.getContext()).toggleGameTime(true);
                            LiveGameSettings.update(group.getContext());
                            if (onThemeChangeListener != null) {
                                onThemeChangeListener.onChanged();
                            }
                        }
                    }

                }
            });
        }

        if (vibrationGroup != null) {
            vibrationGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                    if (isChecked) {
                        group.check(checkedId);
                        if (checkedId == R.id.vibration_show_mb) {
                            new CommonSharedData(group.getContext(), SharedData.SHARED_PREF_TITLE).toggleGameVibration(true);
                            LiveGameSettings.update(group.getContext());
                            if (onThemeChangeListener != null) {
                                onThemeChangeListener.onChanged();
                            }
                        } else if (checkedId == R.id.vibration_hide_mb) {
                            new CommonSharedData(group.getContext(), SharedData.SHARED_PREF_TITLE).toggleGameVibration(false);
                            LiveGameSettings.update(group.getContext());
                            if (onThemeChangeListener != null) {
                                onThemeChangeListener.onChanged();
                            }
                        }
                    }

                }
            });
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onDialogDismissListener != null) {
                    onDialogDismissListener.onDismiss();
                }
            }
        });

    }

    public PopupThemeControl shouldDismiss (boolean shouldDismiss) {
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

    public PopupThemeControl setThemeOnly () {

        /*
        hideView(dividerView);
        hideView(textSizeGroup);
        hideView(soundsGroup);
        hideView(vibrationGroup);
        hideView(timeGroup);
        hideView(scoreGroup);
        hideView(scoreTV);
        hideView(timeTV);
        hideView(vibrationTV);
        hideView(soundTV);
        hideView(textSizeTV);

         */
        hideView(extraLL);

        return this;
    }

    private void hideView (View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    private void refreshViewsColors () {

        Context context = mainView.getContext();

        if (context == null)
            return;

        AppColor appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(context, SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        if (dividerView != null) {
            dividerView.setBackgroundColor(context.getColor(appColor.getNormalDividerColor()));
        }

        if (textSizeTV != null) {
            textSizeTV.setTextColor(ColorStateList.valueOf(context.getColor(appColor.getPopupBodyTextColor())));
        }

        if (scoreTV != null) {
            scoreTV.setTextColor(ColorStateList.valueOf(context.getColor(appColor.getPopupBodyTextColor())));
        }

        if (timeTV != null) {
            timeTV.setTextColor(ColorStateList.valueOf(context.getColor(appColor.getPopupBodyTextColor())));
        }

        if (soundTV != null) {
            soundTV.setTextColor(ColorStateList.valueOf(context.getColor(appColor.getPopupBodyTextColor())));
        }

        if (vibrationTV != null) {
            vibrationTV.setTextColor(ColorStateList.valueOf(context.getColor(appColor.getPopupBodyTextColor())));
        }

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

        showScoreMB.setTextColor(new ColorStateList(states, colorsForText));
        hideScoreMB.setTextColor(new ColorStateList(states, colorsForText));
        showTimeMB.setTextColor(new ColorStateList(states, colorsForText));
        hideTimeMB.setTextColor(new ColorStateList(states, colorsForText));
        vibOnMB.setTextColor(new ColorStateList(states, colorsForText));
        vibOffMB.setTextColor(new ColorStateList(states, colorsForText));
        soundsOnMB.setTextColor(new ColorStateList(states, colorsForText));
        soundsOffMB.setTextColor(new ColorStateList(states, colorsForText));

        dayMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        nightMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        smallTextMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        mediumTextMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        largeTextMB.setStrokeColor(new ColorStateList(states, colorsForStroke));

        showScoreMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        hideScoreMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        showTimeMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        hideTimeMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        vibOnMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        vibOffMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        soundsOnMB.setStrokeColor(new ColorStateList(states, colorsForStroke));
        soundsOffMB.setStrokeColor(new ColorStateList(states, colorsForStroke));

        dayMB.setIconTint(new ColorStateList(states, colorsForText));
        nightMB.setIconTint(new ColorStateList(states, colorsForText));
        smallTextMB.setIconTint(new ColorStateList(states, colorsForText));
        mediumTextMB.setIconTint(new ColorStateList(states, colorsForText));
        largeTextMB.setIconTint(new ColorStateList(states, colorsForText));

        showScoreMB.setIconTint(new ColorStateList(states, colorsForText));
        hideScoreMB.setIconTint(new ColorStateList(states, colorsForText));
        showTimeMB.setIconTint(new ColorStateList(states, colorsForText));
        hideTimeMB.setIconTint(new ColorStateList(states, colorsForText));
        soundsOnMB.setIconTint(new ColorStateList(states, colorsForText));
        soundsOffMB.setIconTint(new ColorStateList(states, colorsForText));

        dayMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        nightMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        smallTextMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        mediumTextMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        largeTextMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));

        showScoreMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        hideScoreMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        showTimeMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        hideTimeMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        soundsOnMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        soundsOffMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        vibOnMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));
        vibOffMB.setBackgroundTintList(new ColorStateList(states, colorsForBg));

        parentCV.setCardBackgroundColor(context.getColor(appColor.getPopupCardBackgroundColor()));

    }

}
