package com.ironbrand.bdokusudoku;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * @author bwinters
 *
 */
public class BoardOpen {

    public static final String SAVED_IN_PROGRESS_FILE_NAME = "in_progress.txt";
    public static final String FIELD_DELIMITER = "@";
    private SavedBoard savedBoard = null;
    private final Context context;

    public BoardOpen(@Nullable Context context) {
        this.context = context;
    }

    /*
     * Checks for already in progress boards with given difficulty.
     */
    public void open() {
        String line;
        boolean boardFound = false;

        try {
            FileInputStream inputStream = context.openFileInput(BoardOpen.SAVED_IN_PROGRESS_FILE_NAME);
            BufferedReader bufferedInput = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedInput.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, BoardOpen.FIELD_DELIMITER);
                savedBoard = new SavedBoard(tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken());
                boardFound = true;
            }

            if (!boardFound) {
                savedBoard = null;
            }
            bufferedInput.close();
        } catch (FileNotFoundException e) {
            Log.d("BoardOpen", "Error Opening Board from File.  File not found");
        } catch (IOException e) {
            Log.d("BoardOpen", "Unknown Error");
        }
    }

    /**
     * Delete the existing board.
     */
    public void deleteExistingFile() {
        if (!context.deleteFile(BoardOpen.SAVED_IN_PROGRESS_FILE_NAME)) {
            Log.d("BoardOpen", "Error Deleting File");
        }
    }

    /**
     * @return the savedBoard
     */
    @Nullable
    public SavedBoard getSavedBoard() {
        return savedBoard;
    }

    /**
     * @param savedBoard
     *            the savedBoard to set
     */
    public void setSavedBoard(@Nullable SavedBoard savedBoard) {
        this.savedBoard = savedBoard;
    }

}
