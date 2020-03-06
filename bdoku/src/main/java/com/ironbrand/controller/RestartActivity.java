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
import android.widget.RelativeLayout;

import com.ironbrand.bdokusudoku.R;

/**
 * @author bwinters
 *
 */
public class RestartActivity extends Activity {

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
        RelativeLayout alertDialog = (RelativeLayout) layout.inflate(R.layout.solved_popup, null);

        Button gameTimeButton = alertDialog.findViewById(R.id.gameTime_button);
        gameTimeButton.setText("Time " + getIntent().getStringExtra(BoardActivity.TIMER_VALUE));

        Button ok = alertDialog.findViewById(R.id.solvOkButton);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startSplash();
            }
        });

        setContentView(alertDialog);
    }

    private void startSplash() {
        Intent resumeIntent = new Intent(this, AndokuActivity.class);
        resumeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(resumeIntent);
    }
}
