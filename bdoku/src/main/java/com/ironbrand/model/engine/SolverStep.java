package com.ironbrand.model.engine;

import androidx.annotation.Nullable;

import com.ironbrand.bdokusudoku.Cell;
import com.ironbrand.model.engine.SolverTechnique.CellModStatus;

/**
 * @author bwinters
 *
 */
public class SolverStep {

    public static final String NAKED_PAIRS_IN_ROW = "NAKED PAIR IN ROW";
    public static final String NAKED_PAIRS_IN_COL = "NAKED PAIR IN COL";
    public static final String NAKED_PAIRS_IN_QUAD = "NAKED PAIR IN QUAD";
    public static final String REDUCE_ROW = "REDUCE ROW";
    public static final String REDUCE_COL = "REDUCE COL";
    public static final String NAKED_TRIPLE_IN_ROW = "NAKED TRIPLE IN ROW";
    public static final String NAKED_TRIPLE_IN_COL = "NAKED TRIPLE IN COL";
    public static final String NAKED_TRIPLE_IN_QUAD = "NAKED TRIPLE IN QUAD";
    public static final String HIDDEN_PAIRS_IN_ROW = "HIDDEN PAIR IN ROW";
    public static final String HIDDEN_PAIRS_IN_COL = "HIDDEN PAIR IN COL";
    public static final String HIDDEN_PAIRS_IN_QUAD = "HIDDEN PAIR IN QUAD";
    public static final String HIDDEN_TRIPLE_IN_ROW = "HIDDEN TRIPLE IN ROW";
    public static final String USER_PLACED = "USER PLACED";
    public static final String SYSTEM_POSSIBILITY_REMOVED = "SYSTEM REMOVED";
    private String algorithm;
    private Cell cell;
    private CellModStatus action;
    private int value;

    public SolverStep(@Nullable Cell cell, @Nullable CellModStatus action, int value, @Nullable String algorithm) {
        this.cell = cell;
        this.action = action;
        this.value = value;
        this.algorithm = algorithm;
    }

    /**
     * @return the cell
     */
    @Nullable
    public Cell getCell() {
        return cell;
    }

    /**
     * @param cell
     *            the cell to set
     */
    public void setCell(@Nullable Cell cell) {
        this.cell = cell;
    }

    /**
     * @return the action
     */
    @Nullable
    public CellModStatus getAction() {
        return action;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(@Nullable CellModStatus action) {
        this.action = action;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * @return the algorithm
     */
    @Nullable
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * @param algorithm
     *            the algorithm to set
     */
    public void setAlgorithm(@Nullable String algorithm) {
        this.algorithm = algorithm;
    }
}
