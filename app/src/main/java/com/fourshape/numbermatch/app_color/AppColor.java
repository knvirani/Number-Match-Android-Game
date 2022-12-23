package com.fourshape.numbermatch.app_color;

import com.fourshape.easythingslib.utils.AppThemes;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.puzzle_core.GameLevel;

public class AppColor {

    private int themeId;

    public AppColor () {
        themeId = AppThemes.DAY;
    }

    public void setTheme(int themeId) {
        this.themeId = themeId;
    }

    public int getDailyChallengeDateColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_challenge_date_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_challenge_date_text;

        return R.color.default_daily_challenge_date_text;

    }

    public int getDailyChallengeTitleColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_challenge_title_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_challenge_title_text;

        return R.color.default_daily_challenge_title_text;

    }

    public int getDailyShapeLevelBorderColor (int gameLevel) {

        if (themeId == AppThemes.DAY) {

            if (gameLevel == GameLevel.EASY)
                return R.color.default_easy_level_shape_border;
            else if (gameLevel == GameLevel.MEDIUM)
                return R.color.default_medium_level_shape_border;
            else if (gameLevel == GameLevel.HARD)
                return R.color.default_hard_level_shape_border;
            else if (gameLevel == GameLevel.START_POINT)
                return R.color.default_start_point_cell_border;
            else
                return R.color.default_untouched_cell_border;

        } else if (themeId == AppThemes.DARK) {

            if (gameLevel == GameLevel.EASY)
                return R.color.theme_1_easy_level_shape_border;
            else if (gameLevel == GameLevel.MEDIUM)
                return R.color.theme_1_medium_level_shape_border;
            else if (gameLevel == GameLevel.HARD)
                return R.color.theme_1_hard_level_shape_border;
            else if (gameLevel == GameLevel.START_POINT)
                return R.color.theme_1_start_point_cell_border;
            else
                return R.color.theme_1_untouched_cell_border;

        }

        if (gameLevel == GameLevel.EASY)
            return R.color.default_easy_level_shape_border;
        else if (gameLevel == GameLevel.MEDIUM)
            return R.color.default_medium_level_shape_border;
        else if (gameLevel == GameLevel.HARD)
            return R.color.default_hard_level_shape_border;
        else if (gameLevel == GameLevel.START_POINT)
            return R.color.default_start_point_cell_border;
        else
            return R.color.default_untouched_cell_border;

    }

    public int getDailyShapeLevelBackgroundColor (int gameLevel) {

        if (themeId == AppThemes.DAY) {

            if (gameLevel == GameLevel.EASY)
                return R.color.default_easy_level_shape_bg;
            else if (gameLevel == GameLevel.MEDIUM)
                return R.color.default_medium_level_shape_bg;
            else if (gameLevel == GameLevel.HARD)
                return R.color.default_hard_level_shape_bg;
            else if (gameLevel == GameLevel.START_POINT)
                return R.color.default_start_point_cell_bg;
            else
                return R.color.default_untouched_cell_bg;

        } else if (themeId == AppThemes.DARK) {

            if (gameLevel == GameLevel.EASY)
                return R.color.theme_1_easy_level_shape_bg;
            else if (gameLevel == GameLevel.MEDIUM)
                return R.color.theme_1_medium_level_shape_bg;
            else if (gameLevel == GameLevel.HARD)
                return R.color.theme_1_hard_level_shape_bg;
            else if (gameLevel == GameLevel.START_POINT)
                return R.color.theme_1_start_point_cell_bg;
            else
                return R.color.theme_1_untouched_cell_bg;

        }

        if (gameLevel == GameLevel.EASY)
            return R.color.default_easy_level_shape_bg;
        else if (gameLevel == GameLevel.MEDIUM)
            return R.color.default_medium_level_shape_bg;
        else if (gameLevel == GameLevel.HARD)
            return R.color.default_hard_level_shape_bg;
        else if (gameLevel == GameLevel.START_POINT)
            return R.color.default_start_point_cell_bg;
        else
            return R.color.default_untouched_cell_bg;

    }

    public int getTappedCellTextColor () {
        if (themeId == AppThemes.DAY)
            return R.color.default_tapped_cell_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_tapped_cell_text;

        return R.color.default_tapped_cell_text;
    }

    public int getTappedCellBorderColor () {
        if (themeId == AppThemes.DAY)
            return R.color.default_tapped_cell_border;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_tapped_cell_border;

        return R.color.default_tapped_cell_border;
    }

    public int getTappedCellBackgroundColor () {
        if (themeId == AppThemes.DAY)
            return R.color.default_tapped_cell_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_tapped_cell_bg;

        return R.color.default_tapped_cell_bg;
    }

    public int getTouchedCellTextColor () {
        if (themeId == AppThemes.DAY)
            return R.color.default_touched_cell_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_touched_cell_text;

        return R.color.default_touched_cell_text;
    }

    public int getUntouchedCellTextColor () {
        if (themeId == AppThemes.DAY)
            return R.color.default_untouched_cell_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_untouched_cell_text;

        return R.color.default_untouched_cell_text;
    }

    public int getTouchedCellConnectorLineColor () {
        if (themeId == AppThemes.DAY)
            return R.color.default_touched_cell_connector_line;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_touched_cell_connector_line;

        return R.color.default_touched_cell_connector_line;
    }

    public int getUntouchedCellConnectorLineColor () {
        if (themeId == AppThemes.DAY)
            return R.color.default_untouched_cell_connector_line;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_untouched_cell_connector_line;

        return R.color.default_untouched_cell_connector_line;
    }

    public int getUntouchedCellBackgroundColor () {
        if (themeId == AppThemes.DAY)
            return R.color.default_untouched_cell_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_untouched_cell_bg;

        return R.color.default_untouched_cell_bg;
    }

    public int getUntouchedCellBorderColor () {
        if (themeId == AppThemes.DAY)
            return R.color.default_untouched_cell_border;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_untouched_cell_border;

        return R.color.default_untouched_cell_border;
    }

    public int getCustomTooltipBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_custom_tooltip_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_custom_tooltip_bg;

        return R.color.default_custom_tooltip_bg;

    }

    public int getButtonStateCheckedTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_primary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_home_page_primary_btn_text;

        return R.color.default_primary_btn_text;

    }

    public int getButtonStateDefaultTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_secondary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_bar_text;

        return R.color.default_popup_secondary_btn_text;

    }

    public int getButtonStateCheckedBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_btn_state_checked;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_btn_state_checked;

        return R.color.default_btn_state_checked;

    }

    public int getButtonStateDefaultBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_btn_state_default;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_btn_state_default;

        return R.color.default_btn_state_default;

    }

    public int getErrorBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_error_item_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_error_item_bg;

        return R.color.default_error_item_bg;

    }

    public int getErrorTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_error_item_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_error_item_text;

        return R.color.default_error_item_text;

    }

    public int getSuccessTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_success_item_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_success_item_text;

        return R.color.default_success_item_text;

    }

    public int getSuccessBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_success_item_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_success_item_bg;

        return R.color.default_success_item_bg;

    }

    public int getSwitchTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_switch_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_switch_text;

        return R.color.default_switch_text;
    }

    public int getSwitchOnTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_switch_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_switch_tint;

        return R.color.default_switch_tint;
    }

    public int getSwitchTrackUncheckedColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_switch_track_normal;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_switch_track_normal;


        return R.color.default_switch_track_normal;
    }

    public int getSwitchTrackCheckedColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_switch_track_checked;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_switch_track_checked;

        return R.color.default_switch_track_checked;
    }

    public int getSwitchSuggestionTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_switch_suggestion_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_switch_suggestion_text;


        return R.color.default_switch_suggestion_text;
    }


    public int getResultPageScoreTitleColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_result_page_score_title;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_result_page_score_title;

        return R.color.default_result_page_score_title;

    }

    public int getResultPageScoreValueColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_result_page_score_value;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_result_page_score_value;

        return R.color.default_result_page_score_value;

    }

    public int getCelebrationBlueColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_celebration_blue;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_celebration_blue;

        return R.color.default_celebration_blue;

    }

    public int getCelebrationYellowColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_celebration_yellow;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_celebration_yellow;

        return R.color.default_celebration_yellow;

    }

    public int getCelebrationPinkColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_celebration_pink;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_celebration_pink;

        return R.color.default_celebration_pink;

    }

    public int getCelebrationGreenColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_celebration_green;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_celebration_green;

        return R.color.default_celebration_green;

    }

    public int getCelebrationViolentColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_celebration_violent;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_celebration_violent;

        return R.color.default_celebration_violent;

    }

    public int getTutorCompletedTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_tc_complete_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_tc_complete_text;

        return R.color.default_tc_complete_text;

    }

    public int getTutorSelectedCellBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_board_tutor_selected_cell_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_board_tutor_selected_cell_bg;

        return R.color.default_game_board_tutor_selected_cell_bg;

    }

    public int getTutorSelectedCellTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_board_tutor_selected_cell_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_board_tutor_selected_cell_text;

        return R.color.default_game_board_tutor_selected_cell_text;

    }

    public int getHowtoplayStepHeaderTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_howtoplay_step_header_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_howtoplay_step_header_text;

        return R.color.default_howtoplay_step_header_text;

    }

    public int getDynamicStatsCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_cv_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_cv_bg;

        return R.color.default_stats_cv_bg;
    }

    public int getDynamicStatsTitleTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_title_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_title_text;

        return R.color.default_stats_title_text;
    }

    public int getDynamicStatsTitleBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_title_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_title_bg;


        return R.color.default_stats_title_bg;
    }

    public int getDynamicStatsValueTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_value_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_value_text;

        return R.color.default_stats_value_text;
    }

    public int getStatsTabIndicatorColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_tab_indicator;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_tab_indicator;


        return R.color.default_stats_tab_indicator;
    }

    public int getStatsTabTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_stats_tab_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_stats_tab_text;


        return R.color.default_stats_tab_text;
    }

    public int getGameSessionLinearProgressIndicatorColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_session_linear_progress_indicator;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_session_linear_progress_indicator;

        return R.color.default_game_session_linear_progress_indicator;
    }

    public int getGameSessionLinearProgressTrackColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_session_linear_progress_track;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_session_linear_progress_track;

        return R.color.default_game_session_linear_progress_track;
    }

    public int getBottomNavigationItemActiveTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_bottom_nav_item_active_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_bottom_nav_item_active_tint;

        return R.color.default_bottom_nav_item_active_tint;
    }

    public int getBottomNavigationBarDividerBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_bottom_navigation_bar_divider;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_bottom_navigation_bar_divider;


        return R.color.default_bottom_navigation_bar_divider;
    }

    public int getPopupPrimaryButtonBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_primary_btn_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_primary_btn_bg;

        return R.color.default_primary_btn_bg;
    }

    public int getPopupPrimaryButtonTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_primary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_primary_btn_text;


        return R.color.default_primary_btn_text;
    }

    public int getPopupSecondaryButtonBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_secondary_btn_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_secondary_btn_bg;

        return R.color.default_secondary_btn_bg;
    }

    public int getPopupSecondaryButtonTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_secondary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_secondary_btn_text;


        return R.color.default_secondary_btn_text;
    }

    public int getPopupTitleTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_title;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_popup_title;

        return R.color.default_popup_title;
    }

    public int getPopupBodyTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_body_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_popup_body_text;

        return R.color.default_popup_body_text;
    }

    public int getPrimaryBtnBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_primary_btn_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_primary_btn_bg;

        return R.color.default_primary_btn_bg;
    }

    public int getPrimaryBtnTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_primary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_primary_btn_text;


        return R.color.default_primary_btn_text;
    }


    public int getDailyPageMonthTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_month_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_month_text;


        return R.color.default_daily_month_text;
    }

    public int getDailyPageYearTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_year_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_year_text;


        return R.color.default_daily_year_text;
    }

    public int getDifficultyLevelTitleColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_difficulty_level_title;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_difficulty_level_title;


        return R.color.default_difficulty_level_title;
    }

    public int getProgressCircularIndicatorColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_circular_progress_indicator;
         else if (themeId == AppThemes.DARK)
            return R.color.theme_1_circular_progress_indicator;

        return R.color.default_circular_progress_indicator;
    }


    public int getPopupCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_popup_card_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_popup_card_bg;

        return R.color.default_popup_card_bg;
    }

    public int getEasyLevelCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_easy_level_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_easy_level_bg;

        return R.color.default_easy_level_bg;
    }

    public int getMediumLevelCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_medium_level_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_medium_level_bg;

        return R.color.default_medium_level_bg;
    }

    public int getHardLevelCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_hard_level_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_hard_level_bg;

        return R.color.default_hard_level_bg;

    }

    public int getGameLevelBoardCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_level_card_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_level_card_bg;

        return R.color.default_game_level_card_bg;
    }

    public int getAppBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_app_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_bg;


        return R.color.default_app_bg;
    }

    public int getAppBarBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_app_bar_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_bar_bg;

        return R.color.default_app_bar_bg;
    }

    public int getAppBarIconTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_app_bar_icon_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_bar_icon_tint;

        return R.color.default_app_bar_icon_tint;
    }

    public int getAppBarTitleTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_app_bar_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_bar_text;

        return R.color.default_app_bar_text;
    }

    public int getAppBarSubTitleTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_app_bar_subtitle_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_bar_subtitle_text;

        return R.color.default_app_bar_subtitle_text;
    }

    public int getBoardBorderColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_board_border;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_board_border;

        return R.color.default_board_border;
    }

    public int getRowColBorderColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_row_col_border;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_row_col_border;

        return R.color.default_row_col_border;
    }

    public int getInactiveCellFillColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_inactive_fill;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_inactive_fill;

        return R.color.default_cell_inactive_fill;
    }

    public int getSelectedCellFillColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_selected_fill;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_selected_fill;

        return R.color.default_cell_selected_fill;
    }

    public int getUnsolvedCellTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_unsolved_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_unsolved_text;

        return R.color.default_cell_unsolved_text;
    }

    public int getSolvedCellTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_solved_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_solved_text;

        return R.color.default_cell_solved_text;
    }

    public int getCellScanAnimationColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_animation_fill;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_animation_fill;

        return R.color.default_cell_animation_fill;
    }

    public int getPointsAnimationTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_cell_points_animation;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_cell_points_animation;

        return R.color.default_cell_points_animation;
    }

    public int getGameStatusBarTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_steps_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_steps_text;

        return R.color.default_game_steps_text;
    }

    public int getGameScoreTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_score_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_score_text;

        return R.color.default_game_score_text;
    }

    public int getNormalDividerColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_normal_divider;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_normal_divider;

        return R.color.default_normal_divider;
    }

    public int getContentDividerColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_content_divider;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_content_divider;

        return R.color.default_content_divider;
    }

    public int getOptionMenuTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_options_menu_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_options_menu_text;

        return R.color.default_options_menu_text;
    }

    public int getGameBoardControlCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_board_control_cv_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_board_control_cv_bg;

        return R.color.default_game_board_control_cv_bg;
    }

    public int getGameBoardControlImgTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_board_control_img_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_board_control_img_tint;

        return R.color.default_game_board_control_img_tint;
    }

    public int getGameBoardControlLimitTextBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_board_control_limit_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_board_control_limit_bg;

        return R.color.default_game_board_control_limit_bg;
    }

    public int getGameBoardControlLimitTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_game_board_control_limit_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_game_board_control_limit_text;


        return R.color.default_game_board_control_limit_text;
    }

    public int getHomeAppTitleColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_app_home_title;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_app_home_title;

        return R.color.default_app_home_title;
    }

    public int getHomeDailyPlayCardBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_play_cv_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_play_cv_bg;


        return R.color.default_daily_play_cv_bg;
    }

    public int getHomeDailyDayTextBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_day_ll_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_day_ll_bg;


        return R.color.default_daily_day_ll_bg;
    }

    public int getHomeDailyDayTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_day_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_day_text;

        return R.color.default_daily_day_text;
    }

    public int getHomeDailyMonthTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_daily_month_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_daily_month_text;

        return R.color.default_daily_month_text;
    }

    public int getHomeDailyPlayRightArrowTintColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_home_page_right_arrow_tint;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_home_page_right_arrow_tint;

        return R.color.default_home_page_right_arrow_tint;
    }

    public int getHomePrimaryBtnBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_home_page_primary_btn_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_home_page_primary_btn_bg;

        return R.color.default_home_page_primary_btn_bg;
    }

    public int getHomePrimaryBtnTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_home_page_primary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_home_page_primary_btn_text;


        return R.color.default_home_page_primary_btn_text;
    }

    public int getHomeSecondaryBtnBackgroundColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_home_page_secondary_btn_bg;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_home_page_secondary_btn_bg;


        return R.color.default_home_page_secondary_btn_bg;
    }

    public int getHomeSecondaryBtnTextColor () {

        if (themeId == AppThemes.DAY)
            return R.color.default_home_page_secondary_btn_text;
        else if (themeId == AppThemes.DARK)
            return R.color.theme_1_home_page_secondary_btn_text;

        return R.color.default_home_page_secondary_btn_text;
    }


}
