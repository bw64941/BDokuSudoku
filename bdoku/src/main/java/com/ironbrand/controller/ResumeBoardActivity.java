package com.ironbrand.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.ironbrand.bdokusudoku.BoardOpen;
import com.ironbrand.bdokusudoku.R;

/**
 * @author bwinters
 *
 */
public class ResumeBoardActivity extends Activity implements OnClickListener {

    /*
     * Creates the resume board Alert dialog for game.
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater layout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertDialog = layout.inflate(R.layout.resumeboardalert, null);

        Button noButton = alertDialog.findViewById(R.id.resumeNo);
        noButton.setOnClickListener(this);

        Button yesButton = alertDialog.findViewById(R.id.resumeYes);
        yesButton.setOnClickListener(this);

        Button deleteButton = alertDialog.findViewById(R.id.delete);
        deleteButton.setOnClickListener(this);

        setContentView(alertDialog);
    }

    @Override
    public void onClick(@Nullable View v) {
        if (v.getId() == R.id.resumeYes) {
            Intent boardActivity = new Intent(getApplicationContext(), BoardActivity.class);
            boardActivity.putExtra(BoardActivity.RESUME, true);
            startActivity(boardActivity);
            finish();
        } else if (v.getId() == R.id.resumeNo) {

            Intent boardChooser = new Intent(getApplicationContext(), DifficultyChooserActivity.class);
            startActivity(boardChooser);
            finish();
        } else if (v.getId() == R.id.delete) {

            BoardOpen opener = new BoardOpen(this);
            opener.deleteExistingFile();

            Intent difficultyChooser = new Intent(getApplicationContext(), DifficultyChooserActivity.class);
            startActivity(difficultyChooser);
            finish();
        }
    }
}
