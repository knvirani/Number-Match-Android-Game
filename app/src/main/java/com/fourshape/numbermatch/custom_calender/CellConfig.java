package com.fourshape.numbermatch.custom_calender;

import com.fourshape.numbermatch.custom_calender.data_formats.DateData;

public class CellConfig {

    public static float cellWidth = 100;
    public static float cellHeight = 100;

    public static boolean ifMonth = true;
    // position of the middle page
    public static int middlePosition = 500;

    // The following two:
    // After clicking shrink and expand, record the position of the current page, and use them to convert the displacement later.
    public static int Month2WeekPos = 500;
    public static int Week2MonthPos = 500;

    // The following two:
    // Only one was set at the beginning, and later it was found that the data was crossed (the changes were made before use)
    // So divided into two
    public static DateData w2mPointDate;
    public static DateData m2wPointDate;

    public static DateData weekAnchorPointDate;

}
