package com.fourshape.numbermatch.puzzle_core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.numbermatch.utils.SharedData;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.ScreenParams;
import com.tomergoldst.tooltips.ToolTipsManager;

public class ToolTip {

    private static final String TAG = "NewToolTip";

    private ToolTipsManager toolTipsManager;
    private com.tomergoldst.tooltips.ToolTip.Builder builder;
    private ViewGroup rootView;
    private View anchorView;
    private AppColor appColor;
    private int offsetX = 0, offsetY = 0, cellSize = 0;

    public ToolTip (@NonNull ViewGroup rootView, @NonNull View anchorView) {
        this.rootView = rootView;
        this.anchorView = anchorView;
        toolTipsManager = new ToolTipsManager();
        appColor = new AppColor();
        builder = new com.tomergoldst.tooltips.ToolTip.Builder(anchorView.getContext(), anchorView, rootView, "Hello", com.tomergoldst.tooltips.ToolTip.POSITION_ABOVE);
    }

    public void setOffsets (int offsetX, int offsetY, int cellSize) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.cellSize = cellSize;
    }

    public void show (String message) {
        builder = new com.tomergoldst.tooltips.ToolTip.Builder(anchorView.getContext(), anchorView, rootView, message, com.tomergoldst.tooltips.ToolTip.POSITION_ABOVE);

        toolTipsManager.dismissAll();

        MakeLog.info(TAG, "X-Offset: " + offsetX + " Y-Offset: " + offsetY);


        if (this.offsetX < getTooltipWidth()) {
            builder.setAlign(com.tomergoldst.tooltips.ToolTip.ALIGN_LEFT);
            builder.setOffsetX(getCalculatedOffsetXForLeftAlignment());
        } else if (this.offsetX + getTooltipWidth() > anchorView.getWidth()){
            builder.setAlign(com.tomergoldst.tooltips.ToolTip.ALIGN_RIGHT);
            builder.setOffsetX(getCalculatedOffsetXForRightAlignment());
        } else {
            builder.setAlign(com.tomergoldst.tooltips.ToolTip.ALIGN_CENTER);
            builder.setOffsetX(getCalculatedOffsetX());
        }

        appColor.setTheme(new CommonSharedData(anchorView.getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());
        builder.setBackgroundColor(anchorView.getContext().getColor(appColor.getCustomTooltipBackgroundColor()));

        builder.setGravity(com.tomergoldst.tooltips.ToolTip.GRAVITY_LEFT);

        builder.setOffsetY(getCalculatedOffsetY());

        toolTipsManager.setAnimationDuration(500);
        toolTipsManager.show(builder.build());
    }

    private int getCalculatedOffsetX () {
        return offsetX - (int)(anchorView.getWidth()/2);
    }


    private int getCalculatedOffsetXForLeftAlignment () {
        return offsetX - cellSize;
    }

    private int getCalculatedOffsetXForRightAlignment () {
        return getCalculatedOffsetX() - getTooltipWidth() - (int)(cellSize * 2.8);
    }

    private int getCalculatedOffsetY () {
        return offsetY - cellSize/3;
    }

    private int getTooltipWidth () {
        return (int) (90 * ScreenParams.getDisplayDensity(anchorView.getContext()));
    }

}
