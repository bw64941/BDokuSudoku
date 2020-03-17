package com.ironbrand.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.ironbrand.bdokusudoku.BoardUtils;
import com.ironbrand.bdokusudoku.R;
import com.ironbrand.controller.BoardActivity;
import com.ironbrand.controller.UIThread;

import java.util.ArrayList;

/**
 * @author bwinters
 *
 */
public class BoardView extends SurfaceView implements SurfaceHolder.Callback {
    public static final String TAG = BoardView.class.getName();
    public static final int HIGHLIGHT = 0;
    public static final int HIGHLIGHT_HINT = 1;
    public static final int HIGHLIGHT_ERROR = 2;
    private final Rect selectedRow = new Rect();
    private final Rect selectedCol = new Rect();
    private final Rect selectedCell = new Rect();
    private final Rect hintCell = new Rect();
    private final Rect errorCell = new Rect();
    private float cellWidth;
    private float cellHeight;
    private int selectedXCoordinate;
    private int selectedYCoordinate;
    private String hintValue = "";
    private UIThread thread = null;

    private String selectedCellValue = "";
    private ArrayList<Integer> selectedCellPencilValues = new ArrayList<>();
    private boolean pencilModeOn = false;
    private BoardUtils boardUtils = null;
    private boolean solved = false;
    private Context context = null;
    private Bitmap bitmap = null;
    private Handler mHandler = new Handler();
    private long mStartTime = 0L;
    private byte[] originalStateOfBoard = null;
    private boolean backgroundDrawn = false;

    private Paint selected = null;
    private Paint error = null;
    private Paint hint = null;

    private Runnable mUpdateTimeTask = new Runnable() {

        private Activity unwrap(Context context) {
            while (!(context instanceof Activity) && context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            }

            return (Activity) context;
        }

        public void run() {
            final long start = mStartTime;
            long millis = System.currentTimeMillis() - start;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            String time = "";
            if (seconds < 10) {
                time = "" + minutes + ":0" + seconds;
            } else {
                time = "" + minutes + ":" + seconds;
            }
            ((BoardActivity) unwrap(context)).updateTimer(time);
            mHandler.postDelayed(this, 1000);
        }
    };

    /**
     * BoardView Constructor
     *
     * @param context Application Context
     *
     */
    public BoardView(@Nullable Context context, @Nullable AttributeSet attributes) {
        super(context, attributes);
        setFocusable(true);
        setFocusableInTouchMode(true);

        getHolder().addCallback(this);
        thread = new UIThread(getHolder(), this);
        boardUtils = new BoardUtils();
        this.context = context;


        hint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // Draw the highlighted cell selection...
        selected = new Paint();
        selected.setColor(getResources().getColor(R.color.highlightColor, null));

        // Draw the error Cell, if it exists...
        error = new Paint();
        error.setColor(getResources().getColor(R.color.errorHighlightColor, null));
    }

    @Override
    public void surfaceChanged(@Nullable SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.d("BoardView", "Surface Changed");

    }

    @Override
    public void surfaceCreated(@Nullable SurfaceHolder arg0) {
        Log.d("BoardView", "Surface Created");
        if (!thread.isAlive()) {
            thread = new UIThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
            boardUtils = new BoardUtils();
            solved = boardUtils.isBoardSolved();
            if (mStartTime == 0L) {
                mStartTime = System.currentTimeMillis();
                mHandler.removeCallbacks(mUpdateTimeTask);
                mHandler.postDelayed(mUpdateTimeTask, 100);
            }
        }
    }

    @Override
    public void surfaceDestroyed(@Nullable SurfaceHolder arg0) {
        Log.d("BoardView", "Surface Destroyed");
        if (thread.isAlive()) {
            thread.setRunning(false);
        }
    }

    /**
     * On touch event for board....determine cell pressed
     */
    @Override
    public boolean onTouchEvent(@Nullable MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            super.performClick();
            return super.onTouchEvent(event);
        }

        clearBoardRectangles();

        if (solved) {
            return super.onTouchEvent(event);
        }

