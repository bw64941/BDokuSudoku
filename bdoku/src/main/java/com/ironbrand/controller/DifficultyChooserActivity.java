/**
 *
 */
package com.ironbrand.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.ironbrand.bdokusudoku.R;

/**
 * @author bwinters
 *
 */
public class DifficultyChooserActivity extends Activity {

    /*
     * Creates the resume board Alert dialog for game.
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LayoutInflater layout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertDialog = layout.inflate(R.layout.boardchooseralert, null);

        final int rc = 0;
        Button easyButton = alertDialog.findViewById(R.id.easyButton);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent boardActivity = new Intent(getApplicationContext(), BoardActivity.class);
                boardActivity.putExtra(BoardActivity.DIFFICULTY_CHOSEN, BoardActivity.BOARD_DIFFICULTY_EASY);
                boardActivity.putExtra(BoardActivity.RESUME, false);
                startActivityForResult(boardActivity, rc);
            }
        });

        Button mediumButton = alertDialog.findViewById(R.id.mediumButton);
        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent boardActivity = new Intent(getApplicationContext(), BoardActivity.class);
                boardActivity.putExtra(BoardActivity.DIFFICULTY_CHOSEN, BoardActivity.BOARD_DIFFICULTY_MEDIUM);
                boardActivity.putExtra(BoardActivity.RESUME, false);
                startActivityForResult(boardActivity, rc);
            }
        });

        Button hardButton = alertDialog.findViewById(R.id.hardButton);
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent boardActivity = new Intent(getApplicationContext(), BoardActivity.class);
                boardActivity.putExtra(BoardActivity.DIFFICULTY_CHOSEN, BoardActivity.BOARD_DIFFICULTY_HARD);
                boardActivity.putExtra(BoardActivity.RESUME, false);
                startActivityForResult(boardActivity, rc);
            }
        });
        setContentView(alertDialog);
    }
}
