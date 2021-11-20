package com.multimed.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mainTitle;
    MediaPlayer mainTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mainTheme= MediaPlayer.create(StartActivity.this,R.raw.boomsday_project_maintheme);
        mainTheme.seekTo(0);
        mainTheme.start();
        mainTheme.setLooping(true);

        mainTitle = findViewById(R.id.activity_start_title);
        mainTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent startGame = new Intent(this, MainActivity.class);
        mainTheme.pause();
        startActivity(startGame);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mainTheme.seekTo(0);
        mainTheme.start();
    }
}