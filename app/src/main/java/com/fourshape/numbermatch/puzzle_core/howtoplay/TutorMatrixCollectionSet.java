package com.fourshape.numbermatch.puzzle_core.howtoplay;

public class TutorMatrixCollectionSet {

    public TutorMatrixCollectionSet () {}

    public int getTotalShapes () {
        return 9;
    }

    public int[] get (int index) {

        if (index == 1)
            return HORIZONTAL_WITHOUT_GAP;
        else if (index == 2)
            return HORIZONTAL_WITH_GAP;
        else if (index == 3)
            return VERTICAL_WITHOUT_GAP;
        else if (index == 4)
            return VERTICAL_WITH_GAP;
        else if (index == 5)
            return DIAGONAL_WITHOUT_GAP;
        else if (index == 6)
            return DIAGONAL_WITH_GAP;
        else if (index == 7)
            return LF_WITHOUT_GAP;
        else if (index == 8)
            return LF_WITH_GAP;
        else if (index == 9)
            return ADD_MORE_NUMBERS;
        else
            return null;

    }

    private static final int[] HORIZONTAL_WITHOUT_GAP = {
            5,4,2,2,3,
            8,7,7,9,5,
            3,9,8,2,6
    };

    private static final int[] HORIZONTAL_WITH_GAP = {
            9,6,3,3,4,
            2,1,1,2,7,
            3,4,5,6,9
    };

    private static final int[] VERTICAL_WITHOUT_GAP = {
            5,1,2,3,4,
            7,1,6,1,8,
            5,8,7,9,5
    };

    private static final int[] VERTICAL_WITH_GAP = {
            5,3,4,9,5,
            8,2,3,3,7,
            5,2,6,5,6
    };

    private static final int[] DIAGONAL_WITHOUT_GAP = {
            5,9,4,5,9,
            2,5,7,6,2,
            1,4,2,9,3
    };

    private static final int[] DIAGONAL_WITH_GAP = {
            7,1,6,2,3,
            4,5,5,1,1,
            1,8,3,4,8
    };

    private static final int[] LF_WITHOUT_GAP = {
            5,1,4,1,7,
            3,8,5,2,6,
            6,1,7,9,3
    };

    private static final int[] LF_WITH_GAP = {
            5,9,5,1,7,
            5,3,2,6,1,
            2,2,6,3,8
    };

    private static final int[] ADD_MORE_NUMBERS = {
            5,9,4,1,7,
            6,3,3,8,8,
            4,2,2,1,9
    };

}
