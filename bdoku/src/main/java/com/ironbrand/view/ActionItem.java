package com.ironbrand.view;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;

public class ActionItem {
    private Drawable icon;
    private String title;
    private OnClickListener listener;

    /**
     * Constructor
     */
    public ActionItem() {
    }

    /**
     * Constructor
     *
     * @param icon {@link Drawable} action icon
     */
    public ActionItem(Drawable icon) {
        this.icon = icon;
    }

    /**
     * Get action title
     *
     * @return action title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Set action title
     *
     * @param title action title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get action icon
     *
     * @return {@link Drawable} action icon
     */
    public Drawable getIcon() {
        return this.icon;
    }

    /**
     * Set action icon
     *
     * @param icon {@link Drawable} action icon
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /**
     * Set on click listener
     *
     * @param listener on click listener {@link View.OnClickListener}
     */
    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * Get on click listener
     *
     * @return on click listener {@link View.OnClickListener}
     */
    public OnClickListener getListener() {
        return this.listener;
    }
}