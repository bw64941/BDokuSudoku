package com.ironbrand.bdokusudoku;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author bwinters
 *
 */
public class SavedBoard {

    private String boardSequenceString = "";
    private String boardDescriptionString = "";
    private String boardValuesString = "";
    private String pencilValuesString = "";
    private String solvedValuesString = "";
    private int[][] arrayValues = null;
    private ArrayList<Integer> userPlacedValues = null;
    private ArrayList<ArrayList<Integer>> userPlacedPencilValuesIndex = null;

    public SavedBoard(@Nullable String boardSequenceString, @Nullable String boardDescriptionString, @Nullable String boardValuesString, @Nullable String pencilValuesString) {
        this.setBoardSequenceString(boardSequenceString);
        this.setBoardDescriptionString(boardDescriptionString);
        this.setBoardValuesString(boardValuesString);
        this.setPencilValuesString(pencilValuesString);
        valuesToArray();
        populateUserPlacedValues();
        pencilValuesToArrayList();
    }

    /**
     * @return the boardSequenceString
     */
    @Nullable
    public String getBoardSequenceString() {
        return boardSequenceString;
    }

    /**
     * @param boardSequenceString
     *            the boardSequenceString to set
     */
    private void setBoardSequenceString(@Nullable String boardSequenceString) {
        this.boardSequenceString = boardSequenceString;
    }

    /**
     * @return the boardDescriptionString
     */
    @Nullable
    public String getBoardDescriptionString() {
        return boardDescriptionString;
    }

    /**
     * @param boardDescriptionString
     *            the boardDescriptionString to set
     */
    private void setBoardDescriptionString(@Nullable String boardDescriptionString) {
        this.boardDescriptionString = boardDescriptionString;
    }

    /**
     * @return the boardValuesString
     */
    @Nullable
    public String getBoardValuesString() {
        return boardValuesString;
    }

    /**
     * @param boardValuesString
     *            the boardValuesString to set
     */
    private void setBoardValuesString(@Nullable String boardValuesString) {
        this.boardValuesString = boardValuesString;
    }

    /**
     * @return the pencilValuesString
     */
    @Nullable
    public String getPencilValuesString() {
        return pencilValuesString;
    }

    /**
     * @param pencilValuesString
     *            the pencilValuesString to set
     */
    private void setPencilValuesString(@Nullable String pencilValuesString) {
        this.pencilValuesString = pencilValuesString;
    }

    /**
     * Get int[][] array values with specified Difficulty
     */
    private void valuesToArray() {
        arrayValues = new int[Board.ROWS][Board.COLUMNS];
        StringTokenizer tokenizer = new StringTokenizer(boardValuesString, ",");

        for (int i = 0; i < Board.ROWS; i++) {
            for (int j = 0; j < Board.COLUMNS; j++) {
                // skip the * at the beginning for user placed values.
                String cellValueString = tokenizer.nextToken();
                int cellValue;
                if (!cellValueString.contains(BoardSaver.CELL_USER_VALUE_INDICATOR)) {
                    cellValue = Integer.parseInt(cellValueString);
                } else {
                    cellValue = Integer.parseInt(cellValueString.substring(1));
                }
                arrayValues[i][j] = cellValue;
            }
        }
    }

    /**
     * Get int[][] array values with specified Difficulty
     */
    private void populateUserPlacedValues() {
        userPlacedValues = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(boardValuesString, ",");

        int cellIndex = 0;
        while (tokenizer.hasMoreTokens()) {
            String cellValueString = tokenizer.nextToken();
            if (cellValueString.indexOf('*') != -1) {
                userPlacedValues.add(cellIndex);
            }
            cellIndex++;
        }
    }

    /**
     * Get int[][] array values with specified Difficulty
     */
    private void pencilValuesToArrayList() {
        userPlacedPencilValuesIndex = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(pencilValuesString, BoardSaver.PENCIL_VALUES_CELL_DELIMITER);

        while (tokenizer.hasMoreTokens()) {
            String cellPossibilitiesList = tokenizer.nextToken();

            StringTokenizer tokenizer2 = new StringTokenizer(cellPossibilitiesList, ",");
            ArrayList<Integer> values = new ArrayList<>();

            while (tokenizer2.hasMoreTokens()) {
                String possibilityValue = tokenizer2.nextToken();
                if (!possibilityValue.equals(" ")) {
                    values.add(Integer.valueOf(possibilityValue));
                }
            }

            userPlacedPencilValuesIndex.add(values);
        }
    }

    /**
     * @return the arrayValues
     */
    @Nullable
    public int[][] getArrayValues() {
        return arrayValues;
    }

    /**
     * @param arrayValues
     *            the arrayValues to set
     */
    public void setArrayValues(@Nullable int[][] arrayValues) {
        this.arrayValues = arrayValues;
    }

    /**
     * @return the userPlacedValues
     */
    @Nullable
    public ArrayList<Integer> getUserPlacedValues() {
        return userPlacedValues;
    }

    /**
     * @param userPlacedValues
     *            the userPlacedValues to set
     */
    public void setUserPlacedValues(@Nullable ArrayList<Integer> userPlacedValues) {
        this.userPlacedValues = userPlacedValues;
    }

    /**
     * @return the userPlacedPencilValuesIndex
     */
    @Nullable
    public ArrayList<ArrayList<Integer>> getUserPlacedPencilValuesIndex() {
        return userPlacedPencilValuesIndex;
    }

    /**
     * @param userPlacedPencilValuesIndex
     *            the userPlacedPencilValuesIndex to set
     */
    public void setUserPlacedPencilValuesIndex(@Nullable ArrayList<ArrayList<Integer>> userPlacedPencilValuesIndex) {
        this.userPlacedPencilValuesIndex = userPlacedPencilValuesIndex;
    }

    /**
     * @return the solvedValuesString
     */
    @Nullable
    public String getSolvedValuesString() {
        return solvedValuesString;
    }

    /**
     * @param solvedValuesString
     *            the solvedValuesString to set
     */
    public void setSolvedValuesString(@Nullable String solvedValuesString) {
        this.solvedValuesString = solvedValuesString;
    }
}
