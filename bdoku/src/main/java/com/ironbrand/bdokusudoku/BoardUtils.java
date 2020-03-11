package com.ironbrand.bdokusudoku;

import androidx.annotation.Nullable;

import com.ironbrand.model.engine.SolverStep;
import com.ironbrand.model.engine.SolverTechnique.CellModStatus;

import java.util.ArrayList;

/**
 * @author bwinters
 *
 */
public class BoardUtils {

    public BoardUtils() {
    }

    /**
     * Get value at x,y coordinate
     */
    @Nullable
    public ValuesArray getValues() {
        return Board.getBoard().getValues();
    }

    /**
     * Get value at x,y coordinate
     */
    public int getValueAt(int row, int col) {
        return Board.getBoard().getValues().getCellWithRowAndColumn(row, col).getValue();
    }

    /**
     * Returns solved values array
     */
    public int getValueAtSolvedCell(int row, int col) {
        int value;

        Cell cell = Board.getBoard().getSolvedValuesArray().getCellWithRowAndColumn(row, col);

        if (!cell.isUserPlaced()) {
            value = cell.getValue();
        } else {
            value = 0;
        }

        return value;
    }

    /**
     * Returns solved values array
     */
    public int getValuesAtSystemCell(int row, int col) {
        int value;

        Cell cell = Board.getBoard().getValues().getCellWithRowAndColumn(row, col);

        if (cell != null && !cell.isUserPlaced()) {
            value = cell.getValue();
        } else {
            value = 0;
        }

        return value;
    }

    /**
     * Returns solved values array
     */
    public int getValueAtUserCell(int row, int col) {
        int value;

        Cell cell = Board.getBoard().getValues().getCellWithRowAndColumn(row, col);
        if (cell != null && cell.isUserPlaced()) {
            value = cell.getValue();
        } else {
            value = 0;
        }

        return value;
    }

    /**
     * Returns solved values array
     */
    @Nullable
    public ArrayList<Integer> getPencilBoardValuesArray(int row, int col) {
        ArrayList<Integer> values;

        Cell cell = Board.getBoard().getValues().getCellWithRowAndColumn(row, col);
        if (cell != null && cell.isEmpty() || cell.isUserPlaced()) {
            values = cell.getUserPencilValues();
        } else {
            values = null;
        }

        return values;
    }

    /**
     * Returns whether the row,col cell has pencil values or not
     */
    public boolean cellHasPencilValue(int row, int col) {
        boolean hasPencilValues = false;

        if (Board.getBoard().getValues().getCellWithRowAndColumn(row, col).getUserPencilValues().size() > 0) {
            hasPencilValues = true;
        }

        return hasPencilValues;
    }

    /**
     * Returns a list of values from userPlaced cells on the board
     */
    @Nullable
    public ArrayList<Integer> getUserPlacedValuesArray() {
        ArrayList<Integer> values = new ArrayList<>();

        for (Cell cell : Board.getBoard().getValues()) {
            if (cell.isUserPlaced()) {
                values.add(cell.getValue());
            }
        }
        return values;
    }

    /*
     * Sets values at particular cell location
     */
    public void setValueAtCell(int row, int col, int value) {
        Cell cell = Board.getBoard().getValues().getCellWithRowAndColumn(row, col);
        if (value == 0) {
            // Clear button pressed
            cell.userClear();
        } else {
            cell.setUserValue(value);
            Board.getBoard().getValues().getHistory().push(new SolverStep(cell, CellModStatus.SET_VALUE, value, SolverStep.USER_PLACED));
        }
    }

    /*
     * Returns true if the board is solved
     */
    public boolean isBoardSolved() {
        return Board.getBoard().isSolved();
    }

    /*
     *Clear user placed values and pencil values
     */
    public void clearUserMods() {
        for (Cell cell : Board.getBoard().getValues()) {
            if (cell.isUserPlaced() || cell.getUserPencilValues().size() > 0) {
                cell.clear();
                cell.getUserPencilValues().clear();
            }
        }
    }
}
