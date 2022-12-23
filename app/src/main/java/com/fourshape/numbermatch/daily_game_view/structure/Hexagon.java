package com.fourshape.numbermatch.daily_game_view.structure;

public class Hexagon {

    private int lastUsedShape;
    private static final int TOTAL_SHAPES = 10;

    public Hexagon () {
        this.lastUsedShape = -1;
    }

    public Hexagon (int lastUsedShape) {
        this.lastUsedShape = lastUsedShape;
    }

    public int getLastUsedShape() {
        return lastUsedShape;
    }

    public int getTotalShape () {
        return TOTAL_SHAPES;
    }

    public int[] getShape () {

        lastUsedShape++;

        if (lastUsedShape <= 0 || lastUsedShape > getTotalShape())
            lastUsedShape = 1;

        return getShape(lastUsedShape);
    }

    public int[] getShape (int shapeId) {

        switch (shapeId) {

            case 2:
                return SHAPE_2;

            case 3:
                return SHAPE_3;

            case 4:
                return SHAPE_4;

            case 5:
                return SHAPE_5;

            case 6:
                return SHAPE_6;

            case 7:
                return SHAPE_7;

            case 8:
                return SHAPE_8;

            case 9:
                return SHAPE_9;

            case 10:
                return SHAPE_10;

            case 1:

            default:
                return SHAPE_1;

        }

    }

    private static final int[] SHAPE_1 = {
            1,19,20,21,22,
            3,2,18,25,23,30,
            4,5,17,26,24,29,31,
            8,6,16,15,27,28,32,33,
            9,7,12,14,41,39,38,34,35,
            10,11,13,42,40,45,37,36,
            61,59,58,43,44,46,47,
            60,57,53,52,49,48,
            56,55,54,51,50
    };

    private static final int[] SHAPE_2 = {
            3,1,9,33,34,
            4,2,8,10,32,35,
            17,5,6,7,11,31,36,
            18,16,15,13,12,29,30,37,
            20,19,23,14,27,28,42,40,38,
            21,22,24,25,26,43,41,39,
            57,56,54,53,52,44,45,
            58,55,61,51,47,46,
            59,60,50,49,48
    };

    private static final int[] SHAPE_3 = {
            5,4,27,28,29,
            6,3,1,26,31,30,
            7,9,2,25,20,32,34,
            59,8,10,24,21,19,33,35,
            58,60,61,11,23,22,18,37,36,
            57,56,12,14,16,17,38,39,
            54,55,13,15,45,44,40,
            53,51,50,46,43,41,
            52,49,48,47,42
    };

    private static final int[] SHAPE_4 = {
            61,60,55,54,53,
            1,2,59,56,52,50,
            14,13,3,58,57,51,49,
            15,12,11,4,5,42,43,48,
            16,17,10,9,6,41,44,45,47,
            18,19,8,7,29,40,38,46,
            20,26,27,28,30,39,37,
            21,25,24,32,31,36,
            22,23,33,34,35
    };

    private static final int[] SHAPE_5 = {
            37,36,1,12,13,
            38,34,35,2,11,14,
            39,33,4,3,9,10,15,
            40,32,5,7,8,23,22,16,
            42,41,31,6,25,24,21,17,18,
            43,45,30,26,27,54,20,19,
            44,46,29,28,53,55,56,
            48,47,51,52,58,57,
            49,50,61,60,59
    };

    private static final int[] SHAPE_6 = {
            8,7,5,28,29,
            10,9,6,4,27,30,
            11,12,2,3,26,31,33,
            14,13,20,1,23,25,32,34,
            16,15,19,21,22,24,39,35,36,
            17,18,59,61,47,40,38,37,
            57,58,60,48,46,41,42,
            56,54,53,49,45,43,
            55,52,51,50,44
    };

    private static final int[] SHAPE_7 = {
            19,20,21,24,26,
            17,18,22,23,25,27,
            61,16,13,12,11,28,29,
            59,60,15,14,10,9,30,31,
            58,57,1,2,5,7,8,32,33,
            56,54,3,4,6,37,36,34,
            55,53,48,46,38,39,35,
            52,49,47,45,40,41,
            51,50,44,43,42
    };

    private static final int[] SHAPE_8 = {
            29,30,31,45,46,
            26,28,34,32,44,47,
            25,27,35,33,42,43,48,
            23,24,37,36,40,41,49,51,
            22,20,19,38,39,56,55,50,52,
            21,18,5,6,7,57,54,53,
            17,16,4,1,8,58,59,
            15,13,3,2,9,60,
            14,12,11,10,61
    };

    private static final int[] SHAPE_9 = {
            30,31,34,36,38,
            29,32,33,35,37,39,
            1,28,26,25,41,40,61,
            2,3,27,24,42,43,60,59,
            5,4,8,23,21,20,44,56,58,
            6,7,9,22,19,45,55,57,
            13,12,10,18,46,53,54,
            14,11,17,48,47,52,
            15,16,49,50,51
    };

    private static final int[] SHAPE_10 = {
      51,49,48,44,43,
      52,50,47,45,41,42,
      57,53,54,46,40,38,37,
      58,56,55,23,24,39,36,35,
      59,60,18,22,25,26,28,33,34,
      61,17,19,21,7,27,29,32,
      16,14,20,8,6,30,31,
      15,13,9,5,3,2,
      12,11,10,4,1
    };

}
