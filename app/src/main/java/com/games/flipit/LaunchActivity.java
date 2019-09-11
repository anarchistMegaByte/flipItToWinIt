package com.games.flipit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity {

    Button btnStart;
    Button btnInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);
        getSupportActionBar().hide();
        btnStart = findViewById(R.id.btn_start_game);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentStart = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intentStart);
            }
        });
        btnInstructions = findViewById(R.id.btn_show_instructions);
        btnInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentStart = new Intent(LaunchActivity.this, InstructionsActivity.class);
                startActivity(intentStart);
            }
        });
    }
}
