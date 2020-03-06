package com.ironbrand.model.engine;


public interface Rules {

    /**
     * Runs methods to check row, column, and quadrant.
     *
     * @param cell
     * @param valueToCheck
     */
    boolean runRules(int row, int col, int quadrant, int valueToCheck);

    /**
     * Returns whether or not the specified value can be placed in the given
     * cell with respect to the other values in the same row.
     *
     * @param cells
     * @param cell
     * @param tempValue
     * @return
     */
    boolean isRowOK(int row, int tempValue);

    /**
     * Returns whether or not the specified value can be placed in the given
     * cell with respect to the other values in the same column.
     *
     * @param cells
     * @param cell
     * @param tempValue
     * @return
     */
    boolean isColOK(int col, int tempValue);

    /**
     * Returns whether or not the specified value can be placed in the given
     * cell with respect to the other values in the same quadrant.
     *
     * @param cells
     * @param cell
     * @param tempValue
     * @return
     */
    boolean isQuadOK(int quad, int tempValue);
}
