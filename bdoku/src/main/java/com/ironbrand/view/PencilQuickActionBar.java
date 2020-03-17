package com.ironbrand.view;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;

import androidx.annotation.Nullable;

import com.ironbrand.bdokusudoku.Board;
import com.ironbrand.bdokusudoku.R;

import java.util.ArrayList;

/**
 * @author bwinters
 *
 */
public class PencilQuickActionBar extends QuickAction implements OnDismissListener {

    private final ArrayList<Integer> pencilValues;
    private final ArrayList<ToggleItem> valuesArray = new ArrayList<>();
    private final ToggleItem allActionItem = new ToggleItem();

    public PencilQuickActionBar(@Nullable View anchor, @Nullable ArrayList<Integer> userPencilValues) {
        super(anchor, R.layout.quickaction, STYLE_TOGGLE, R.layout.user_pencil_btn);
        this.pencilValues = userPencilValues;
        obtainPencilButtons();
        this.setOnDismissListener(this);
    }

    /*
     * Obtain buttons from view xml.
     */
    private void obtainPencilButtons() {
        if (pencilValues.size() == 9) {
            allActionItem.setChecked(true);
        }

        allActionItem.setTitle(anchor.getContext().getResources().getString(R.string.allButtonText));

        for (int i = 1; i <= Board.ROWS; i++) {
            final ToggleItem item = new ToggleItem();
            item.setTitle(String.valueOf(i));
            if (pencilValues.contains(i)) {
                item.setChecked(true);
            }
            valuesArray.add(item);
        }

        populatePencilBarListeners();
    }

    /**
     * Populate action bar with 1-9 action items.
     */
    private void populatePencilBarListeners() {
        allActionItem.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                allActionItem.setChecked(true);
                for (ToggleItem possibility : valuesArray) {
                    possibility.setChecked(isChecked);
                }
                dismiss();
            }
        });
        this.addToggleItem(allActionItem);

        for (final ToggleItem possibility : valuesArray) {
            possibility.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    possibility.setChecked(isChecked);
                }
            });
            this.addToggleItem(possibility);
        }

    }

    @Override
    public void onDismiss() {
        super.dismiss();

        pencilValues.clear();

        for (ToggleItem possibility : valuesArray) {
            if (possibility.isChecked()) {
                pencilValues.add(Integer.parseInt(possibility.getTitle()));
            }
        }
    }
}
