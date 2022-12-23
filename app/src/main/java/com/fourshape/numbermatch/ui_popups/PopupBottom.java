package com.fourshape.numbermatch.ui_popups;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.DimPopupWindow;
import com.fourshape.easythingslib.utils.MakeLog;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class PopupBottom {

    private View view;
    private PopupWindow popupWindow;

    private TextView popupBodyTV;
    private MaterialCardView popupCV;
    private MaterialButton closeMB;

    public PopupBottom (Context context) {
        this.view = LayoutInflater.from(context).inflate(R.layout.daily_challenge_bottom_popup, null);
        prepare();
        setViewsClickListener();
        refreshViewsColor();
    }

    private void prepare () {

        popupCV = this.view.findViewById(R.id.popup_cv);
        popupBodyTV = this.view.findViewById(R.id.popup_body);
        closeMB = this.view.findViewById(R.id.close_mb);

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false;

        this.popupWindow = new PopupWindow(this.view, width, height, focusable);

    }

    private void refreshViewsColor () {

        AppColor appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(this.view.getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        Context context = this.view.getContext();

        this.popupCV.setCardBackgroundColor(ColorStateList.valueOf(context.getColor(appColor.getPopupCardBackgroundColor())));
        this.popupBodyTV.setTextColor(ColorStateList.valueOf(context.getColor(appColor.getPopupBodyTextColor())));
        this.closeMB.setBackgroundTintList(ColorStateList.valueOf(context.getColor(appColor.getPopupPrimaryButtonBackgroundColor())));
        this.closeMB.setTextColor(ColorStateList.valueOf(context.getColor(appColor.getPopupPrimaryButtonTextColor())));

    }

    public void setMessage (String message) {

        if (message != null) {
            this.popupBodyTV.setText(message);
        }

        if (this.view == null) {
            return;
        }

        if (this.popupWindow == null) {
            return;
        }

        if (this.popupWindow.isShowing())
            return;

        popupWindow.showAtLocation(this.view, Gravity.CENTER, 0, 0);
        DimPopupWindow.dimBehindWithFactor(popupWindow, 0.5f);

    }

    private void setViewsClickListener () {

        this.closeMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void dismiss () {
        if (this.popupWindow != null){
            if (this.popupWindow.isShowing())
                this.popupWindow.dismiss();
        }
    }

}
