/**
 *
 */
package com.ironbrand.model.engine;

import com.ironbrand.bdokusudoku.Cell;
import com.ironbrand.bdokusudoku.ValuesArray;

import java.util.ArrayList;

/**
 * @author bwinters
 *
 */
public interface SolverTechnique {

    void executeTechnique(ValuesArray values);

    void processCellGrouping(ArrayList<Cell> cells);

    enum CellModStatus {
        SET_VALUE, REMOVE_POSSIBILITY
    }
}
