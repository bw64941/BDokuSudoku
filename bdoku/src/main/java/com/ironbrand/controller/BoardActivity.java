package com.ironbrand.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ironbrand.bdokusudoku.Board;
import com.ironbrand.bdokusudoku.BoardOpen;
import com.ironbrand.bdokusudoku.BoardSaver;
import com.ironbrand.bdokusudoku.Cell;
import com.ironbrand.bdokusudoku.PredefinedBoardList;
import com.ironbrand.bdokusudoku.R;
import com.ironbrand.bdokusudoku.SavedBoard;
import com.ironbrand.model.engine.SolverStep;
import com.ironbrand.view.BoardView;

import java.util.ArrayList;
import java.util.Random;

public class BoardActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

    public static final String TAG = BoardActivity.class.getName();
    public static final String RESUME = "resume";
    public static final String DIFFICULTY_CHOSEN = "difficulty";
    public static final String BOARD_DIFFICULTY_EASY = "Easy";
    public static final String BOARD_DIFFICULTY_MEDIUM = "Medium";
    public static final String BOARD_DIFFICULTY_HARD = "Hard";
    public static final String TIMER_VALUE = "gameTime";
    private static final int SOLVED_TIME_DISPLAY_POST = 100;
    public static final int ROWS = Board.ROWS;
    public static final int COLUMNS = Board.COLUMNS;
    private static Board board = null;
    private BoardView boardView = null;
    private boolean solved = false;
    private TextView timeText = null;
    private RewardedAd rewardVideoAd;

    /*
     * Creates main board view and creates background board entity.
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.board);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "Mobile Ads Initialized");
            }
        });

        rewardVideoAd = new RewardedAd(this, getString(R.string.rewardedVideoAd));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad loaded
                Log.d(TAG, "Reward Ad Loaded");
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
                Log.d(TAG, "Reward Ad Failed to Load " + errorCode);
            }
        };
        rewardVideoAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        boardView = findViewById(R.id.boardView);

        Animation openFadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        openFadeAnimation.reset();

        LinearLayout relativeLayout = findViewById(R.id.boardLayout);
        relativeLayout.setAnimation(openFadeAnimation);

        Button undo = findViewById(R.id.undo);
        undo.setOnClickListener(this);

        ImageButton checkWork = findViewById(R.id.validate);
        checkWork.setOnClickListener(this);

        Button solveBtn = findViewById(R.id.solveButton);
        solveBtn.setOnClickListener(this);

        Button hintBtn = findViewById(R.id.hintButton);
        hintBtn.setOnClickListener(this);

        ToggleButton pencilToggle = findViewById(R.id.pencilToggle);
        pencilToggle.setOnCheckedChangeListener(this);

        TextView difficultyText = findViewById(R.id.difficultyChosen);
        difficultyText.setText(String.format(getString(R.string.difficultyChosen), getIntent().getStringExtra(BoardActivity.DIFFICULTY_CHOSEN)));

        ImageButton save = findViewById(R.id.save);
        save.setOnClickListener(this);

        Board board = (Board) getLastNonConfigurationInstance();
        if (board == null) {
            solved = false;
            /*
             * Initialize the board chosen from parent activity and solve
             * background board.
             */
            int rc = showBoard();
            if (rc == 0) {
                boardView.requestFocus();
            }
        }
    }

    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this, getString(R.string.rewardedVideoAd));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                Log.d(TAG, "Create and Load - Loaded");
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                Log.d(TAG, "Create and Load - Failed to Load " + errorCode);
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    /*
     * Update timer view from game timer.
     */
    public void updateTimer(@Nullable String time) {
        timeText = findViewById(R.id.timerText);
        timeText.setText(time);
    }

    /*
     * Returns the predefined board to play given chosen board from dialog box
     * on splash screen.
     */
    private int showBoard() {
        int rc = 0;
        int[][] predefinedBoardValuesArray;
        BoardOpen opener = new BoardOpen(this);

        boolean resumeInProgressGame = getIntent().getBooleanExtra(BoardActivity.RESUME, false);

        if (resumeInProgressGame) {
            ArrayList<ArrayList<Integer>> savedPencilValues;
            opener.open();
            SavedBoard savedInProgressBoard = opener.getSavedBoard();
            predefinedBoardValuesArray = savedInProgressBoard.getArrayValues();
            savedPencilValues = savedInProgressBoard.getUserPlacedPencilValuesIndex();
            board = Board.getBoard(predefinedBoardValuesArray);
            ArrayList<Integer> userPlacedValuesIndexes = savedInProgressBoard.getUserPlacedValues();
            if (userPlacedValuesIndexes.size() > 0) {
                for (Integer userPlacedValueIndex : userPlacedValuesIndexes) {
                    board.getValues().get(userPlacedValueIndex).setUserPlaced(true);
                }
            }

            if (savedPencilValues != null && !savedPencilValues.isEmpty()) {
                int cellIndex = 0;
                for (Cell cell : board.getValues()) {
                    cell.setUserPencilValues(savedPencilValues.get(cellIndex));
                    cellIndex++;
                }
            }

            board.solveBackGroundBoard();
        } else {
            PredefinedBoardList listOfPredefinedBoards = PredefinedBoardList.getSavedBoardList(this);
            String difficultyChosen = getIntent().getStringExtra(BoardActivity.DIFFICULTY_CHOSEN);
            SavedBoard predefinedBoard = listOfPredefinedBoards.getPredefinedBoardWithDifficulty(difficultyChosen);
            if (predefinedBoard != null) {
                predefinedBoardValuesArray = predefinedBoard.getArrayValues();
                board = Board.getBoard(predefinedBoardValuesArray);
                board.solveBackGroundBoard();
            } else {
                rc = -1;
            }
        }

        return rc;
    }

    /**
     * Checks all of the user placed values on the board.
     */
    private void checkWorkDoneOnBoard() {
        if (!solved) {
            if (!checkBoardValidity()) {
                showToast(getResources().getString(R.string.mistakeText));
            } else {
                showToast(getResources().getString(R.string.doingGreatText));
            }
        }
    }

    /**
     * Save Board
     *
     * void
     */
    private void saveBoard() {
        if (!solved) {
            if (checkBoardValidity()) {
                new BoardSaver(this).execute(board);
            } else {
                showToast(getResources().getString(R.string.correctMistakesText));
            }
        }
    }

    /*
     * Checks for mistakes on the board and highlights erroneous cell.
     *
     * @return
     */
    private boolean checkBoardValidity() {
        boolean validBoard = true;
        for (int cellIndex = 0; cellIndex < board.getSolvedValuesArray().size(); cellIndex++) {
            if (!board.getValues().get(cellIndex).isEmpty() && board.getSolvedValuesArray().get(cellIndex).getValue() != board.getValues().get(cellIndex).getValue()) {
                validBoard = false;
                boardView.highlightSelectedArea(board.getValues().get(cellIndex).getCol(), board.getValues().get(cellIndex).getRow(), BoardView.HIGHLIGHT_ERROR);
            }
        }

        return validBoard;
    }

    /*
     * Get next Solved Value not figure out by player for Hint.
     */
    private void getRandomHint() {
        if (!solved) {
            Cell hintCell = null;

            /*
             * Pick a random cell on the board that is either empty or been
             * incorrectly solved by user. Give correct hint for that cell.
             */
            while (hintCell == null) {
                Random randomNumber = new Random();
                int randomIndex = randomNumber.nextInt(board.getValues().size());
                if ((board.getValues().get(randomIndex).isUserPlaced() || board.getValues().get(randomIndex).isEmpty()) && board.getSolvedValuesArray().get(randomIndex).getValue() != board.getValues().get(randomIndex).getValue()) {
                    hintCell = board.getSolvedValuesArray().get(randomIndex);
                }
            }

            boardView.highlightSelectedArea(hintCell.getCol(), hintCell.getRow(), BoardView.HIGHLIGHT_HINT);
        }
    }

    /*
     * Clears a cell on the board after clear button has been pressed.
     */
    private void undoLastMove() {
        if (board.getValues().getHistory().size() > 0 && !solved) {
            SolverStep setValueStep;

            if (board.getValues().getHistory().peek().getAlgorithm().equals(SolverStep.USER_PLACED)) {
                setValueStep = board.getValues().getHistory().pop();
                setValueStep.getCell().clear();
                boardView.highlightSelectedArea(setValueStep.getCell().getCol(), setValueStep.getCell().getRow(), BoardView.HIGHLIGHT);
                board.getValues().evaluateInitialValues();
                boardView.clearBoardRectangles();
            }

            while (!board.getValues().getHistory().isEmpty() && board.getValues().getHistory().peek().getAlgorithm().equals(SolverStep.SYSTEM_POSSIBILITY_REMOVED)) {
                SolverStep removePossibilitiesStep = board.getValues().getHistory().pop();
                removePossibilitiesStep.getCell().getRemainingPossibilities().add(removePossibilitiesStep.getValue());
                if (board.getValues().getHistory().empty()) {
                    break;
                }
            }
        }
    }

    /*
     * Toggle Pencil Mode for game.
     */
    private void togglePencilMode(boolean value) {
        if (!solved) {
            boardView.setPencilModeOn(value);
        }
    }

    /**
     * the solved to set
     */
    public void setSolvedOn() {
        Intent boardActivity = new Intent(getApplicationContext(), RestartActivity.class);
        boardActivity.putExtra("gameTime", timeText.getText());
        startActivityForResult(boardActivity, BoardActivity.SOLVED_TIME_DISPLAY_POST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        Log.d("BoardActivity","onActivityResult");
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Log.d(TAG, "Pause called");
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // Log.d(TAG, "Resume called");
        solved = board.isSolved();
        super.onResume();

    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        if (boardView.getThread().isAlive()) {
            boardView.getThread().setRunning(false);
            boardView.getmHandler().removeCallbacks(boardView.getmUpdateTimeTask());
        }
        super.onDestroy();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onRetainNonConfigurationInstance()
     */
    @Nullable
    @Override
    public Object onRetainNonConfigurationInstance() {
        return Board.getBoard();
    }

    @Override
    public void onClick(@Nullable View v) {
        if (v.getId() == R.id.undo) {
            undoLastMove();
        } else if (v.getId() == R.id.validate) {
            checkWorkDoneOnBoard();
        } else if (v.getId() == R.id.hintButton) {
            if (rewardVideoAd.isLoaded()) {
                RewardedAdCallback adCallback = new RewardedAdCallback() {

                    @Override
                    public void onRewardedAdOpened() {
                        Log.d(TAG, "Reward Ad Opened");
                        super.onRewardedAdOpened();
                    }

                    @Override
                    public void onRewardedAdClosed() {
                        Log.d(TAG, "Reward Ad Closed");
                        rewardVideoAd = createAndLoadRewardedAd();
                        super.onRewardedAdClosed();
                    }

                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        Log.d(TAG, "Reward Ad Earned");
                        getRandomHint();
                    }

                    @Override
                    public void onRewardedAdFailedToShow(int i) {
                        Log.d(TAG, "Reward Ad Failed to Load");
                        super.onRewardedAdFailedToShow(i);
                    }
                };
                rewardVideoAd.show(this, adCallback);
            } else {
                Log.d(TAG, "The rewarded ad wasn't loaded yet.");
                showToast("Cannot Retrieve Hint");
            }
        } else if (v.getId() == R.id.solveButton) {
            this.solved = true;
            boardView.setSolved(true);
            setSolvedOn();
        } else if (v.getId() == R.id.save) {
            saveBoard();
        }
    }

    @Override
    public void onCheckedChanged(@Nullable CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.pencilToggle) {
            togglePencilMode(isChecked);
        }
    }
}
