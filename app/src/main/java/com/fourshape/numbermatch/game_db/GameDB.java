package com.fourshape.numbermatch.game_db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.fourshape.numbermatch.puzzle_core.structure.GameData;
import com.fourshape.numbermatch.puzzle_core.structure.MainType;
import com.fourshape.numbermatch.utils.MakeLog;

import java.util.ArrayList;

public class GameDB extends SQLiteOpenHelper {

    private final static String TAG = "GameDB";
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "gamedata.db";

    private final static String TABLE_CREATE_SQL = "CREATE TABLE " + DbTables.GAMES_TABLE + " "
            + "( " + DbCols.ID + " INTEGER PRIMARY KEY,"
            + DbCols.LEVEL + " INTEGER,"
            + DbCols.MAIN_TYPE + " INTEGER,"
            + DbCols.DAY + " INTEGER,"
            + DbCols.MONTH + " INTEGER,"
            + DbCols.YEAR + " INTEGER,"
            + DbCols.COMPLETION + " INTEGER,"
            + DbCols.SCORE + " INTEGER,"
            + DbCols.STEP + " INTEGER,"
            + DbCols.TIME + " INTEGER,"
            + DbCols.ROW_CONTROL_LIMIT + " INTEGER,"
            + DbCols.GAME_DATA + " TEXT )";

