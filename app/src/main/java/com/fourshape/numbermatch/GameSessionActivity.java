package com.fourshape.numbermatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fourshape.customcalendarlib.data_formats.DateData;
import com.fourshape.customcalendarlib.utils.CurrentCalendar;
import com.fourshape.easythingslib.listeners.OnAppRateListener;
import com.fourshape.easythingslib.listeners.OnDialogDismissListener;
import com.fourshape.easythingslib.listeners.OnDialogShowListener;
import com.fourshape.easythingslib.listeners.OnRewardedAdItemAccountListener;
import com.fourshape.easythingslib.listeners.OnThemeChangeListener;
import com.fourshape.easythingslib.ui_popups.PopupRatings;
import com.fourshape.easythingslib.utils.AppDialog;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.DeviceType;
import com.fourshape.easythingslib.utils.FormattedData;
import com.fourshape.numbermatch.analytics.TrackScreen;
import com.fourshape.numbermatch.app_ads.PlacementIds;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.daily_game_view.ChangeGameStatus;
import com.fourshape.numbermatch.game_db.GameDB;
import com.fourshape.numbermatch.listeners.OnAfterWonControlListener;
import com.fourshape.numbermatch.listeners.OnBoardFontSizeChangeListener;
import com.fourshape.numbermatch.listeners.OnBoardStartAnimationListener;
import com.fourshape.numbermatch.listeners.OnCellOpacityAnimationListener;
import com.fourshape.numbermatch.listeners.OnCellScanAnimationListener;
import com.fourshape.numbermatch.listeners.OnCellSelectListener;
import com.fourshape.numbermatch.listeners.OnExitSessionPopupDismissListener;
import com.fourshape.numbermatch.listeners.OnGameScoreChangeListener;
import com.fourshape.numbermatch.listeners.OnPauseStatusListener;
import com.fourshape.numbermatch.listeners.OnRowRemoveAnimationListener;
import com.fourshape.numbermatch.listeners.OnSessionClockTickListener;
import com.fourshape.numbermatch.listeners.OnSessionStatusListener;
import com.fourshape.numbermatch.puzzle_core.BoardView;
import com.fourshape.numbermatch.puzzle_core.GameControlLimit;
import com.fourshape.numbermatch.puzzle_core.GameControlStatus;
import com.fourshape.numbermatch.puzzle_core.GameLevel;
import com.fourshape.numbermatch.puzzle_core.GameLevelTitle;
import com.fourshape.numbermatch.puzzle_core.GameScore;
import com.fourshape.numbermatch.puzzle_core.GameStep;
import com.fourshape.numbermatch.puzzle_core.GameType;
import com.fourshape.numbermatch.puzzle_core.Matrix;
import com.fourshape.numbermatch.puzzle_core.SessionClock;
import com.fourshape.numbermatch.puzzle_core.structure.Achievement;
import com.fourshape.numbermatch.puzzle_core.structure.Cell;
import com.fourshape.numbermatch.puzzle_core.structure.CellRC;
import com.fourshape.numbermatch.puzzle_core.structure.GameCompletion;
import com.fourshape.numbermatch.puzzle_core.structure.GameData;
import com.fourshape.numbermatch.puzzle_core.structure.GameTypeTitle;
import com.fourshape.numbermatch.puzzle_core.structure.SetCollection;
import com.fourshape.numbermatch.ui_popups.PopupExitSession;
import com.fourshape.numbermatch.ui_popups.PopupGamePause;
import com.fourshape.numbermatch.ui_popups.PopupShowAdOnGameSession;
import com.fourshape.numbermatch.ui_popups.PopupTheme;
import com.fourshape.numbermatch.ui_popups.PopupThemeControl;
import com.fourshape.numbermatch.ui_popups.PopupWatchAdForFeature;
import com.fourshape.numbermatch.ui_views.ViewAfterWon;
import com.fourshape.numbermatch.ui_views.ViewCelebration;
import com.fourshape.numbermatch.utils.ActionSnackbar;
import com.fourshape.numbermatch.utils.BundleParams;
import com.fourshape.numbermatch.utils.LiveGameSettings;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.numbermatch.utils.PlaySound;
import com.fourshape.numbermatch.utils.RandNumber;
import com.fourshape.numbermatch.utils.ScreenConfiguration;
import com.fourshape.numbermatch.utils.ScreenParams;
import com.fourshape.numbermatch.utils.SharedData;
import com.fourshape.numbermatch.utils.VariablesControl;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import nl.dionsegijn.konfetti.KonfettiView;

public class GameSessionActivity extends AppCompatActivity {

    private static final String TAG = "BoardActivity";

    private Matrix matrix;
    private BoardView boardView;

    private ConstraintLayout parentCL, appBar;
    private LinearLayoutCompat bottomStatusLayout, afterWonLayout;
    private FrameLayout bottomControlLayout, level2TopStatusBarLayout;
    private MaterialCardView backCV, pauseCV, themeCV, settingsCV, sessionLevelCV;
    private FloatingActionButton hintFAB, addRowFAB;
    private ImageView backIV, pauseIV, themeIV, settingsIV;
    private TextView stepTV, scoreTV, allTimeBestScoreTV, addRowLimitTV, hintLimitTV, sessionClockTV, sessionLevelTV, sessionTypeTV;
    private ScrollView scrollView;
    private LinearProgressIndicator linearProgressIndicator;
    private KonfettiView konfettiView;

    private AppDialog exitDialog, pauseDialog;
    private boolean isPausedWithoutDialog = false;

    private AppColor appColor;
    private GameScore gameScore;
    private GameControlStatus gameControlStatus;
    private GameStep gameStep;
    private SharedData sharedData;

    private boolean hasGameStarted = false;
    private String savedDBSession = null;
    private int gameType = -1, gameLevel = -1, dailyDay = -1, dailyMonth = -1, dailyYear = -1;
    private boolean shouldSaveDailyToSharedPref = false;
    private boolean isGameWon = false;
    private boolean isEligibleForWinStreak = true;
    private int dailyGameRecordId = -1;
    private boolean isFreshSession = true;
    private GameData gameData;
    private int cellRank;

