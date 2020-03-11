package com.ironbrand.bdokusudoku;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * @author bwinters
 *
 */
public class PredefinedBoardList extends ArrayList<SavedBoard> {

    private static final long serialVersionUID = 1L;
    private static PredefinedBoardList savedBoardList = null;
    private final WeakReference<Context> context;

    private PredefinedBoardList(Context context) {
        this.context = new WeakReference<>(context);
        loadSavedBoardFromFile();
    }

    /**
     * @return the savedBoardList
     */
    @Nullable
    public static PredefinedBoardList getSavedBoardList(@Nullable Context context) {
        if (savedBoardList == null) {
            savedBoardList = new PredefinedBoardList(context);
        }
        return savedBoardList;
    }

    /**
     * @param savedBoardList
     *            the savedBoardList to set
     */
    public static void setSavedBoardList(@Nullable PredefinedBoardList savedBoardList) {
        PredefinedBoardList.savedBoardList = savedBoardList;
    }

    /**
     * Get all board descriptions
     */
    private void loadSavedBoardFromFile() {
        String line;

        try {
            InputStream input = context.get().getResources().openRawResource(R.raw.preinstalledboards);
            BufferedReader bufferedInput = new BufferedReader(new InputStreamReader(input));
            while ((line = bufferedInput.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, BoardOpen.FIELD_DELIMITER);
                SavedBoard savedboard = new SavedBoard(tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken());
                this.add(savedboard);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("PreDefinedBoardList", "File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("PreDefinedBoardList", "General Exception");
        }
    }

    /**
     * Get Boards with specified Difficulty
     */
    @Nullable
    public SavedBoard getPredefinedBoardWithDifficulty(@Nullable String difficulty) {
        ArrayList<SavedBoard> boards = new ArrayList<>();
        SavedBoard chosenBoard = null;

        for (SavedBoard board : this) {
            if (board.getBoardDescriptionString().equals(difficulty)) {
                boards.add(board);
            }
        }

        int randIndex;
        if (boards.size() > 0) {
            Random random = new Random();
            randIndex = random.nextInt(boards.size());
            if (randIndex == boards.size()) {
                randIndex -= 1;
            }
            chosenBoard = boards.get(randIndex);
        }

        return chosenBoard;
    }
}
