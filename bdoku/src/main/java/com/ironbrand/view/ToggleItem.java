package com.ironbrand.view;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CompoundButton.OnCheckedChangeListener;

import androidx.annotation.Nullable;

/**
 * Action item, displayed as menu with icon and text.
 *
 * @author Yuki Anzai
 */
class ToggleItem {
    private Drawable icon;
    private String title;
    private boolean checked;
    private OnCheckedChangeListener listener;

    /**
     * Constructor
     */
    public ToggleItem() {
    }

    /**
     * Constructor
     *
     * @param icon {@link Drawable} action icon
     */
    public ToggleItem(@Nullable Drawable icon) {
        this.icon = icon;
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
     * Set action icon
     *
     * @param icon {@link Drawable} action icon
     */
    public void setIcon(@Nullable Drawable icon) {
        this.icon = icon;
    }

    /**
     * Set on click listener
     *
     * @param listener on click listener {@link View.OnClickListener}
     */
    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Get on click listener
     *
     * @return on click listener {@link View.OnClickListener}
     */
    @Nullable
    public OnCheckedChangeListener getListener() {
        return this.listener;
    }

    /**
     * @return the checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}