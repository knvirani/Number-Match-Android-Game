package com.fourshape.numbermatch.fragments.howtoplay;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.MakeLog;
import com.fourshape.easythingslib.utils.ScreenParams;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.listeners.OnBoardStartAnimationListener;
import com.fourshape.numbermatch.puzzle_core.BoardView;
import com.fourshape.numbermatch.puzzle_core.Matrix;
import com.fourshape.numbermatch.puzzle_core.howtoplay.TutorMatrixCollectionSet;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

public class Step1Fragment extends Fragment {

    private static final String TAG = "Step1Fragment";

    private View mainView;
    private TextView appLogoTV, contentTV;
    private BoardView boardView;
    private LinearLayoutCompat boardViewContainer;
    private MaterialCardView boardContainerCV, contentCV;
    private FrameLayout parentFL;
    private MaterialButton nextMB;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_step1, null);

        appLogoTV = mainView.findViewById(R.id.app_logo_tv);
        contentTV = mainView.findViewById(R.id.introduction_text);
        boardViewContainer = mainView.findViewById(R.id.board_view_container);
        boardContainerCV = mainView.findViewById(R.id.board_cv);
        contentCV = mainView.findViewById(R.id.content_cv);
        parentFL = mainView.findViewById(R.id.parent_fl);
        nextMB = mainView.findViewById(R.id.next_mb);

        Matrix matrix = new Matrix();
        matrix.setItemsPerRow(5);
        matrix.setCollection(new TutorMatrixCollectionSet().get(2));
        matrix.customizeCellsForTutorial(2);

        matrix.setSelectedRC(1,2);
        matrix.setSelectedRC(3,2);

        boardView = mainView.findViewById(R.id.board_view);
        boardView.setMatrix(matrix);
        boardView.setForTutorial();

        boardView.setOnBoardStartAnimationListener(onBoardStartAnimationListener);

        boardView.enableBoardStartAnimation();
        boardView.enableBoardDrawing();

        String introductionText = "";

        introductionText = "Number Match is a puzzle game in which, you've to match two similar numbers or numbers with summation of 10."
                + "\n\n"
                + "The match should be in 4 direction only. The directions are as following:" +
                "\n" +
                "\n\t\u2022" +
                " Horizontal" +
                "\n\t\u2022" +
                " Vertical" +
                "\n\t\u2022" +
                " Diagonal" +
                "\n\t\u2022" +
                " Last-First";

        contentTV.setText(introductionText);

        resetViewsColor();
        boardView.refreshViewsColor();

        nextMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null && container != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("step2_fragment").setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit).replace(container.getId(), new Step2Fragment()).commit();
                }
            }
        });

        return mainView;

    }

    private void resetViewsColor () {

        if (getContext() == null)
            return;

        AppColor appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        parentFL.setBackgroundColor(getContext().getColor(appColor.getAppBackgroundColor()));
        appLogoTV.setTextColor(getContext().getColor(appColor.getPrimaryBtnBackgroundColor()));
        boardContainerCV.setCardBackgroundColor(getContext().getColor(appColor.getPopupCardBackgroundColor()));
        contentCV.setCardBackgroundColor(getContext().getColor(appColor.getAppBarBackgroundColor()));
        contentTV.setTextColor(getContext().getColor(appColor.getAppBarTitleTextColor()));
        nextMB.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(appColor.getPrimaryBtnBackgroundColor())));
        nextMB.setTextColor(getContext().getColor(appColor.getPrimaryBtnTextColor()));


    }

    private void setBoardViewDimensions () {

        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(boardView.getViewWidth(), boardView.getViewHeight());
        MakeLog.info(TAG, "Width: " + boardView.getViewWidth() + " Height: " + boardView.getViewHeight());
        boardView.setLayoutParams(layoutParams);

    }

    private final OnBoardStartAnimationListener onBoardStartAnimationListener = new OnBoardStartAnimationListener() {
        @Override
        public void onFinish() {
            boardView.enableTextDrawingOnBoard();
        }
    };

}