    private Achievement achievement;

    private static final int GAME_PLAY_LINEAR_PROGRESS_END_TIME = 1500;

    private Vibrator vibrator;
    private AudioAttributes audioAttributes;
    private SoundPool soundPool;
    private int[] soundId = new int[4];

    private ToolTipsManager toolTipsManager;
    private boolean isGameStepIncreased = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        ScreenConfiguration.set(this, this);

        setContentView(R.layout.activity_game_session);

        TrackScreen.now(this);

        sharedData = new SharedData(getWeakContext());

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        scrollView = findViewById(R.id.scroll_view);
        boardView = findViewById(R.id.board_view);
        parentCL = findViewById(R.id.parent_cl);
        appBar = findViewById(R.id.app_bar_layout);
        backCV = findViewById(R.id.action_back_cv);
        pauseCV = findViewById(R.id.action_pause_cv);
        themeCV = findViewById(R.id.action_palette_cv);
        settingsCV = findViewById(R.id.action_options_cv);
        backIV = findViewById(R.id.back_iv);
        themeIV = findViewById(R.id.palette_iv);
        pauseIV = findViewById(R.id.pause_iv);
        settingsIV = findViewById(R.id.options_iv);
        stepTV = findViewById(R.id.session_step);
        scoreTV = findViewById(R.id.session_score);
        allTimeBestScoreTV = findViewById(R.id.session_all_time_best_score_tv);
        addRowFAB = findViewById(R.id.add_row_fab);
        addRowLimitTV = findViewById(R.id.add_row_limit_tv);
        hintFAB = findViewById(R.id.hint_fab);
        hintLimitTV = findViewById(R.id.hint_limit_tv);
        sessionLevelCV = findViewById(R.id.session_level_cv);
        sessionLevelTV = findViewById(R.id.session_level_tv);
        sessionClockTV = findViewById(R.id.session_clock_tv);
        sessionTypeTV = findViewById(R.id.session_type_tv);
        linearProgressIndicator = findViewById(R.id.linear_progress_indicator);
        bottomControlLayout = findViewById(R.id.bottom_control_bar);
        bottomStatusLayout = findViewById(R.id.bottom_layout);
        level2TopStatusBarLayout = findViewById(R.id.level_2_top_status_bar);
        afterWonLayout = findViewById(R.id.after_won_layout);
        konfettiView = findViewById(R.id.viewKonfetti);

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(8)
                .setAudioAttributes(audioAttributes)
                .build();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        soundId[0] = soundPool.load(getWeakContext(), R.raw.ui_click, 1);
        soundId[1] = soundPool.load(getWeakContext(), R.raw.step_raise, 1);
        soundId[2] = soundPool.load(getWeakContext(), R.raw.row_solve, 1);
        soundId[3] = soundPool.load(getWeakContext(), R.raw.board_game_win, 1);

        pauseDialog = new AppDialog();
        exitDialog = new AppDialog();

        appColor = new AppColor();

        gameControlStatus = new GameControlStatus();
        gameControlStatus.setAddRowStatus(GameControlLimit.ADD_ROW_LIMIT);
        gameControlStatus.setHintStatus(sharedData.getHintControl());

        gameScore = new GameScore();
        gameScore.setOnGameScoreChangeListener(onGameScoreChangeListener);

        gameStep = new GameStep();

        matrix = new Matrix();
        matrix.setOnCellSelectListener(onCellSelectListener);

        int dimen = Math.min(ScreenParams.getDisplayWidthPixels(this), ScreenParams.getDisplayHeightPixels(this));
        scrollView.setLayoutParams(new LinearLayoutCompat.LayoutParams(dimen, dimen));

        refreshViewsColor();

        boardView.setOnBoardStartAnimationListener(onBoardStartAnimationListener);
        boardView.setOnRowRemoveAnimationListener(onRowRemoveAnimationListener);

        if (gameControlStatus.getHintStatus() == 0) {
            hintLimitTV.setText("Ad");
        } else {
            hintLimitTV.setText(String.valueOf(gameControlStatus.getHintStatus()));
        }

        setViewsClickListener();

        gameData = null;
        LiveGameSettings.update(getWeakContext());

        Intent intent = getIntent();

        if (intent != null) {
            gameType = intent.getIntExtra(BundleParams.GAME_TYPE, -1);
            gameLevel = intent.getIntExtra(BundleParams.GAME_LEVEL, -1);
            savedDBSession = intent.getStringExtra(BundleParams.GAME_SESSION);
            dailyDay = intent.getIntExtra(BundleParams.DAILY_GAME_DAY, -1);
            dailyMonth = intent.getIntExtra(BundleParams.DAILY_GAME_MONTH, -1);
            dailyYear = intent.getIntExtra(BundleParams.DAILY_GAME_YEAR, -1);
            dailyGameRecordId = intent.getIntExtra(BundleParams.DAILY_GAME_RECORD_ID, -1);
            cellRank = intent.getIntExtra(BundleParams.CELL_RANK, -1);
        }

        if (gameType == GameType.GENERAL) {
            gameData = null;
        } else if (gameType == GameType.SAVED_GENERAL) {
            gameData = new Gson().fromJson(new SharedData(getWeakContext()).getSavedGeneralGame(), GameData.class);
            gameLevel = gameData.getLevel();
        } else if (gameType == GameType.DAILY) {
            gameData = null;
        } else if (gameType == GameType.SAVED_DAILY) {
            gameData = new Gson().fromJson(new SharedData(getWeakContext()).getSavedDailyGame(), GameData.class);
            dailyGameRecordId = gameData.getRecordId();
            gameLevel = gameData.getLevel();
        } else {
            gameData = null;
            gameType = GameType.GENERAL;
            gameLevel = RandNumber.get(GameLevel.EASY, GameLevel.HARD);
        }

        if (sharedData.getAchievementJson(gameLevel) == null)
            achievement = new Achievement();
        else {
            achievement = new Achievement(new Gson().fromJson(sharedData.getAchievementJson(gameLevel), Achievement.class));
        }

