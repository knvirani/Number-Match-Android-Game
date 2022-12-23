package com.fourshape.numbermatch.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;

import com.fourshape.easythingslib.utils.DeviceType;
import com.fourshape.numbermatch.R;

public class ScreenConfiguration {

    private static final String TAG = "ScreenConfiguration";

    public static void set (Activity activity, Context context) {

        if (activity != null && context != null) {

            boolean isTablet = context.getResources().getBoolean(R.bool.is_tablet);

            if (isTablet) {
                DeviceType.TYPE = DeviceType.TABLET;
                MakeLog.info(TAG, "isTablet = YES");
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
            } else{
                DeviceType.TYPE = DeviceType.MOBILE;
                MakeLog.info(TAG, "isTablet = NO");
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
            }

        }

    }

}
