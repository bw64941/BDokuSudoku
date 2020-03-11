package com.ironbrand.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ironbrand.bdokusudoku.R;

import java.util.Objects;

public class AboutActivity extends Activity {

    /*
     * Creates the about dialog for game.
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LayoutInflater layout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View aboutDialog = Objects.requireNonNull(layout).inflate(R.layout.about, null);

        TextView textView = aboutDialog.findViewById(R.id.about_content);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(getResources().getString(R.string.aboutDialogText));

        setContentView(aboutDialog);

    }
}
