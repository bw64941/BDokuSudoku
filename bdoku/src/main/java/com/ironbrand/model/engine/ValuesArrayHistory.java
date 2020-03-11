package com.ironbrand.model.engine;

import androidx.annotation.Nullable;

import java.util.Stack;

/**
 * @author bwinters
 *
 */
public class ValuesArrayHistory extends Stack<SolverStep> {

    private static final long serialVersionUID = 4484739358617232468L;

    public ValuesArrayHistory() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Stack#peek()
     */
    @Nullable
    @Override
    public synchronized SolverStep peek() {
        return super.peek();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Stack#pop()
     */
    @Nullable
    @Override
    public synchronized SolverStep pop() {
        return super.pop();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Stack#push(java.lang.Object)
     */
    @Nullable
    @Override
    public SolverStep push(@Nullable SolverStep step) {
        return super.push(step);
    }
}