        if (achievement == null)
            achievement = new Achievement();

        SessionClock.init();
        SessionClock.setOnSessionClockTickListener(onSessionClockTickListener);

        MakeLog.info(TAG,"Cell Rank: " + cellRank);

        gamePlayStep1();


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isPausedWithoutDialog) {
            pauseGameWithoutDialog();
        }
    }

    @Override
    public void onBackPressed() {

        if (exitDialog != null) {
            if (exitDialog.isDead()) {
                new PopupExitSession(getWeakContext()).setOnSessionStatusListener(onSessionStatusListener).setOnExitSessionPopupDismissListener(onExitSessionPopupDismissListener).setOnDialogDismissListener(new OnDialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        exitDialog.setDead();
                    }
                }).setOnDialogShowListener(new OnDialogShowListener() {
                    @Override
                    public void onShow() {
                        exitDialog.setLive();
                    }
                }).show();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPausedWithoutDialog)
            resumeGame();
    }

    private void showAdDialog () {

        final CommonSharedData commonSharedData = new CommonSharedData(getWeakContext(), SharedData.SHARED_PREF_TITLE);

        boolean isFirstTime = commonSharedData.isFirstTime();
        boolean gonnaAskForRatings = commonSharedData.getAppRatings() == -0.1f && CurrentCalendar.getCurrentDateData().getDay() % 3 == 0;

        if (!isFirstTime && !gonnaAskForRatings && pauseDialog.isDead()) {
            new PopupShowAdOnGameSession(
                    getWeakContext(),
                    getWeakActivity(),
                    gameType == GameType.DAILY || gameType == GameType.SAVED_DAILY ? PlacementIds.INTERSTITIAL_ON_DAILY_GAME_START_AD : PlacementIds.INTERSTITIAL_ON_GAME_START_AD)
                    .setOnDialogDismissListener(new OnDialogDismissListener() {
                        @Override
                        public void onDismiss() {
                            resumeGame();
                        }
                    })
                    .setOnDialogShowListener(new OnDialogShowListener() {
                        @Override
                        public void onShow() {
                            pauseGameWithoutDialog();
                        }
                    });
        } else {
            MakeLog.info("PopupShowAdOnGameSession", "Conditions are unmet to show up.");
        }

    }

    private Activity getWeakActivity () {
        return new WeakReference<Activity>(GameSessionActivity.this).get();
    }

    private Context getWeakContext () {
        return new WeakReference<Context>(GameSessionActivity.this).get();
    }


    private void buildTooltip (@NonNull Context context, @NonNull View anchorView, @NonNull ViewGroup rootView, @NonNull String message) {

        if (toolTipsManager == null)
            toolTipsManager = new ToolTipsManager();

        ToolTip.Builder builder = new ToolTip.Builder(context, anchorView, rootView, message, ToolTip.POSITION_ABOVE);

        builder.setAlign(ToolTip.ALIGN_CENTER);
        builder.setBackgroundColor(getColor(appColor.getCustomTooltipBackgroundColor()));
        builder.setGravity(ToolTip.GRAVITY_LEFT);

        toolTipsManager.dismissAll();
        toolTipsManager.setAnimationDuration(1000);
        toolTipsManager.show(builder.build());
    }

    private void showAddMoreRowToolTip () {
        buildTooltip(getWeakContext(), addRowFAB, parentCL, "Add more numbers from here");
    }

    private void vibrate () {

        if (LiveGameSettings.GAME_VIBRATION) {
            int vibrationMillisecs = 25;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(vibrationMillisecs, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(vibrationMillisecs);
            }
        }

    }

    private void triggerGameWonView () {

        isGameWon = true;

        ViewAfterWon viewAfterWon = new ViewAfterWon(getWeakContext()).setOnAfterWonControlListener(onAfterWonControlListener);

        if (viewAfterWon != null) {
            viewAfterWon.setData(gameLevel, GameLevelTitle.get(gameLevel), gameStep.getStep(), gameScore.getScore(), SessionClock.getSeconds());
            if (gameType != GameType.GENERAL && gameType != GameType.SAVED_GENERAL)
                viewAfterWon.hideNewSessionButton();
            if (afterWonLayout != null) {
                afterWonLayout.removeAllViews();
                afterWonLayout.addView(viewAfterWon.getMainView());
            }
        }

        if (appBar != null)
            appBar.setVisibility(View.INVISIBLE);

        if (level2TopStatusBarLayout != null)
            level2TopStatusBarLayout.setVisibility(View.INVISIBLE);

        if (bottomControlLayout != null)
            bottomControlLayout.setVisibility(View.INVISIBLE);

        if (bottomStatusLayout != null)
            bottomStatusLayout.setVisibility(View.INVISIBLE);

        if (boardView != null) {
            boardView.disableTextDrawingOnBoard();
            boardView.enableBoardStartAnimation();
        }

        makeViewInvisible(boardView);

    }

    private void gamePlayStep1 () {

        /*
        Hide all layout except linear progress indicator
         */

        isGameStepIncreased = false;
        hasGameStarted = false;
        SessionClock.resetClock();
        gameStep.reset();
        gameScore.reset();
        gameControlStatus.resetRowControl();
        boardView.invalidateAnimation();

        boardView.unlockInvalidate();

        makeViewInvisible(appBar);
        makeViewInvisible(level2TopStatusBarLayout);
        makeViewInvisible(bottomControlLayout);
        makeViewInvisible(bottomStatusLayout);
        afterWonLayout.removeAllViews();
        makeViewInvisible(afterWonLayout);

        makeViewVisible(linearProgressIndicator);



        int allTimeBestScore = sharedData.getAllTimeBestScore(gameLevel);

        if (allTimeBestScore == 0) {
            allTimeBestScoreTV.setVisibility(View.GONE);
        } else {
            allTimeBestScoreTV.setVisibility(View.VISIBLE);
            allTimeBestScoreTV.setText(String.valueOf(allTimeBestScore));
        }

        stepTV.setText(String.valueOf(gameStep.getStep()));
        sessionClockTV.setText(FormattedData.getFormattedTime(SessionClock.getSeconds()));
        scoreTV.setText(String.valueOf(gameScore.getScore()));
        addRowLimitTV.setText(String.valueOf(gameControlStatus.getAddRowStatus()));

        if (isGameWon) {
            if (gameType == GameType.SAVED_GENERAL) {
                gameType = GameType.GENERAL;
            }
        }

        isGameWon = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gamePlayStep2();
            }
        }, GAME_PLAY_LINEAR_PROGRESS_END_TIME);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null)
            soundPool.release();
    }

    private void gamePlayStep2 () {

        /*
        Hide linear progress indicator and only board view visible.
         */

        makeViewGone(linearProgressIndicator);
        makeViewVisible(boardView);

        setFreshMatrixAndBoard();

    }

    private void gamePlayStep3 () {

        makeViewVisible(appBar);
        makeViewVisible(level2TopStatusBarLayout);
        makeViewVisible(bottomControlLayout);
        makeViewVisible(bottomStatusLayout);

        updateBoardItemVisibility();

        hasGameStarted = true;
        boardView.lockInvalidate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getWeakActivity() != null) {
                    if (getWeakActivity().isDestroyed() || getWeakActivity().isFinishing())
                        return;
                    showAdDialog();
                }

            }
        }, 3000);

    }

    private void makeViewVisible (View view) {
        if (view != null) {
            if (view.getVisibility() != View.VISIBLE)
                view.setVisibility(View.VISIBLE);
        }
    }

    private void makeViewGone (View view) {
        if (view != null) {
            if (view.getVisibility() == View.VISIBLE)
                view.setVisibility(View.GONE);
        }
    }

    private void makeViewInvisible (View view) {
        if (view != null) {
            if (view.getVisibility() == View.VISIBLE)
                view.setVisibility(View.INVISIBLE);
        }
    }

    private void setGameStep (int gameStep) {
        if (stepTV != null) {
            stepTV.setText(String.valueOf("Step: " + gameStep));
        }
    }

    private void raiseStepAndLoadNewMatrix () {

        gameStep.increaseStep();
        isGameStepIncreased = true;

        if (achievement != null) {
            if (!achievement.getFirstStep()) {
                achievement.setFirstStep(true);
                sharedData.setAchievement(new Gson().toJson(achievement, Achievement.class), gameLevel);
            }
        }

        gameScore.stepSolved();

        setFreshMatrixAndBoard();

    }

    private void setFreshMatrixAndBoard () {

        sessionLevelTV.setText(GameLevelTitle.get(gameLevel));
        sessionTypeTV.setText(GameTypeTitle.get(gameType));

        boardView.unlockInvalidate();
        boardView.disableTextDrawingOnBoard();

        if (gameType == GameType.GENERAL || gameType == GameType.DAILY) {

            setGameStep(gameStep.getStep());

            SetCollection setCollection = new SetCollection();
            setCollection.setGameLevel(gameLevel);
            setCollection.setLastSetId(sharedData.getSetId());
            setCollection.setLastSetIncrementLevel(sharedData.getSetIncrementId());

            MakeLog.info(TAG, "Saved Set Id: " + sharedData.getSetId() + " Level: " + sharedData.getSetIncrementId());
            MakeLog.info(TAG, "New Set Id: " + setCollection.getSetId() + " Level: " + setCollection.getSetIncrementLevel());

            matrix.resetMatrix();
            matrix.resetSelectedRC();
            matrix.setCollection(setCollection.getSet());

            sharedData.setSetId(setCollection.getSetId());
            sharedData.setSetIncrementId(setCollection.getSetIncrementLevel());

        } else {

            if (!isGameStepIncreased) {

                gameStep.setStep(gameData != null ? gameData.getStep() : 1);
                gameScore.setScore(gameData != null ? gameData.getScore() : 0);
                SessionClock.setSeconds(gameData != null ? gameData.getTime() : 0);

                setGameStep(gameStep.getStep());

                matrix.setGameLevel(gameLevel);

                if (gameData != null) {
                    matrix.setCellsData(new Gson().fromJson(gameData.getData(), Cell[][].class));
                    gameControlStatus.setAddRowStatus(gameData.getRowControlLimit());
                } else {
                    SetCollection setCollection = new SetCollection();
                    setCollection.setGameLevel(gameLevel);
                    setCollection.setLastSetId(sharedData.getSetId());
                    setCollection.setLastSetIncrementLevel(sharedData.getSetIncrementId());

                    MakeLog.info(TAG, "Saved Set Id: " + sharedData.getSetId() + " Level: " + sharedData.getSetIncrementId());
                    MakeLog.info(TAG, "New Set Id: " + setCollection.getSetId() + " Level: " + setCollection.getSetIncrementLevel());

                    matrix.resetMatrix();
                    matrix.resetSelectedRC();
                    matrix.setCollection(setCollection.getSet());

                    sharedData.setSetId(setCollection.getSetId());
                    sharedData.setSetIncrementId(setCollection.getSetIncrementLevel());
                }

            } else {
                SetCollection setCollection = new SetCollection();
                setCollection.setGameLevel(gameLevel);
                setCollection.setLastSetId(sharedData.getSetId());
                setCollection.setLastSetIncrementLevel(sharedData.getSetIncrementId());

                MakeLog.info(TAG, "Saved Set Id: " + sharedData.getSetId() + " Level: " + sharedData.getSetIncrementId());
                MakeLog.info(TAG, "New Set Id: " + setCollection.getSetId() + " Level: " + setCollection.getSetIncrementLevel());

                matrix.resetMatrix();
                matrix.resetSelectedRC();
                matrix.setCollection(setCollection.getSet());

                sharedData.setSetId(setCollection.getSetId());
                sharedData.setSetIncrementId(setCollection.getSetIncrementLevel());
            }

        }

        updateGameBoardStatus();

        boardView.setMatrix(matrix);
        setBoardViewDimensions();

        boardView.setFontSizeFactor(sharedData.getBoardFontSize());

        boardView.enableBoardStartAnimation();
        boardView.enableBoardDrawing();

        PlaySound.now(soundPool, soundId[1]);

    }

    private void setBoardViewDimensions () {


        LinearLayoutCompat.LayoutParams layoutParams;

        if (DeviceType.TYPE == DeviceType.MOBILE) {
            layoutParams = new LinearLayoutCompat.LayoutParams(boardView.getViewWidth(), boardView.getViewHeight());
        } else {
            layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, boardView.getViewHeight());
        }
        boardView.setLayoutParams(layoutParams);

    }

    private void refreshViewsColor () {

        appColor.setTheme(new CommonSharedData(getWeakContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        parentCL.setBackgroundColor(this.getColor(this.appColor.getAppBackgroundColor()));
        appBar.setBackgroundColor(this.getColor(this.appColor.getAppBackgroundColor()));
        backCV.setCardBackgroundColor(this.getColor(this.appColor.getAppBackgroundColor()));
        pauseCV.setCardBackgroundColor(this.getColor(this.appColor.getAppBackgroundColor()));
        themeCV.setCardBackgroundColor(this.getColor(this.appColor.getAppBackgroundColor()));
        settingsCV.setCardBackgroundColor(this.getColor(this.appColor.getAppBackgroundColor()));
        backIV.setImageTintList(ColorStateList.valueOf(this.getColor(this.appColor.getAppBarIconTintColor())));
        pauseIV.setImageTintList(ColorStateList.valueOf(this.getColor(this.appColor.getAppBarIconTintColor())));
        themeIV.setImageTintList(ColorStateList.valueOf(this.getColor(this.appColor.getAppBarIconTintColor())));
        settingsIV.setImageTintList(ColorStateList.valueOf(this.getColor(this.appColor.getAppBarIconTintColor())));
        scoreTV.setTextColor(this.getColor(this.appColor.getGameScoreTextColor()));
        stepTV.setTextColor(this.getColor(this.appColor.getGameStatusBarTextColor()));
        allTimeBestScoreTV.setTextColor(this.getColor(this.appColor.getGameStatusBarTextColor()));
        addRowFAB.setBackgroundTintList(ColorStateList.valueOf(this.getColor(this.appColor.getGameBoardControlCardBackgroundColor())));
        addRowFAB.setImageTintList(ColorStateList.valueOf(this.getColor(this.appColor.getGameBoardControlImgTintColor())));
        addRowLimitTV.setTextColor(this.getColor(this.appColor.getGameBoardControlLimitTextColor()));
        hintFAB.setBackgroundTintList(ColorStateList.valueOf(this.getColor(this.appColor.getGameBoardControlCardBackgroundColor())));
        hintFAB.setImageTintList(ColorStateList.valueOf(this.getColor(this.appColor.getGameBoardControlImgTintColor())));
        hintLimitTV.setTextColor(this.getColor(this.appColor.getGameBoardControlLimitTextColor()));
        sessionTypeTV.setTextColor(this.getColor(this.appColor.getGameStatusBarTextColor()));
        sessionLevelTV.setTextColor(this.getColor(this.appColor.getGameStatusBarTextColor()));
        sessionClockTV.setTextColor(this.getColor(this.appColor.getGameStatusBarTextColor()));
        sessionLevelCV.setCardBackgroundColor(this.getColor(this.appColor.getGameLevelBoardCardBackgroundColor()));

        Drawable limitBgDrawable = this.getDrawable(R.drawable.shape_circle_bg);
        limitBgDrawable.setTintList(ColorStateList.valueOf(this.getColor(this.appColor.getGameBoardControlLimitTextBackgroundColor())));
        addRowLimitTV.setBackground(limitBgDrawable);
        hintLimitTV.setBackground(limitBgDrawable);

        boardView.refreshViewsColor();

    }

    private void updateBoardItemVisibility () {

        if (LiveGameSettings.GAME_SCORE) {
            scoreTV.setVisibility(View.VISIBLE);
        } else {
            scoreTV.setVisibility(View.GONE);
        }

        if (LiveGameSettings.GAME_TIME) {
            sessionClockTV.setVisibility(View.VISIBLE);
        } else {
            sessionClockTV.setVisibility(View.GONE);
        }

    }

    private void updateGameControlStatus () {

        if (addRowLimitTV != null) {
            if (gameControlStatus != null) {
                addRowLimitTV.setText(String.valueOf(gameControlStatus.getAddRowStatus()));
            }
        }

        if (hintLimitTV != null) {
            if (gameControlStatus != null) {
                if (gameControlStatus.isHintControlAvailable())
                    hintLimitTV.setText(String.valueOf(gameControlStatus.getHintStatus()));
            }
        }

    }

    private void updateGameScore () {

        if (scoreTV != null) {
            scoreTV.setText(String.valueOf(gameScore.getScore()));
        }

        if (gameScore.getScoreForAnimation() > 0) {
            boardView.startScoreSizeAnimation(gameScore.getScoreForAnimation());
        }

    }


    private void updateGameStep () {

        if (stepTV != null) {
            stepTV.setText("Step: " + String.valueOf(gameStep.getStep()));
        }

    }

    private void updateGameBoardStatus () {

        updateGameScore();
        updateGameControlStatus();
        updateGameStep();

    }

    private void pauseGameWithoutDialog () {

        // 1. stop the clock
        SessionClock.stop();

        // 2. Save the game only when the clock is not terminated.
        if (!SessionClock.isTerminated() && hasGameStarted)
            saveGame();

        // 3. Disable the board drawing
        if (boardView != null) {
            boardView.disableBoardDrawing();
        }

        isPausedWithoutDialog = true;

    }

    private void resumeGame () {

        SessionClock.resume();
        boardView.enableBoardDrawing();
        isPausedWithoutDialog = false;

    }

    private void pauseGameWithDialog () {

        pauseGameWithoutDialog();

        // 4. Show the game pause dialog
        if ((!GameSessionActivity.this.isFinishing() || !GameSessionActivity.this.isDestroyed()) && !SessionClock.isTerminated()) {
            if (hasGameStarted)
            {

                if (pauseDialog != null) {
                    if (pauseDialog.isDead()) {
                        new PopupGamePause(getWeakContext()).setOnPauseStatusListener(onPauseStatusListener).setData(gameScore.getScore(), gameStep.getStep(), SessionClock.getSeconds(), GameLevelTitle.get(gameLevel))
                                .setOnDialogDismissListener(new OnDialogDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        pauseDialog.setDead();
                                    }
                                }).setOnDialogShowListener(new OnDialogShowListener() {
                            @Override
                            public void onShow() {
                                pauseDialog.setLive();
                            }
                        }).show();
                    }
                }

            }
        }

    }

    private void saveGame () {

        GameDB gameDB = new GameDB(this);

        if (dailyGameRecordId == -1) {

            /*
            -1 record id means the game session either new daily or new general. So first generate it.
             */

            gameDB.addRecord(new GameData(dailyGameRecordId, gameLevel, gameType, SessionClock.getSeconds(), dailyDay, dailyMonth, dailyYear, gameScore.getScore(), isGameWon ? GameCompletion.COMPLETE : GameCompletion.INCOMPLETE, gameStep.getStep(), gameControlStatus.getAddRowStatus(), ""));

            dailyGameRecordId = gameDB.getLastRowId();

        }

        gameDB.updateData(dailyGameRecordId, new GameData(dailyGameRecordId, gameLevel, gameType, SessionClock.getSeconds(), dailyDay, dailyMonth, dailyYear, gameScore.getScore(), isGameWon ? GameCompletion.COMPLETE : GameCompletion.INCOMPLETE, gameStep.getStep(), gameControlStatus.getAddRowStatus(), ""));

        gameDB.closeDB();

        if (gameType == GameType.GENERAL || gameType == GameType.SAVED_GENERAL) {

            if (!isGameWon) {
                String json = new Gson().toJson(new GameData(dailyGameRecordId, gameLevel, gameType, SessionClock.getSeconds(), dailyDay, dailyMonth, dailyYear, gameScore.getScore(), isGameWon ? GameCompletion.COMPLETE : GameCompletion.INCOMPLETE, gameStep.getStep(), gameControlStatus.getAddRowStatus(), new Gson().toJson(matrix.getBoard())));
                new SharedData(getWeakContext()).saveGeneralGame(json);
            } else {
                new SharedData(getWeakContext()).saveGeneralGame(null);
            }

        } else if (gameType == GameType.DAILY || gameType == GameType.SAVED_DAILY) {

            if (!isGameWon) {

                String json = new Gson().toJson(new GameData(dailyGameRecordId, gameLevel, gameType, SessionClock.getSeconds(), dailyDay, dailyMonth, dailyYear, gameScore.getScore(), isGameWon ? GameCompletion.COMPLETE : GameCompletion.INCOMPLETE, gameStep.getStep(), gameControlStatus.getAddRowStatus(), new Gson().toJson(matrix.getBoard())));
                // save the game data to shared preference.
                new SharedData(getWeakContext()).saveDailyGame(json);

            } else {
                new SharedData(getWeakContext()).saveDailyGame(null);
            }

        } else if (gameType == GameType.NEW_DAILY_WITH_DB || gameType == GameType.SAVED_DAILY_FROM_DB) {

            // save only to database. And, check the date, if found match to today's one, then save to shared preference also.

            DateData cDate = CurrentCalendar.getCurrentDateData();

            if (dailyDay == cDate.getDay() && dailyMonth == cDate.getMonth() && dailyYear == cDate.getYear()) {

                if (isGameWon) {
                    new SharedData(getWeakContext()).saveDailyGame(null);
                } else {
                    String json = new Gson().toJson(new GameData(dailyGameRecordId, gameLevel, gameType, SessionClock.getSeconds(), dailyDay, dailyMonth, dailyYear, gameScore.getScore(), isGameWon ? GameCompletion.COMPLETE : GameCompletion.INCOMPLETE, gameStep.getStep(), gameControlStatus.getAddRowStatus(), new Gson().toJson(matrix.getBoard())));
                    new SharedData(getWeakContext()).saveDailyGame(json);
                }

            }

        }

    }

    private void setViewsClickListener () {

        if (addRowFAB != null) {
            addRowFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (gameControlStatus != null && gameControlStatus.isAddRowControlAvailable()) {
                        boardView.unlockInvalidate();
                        boardView.enableCellScanAnimation(matrix.getRCofNewCellsRequired());
                        gameControlStatus.consumeAddMoreRowControl();
                    }

                    updateGameControlStatus();

                }
            });
        }

        if (hintFAB != null) {
            hintFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (gameControlStatus != null && gameControlStatus.isHintControlAvailable()) {

                        hintFAB.setEnabled(false);
                        new CheckPossibleHintMatchInBG().execute();

                    } else {
                        hintLimitTV.setText("Ad");
                        if (SessionClock.isRunning()) {
                            SessionClock.stop();
                        }
                        boardView.disableBoardDrawing();
                        new PopupWatchAdForFeature(getWeakContext()).setActivity(getWeakActivity()).setOnRewardedAdItemAccountListener(onRewardedAdItemAccountListener).show();
                    }

                    updateGameControlStatus();

                }
            });
        }

        if (backCV != null) {
            backCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (exitDialog != null) {
                        if (exitDialog.isDead()) {
                            new PopupExitSession(getWeakContext()).setOnExitSessionPopupDismissListener(onExitSessionPopupDismissListener).setOnSessionStatusListener(onSessionStatusListener).setOnDialogDismissListener(new OnDialogDismissListener() {
                                @Override
                                public void onDismiss() {
                                    exitDialog.setDead();
                                }
                            }).setOnDialogShowListener(new OnDialogShowListener() {
                                @Override
                                public void onShow() {
                                    exitDialog.setLive();
                                }
                            }).show();
                        }
                    } else {
                        finish();
                    }

                }
            });
        }

        if (pauseCV != null) {
            pauseCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pauseGameWithDialog();
                }
            });
        }

        if (themeCV != null) {
            themeCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new PopupThemeControl(getWeakContext())
                            .setOnBoardFontSizeChangeListener(onBoardFontSizeChangeListener)
                            .setOnDialogDismissListener(new OnDialogDismissListener() {
                                @Override
                                public void onDismiss() {
                                    updateBoardItemVisibility();
                                }
                            })
                            .setOnThemeChangeListener(onThemeChangeListener).show(view);

                }
            });
        }

        if (settingsCV != null) {
            settingsCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getWeakContext(), OptionsActivity.class));
                }
            });
        }

    }

    private void triggerGameWonProcedure () {

        isGameWon = true;
        SessionClock.stop();
        saveGame();
        triggerGameWonView();

        if (sharedData.getAllTimeBestScore(gameLevel) < gameScore.getScore()) {
            sharedData.setAllTimeBestScore(gameScore.getScore(), gameLevel);
        }

        if (afterWonLayout.getVisibility() != View.VISIBLE)
            afterWonLayout.setVisibility(View.VISIBLE);

        if (konfettiView != null) {

            if (konfettiView.getVisibility() != View.VISIBLE)
                konfettiView.setVisibility(View.VISIBLE);

            ViewCelebration.start(konfettiView);

            PlaySound.now(soundPool, soundId[3]);

            if (new CommonSharedData(getWeakContext(), SharedData.SHARED_PREF_TITLE).getAppRatings() == CommonSharedData.DEFAULT_FLOAT) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!GameSessionActivity.this.isFinishing() || !GameSessionActivity.this.isDestroyed())
                            new PopupRatings(getWeakContext()).setOnAppRateListener(onAppRateListener).setTextData("Rate us", VariablesControl.APP_STORE_URL).show();
                    }
                }, 3000);

            }

        }


    }

    private void unfreezeBoardAndClockAfterPopupClose () {

        if (boardView != null) {
            boardView.enableBoardDrawing();
        }

        if (!SessionClock.isRunning())
            SessionClock.resume();

    }

    private final OnBoardFontSizeChangeListener onBoardFontSizeChangeListener = new OnBoardFontSizeChangeListener() {
        @Override
        public void onChange(float fontSize) {
            boardView.setFontSizeFactor(fontSize);
        }
    };

    private final OnThemeChangeListener onThemeChangeListener = new OnThemeChangeListener() {
        @Override
        public void onChanged() {
            refreshViewsColor();
        }
    };

    private final OnAfterWonControlListener onAfterWonControlListener = new OnAfterWonControlListener() {
        @Override
        public void onHome() {
            finish();
        }

        @Override
        public void onNewSession() {
            gamePlayStep1();
        }
    };

    private final OnPauseStatusListener onPauseStatusListener = new OnPauseStatusListener() {
        @Override
        public void onExit() {
            SessionClock.terminate();
            boardView.disableBoardDrawing();
            finish();
        }

        @Override
        public void onResume() {
            SessionClock.resume();
            boardView.enableBoardDrawing();
        }
    };

    private final OnExitSessionPopupDismissListener onExitSessionPopupDismissListener = new OnExitSessionPopupDismissListener() {
        @Override
        public void onPopupDismiss() {
            boardView.enableBoardDrawing();
        }
    };

    private final OnSessionStatusListener onSessionStatusListener = new OnSessionStatusListener() {
        @Override
        public void onExit() {
            SessionClock.stop();
            saveGame();
            SessionClock.terminate();
            boardView.disableBoardDrawing();
            finish();
        }

        @Override
        public void onContinue() {
            boardView.enableBoardDrawing();
        }
    };

    private final OnSessionClockTickListener onSessionClockTickListener = new OnSessionClockTickListener() {
        @Override
        public void onTick(int seconds) {
            if (sessionClockTV != null) {
                sessionClockTV.setText(FormattedData.getFormattedTime(seconds));
            }
        }
    };

    private final OnBoardStartAnimationListener onBoardStartAnimationListener = new OnBoardStartAnimationListener() {
        @Override
        public void onFinish() {

            if (isGameWon) {


            } else {

                boardView.enableTextDrawingOnBoard();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gamePlayStep3();
                    }
                }, 700);

                if (!SessionClock.isRunning()) {
                    SessionClock.start();
                }
            }

        }
    };

    private final OnRewardedAdItemAccountListener onRewardedAdItemAccountListener = new OnRewardedAdItemAccountListener() {
        @Override
        public void onItemRewarded(boolean isRewarded) {

            unfreezeBoardAndClockAfterPopupClose();

            if (isRewarded) {
                new SharedData(parentCL.getContext()).setHintControl(GameControlLimit.HINT_LIMIT);
                gameControlStatus.setHintStatus(new SharedData(parentCL.getContext()).getHintControl());
                updateGameControlStatus();
            }

        }

        @Override
        public void onFailed(int errCode) {
            unfreezeBoardAndClockAfterPopupClose();
            if (errCode == 0) {
                new ActionSnackbar().show(parentCL, false, "Perhaps, internet connection isn't working.");
            } else if (errCode == 3) {
                new ActionSnackbar().show(parentCL, false, "Perhaps, it'll be available later.");
            }
        }
    };

    private final OnAppRateListener onAppRateListener = new OnAppRateListener() {
        @Override
        public void onRated() {
            new CommonSharedData(GameSessionActivity.this, SharedData.SHARED_PREF_TITLE).setAppRatings(5.0f);
        }
    };

    private final OnGameScoreChangeListener onGameScoreChangeListener = new OnGameScoreChangeListener() {
        @Override
        public void onChange(int score) {
            updateGameScore();
        }
    };

    private final OnRowRemoveAnimationListener onRowRemoveAnimationListener = new OnRowRemoveAnimationListener() {
        @Override
        public void onFinish() {

            boardView.unlockInvalidate();
            matrix.removeSolvedRow();

            if (achievement != null) {
                if (!achievement.getFirstRow()) {
                    achievement.setFirstRow(true);
                    sharedData.setAchievement(new Gson().toJson(achievement, Achievement.class), gameLevel);
                }
            }

            setBoardViewDimensions();
            boardView.invalidate();
            boardView.singleLockInvalidate();

            if (gameScore != null) {
                gameScore.rowSolved();
            }

            new CheckPossibleMatchInBG().execute();

        }
    };

    private final OnCellSelectListener onCellSelectListener = new OnCellSelectListener() {
        @Override
        public void onSelected(boolean isSolved, boolean hasGap, boolean shouldAnimateOnNoSolution) {
            MakeLog.info(TAG, "Status: " + (isSolved ? "Solved" : "Unsolved") + " ShouldAnimate: " + (shouldAnimateOnNoSolution ? "Animate" : "No Animation"));

            if (isSolved) {

                boardView.unsetForHint();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        matrix.solveSelectedCells();
                        matrix.resetSelectedRC();

                        if (gameScore != null) {
                            if (hasGap) {
                                gameScore.solvedWithGap();
                            } else {
                                gameScore.solvedWithoutGap();
                            }
                        }

                        ArrayList<CellRC> cellRCArrayList = matrix.getRemoveEligibleRowCellsData();
                        if (cellRCArrayList != null && cellRCArrayList.size() > 0) {
                            MakeLog.info(TAG, "RemovalRowArr size: " + cellRCArrayList.size());
                            boardView.enableRowRemoveAnimation(cellRCArrayList);
                            PlaySound.now(soundPool, soundId[2]);
                        } else {
                            new CheckPossibleMatchInBG().execute();
                        }



                    }
                }, 200);

                if (achievement != null) {
                    if (!achievement.getFirstNumber()) {
                        achievement.setFirstNumber(true);
                        sharedData.setAchievement(new Gson().toJson(achievement, Achievement.class), gameLevel);
                    }
                }

            } else if (shouldAnimateOnNoSolution) {

                vibrate();

                MakeLog.info(TAG, "Enabling animation");

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        boardView.enableCellOpacityAnimation();
                    }
                });

                new CheckPossibleMatchInBG().execute();

            } else {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vibrate();
                        matrix.resetSelectedRC();
                        new CheckPossibleMatchInBG().execute();
                        boardView.invalidate();
                    }
                }, 200);

            }

        }
    };

    private class CheckPossibleHintMatchInBG extends AsyncTask<Void, Void, ArrayList<CellRC>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MakeLog.info(TAG, "BEGIN: Check Possible Match");
        }

        @Override
        protected void onPostExecute(ArrayList<CellRC> cellRCArrayList) {

            super.onPostExecute(cellRCArrayList);

            hintFAB.setEnabled(true);

            if (cellRCArrayList == null) {

                MakeLog.info(TAG, "No Cell match.");

                gameControlStatus.consumeHintControl();
                sharedData.setHintControl(gameControlStatus.getHintStatus());

                if (gameControlStatus.isAddRowControlAvailable()) {
                    showAddMoreRowToolTip();
                }

                if (!gameControlStatus.isHintControlAvailable()) {
                    hintLimitTV.setText("Ad");
                }

            } else {

                gameControlStatus.consumeHintControl();
                sharedData.setHintControl(gameControlStatus.getHintStatus());
                if (!gameControlStatus.isHintControlAvailable()) {
                    hintLimitTV.setText("Ad");
                }

                int row1 = cellRCArrayList.get(0).getRow();
                int col1 = cellRCArrayList.get(0).getCol();
                int row2 = cellRCArrayList.get(1).getRow();
                int col2 = cellRCArrayList.get(1).getCol();

                matrix.selectRCForTutor(row1+1, col1+1, row2+1, col2+1);
                boardView.setForHint();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        boardView.invalidate();
                    }
                });

            }

            updateGameControlStatus();


        }

        @Override
        protected ArrayList<CellRC> doInBackground(Void... voids) {

            ArrayList<CellRC> cellRCArrayList;

            try {
                cellRCArrayList = matrix.getSinglePossibleMatch();
            } catch (Exception e) {
                return null;
            }

            return cellRCArrayList;

        }
    }

    private class CheckPossibleMatchInBG extends AsyncTask<Void, Void, ArrayList<CellRC>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MakeLog.info(TAG, "BEGIN: Check Possible Match");
        }

        @Override
        protected void onPostExecute(ArrayList<CellRC> cellRCArrayList) {

            super.onPostExecute(cellRCArrayList);

            if (cellRCArrayList == null) {

                MakeLog.info(TAG, "No Cell match.");

                if (!gameControlStatus.isAddRowControlAvailable())  {

                    if (matrix.isWholeMatrixSolved()) {

                        raiseStepAndLoadNewMatrix();

                    } else {

                        isGameWon = true;

                        MakeLog.info(TAG, "Session Completed.");
                        if (achievement != null) {
                            if (!achievement.getFirstSession()) {
                                achievement.setFirstSession(true);
                                sharedData.setAchievement(new Gson().toJson(achievement, Achievement.class), gameLevel);
                            }
                        }

                        new ChangeGameStatus().markCompleted(GameSessionActivity.this, com.fourshape.numbermatch.custom_calender.utils.CurrentCalendar.getCurrentDateData(), gameLevel, cellRank);

                        triggerGameWonProcedure();

                    }


                } else {

                    if (matrix.isWholeMatrixSolved()) {
                        raiseStepAndLoadNewMatrix();
                    }

                }

            } else {

                MakeLog.info(TAG, "Match at Cell-1: R: " + cellRCArrayList.get(0).getRow() + " C: " + cellRCArrayList.get(0).getCol());
                MakeLog.info(TAG, "Match at Cell-1: R: " + cellRCArrayList.get(1).getRow() + " C: " + cellRCArrayList.get(1).getCol());

            }


        }

        @Override
        protected ArrayList<CellRC> doInBackground(Void... voids) {

            ArrayList<CellRC> cellRCArrayList;

            try {
                cellRCArrayList = matrix.getSinglePossibleMatch();
            } catch (Exception e) {
                return null;
            }

            return cellRCArrayList;

        }
    }

}