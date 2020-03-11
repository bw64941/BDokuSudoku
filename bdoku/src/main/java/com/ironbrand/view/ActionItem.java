package com.ironbrand.view;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.Nullable;

class ActionItem {
    private Drawable icon;
    private String title;
    private OnClickListener listener;

    /**
     * Constructor
     */
    public ActionItem() {
    }

    /**
     * Get action title
     *
     * @return action title
     */
    @Nullable
    public String getTitle() {
        return this.title;
    }

    /**
     * Set action title
     *
     * @param title action title
     */
    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    /**
     * Get action icon
     *
     * @return {@link Drawable} action icon
     */
    @Nullable
    public Drawable getIcon() {
        return this.icon;
    }

    /**
     * Set on click listener
     *
     * @param listener on click listener {@link View.OnClickListener}
     */
    public void setOnClickListener(@Nullable OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * Get on click listener
     *
     * @return on click listener {@link View.OnClickListener}
     */
    @Nullable
    public OnClickListener getListener() {
        return this.listener;
    }
}