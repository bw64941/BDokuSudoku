package com.ironbrand.bdokusudoku;

import androidx.annotation.Nullable;

import com.ironbrand.model.engine.Solver;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author bwinters
 *
 */
public class Board {

    public static final int ROWS = 9;
    public static final int COLUMNS = 9;
    private static Board board = null;
    private static ValuesArray values = null;
    private static ValuesArray solvedValuesArray = null;
    private Solver solver = null;
    private int numberOfRecursionsNeededToSolve = 0;
    private int numberOfStepsToSolve = 0;

    /**
     * Private Board Constructor
     */
    private Board() {
    }

    /**
     * @return the board after creating new values/cells
     */
    @Nullable
    public static synchronized Board getBoard() {
        if (board == null) {
            board = new Board();
            values = new ValuesArray();
            solvedValuesArray = new ValuesArray();
        }
        return board;
    }

    /**
     * @param board
     *            the board to set Probably will not use.....
     */
    public static synchronized void setBoard(@Nullable Board board) {
        Board.board = board;
    }

    /**
     * @return the board given values as parameter
     */
    @Nullable
    public static synchronized Board getBoard(@Nullable int[][] valuesIntArray) {
        if (board == null) {
            board = new Board();
        }
        values = new ValuesArray(valuesIntArray);
        solvedValuesArray = new ValuesArray(valuesIntArray);
        return board;
    }

    /**
     * @return reset the board to initial state.
     */
    public synchronized void clear() {
        values.clear();
        numberOfRecursionsNeededToSolve = 0;
        numberOfStepsToSolve = 0;
    }

    /**
     * @return the values
     */
    @Nullable
    public synchronized ValuesArray getValues() {
        return values;
    }

    /**
     * @param values
     *            the values to set
     */
    public synchronized void setValues(@Nullable ValuesArray values) {
        Board.values = values;
    }

    /**
     * Solves current board
     */
    public synchronized void solve() {
        solver = new Solver(values);
        Thread solverThread = new Thread(solver);
        solverThread.run();
        numberOfRecursionsNeededToSolve = solver.getNumberOfRecursionsNeededToSolve();
        numberOfStepsToSolve = solver.getNumberOfStepsToSolve();
    }

    /**
     * Solves current board
     */
    public synchronized void solveBackGroundBoard() {
        solver = new Solver(solvedValuesArray);
        Thread solverThread = new Thread(solver);
        solverThread.run();
        numberOfRecursionsNeededToSolve = solver.getNumberOfRecursionsNeededToSolve();
        numberOfStepsToSolve = solver.getNumberOfStepsToSolve();
    }

    /**
     * @return the numberOfRecursionsNeededToSolve
     */
    public synchronized int getNumberOfRecursionsNeededToSolve() {
        return numberOfRecursionsNeededToSolve;
    }

    /**
     * @param numberOfRecursionsNeededToSolve
     *            the numberOfRecursionsNeededToSolve to set
     */
    public synchronized void setNumberOfRecursionsNeededToSolve(int numberOfRecursionsNeededToSolve) {
        this.numberOfRecursionsNeededToSolve = numberOfRecursionsNeededToSolve;
    }

    /**
     * @return the numberOfStepsToSolve
     */
    public synchronized int getNumberOfStepsToSolve() {
        return numberOfStepsToSolve;
    }

    /**
     * @param numberOfStepsToSolve
     *            the numberOfStepsToSolve to set
     */
    public synchronized void setNumberOfStepsToSolve(int numberOfStepsToSolve) {
        this.numberOfStepsToSolve = numberOfStepsToSolve;
    }

    /**
     * @return the solvedValuesArray
     */
    @Nullable
    public synchronized ValuesArray getSolvedValuesArray() {
        return solvedValuesArray;
    }

    /**
     * @param solvedValuesArray
     *            the solvedValuesArray to set
     */
    public synchronized void setSolvedValuesArray(@Nullable ValuesArray solvedValuesArray) {
        Board.solvedValuesArray = solvedValuesArray;
    }

    /**
     * @return the solved
     */
    public boolean isSolved() {
        boolean solved = true;

        ArrayList<Cell> userCells = values.getAllUserPlacedCells();
        ArrayList<Cell> emptyCells = values.getEmptyCells();

        if (Objects.requireNonNull(emptyCells).size() > 0 || Objects.requireNonNull(userCells).size() == 0) {
            solved = false;
        } else {
            for (Cell cell : userCells) {
                if (cell.getValue() != Objects.requireNonNull(solvedValuesArray.getCellWithRowAndColumn(cell.getRow(), cell.getCol())).getValue()) {
                    solved = false;
                    break;
                }
            }

        }
        return solved;
    }
}
