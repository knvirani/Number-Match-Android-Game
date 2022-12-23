package com.fourshape.customcalendarlib;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.fourshape.customcalendarlib.R;

public class MarkStyle {

    public static final int BACKGROUND = 1;
    public static final int DOT = 2;
    public static final int LEFTSIDEBAR = 3;
    public static final int RIGHTSIDEBAR = 4;
    public static final int TEXT = 5;
    public static final int DEFAULT = 10;
    public static final int SESSION_COMPLETE = 11;
    public static final int SESSION_INCOMPLETE = 12;
    public static final int TODAY_DATE_COMPLETE = 13;
    public static final int TODAY_DATE_INCOMPLETE = 14;
    public static final int SELECTED_DATE = 15;

    public static int defaultColor = Color.rgb(0, 148, 243);

    public static String text;
    public static int textColor;

    public static int current = DEFAULT;

    public static Drawable choose = new Drawable() {
        private Paint paint;

        {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.LTGRAY);
        }
        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(getBounds().width() / 2.0f,
                    getBounds().height() / 2.0f,
                    getBounds().height() / 3.0f,
                    paint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @SuppressLint("WrongConstant")
        @Override
        public int getOpacity() {
            return 0;
        }
    };

    private int style;
    private int color;

    public MarkStyle() {
        this.style = MarkStyle.DEFAULT;
        this.color = MarkStyle.defaultColor;
    }

    public MarkStyle(int style, int color) {
        this.style = style;
        this.color = color;
    }

    public int getStyle() {
        return style;
    }

    public MarkStyle setStyle(int style) {
        this.style = style;
        current = style;
        return this;
    }

    public int getColor() {
        return color;
    }

    public MarkStyle setColor(int color) {
        this.color = color;
        return this;
    }

}