    public GameDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbTables.GAMES_TABLE);
        onCreate(sqLiteDatabase);
    }

    @SuppressLint("Range")
    public int getLastRowId () {

        String SQL = "SELECT " + DbCols.ID + " FROM " + DbTables.GAMES_TABLE + " ORDER BY " + DbCols.ID + " DESC LIMIT 1";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(SQL, null);

        if (cursor == null) {
            MakeLog.error(TAG, "NULL Cursor.");
            database.close();
            return -1;
        }

        if (cursor.getCount() == 0) {
            MakeLog.error(TAG, "0 Record found.");
            cursor.close();
            database.close();
            return -1;
        }

        try {

            cursor.moveToFirst();

            int Id = -1;
            while (!cursor.isAfterLast()) {
                Id = cursor.getInt(cursor.getColumnIndex(DbCols.ID));
                cursor.moveToNext();
            }

            return Id;

        } catch (Exception e) {
            MakeLog.exception(e);
        } finally {

            cursor.close();
            database.close();
        }

        return -1;

    }


    @SuppressLint("Range")
    public String getGameSessionData (int refId) {

        if (refId == -1)
        {
            MakeLog.info(TAG, "Ref Id: " + refId);
            return null;
        }

        String FETCH_SQL = "SELECT " + DbCols.GAME_DATA + " FROM " + DbTables.GAMES_TABLE + " WHERE " + DbCols.ID + "=" + refId;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(FETCH_SQL, null);

        if (cursor == null) {
            MakeLog.error(TAG, "NULL Cursor in Game Session retrieval.");
            database.close();
            return null;
        }

        if (cursor.getCount() == 0) {
            MakeLog.error(TAG, "0 Record found in Game Session retrieval.");
            cursor.close();
            database.close();
            return null;
        }

        try {

            cursor.moveToFirst();

            String data = "";

            while (!cursor.isAfterLast()) {

                data = cursor.getString(cursor.getColumnIndex(DbCols.GAME_DATA));

                cursor.moveToNext();

            }

            return data;

        } catch (Exception e) {
            MakeLog.exception(e);
        } finally {

            cursor.close();
            database.close();
        }

        return null;

    }

    public void closeDB () {
        this.close();
    }

    @SuppressLint("Range")
    public GameData getFullRecordWithSessionData (int refId) {

        GameData gameData = null;

        String RECORD_FETCH_QUERY = "SELECT " + DbCols.ID  + ", " + DbCols.DAY + ", " + DbCols.MONTH + ", " + DbCols.YEAR + ", " + DbCols.COMPLETION + ", " + DbCols.TIME + ", " + DbCols.MAIN_TYPE + ", " + DbCols.SCORE + ", " + DbCols.LEVEL + ", " + DbCols.STEP + ", " + DbCols.GAME_DATA + ", " + DbCols.ROW_CONTROL_LIMIT + " FROM " + DbTables.GAMES_TABLE + " WHERE " + DbCols.ID + "=" + refId + " LIMIT 1";

        SQLiteDatabase database = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(RECORD_FETCH_QUERY, null);

        if (cursor == null) {
            MakeLog.error(TAG, "NULL Cursor.");
            database.close();
            return null;
        }

        if (cursor.getCount() == 0) {
            MakeLog.error(TAG, "0 Record found.");
            cursor.close();
            database.close();
            return null;
        }

        try {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                int id = cursor.getInt(cursor.getColumnIndex(DbCols.ID));
                int day = cursor.getInt(cursor.getColumnIndex(DbCols.DAY));
                int month = cursor.getInt(cursor.getColumnIndex(DbCols.MONTH));
                int year = cursor.getInt(cursor.getColumnIndex(DbCols.YEAR));
                int completion = cursor.getInt(cursor.getColumnIndex(DbCols.COMPLETION));
                int score = cursor.getInt(cursor.getColumnIndex(DbCols.SCORE));
                int time = cursor.getInt(cursor.getColumnIndex(DbCols.TIME));
                int step = cursor.getInt(cursor.getColumnIndex(DbCols.STEP));
                int mainType = cursor.getInt(cursor.getColumnIndex(DbCols.MAIN_TYPE));
                int level = cursor.getInt(cursor.getColumnIndex(DbCols.LEVEL));
                int rowControlLimit = cursor.getInt(cursor.getColumnIndex(DbCols.ROW_CONTROL_LIMIT));
                String data = cursor.getString(cursor.getColumnIndex(DbCols.GAME_DATA));

                gameData = new GameData(id, level, mainType, time, day, month, year, score, completion, step, rowControlLimit, data);

                cursor.moveToNext();

            }

            return gameData;

        } catch (Exception e) {
            MakeLog.exception(e);
        } finally {

            cursor.close();
            database.close();
        }

        return null;

    }


    @SuppressLint("Range")
    public ArrayList<GameData> getRecordsPerLevelWithoutSessionData (int gameLevel) {

        ArrayList<GameData> gameData = null;

        String RECORD_FETCH_QUERY = "SELECT " + DbCols.ID  + ", " + DbCols.DAY + ", " + DbCols.MONTH + ", " + DbCols.YEAR + ", " + DbCols.COMPLETION + ", " + DbCols.TIME + ", " + DbCols.MAIN_TYPE + ", " + DbCols.SCORE + ", " + DbCols.LEVEL + ", " + DbCols.STEP + ", " + DbCols.ROW_CONTROL_LIMIT  + " FROM " + DbTables.GAMES_TABLE + " WHERE " + DbCols.LEVEL + "=" + gameLevel;

        SQLiteDatabase database = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(RECORD_FETCH_QUERY, null);

        if (cursor == null) {
            MakeLog.error(TAG, "NULL Cursor.");
            database.close();
            return null;
        }

        if (cursor.getCount() == 0) {
            MakeLog.error(TAG, "0 Record found.");
            cursor.close();
            database.close();
            return null;
        }

        try {

            gameData = new ArrayList<>();

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                int id = cursor.getInt(cursor.getColumnIndex(DbCols.ID));
                int day = cursor.getInt(cursor.getColumnIndex(DbCols.DAY));
                int month = cursor.getInt(cursor.getColumnIndex(DbCols.MONTH));
                int year = cursor.getInt(cursor.getColumnIndex(DbCols.YEAR));
                int completion = cursor.getInt(cursor.getColumnIndex(DbCols.COMPLETION));
                int score = cursor.getInt(cursor.getColumnIndex(DbCols.SCORE));
                int time = cursor.getInt(cursor.getColumnIndex(DbCols.TIME));
                int step = cursor.getInt(cursor.getColumnIndex(DbCols.STEP));
                int mainType = cursor.getInt(cursor.getColumnIndex(DbCols.MAIN_TYPE));
                int level = cursor.getInt(cursor.getColumnIndex(DbCols.LEVEL));
                int rowControlLimit = cursor.getInt(cursor.getColumnIndex(DbCols.ROW_CONTROL_LIMIT));

                if (gameData != null) {
                    gameData.add(new GameData(id, level, mainType, time, day, month, year, score, completion, step, rowControlLimit, null));
                }

                cursor.moveToNext();

            }

            return gameData;

        } catch (Exception e) {
            MakeLog.exception(e);
        } finally {

            cursor.close();
            database.close();
        }

        return gameData;

    }

    /*
    Following function is only for daily challenge data.
     */



    @SuppressLint("Range")
    public ArrayList<GameData> getDailyGameRecordsWithoutSessionData () {

        ArrayList<GameData> gameData = null;

        String RECORD_FETCH_QUERY = "SELECT " + DbCols.ID  + ", " + DbCols.DAY + ", " + DbCols.MONTH + ", " + DbCols.YEAR + ", " + DbCols.COMPLETION + ", " + DbCols.TIME + ", " + DbCols.LEVEL + ", " + DbCols.MAIN_TYPE + ", " + DbCols.SCORE + ", " + DbCols.STEP + ", " + DbCols.ROW_CONTROL_LIMIT  + " FROM " + DbTables.GAMES_TABLE + " WHERE " + DbCols.MAIN_TYPE + "=" + MainType.DAILY_GAME;

        SQLiteDatabase database = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(RECORD_FETCH_QUERY, null);

        if (cursor == null) {
            MakeLog.error(TAG, "NULL Cursor.");
            database.close();
            return null;
        }

        if (cursor.getCount() == 0) {
            MakeLog.error(TAG, "0 Record found.");
            cursor.close();
            database.close();
            return null;
        }

        try {

            gameData = new ArrayList<>();

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                int id = cursor.getInt(cursor.getColumnIndex(DbCols.ID));
                int day = cursor.getInt(cursor.getColumnIndex(DbCols.DAY));
                int month = cursor.getInt(cursor.getColumnIndex(DbCols.MONTH));
                int year = cursor.getInt(cursor.getColumnIndex(DbCols.YEAR));
                int completion = cursor.getInt(cursor.getColumnIndex(DbCols.COMPLETION));
                int score = cursor.getInt(cursor.getColumnIndex(DbCols.SCORE));
                int time = cursor.getInt(cursor.getColumnIndex(DbCols.TIME));
                int step = cursor.getInt(cursor.getColumnIndex(DbCols.STEP));
                int mainType = cursor.getInt(cursor.getColumnIndex(DbCols.MAIN_TYPE));
                int level = cursor.getInt(cursor.getColumnIndex(DbCols.LEVEL));
                int rowControlLimit = cursor.getInt(cursor.getColumnIndex(DbCols.ROW_CONTROL_LIMIT));

                if (gameData != null) {
                    gameData.add(new GameData(id, level, mainType, time, day, month, year, score, completion, step, rowControlLimit, null));
                }

                cursor.moveToNext();

            }

            return gameData;

        } catch (Exception e) {
            MakeLog.exception(e);
        } finally {

            cursor.close();
            database.close();
        }

        return gameData;

    }

    public void updateData (int refId, GameData gameData) {

        if (gameData == null)
            return;

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCols.DAY, gameData.getDay());
        contentValues.put(DbCols.MONTH, gameData.getMonth());
        contentValues.put(DbCols.YEAR, gameData.getYear());
        contentValues.put(DbCols.COMPLETION, gameData.getCompletion());
        contentValues.put(DbCols.LEVEL, gameData.getLevel());
        contentValues.put(DbCols.SCORE, gameData.getScore());
        contentValues.put(DbCols.STEP, gameData.getStep());
        contentValues.put(DbCols.TIME, gameData.getTime());
        contentValues.put(DbCols.MAIN_TYPE, gameData.getMainType());
        contentValues.put(DbCols.GAME_DATA, gameData.getData());
        contentValues.put(DbCols.ROW_CONTROL_LIMIT, gameData.getRowControlLimit());

        SQLiteDatabase db = this.getWritableDatabase();
        long status = db.update(DbTables.GAMES_TABLE, contentValues, DbCols.ID + " = ?", new String[]{String.valueOf(refId)});

        MakeLog.info(TAG, "Record Update Status: " + status);

        db.close();

    }

    public void addRecord (GameData gameData) {

        if (gameData == null)
            return;

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCols.DAY, gameData.getDay());
        contentValues.put(DbCols.MONTH, gameData.getMonth());
        contentValues.put(DbCols.YEAR, gameData.getYear());
        contentValues.put(DbCols.COMPLETION, gameData.getCompletion());
        contentValues.put(DbCols.LEVEL, gameData.getLevel());
        contentValues.put(DbCols.SCORE, gameData.getScore());
        contentValues.put(DbCols.STEP, gameData.getStep());
        contentValues.put(DbCols.TIME, gameData.getTime());
        contentValues.put(DbCols.MAIN_TYPE, gameData.getMainType());
        contentValues.put(DbCols.GAME_DATA, gameData.getData());
        contentValues.put(DbCols.ROW_CONTROL_LIMIT, gameData.getRowControlLimit());

        SQLiteDatabase db = this.getWritableDatabase();
        long status = db.insert(DbTables.GAMES_TABLE, null, contentValues);

        MakeLog.info(TAG, "Record Insert Status: " + status);

        db.close();

    }

}
