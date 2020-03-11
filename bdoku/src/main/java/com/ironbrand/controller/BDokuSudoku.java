package com.ironbrand.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ironbrand.bdokusudoku.BoardOpen;
import com.ironbrand.bdokusudoku.R;

import java.io.FileNotFoundException;

public class BDokuSudoku extends Activity implements OnClickListener {

    /*
     * Main Activity for BDokuSudoku Game
     *
     * Obtain 3 splash screen button from view and assign onClickListeners.
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        ImageView layout = findViewById(R.id.splashImage);
        layout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_grow_from_top));

        TextView logo = findViewById(R.id.splashNameLogo);
        logo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_grow_from_top));

        /*
         * Assign onClickListeners to each button on splash screen.
         */
        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    }

    /*
     * Default onResume action
     *
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /*
     * Default onPause action
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /*
     * Determine button press and call appropriate action.
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(@NonNull View componentPressed) {
        if (componentPressed.getId() == R.id.about_button) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if (componentPressed.getId() == R.id.new_button) {
            if (doesInProgressBoardExist()) {
                Intent resumeIntent = new Intent(this, ResumeBoardActivity.class);
                resumeIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(resumeIntent);
            } else {
                Intent boardChooser = new Intent(getApplicationContext(), DifficultyChooserActivity.class);
                boardChooser.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(boardChooser);
            }
        } else if (componentPressed.getId() == R.id.exit_button) {
            this.finish();
        }
    }

    /*
     * Checks for already in progress board file
     */
    private boolean doesInProgressBoardExist() {
        boolean boardFound = true;

        try {
            openFileInput(BoardOpen.SAVED_IN_PROGRESS_FILE_NAME);
        } catch (FileNotFoundException e) {
            boardFound = false;
        }

        return boardFound;
    }
}