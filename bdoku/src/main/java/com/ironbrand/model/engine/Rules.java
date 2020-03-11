package com.ironbrand.model.engine;


@SuppressWarnings("ALL")
public interface Rules {

    /**
     * Runs methods to check row, column, and quadrant.
     *
     * @param valueToCheck
     */
    boolean runRules(int row, int col, int quadrant, int valueToCheck);

    /**
     * Returns whether or not the specified value can be placed in the given
     * cell with respect to the other values in the same row.
     *
     * @param tempValue
     * @return
     */
    boolean isRowOK(int row, int tempValue);

    /**
     * Returns whether or not the specified value can be placed in the given
     * cell with respect to the other values in the same column.
     *
     * @param tempValue
     * @return
     */
    boolean isColOK(int col, int tempValue);

    /**
     * Returns whether or not the specified value can be placed in the given
     * cell with respect to the other values in the same quadrant.
     *
     * @param tempValue
     * @return
     */
    boolean isQuadOK(int quad, int tempValue);
}