        int xPosTouched = (int) (event.getX() / cellWidth);
        int yPosTouched = (int) (event.getY() / cellHeight);

        highlightSelectedArea(xPosTouched, yPosTouched, BoardView.HIGHLIGHT);
        showTouchDialog(selectedXCoordinate, selectedYCoordinate);
        return true;
    }

    @Override
    public boolean performClick() {
        Log.d("BoardView", "PerformClick");
        return super.performClick();
    }

    /**
     * Select Cell at x,y coordinates
     *
     * @param x - x
     * @param y - y
     */
    public void highlightSelectedArea(int x, int y, int type) {
        selectedXCoordinate = Math.min(Math.max(x, 0), 8);
        selectedYCoordinate = Math.min(Math.max(y, 0), 8);
        selectedCell.set((int) (x * cellWidth), (int) (y * cellHeight), (int) (x * cellWidth + (cellWidth)), (int) (y * cellHeight + cellHeight));
        switch (type) {
            case BoardView.HIGHLIGHT:
                // left,top,right,bottom
                selectedRow.set(0, (int) (y * cellHeight), (int) (x * cellWidth + (9 * cellWidth)), (int) (y * cellHeight + (cellHeight)));
                selectedCol.set((int) (x * cellWidth), 0, (int) (x * cellWidth + (cellWidth)), (int) (y * cellHeight + (9 * cellHeight)));
                selectedCellValue = String.valueOf(boardUtils.getValueAt(selectedYCoordinate, selectedXCoordinate));
                selectedCellPencilValues = boardUtils.getPencilBoardValuesArray(selectedYCoordinate, selectedXCoordinate);
                break;
            case BoardView.HIGHLIGHT_HINT:
                hintCell.set((int) (x * cellWidth), (int) (y * cellHeight), (int) (x * cellWidth + (cellWidth)), (int) (y * cellHeight + cellHeight));
                hintValue = String.valueOf(boardUtils.getValueAtSolvedCell(y, x));
                break;
            case BoardView.HIGHLIGHT_ERROR:
                errorCell.set((int) (x * cellWidth), (int) (y * cellHeight), (int) (x * cellWidth + cellWidth), (int) (y * cellHeight + cellHeight));
                break;
            default:
                break;
        }

    }

    /**
     * Show appropriate game dialog.
     */
    private void showTouchDialog(int x, int y) {
        // Flip X and Y when indexing values array.
        if (pencilModeOn && selectedCellPencilValues != null) {
            PencilQuickActionBar valueBar = new PencilQuickActionBar(this, selectedCellPencilValues);
            valueBar.show();
        } else {
            if (boardUtils.getValuesAtSystemCell(y, x) == 0) {
                ValueQuickActionBar valueBar = new ValueQuickActionBar(this, selectedCellPencilValues);
                valueBar.show();
            }
        }
    }

    /**
     * Determine if chosen value can be placed in location.
     *
     * @param possibilityChosen - y
     */
    public void setValueInSelectedCell(int possibilityChosen) {
        boardUtils.setValueAtCell(selectedYCoordinate, selectedXCoordinate, possibilityChosen);
        if (boardUtils.isBoardSolved()) {
            ((BoardActivity) context).setSolvedOn();
            solved = true;
        }

    }

    /*
     * Re-draw the board.
     *
     */
    public void doDraw(@Nullable Canvas canvas, float xOffset, float yOffset) {
//        Log.d("BoardView", "doDraw");
        // Always draw system placed values.
        drawBackground(canvas, xOffset, yOffset);
        drawSystemPlacedValues(canvas);
        drawUserPlacedValues(canvas);

        // draw pencil values or user placed values.
        if (pencilModeOn) {
            drawPencilValues(canvas);
        }

        if (!hintCell.isEmpty()) {
            hint.setColor(getResources().getColor(R.color.errorHighlightColor, null));
            hint.setStyle(Style.FILL);
            hint.setTextSize(cellHeight * 0.75f);
            hint.setTextScaleX(cellWidth / cellHeight);
            hint.setTextAlign(Paint.Align.CENTER);
            // Draw the number in the center of the tile
            FontMetrics fm = hint.getFontMetrics();
            // Centering in X: use alignment (and X at midpoint)
            float x = cellWidth / 2;
            // Centering in Y: measure ascent/descent first
            float y = cellHeight / 2 - (fm.ascent + fm.descent) / 2;


            canvas.drawText(hintValue, hintCell.left + x, hintCell.top + y, hint);
            hint.setAlpha(hint.getAlpha() - 1);
        }


        canvas.drawRect(selectedRow, selected);
        canvas.drawRect(selectedCol, selected);
        canvas.drawRect(errorCell, error);
    }

    /*
     * Re-draw the board background.
     */
    private void drawBackground(Canvas canvas, float xOffset, float yOffset) {
//         Log.d("BoardView", "drawBackground");
        cellWidth = (this.getWidth() / (float) 9) - 1;
        cellHeight = (this.getHeight() / (float) 9) - 1;

        // Draw the background...
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.white, null));
        background.setAntiAlias(true);
        background.setShadowLayer(cellWidth / 30, cellWidth / 30, cellWidth / 30, Color.BLACK);
        canvas.drawRect(xOffset, yOffset, getWidth(), getHeight(), background);

        // Define colors for the grid lines
        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.majorBoardGridLinesColor, null));
        dark.setStyle(Style.FILL_AND_STROKE);
        dark.setStrokeWidth(cellWidth / 9);
        dark.setShadowLayer(cellWidth / 30, cellWidth / 30, cellWidth / 30, R.color.highlightColor);

        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.bdoku_faded_blue, null));
        light.setStyle(Style.FILL_AND_STROKE);
        light.setStrokeWidth(cellWidth / 20);
        light.setShadowLayer(cellWidth / 30, cellWidth / 30, cellWidth / 30, R.color.highlightColor);

        // Draw the minor grid lines
        for (int i = 0; i <= 10; i++) {
            if (i % 3 == 0) {
                canvas.drawLine(0, i * cellHeight, getWidth(), i * cellHeight, dark);
                canvas.drawLine(i * cellWidth, 0, i * cellWidth, getHeight(), dark);
            } else {
                canvas.drawLine(0, i * cellHeight, getWidth(), i * cellHeight, light);
                canvas.drawLine(i * cellWidth, 0, i * cellWidth, getHeight(), light);
            }
        }
    }

    /**
     * Draws system placed values placed on the board.
     *
     * @param canvas -
     */
    private void drawSystemPlacedValues(Canvas canvas) {
        Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(R.color.systemValueColor, null));
        foreground.setStyle(Style.FILL_AND_STROKE);
        foreground.setTextSize(cellHeight * 0.55f);
        foreground.setTextScaleX(cellWidth / cellHeight);
        foreground.setTextAlign(Paint.Align.CENTER);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.alfa_slab_one);
        foreground.setTypeface(typeface);

        // Draw the number in the center of the tile
        FontMetrics fm = foreground.getFontMetrics();
        // Centering in X: use alignment (and X at midpoint)
        float x = cellWidth / 2;
        // Centering in Y: measure ascent/descent first
        float y = cellHeight / 2 - (fm.ascent + fm.descent) / 2;

        if (solved) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    int value = boardUtils.getValueAtSolvedCell(row, col);
                    if (value > 0) {
                        canvas.drawText(String.valueOf(value), col * cellWidth + x, row * cellHeight + y, foreground);
                        // imageCanvas.drawText(String.valueOf(value), col *
                        // cellWidth + x, row * cellHeight + y, foreground);
                    }
                }
            }
        } else {
            Paint selectedCellPaint = new Paint();
            selectedCellPaint.setColor(getResources().getColor(R.color.highlightColor, null));

            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    int value = boardUtils.getValuesAtSystemCell(row, col);
                    if (value > 0) {
                        if (String.valueOf(value).equals(selectedCellValue)) {
                            canvas.drawRect(new Rect((int) (col * cellWidth), (int) (row * cellHeight), (int) (col * cellWidth + (cellWidth)),
                                    (int) (row * cellHeight + cellHeight)), selectedCellPaint);
                        }
                        canvas.drawText(String.valueOf(value), col * cellWidth + x, row * cellHeight + y, foreground);
                    }
                }
            }
        }
    }

    /**
     * Draws User Placed values placed on the board.
     *
     * @param canvas -
     */
    private void drawUserPlacedValues(Canvas canvas) {
        Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(R.color.userPlacedValueColor, null));
        foreground.setStyle(Style.FILL);
        foreground.setTextSize(cellHeight * 0.55f);
        foreground.setTextScaleX(cellWidth / cellHeight);
        foreground.setTextAlign(Paint.Align.CENTER);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.alfa_slab_one);
        foreground.setTypeface(typeface);

        // Draw the number in the center of the tile
        FontMetrics fm = foreground.getFontMetrics();
        // Centering in X: use alignment (and X at midpoint)
        float x = cellWidth / 2;
        // Centering in Y: measure ascent/descent first
        float y = cellHeight / 2 - (fm.ascent + fm.descent) / 2;

        Paint selectedCellPaint = new Paint();
        selectedCellPaint.setColor(getResources().getColor(R.color.highlightColor, null));

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = boardUtils.getValueAtUserCell(row, col);
                if (value > 0) {
                    if (pencilModeOn && boardUtils.cellHasPencilValue(row, col)) {
                        foreground.setAlpha(128);
                    }
                    if (String.valueOf(value).equals(selectedCellValue)) {
                        canvas.drawRect(new Rect((int) (col * cellWidth), (int) (row * cellHeight), (int) (col * cellWidth + (cellWidth)), (int) (row
                                * cellHeight + cellHeight)), selectedCellPaint);
                        // imageCanvas.drawRect(new Rect((int) (col *
                        // cellWidth), (int) (row * cellHeight), (int) (col *
                        // cellWidth + (cellWidth)), (int) (row
                        // * cellHeight + cellHeight)), selectedCellPaint);
                    }
                    canvas.drawText(String.valueOf(value), col * cellWidth + x, row * cellHeight + y, foreground);
                    // imageCanvas.drawText(String.valueOf(value), col *
                    // cellWidth + x, row * cellHeight + y, foreground);
                }
            }
        }
    }

    /**
     * Draws only the possibilities that remain in each cell.
     *
     * @param canvas -
     */
    private void drawPencilValues(Canvas canvas) {
        // Draw the possibilities for empty cells...
        // Define color and style for numbers
        Paint foreground_poss = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground_poss.setColor(getResources().getColor(R.color.pencilValueColor, null));
        foreground_poss.setStyle(Style.FILL);
        foreground_poss.setStrokeWidth(cellWidth / 50);
        foreground_poss.setTextSize(cellHeight * 0.25f);
        foreground_poss.setTextScaleX(cellWidth / cellHeight);
        foreground_poss.setTextAlign(Paint.Align.CENTER);
        foreground_poss.setStrikeThruText(false);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.alfa_slab_one);
        foreground_poss.setTypeface(typeface);

        // Centering in X: use alignment (and X at midpoint)
        float x_poss = cellWidth / 3.3f;
        // Centering in Y: measure ascent/descent first
        float y_poss = cellHeight / 2.9f;
        float centerText = foreground_poss.getTextSize() / 2.65f;
        // Draw values on board.

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                ArrayList<Integer> valuesToDraw = boardUtils.getPencilBoardValuesArray(row, col);
                if (valuesToDraw != null) {
                    for (Integer value : valuesToDraw) {
                        float xStart = 0;
                        float yStart = 0;
                        switch (value) {
                            case 1:
                                xStart = (col * cellWidth + (x_poss * 1) - centerText);
                                yStart = (row * cellHeight + (y_poss * 1) - centerText);
                                break;
                            case 2:
                                xStart = (col * cellWidth + (x_poss * 2) - centerText);
                                yStart = (row * cellHeight + (y_poss * 1) - centerText);
                                break;
                            case 3:
                                xStart = (col * cellWidth + (x_poss * 3) - centerText);
                                yStart = (row * cellHeight + (y_poss * 1) - centerText);
                                break;
                            case 4:
                                xStart = (col * cellWidth + (x_poss * 1) - centerText);
                                yStart = (row * cellHeight + (y_poss * 2) - centerText);
                                break;
                            case 5:
                                xStart = (col * cellWidth + (x_poss * 2) - centerText);
                                yStart = (row * cellHeight + (y_poss * 2) - centerText);
                                break;
                            case 6:
                                xStart = (col * cellWidth + (x_poss * 3) - centerText);
                                yStart = (row * cellHeight + (y_poss * 2) - centerText);
                                break;
                            case 7:
                                xStart = (col * cellWidth + (x_poss * 1) - centerText);
                                yStart = (row * cellHeight + (y_poss * 3) - centerText);
                                break;
                            case 8:
                                xStart = (col * cellWidth + (x_poss * 2) - centerText);
                                yStart = (row * cellHeight + (y_poss * 3) - centerText);
                                break;
                            case 9:
                                xStart = (col * cellWidth + (x_poss * 3) - centerText);
                                yStart = (row * cellHeight + (y_poss * 3) - centerText);
                                break;
                            default:
                                break;
                        }

                        canvas.drawText(String.valueOf(value), xStart, yStart, foreground_poss);
                        // imageCanvas.drawText(String.valueOf(value), xStart,
                        // yStart, foreground_poss);
                    }
                }
            }
        }
    }

    /**
     * Clear view rectangle
     */
    public void clearBoardRectangles() {
        hintCell.setEmpty();
        errorCell.setEmpty();
        selectedCell.setEmpty();
        selectedRow.setEmpty();
        selectedCol.setEmpty();
        selectedCellValue = "";
    }

    /**
     * @return the pencilModeOn
     */
    public boolean isPencilModeOn() {
        return pencilModeOn;
    }

    /**
     * @param pencilModeOn
     *            the pencilModeOn to set
     */
    public void setPencilModeOn(boolean pencilModeOn) {
        this.pencilModeOn = pencilModeOn;
    }

    /**
     * @return the thread
     */
    @Nullable
    public UIThread getThread() {
        return thread;
    }

    /**
     * @param thread
     *            the thread to set
     */
    public void setThread(@Nullable UIThread thread) {
        this.thread = thread;
    }

    /**
     * @return the solved
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * @param solved
     *            the solved to set
     */
    public void setSolved(boolean solved) {
        if (solved) {
            clearBoardRectangles();
            mHandler.removeCallbacks(mUpdateTimeTask);
            boardUtils.clearUserMods();
        }
        this.solved = solved;
    }

    /**
     * @return the mHandler
     */
    @Nullable
    public Handler getmHandler() {
        return mHandler;
    }

    /**
     * @param mHandler
     *            the mHandler to set
     */
    public void setmHandler(@Nullable Handler mHandler) {
        this.mHandler = mHandler;
    }

    /**
     * @return the mUpdateTimeTask
     */
    @Nullable
    public Runnable getmUpdateTimeTask() {
        return mUpdateTimeTask;
    }

    /**
     * @param mUpdateTimeTask
     *            the mUpdateTimeTask to set
     */
    public void setmUpdateTimeTask(@Nullable Runnable mUpdateTimeTask) {
        this.mUpdateTimeTask = mUpdateTimeTask;
    }

    /**
     * @return the originalStateOfBoard
     */
    @Nullable
    public byte[] getOriginalStateOfBoard() {
        return originalStateOfBoard;
    }

    /**
     * @param originalStateOfBoard the originalStateOfBoard to set
     */
    public void setOriginalStateOfBoard(@Nullable byte[] originalStateOfBoard) {
        this.originalStateOfBoard = originalStateOfBoard;
    }
}
