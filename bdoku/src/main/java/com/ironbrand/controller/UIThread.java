package com.ironbrand.controller;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import androidx.annotation.Nullable;

import com.ironbrand.view.BoardView;

/**
 * @author bwinters
 *
 */
public class UIThread extends Thread {

    private SurfaceHolder surfaceHolder;
    /**
     * Time per frame for 60 FPS
     */
    private static final int MAX_FRAME_TIME = (int) (1000.0 / 30.0);
    private boolean isRunning = false;
    private final BoardView boardView;

    public UIThread(@Nullable SurfaceHolder surfaceHolder, @Nullable BoardView boardView) {
        this.surfaceHolder = surfaceHolder;
        this.boardView = boardView;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        long frameStartTime;
        long frameTime;

        while (isRunning) {
            frameStartTime = System.nanoTime();
            final Canvas canvas = surfaceHolder.lockCanvas(null);
            try {
                synchronized (surfaceHolder) {
                    if (canvas != null) {
                        boardView.doDraw(canvas, 0, 0);
                        if (boardView.isSolved()) {
                            isRunning = false;
                        }

                    }
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }


            // calculate the time required to draw the frame in ms
            frameTime = (System.nanoTime() - frameStartTime) / 1000000;

            if (frameTime < MAX_FRAME_TIME) // faster than the max fps - limit the FPS
            {
                try {
                    Thread.sleep(MAX_FRAME_TIME - frameTime);
                } catch (InterruptedException e) {
                    // ignore
                }
            }

        }
    }

    /**
     * @return the surfaceHolder
     */
    @Nullable
    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    /**
     * @param surfaceHolder
     *            the surfaceHolder to set
     */
    public void setSurfaceHolder(@Nullable SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

}
