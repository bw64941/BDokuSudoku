package com.ironbrand.model.engine;

import androidx.annotation.Nullable;

import com.ironbrand.bdokusudoku.Cell;
import com.ironbrand.bdokusudoku.ValuesArray;
import com.ironbrand.model.engine.SolverTechnique.CellModStatus;

import java.util.Observable;
import java.util.Observer;

/**
 * @author bwinters
 *
 */
public class CellChangeObserver implements Observer {

    private final ValuesArray values;

    /*
     * BoardChangeListener
     */
    public CellChangeObserver(@Nullable ValuesArray values) {
        this.values = values;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public synchronized void update(@Nullable Observable o, @Nullable Object cellChanged) {
        /*
         * If cell object passed in, then setValue or removePossibility was
         * called from solver.
         */
        if (cellChanged instanceof Cell) {
            Cell cell = (Cell) cellChanged;

            /*
             * Remove the recently placed values from that cell's quadrant, row,
             * and column.
             */
            for (Cell areaCell : values.getCellsInSameRowORColORQuad(cell)) {
                if (!areaCell.equals(cellChanged) && !areaCell.isLocked() && areaCell.isEmpty()) {
                    areaCell.removePossibility(cell.getValue());
                    values.getHistory().push(
                            new SolverStep(areaCell, CellModStatus.REMOVE_POSSIBILITY, cell.getValue(), SolverStep.SYSTEM_POSSIBILITY_REMOVED));
                }
            }
        }
    }

}
