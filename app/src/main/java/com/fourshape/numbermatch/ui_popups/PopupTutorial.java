package com.fourshape.numbermatch.ui_popups;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.DimPopupWindow;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.listeners.OnTutorialOptionChooseListener;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class PopupTutorial {

    private View mainView;
    private MaterialCardView parentCV;
    private MaterialButton openMB, skipMB;
    private TextView headerTV, bodyTV;
    private PopupWindow popupWindow;

    private OnTutorialOptionChooseListener onTutorialOptionChooseListener;

    public PopupTutorial (Context context) {
        mainView = LayoutInflater.from(context).inflate(R.layout.popup_tutorial, null);
        prepare();
        setViewsClickListener();

    }

    public PopupTutorial setOnTutorialOptionChooseListener(OnTutorialOptionChooseListener onTutorialOptionChooseListener) {
        this.onTutorialOptionChooseListener = onTutorialOptionChooseListener;
        return this;
    }

    private void setViewsClickListener () {

        if (openMB != null) {
            openMB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (popupWindow != null) {
                        if (popupWindow.isShowing())
                            popupWindow.dismiss();
                    }

                    if (onTutorialOptionChooseListener != null) {
                        onTutorialOptionChooseListener.onOpen();
                    }

                }
            });
        }

        if (skipMB != null) {
            skipMB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (popupWindow != null) {
                        if (popupWindow.isShowing())
                            popupWindow.dismiss();
                    }

                    if (onTutorialOptionChooseListener != null) {
                        onTutorialOptionChooseListener.onSkip();
                    }

                }
            });
        }

    }

    private void prepare () {

        parentCV = mainView.findViewById(R.id.parent_cv);
        openMB = mainView.findViewById(R.id.open_tutor_mb);
        skipMB = mainView.findViewById(R.id.skip_tutor_mb);
        headerTV = mainView.findViewById(R.id.popup_header);
        bodyTV = mainView.findViewById(R.id.popup_body);

        int width = FrameLayout.LayoutParams.WRAP_CONTENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false;

        popupWindow = new PopupWindow(mainView, width, height, focusable);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                new SharedData(mainView.getContext()).setTutorDialogShown();
            }
        });

        Context context = mainView.getContext();

        if (context == null)
            return;

        AppColor appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(context, SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        parentCV.setCardBackgroundColor(context.getColor(appColor.getPopupCardBackgroundColor()));
        headerTV.setTextColor(context.getColor(appColor.getPopupTitleTextColor()));
        bodyTV.setTextColor(context.getColor(appColor.getPopupBodyTextColor()));
        openMB.setBackgroundTintList(ColorStateList.valueOf(context.getColor(appColor.getPrimaryBtnBackgroundColor())));
        openMB.setTextColor(context.getColor(appColor.getPrimaryBtnTextColor()));
        skipMB.setBackgroundTintList(ColorStateList.valueOf(context.getColor(appColor.getPrimaryBtnBackgroundColor())));
        skipMB.setTextColor(context.getColor(appColor.getPrimaryBtnTextColor()));

    }

    public PopupTutorial show () {

        if (this.mainView == null)
            return this;

        if (this.popupWindow == null)
            return this;

        if (this.popupWindow.isShowing())
            return this;

        this.popupWindow.showAtLocation(this.mainView, Gravity.CENTER, 0, 0);
        DimPopupWindow.lightDimBehind(this.popupWindow);

        return this;

    }

}
