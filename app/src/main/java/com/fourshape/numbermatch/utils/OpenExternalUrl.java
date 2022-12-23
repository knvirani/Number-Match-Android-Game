package com.fourshape.numbermatch.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

public class OpenExternalUrl {

    public static void open (Context context, View linkView, Uri uri) {

        try {
            if (context != null) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);

            }
        } catch (Exception e){

            MakeLog.exception(e);

            if (context != null) {
                new ActionSnackbar().show(linkView, true, "No app can handle");
            }

        }

    }

    public static void open (Context context, View linkView, String url) {

        try {
            if (context != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
            }
        } catch (Exception e){

            MakeLog.exception(e);

            if (context != null) {
                new ActionSnackbar().show(linkView, true, "No app can handle");
            }

        }
    }

}
