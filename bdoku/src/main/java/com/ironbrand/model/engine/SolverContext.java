package com.ironbrand.model.engine;

import androidx.annotation.Nullable;

import com.ironbrand.bdokusudoku.ValuesArray;

/**
 * @author bwinters
 *
 */
@SuppressWarnings("ALL")
public class SolverContext {

    private SolverTechnique technique;

    /**
     * Solver Context Constructor
     * @param technique
     */
    public SolverContext(@Nullable SolverTechnique technique) {
        this.technique = technique;
    }

    /**
     * Call execution interface to SolverTechniques
     * @param values
     */
    public void executeTechnique(@Nullable ValuesArray values) {
        this.technique.executeTechnique(values);
    }
}
