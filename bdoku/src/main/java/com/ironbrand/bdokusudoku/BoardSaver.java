package com.ironbrand.bdokusudoku;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * @author bwinters
 *
 */
public class BoardSaver extends AsyncTask<Board, Void, Integer> {

    public static final String PENCIL_VALUES_CELL_DELIMITER = "|";
    public static final String CELL_USER_VALUE_INDICATOR = "*";
    private static final String IN_PROGRESS_SEQUENCE_ID = "0";
    private static final String IN_PROGRESS_DESCRIPTION = "IN_PROGRESS";
    private static final String SAVED_IN_PROGRESS_FILE_NAME = "in_progress.txt";
    private static final String FIELD_DELIMITER = "@";
    private static final String CELL_VALUES_DELIMITER = ",";
    private final WeakReference<Context> context;

    public BoardSaver(@Nullable Context context) {
        this.context = new WeakReference<>(context);
    }

    @Nullable
    @Override
    protected Integer doInBackground(@Nullable Board... boardsToSave) {
        StringBuilder valuesBuffer = new StringBuilder();
        StringBuilder pencilValuesBuffer = new StringBuilder();
        StringBuilder solvedValuesBuffer = new StringBuilder();
        int response = 0;

        for (Board board : boardsToSave) {
            for (Cell cell : board.getValues()) {
                if (cell.isUserPlaced()) {
                    valuesBuffer.append(BoardSaver.CELL_USER_VALUE_INDICATOR).append(cell.getValue()).append(BoardSaver.CELL_VALUES_DELIMITER);
                } else {
                    valuesBuffer.append(cell.getValue()).append(BoardSaver.CELL_VALUES_DELIMITER);
                }

                if (cell.getUserPencilValues().size() == 0) {
                    pencilValuesBuffer.append(BoardSaver.IN_PROGRESS_SEQUENCE_ID + BoardSaver.CELL_VALUES_DELIMITER);
                } else {
                    for (Integer pencilValue : cell.getUserPencilValues()) {
                        pencilValuesBuffer.append(pencilValue).append(BoardSaver.CELL_VALUES_DELIMITER);
                    }
                }

                pencilValuesBuffer.append(BoardSaver.PENCIL_VALUES_CELL_DELIMITER);
            }

            for (Cell cell : Board.getBoard().getSolvedValuesArray()) {
                solvedValuesBuffer.append(cell.getValue()).append(BoardSaver.CELL_VALUES_DELIMITER);
            }

            SavedBoard savedBoard;
            if (pencilValuesBuffer.length() > 0) {
                savedBoard = new SavedBoard(BoardSaver.IN_PROGRESS_SEQUENCE_ID, BoardSaver.IN_PROGRESS_DESCRIPTION, valuesBuffer.substring(0,
                        valuesBuffer.length() - 1), pencilValuesBuffer.substring(0, pencilValuesBuffer.length() - 1));
            } else {
                savedBoard = new SavedBoard(BoardSaver.IN_PROGRESS_SEQUENCE_ID, BoardSaver.IN_PROGRESS_DESCRIPTION, valuesBuffer.substring(0,
                        valuesBuffer.length() - 1), pencilValuesBuffer.substring(0, pencilValuesBuffer.length()));
            }

            FileOutputStream output;
            try {
                output = context.get().openFileOutput(BoardSaver.SAVED_IN_PROGRESS_FILE_NAME, Context.MODE_PRIVATE);
                String line = savedBoard.getBoardSequenceString() + BoardSaver.FIELD_DELIMITER + savedBoard.getBoardDescriptionString()
                        + BoardSaver.FIELD_DELIMITER + savedBoard.getBoardValuesString() + BoardSaver.FIELD_DELIMITER
                        + savedBoard.getPencilValuesString() + BoardSaver.FIELD_DELIMITER + savedBoard.getSolvedValuesString();

                output.write(line.getBytes());
                output.close();
            } catch (FileNotFoundException e) {
                response = -1;
            } catch (IOException e) {
                response = -2;
            }
        }

        return response;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(@Nullable Integer result) {
        if (result == 0) {
            Toast saveToast = Toast.makeText(context.get(), "Board Save Successful", Toast.LENGTH_SHORT);
            saveToast.setGravity(Gravity.CENTER, 0, 0);
            saveToast.show();
        } else {
            Toast saveToast = Toast.makeText(context.get(), "Board Not Saved!", Toast.LENGTH_SHORT);
            saveToast.setGravity(Gravity.CENTER, 0, 0);
            saveToast.show();
        }
    }
}
