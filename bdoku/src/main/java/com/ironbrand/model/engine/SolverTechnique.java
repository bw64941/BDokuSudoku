package com.ironbrand.model.engine;

import androidx.annotation.Nullable;

import com.ironbrand.bdokusudoku.Cell;
import com.ironbrand.bdokusudoku.ValuesArray;

import java.util.ArrayList;

/**
 * @author bwinters
 *
 */
public interface SolverTechnique {

    void executeTechnique(@Nullable ValuesArray values);

    void processCellGrouping(@Nullable ArrayList<Cell> cells);

    enum CellModStatus {
        SET_VALUE, REMOVE_POSSIBILITY
    }
}
