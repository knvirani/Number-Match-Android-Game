package com.fourshape.numbermatch.puzzle_core.howtoplay;

public class TutorInfo {

    public static String getStepHeader (int stepIndex) {

        switch (stepIndex) {

            case 1:
                return "1/9: Horizontal match";

            case 2:
                return "2/9: Horizontal match";

            case 3:
                return "3/9: Vertical match";

            case 4:
                return "4/9: Vertical match";

            case 5:
                return "5/9: Diagonal match";

            case 6:
                return "6/9: Diagonal match";

            case 7:
                return "7/9: Last-First match";

            case 8:
                return "8/9: Last-First match";

            case 9:
                return "9/9: Add More Numbers";

            default:
                return "";
        }

    }

    public static String getStepContent (int stepIndex) {

        switch (stepIndex) {

            case 1:
                return "Find same numbers or numbers with a sum of 10 in the same row and there is no unsolved number (in black color) placed between." +
                        "\n\n" +
                        "Here, the numbers 2 and 2 are similar and on the same row. Hence, select both of them one-by-one." +
                        "\n\n" +
                        "Similarly, repeat the step for numbers 7 and 7 on the 2nd row.";

            case 2:
                return "Find same numbers or numbers with sum of 10 in the same row. If any two numbers are same or sum of them is 10 and placed on the same row, but there are few numbers placed between and all of them are solved then, your target numbers can also be solved." +
                        "\n\n" +
                        "Here, the numbers 6 and 4 are sum of 10 and also on the same row. So, it's a horizontal match. Here, notice the light-dark numbers 3 and 3 here. They are solved numbers, hence they are light-dark." +
                        "\n\n" +
                        "Now, because 3, 3 are solved, the numbers 6 and 4 can also be solved. So, select both unsolved numbers (6, and 4) one-by-one.";

            case 3:
                return "Find same numbers or numbers with a sum of 10 in the same column and there is no unsolved number placed between." +
                        "\n\n" +
                        "Here, the numbers 1 and 1 are similar and on the same column (2nd one). Hence, select both of them one-by-one." +
                        "\n\n" +
                        "Similarly, repeat the step for numbers 1 and 9 on the 4th column.";

            case 4:
                return "Find same numbers or numbers with sum of 10 in the same column. If any two numbers are same or sum of them is 10 and placed on the same column, but there are few numbers placed between and all of them are solved then, your target numbers can also be solved." +
                        "\n\n" +
                        "Here, the numbers 5 and 5 are same and also on the same column. So, it's a vertical match. Here, notice the light-dark number 8 here. It's a solved number, hence it's light-dark." +
                        "\n\n" +
                        "Now, because 8 is solved, the numbers 5 and 5 can also be solved. So, select both unsolved numbers (5, and 5) one-by-one.";

            case 5:
                return "Find same numbers or numbers with a sum of 10 placed diagonally and there is no unsolved number placed between." +
                        "\n\n" +
                        "Here, the numbers 5 and 5 are similar and placed diagonally and directly connected. Hence, select both of them one-by-one." +
                        "\n\n" +
                        "Similarly, repeat the step for numbers (4,6) and (3,7).";

            case 6:
                return "Find same numbers or numbers with sum of 10 placed diagonally. If any two numbers are same or sum of them is 10 and placed diagonally, but there are few numbers placed between and all of them are solved then, your target numbers can also be solved." +
                        "\n\n" +
                        "Here, the numbers 7 and 3 are sum of 10 and also placed diagonally. So, it's a diagonal match. Here, notice the light-dark number 5 here. It's a solved number, hence it's light-dark." +
                        "\n\n" +
                        "Now, because 5 is solved, the numbers 7 and 3 can also be solved because the path is clear. So, select both unsolved numbers (7, and 3) one-by-one.";

            case 7:
                return "If a number placed on the row and it's a last number or no unsolved number is placed after, and a number placed on the just following row and it's either same or sum of 10 then both can be solved." +
                        "\n\n" +
                        "Here, the numbers 7 and 3 are sum of 10 and placed LF (Last-First) manner. Hence, select both of them one-by-one." +
                        "\n\n" +
                        "Similarly, repeat the step for numbers (6,6).";

            case 8:
                return "If a number placed on the row and it's a last number or any solved numbers are placed after, and a number placed on the just following row and it's either same or sum of 10 and if any or few solved numbers placed before it, then both target numbers can be solved." +
                        "\n\n" +
                        "Here, the numbers 7 and 3 are sum of 10 and placed LF (Last-First) manner. Here, 7 is on the 1st row and no number is placed after. And, 3 is on the second row but 5 is placed before it. Since, 5 is a solved number, both 7 and 3 can be solved. Hence, select both of them one-by-one." +
                        "\n\n" +
                        "Similarly, repeat the step for numbers (6,6).";

            case 9:
                return "If no pair is possible but some number are still on board, then you can add numbers by taping on '+' button." +
                        "\n\n" +
                        "Here, you will get the same numbers that were on the board just before the button-press.";

            default:
                return "";

        }

    }

}
