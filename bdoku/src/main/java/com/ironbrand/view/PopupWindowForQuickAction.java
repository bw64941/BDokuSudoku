package com.ironbrand.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import androidx.annotation.Nullable;

import com.ironbrand.bdokusudoku.R;

@SuppressWarnings("ALL")
public class PopupWindowForQuickAction implements OnDismissListener {
    protected final View anchor;
    protected final PopupWindow window;
    protected final WindowManager windowManager;
    private View rootView;
    private Drawable background = null;

    /**
     * Create a QuickAction
     *
     * @param anchor the view that the QuickAction will be displaying 'from'
     */
    public PopupWindowForQuickAction(@Nullable View anchor) {
        this.anchor = anchor;
        this.window = new PopupWindow(anchor.getContext());

        // when a touch even happens outside of the window
        // make the window go away
        window.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    PopupWindowForQuickAction.this.window.dismiss();
                    v.performClick();
                    return true;
                }

                return false;
            }
        });

        windowManager = (WindowManager) anchor.getContext().getSystemService(Context.WINDOW_SERVICE);

        onCreate();
    }

    /**
     * Anything you want to have happen when created. Probably should create a
     * view and setup the event listeners on child views.
     */
    protected void onCreate() {
    }

    /**
     * In case there is stuff to do right before displaying.
     */
    protected void onShow() {
    }

    protected void preShow() {
        if (rootView == null) {
            throw new IllegalStateException("setContentView was not called with a view to display.");
        }

        onShow();

        if (background == null) {
            window.setBackgroundDrawable(new ColorDrawable(anchor.getContext().getColor(R.color.bdoku_dark_blue)));
        } else {
            window.setBackgroundDrawable(background);
        }

        // if using PopupWindow#setBackgroundDrawable this is the only values of
        // the width and height that make it work
        // otherwise you need to set the background of the root viewgroup
        // and set the popupwindow background to an empty BitmapDrawable

        window.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        window.setTouchable(true);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setOnDismissListener(this);
        window.setContentView(rootView);
    }

    public void setBackgroundDrawable(@Nullable Drawable background) {
        this.background = background;
    }

    /**
     * Sets the content view.
     *
     * @param root the view the popup will display
     */
    public void setContentView(@Nullable View root) {
        this.rootView = root;

        window.setContentView(root);
    }

    /**
     * Will inflate and set the view from a resource id
     *
     * @param layoutResID
     */
    public void setContentView(int layoutResID) {
        LayoutInflater inflator = (LayoutInflater) anchor.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setContentView(inflator.inflate(layoutResID, null));
    }

    /**
     * If you want to do anything when dismissed
     *
     * @param listener
     */
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        window.setOnDismissListener(listener);
    }

    /**
     * Displays like a popdown menu from the anchor view
     */
    public void showDropDown() {
        showDropDown(0, 0);
    }

    /**
     * Displays like a popdown menu from the anchor view.
     *
     * @param xOffset offset in X direction
     * @param yOffset offset in Y direction
     */
    public void showDropDown(int xOffset, int yOffset) {
        preShow();

        window.setAnimationStyle(R.style.Animations_PopDownMenu);
        // window.setAnimationStyle(R.style.Animations_PopDownMenu_Left);

        window.showAsDropDown(anchor, xOffset, yOffset);
    }

    /**
     * Displays like a QuickAction from the anchor view.
     */
    public void showLikeQuickAction() {
        showLikeQuickAction(0, 0);
    }

    /**
     * Displays like a QuickAction from the anchor view.
     *
     * @param xOffset offset in the X direction
     * @param yOffset offset in the Y direction
     */
    public void showLikeQuickAction(int xOffset, int yOffset) {
        preShow();

        window.setAnimationStyle(R.style.Animations_PopUpMenu_Center);

        int[] location = new int[2];
        anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

        rootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        rootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        int rootWidth = rootView.getMeasuredWidth();
        int rootHeight = rootView.getMeasuredHeight();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;

        int xPos = ((screenWidth - rootWidth) / 2) + xOffset;
        int yPos = anchorRect.top - rootHeight + yOffset;

        // display on bottom
        if (rootHeight > anchorRect.top) {
            yPos = anchorRect.bottom + yOffset;

            window.setAnimationStyle(R.style.Animations_PopDownMenu_Center);
        }

        window.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
    }

    public void dismiss() {
        window.dismiss();
    }

    @Override
    public void onDismiss() {
        window.dismiss();
    }
}