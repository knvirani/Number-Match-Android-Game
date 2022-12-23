package com.fourshape.numbermatch.fragments.howtoplay;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.listeners.OnBoardStartAnimationListener;
import com.fourshape.numbermatch.listeners.OnCellScanAnimationListener;
import com.fourshape.numbermatch.listeners.OnCellSelectListener;
import com.fourshape.numbermatch.listeners.OnRowRemoveAnimationListener;
import com.fourshape.numbermatch.puzzle_core.BoardView;
import com.fourshape.numbermatch.puzzle_core.Matrix;
import com.fourshape.numbermatch.puzzle_core.howtoplay.TutorInfo;
import com.fourshape.numbermatch.puzzle_core.howtoplay.TutorMatrixCollectionSet;
import com.fourshape.numbermatch.puzzle_core.structure.CellRC;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Step2Fragment extends Fragment {

    private static final String TAG = "Step2Fragment";

    private View mainView;
    private BoardView boardView;
    private MaterialCardView boardContainerCV, contentCV, addRowCV;
    private TextView stepHeaderTV, stepContentTV;
    private MaterialButton nextMB;
    private FrameLayout parentFL;
    private ImageView addRowIV;
    private Matrix matrix;

    private TutorMatrixCollectionSet tutorMatrixCollectionSet;
    private int stepIndex = 1;
    private int fragmentContainerId = -1;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_step2, null);

        boardContainerCV = mainView.findViewById(R.id.board_cv);
        contentCV = mainView.findViewById(R.id.content_cv);
        stepHeaderTV = mainView.findViewById(R.id.step_header_tv);
        stepContentTV = mainView.findViewById(R.id.step_content_tv);
        nextMB = mainView.findViewById(R.id.next_mb);
        parentFL = mainView.findViewById(R.id.parent_fl);
        addRowCV = mainView.findViewById(R.id.add_row_cv);
        addRowIV = mainView.findViewById(R.id.add_row_iv);

        addRowCV.setVisibility(View.GONE);

        if (container != null)
            this.fragmentContainerId = container.getId();

        matrix = new Matrix();
        matrix.setItemsPerRow(5);
        tutorMatrixCollectionSet = new TutorMatrixCollectionSet();

        matrix.setCollection(tutorMatrixCollectionSet.get(stepIndex));
        MakeLog.info(TAG, "Step Index: " + stepIndex);

        boardView = mainView.findViewById(R.id.board_view);
        boardView.setForTutorial();
        boardView.setOnBoardStartAnimationListener(onBoardStartAnimationListener);
        boardView.setOnRowRemoveAnimationListener(onRowRemoveAnimationListener);
        matrix.setOnCellSelectListener(onCellSelectListener);

        ArrayList<CellRC> cellRCArrayList = matrix.getSinglePossibleMatch();

        if (cellRCArrayList != null) {

            int row1 = cellRCArrayList.get(0).getRow();
            int col1 = cellRCArrayList.get(0).getCol();
            int row2 = cellRCArrayList.get(1).getRow();
            int col2 = cellRCArrayList.get(1).getCol();

            matrix.selectRCForTutor(row1+1, col1+1, row2+1, col2+1);

        }

        boardView.setMatrix(matrix);
        boardView.disableTextDrawingOnBoard();
        boardView.enableBoardStartAnimation();
        boardView.enableBoardDrawing();

        nextMB.setEnabled(false);

        setHeaderAndContent();

        addRowCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stepIndex++;
                boardView.enableCellScanAnimation(matrix.getRCofNewCellsRequired());
                nextMB.setEnabled(true);

                addRowCV.setVisibility(View.GONE);

            }
        });

        nextMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNewStep();
            }
        });

        resetViewsColor();

        return mainView;
    }

    private void setHeaderAndContent () {

        if (stepHeaderTV != null && stepContentTV != null) {

            stepHeaderTV.setText(TutorInfo.getStepHeader(stepIndex));
            stepContentTV.setText(TutorInfo.getStepContent(stepIndex));

        }

    }

    private void loadNewStep () {
        if (stepIndex <= tutorMatrixCollectionSet.getTotalShapes()) {

            nextMB.setEnabled(false);

            stepIndex++;
            matrix.resetSelectedRC();
            matrix.resetMatrixForTutor();
            matrix.setCollection(tutorMatrixCollectionSet.get(stepIndex));
            matrix.customizeCellsForTutorial(stepIndex);
            MakeLog.info(TAG, "Step Index: " + stepIndex);

            setHeaderAndContent();

            ArrayList<CellRC> cellRCArrayList = matrix.getSinglePossibleMatch();

            if (cellRCArrayList != null) {

                int row1 = cellRCArrayList.get(0).getRow();
                int col1 = cellRCArrayList.get(0).getCol();
                int row2 = cellRCArrayList.get(1).getRow();
                int col2 = cellRCArrayList.get(1).getCol();

                matrix.selectRCForTutor(row1+1, col1+1, row2+1, col2+1);

            }

        } else {
            if (getActivity() != null && fragmentContainerId != -1) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("step3_fragment").replace(fragmentContainerId, new Step3Fragment()).commit();
            }
        }

        if (stepIndex == tutorMatrixCollectionSet.getTotalShapes()) {
            nextMB.setText("Finish Tutorial");
        }

    }

    private void resetViewsColor () {

        if (getContext() == null)
            return;

        AppColor appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        boardView.refreshViewsColor();
        parentFL.setBackgroundColor(this.getContext().getColor(appColor.getAppBackgroundColor()));
        boardContainerCV.setCardBackgroundColor(this.getContext().getColor(appColor.getPopupCardBackgroundColor()));
        contentCV.setCardBackgroundColor(this.getContext().getColor(appColor.getPopupCardBackgroundColor()));
        addRowCV.setCardBackgroundColor(this.getContext().getColor(appColor.getGameBoardControlCardBackgroundColor()));
        stepHeaderTV.setTextColor(this.getContext().getColor(appColor.getHowtoplayStepHeaderTextColor()));
        stepContentTV.setTextColor(this.getContext().getColor(appColor.getAppBarTitleTextColor()));
        addRowIV.setImageTintList(ColorStateList.valueOf(this.getContext().getColor(appColor.getGameBoardControlImgTintColor())));
        nextMB.setBackgroundTintList(ColorStateList.valueOf(this.getContext().getColor(appColor.getPrimaryBtnBackgroundColor())));
        nextMB.setTextColor(this.getContext().getColor(appColor.getPrimaryBtnTextColor()));
        nextMB.setIconTint(ColorStateList.valueOf(this.getContext().getColor(appColor.getPrimaryBtnTextColor())));

    }

    private final OnBoardStartAnimationListener onBoardStartAnimationListener = new OnBoardStartAnimationListener() {
        @Override
        public void onFinish() {
            boardView.enableTextDrawingOnBoard();
        }
    };

    private final OnRowRemoveAnimationListener onRowRemoveAnimationListener = new OnRowRemoveAnimationListener() {
        @Override
        public void onFinish() {
            matrix.removeSolvedRow();
        }
    };

    private final OnCellScanAnimationListener onCellScanAnimationListener = new OnCellScanAnimationListener() {
        @Override
        public void onFinish() {



        }
    };

    private final OnCellSelectListener onCellSelectListener = new OnCellSelectListener() {
        @Override
        public void onSelected(boolean isSolved, boolean hasGapBetweenSelectedCells, boolean shouldAnimateOnNoSolution) {

            if (isSolved) {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        matrix.solveSelectedCells();
                        matrix.resetSelectedRC();
                        matrix.resetRCForTutor();

                        ArrayList<CellRC> cellRCArrayList = matrix.getSinglePossibleMatch();

                        if (cellRCArrayList != null) {

                            int row1 = cellRCArrayList.get(0).getRow();
                            int col1 = cellRCArrayList.get(0).getCol();
                            int row2 = cellRCArrayList.get(1).getRow();
                            int col2 = cellRCArrayList.get(1).getCol();

                            matrix.selectRCForTutor(row1+1, col1+1, row2+1, col2+1);

                        } else {

                            if (stepIndex == 9) {
                                if (addRowCV != null) {
                                    if (addRowCV.getVisibility() != View.VISIBLE)
                                        addRowCV.setVisibility(View.VISIBLE);
                                }
                            }

                            if (stepIndex < tutorMatrixCollectionSet.getTotalShapes())
                                loadNewStep();

                            if (stepIndex < tutorMatrixCollectionSet.getTotalShapes()) {
                                if (nextMB != null)
                                    nextMB.setEnabled(true);
                            }

                        }

                        ArrayList<CellRC> cellRCArrayList1 = matrix.getRemoveEligibleRowCellsData();
                        if (cellRCArrayList1 != null && cellRCArrayList1.size() > 0) {
                            MakeLog.info(TAG, "RemovalRowArr size: " + cellRCArrayList1.size());
                            boardView.enableRowRemoveAnimation(cellRCArrayList1);
                        }

                    }
                }, 250);

            }

        }
    };

}
