package com.games.flipit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements FlipInterface{

    private RecyclerView recyclerView;
    FlipAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<FlipDataObject> fdo;

    TextView tvPlayer1Score;
    TextView tvPlayer2Score;
    TextView tvPlayerTurnName;

    static PlayerObject PlayerOne;
    static PlayerObject PlayerTwo;

    HashMap<Integer, ArrayList<Integer>> mapping = new HashMap<>();

    FrameLayout flGameOver;
    TextView tvGameOver;

    Button btnRestart;
    TextView tvLevel;
    Button btnLevel;

    Handler mHandler = new Handler();
    Runnable mTask1 = new Runnable() {
        @Override
        public void run() {
            playerOneChanceDone();
        }
    };

    Runnable mtask2 = new Runnable() {
        @Override
        public void run() {
            playerTwoChancedone();
        }
    };

    int totalLevels = 3;
    int currentLevel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initViews();
        setRecyclerView(6);
        initPlayers(currentLevel);
    }

    private void initPlayers(int curr_level) {
        PlayerOne = new PlayerObject();
        PlayerTwo = new PlayerObject();
        PlayerOne.isTurn = true;
        tvPlayerTurnName.setText("Player 1");
        tvPlayer1Score.setText(PlayerOne.playerScore + "");
        tvPlayer2Score.setText(PlayerTwo.playerScore + "");
        mapping = new HashMap<>();
        tvLevel.setText("Level " + curr_level);
    }

    public void restartTheGame() {
        currentLevel = 1;
        setRecyclerView(6);
        initPlayers(currentLevel);
        flGameOver.setVisibility(View.GONE);
    }

    private void initViews() {
        tvPlayer1Score = findViewById(R.id.tv_player_1_score);
        tvPlayer2Score = findViewById(R.id.tv_player_2_score);
        tvPlayerTurnName = findViewById(R.id.tv_turn_by_player);
        flGameOver = findViewById(R.id.fl_game_over);
        tvGameOver = findViewById(R.id.tv_game_over);
        btnRestart = findViewById(R.id.btn_restart);
        tvLevel = findViewById(R.id.tv_level);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartTheGame();
            }
        });
        btnLevel = findViewById(R.id.btn_level);
        btnLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnLevel.getText().toString().equals("Exit")) {
                    Intent intent = new Intent(MainActivity.this, LaunchActivity.class);
                    startActivity(intent);
                }
                if (currentLevel < totalLevels) {
                    currentLevel++;
                    if (currentLevel == 2)
                        setRecyclerView(8);
                    if (currentLevel == 3)
                        setRecyclerView(10);
                    initPlayers(currentLevel);
                    flGameOver.setVisibility(View.GONE);
                } else {
                    restartTheGame();
                }
            }
        });
    }

    public int returnDrawable(int imageNum) {
        if (imageNum == 1) {
            return R.drawable.a1;
        }
        if (imageNum == 2) {
            return R.drawable.a2;
        }
        if (imageNum == 3) {
            return R.drawable.a3;
        }
        if (imageNum == 4) {
            return R.drawable.a4;
        }
        if (imageNum == 5) {
            return R.drawable.a5;
        }
        if (imageNum == 6) {
            return R.drawable.a6;
        }
        if (imageNum == 7) {
            return R.drawable.a7;
        }
        if (imageNum == 8) {
            return R.drawable.a8;
        }
        if (imageNum == 9) {
            return R.drawable.a9;
        }
        if (imageNum == 10) {
            return R.drawable.a10;
        } else {
            return R.drawable.a1;
        }
    }

    public ArrayList<FlipDataObject> assignImages(int totalNum) {
        ArrayList<Integer> listInt = new ArrayList<>();
        for (int i=1; i<=totalNum; i++) {
            listInt.add(i);
            listInt.add(i);
        }
        Collections.shuffle(listInt);
        for(int i=0; i<listInt.size(); i++) {
            if (mapping.containsKey(listInt.get(i))) {
                mapping.get(listInt.get(i)).add(i+1);
            } else {
                ArrayList<Integer> places = new ArrayList<>();
                places.add(i+1);
                mapping.put(listInt.get(i), places);

            }
            Log.e("Array Random : ", listInt.get(i) + "");
        }
        for (Integer image: mapping.keySet()){
            Log.e("Image Number:", image + "");
            for (int places: mapping.get(image)) {
                Log.e("Place:" , places + "");
            }
        }

        ArrayList<FlipDataObject> listFlipData = new ArrayList<>();
        for(int eachNum: listInt) {
            FlipDataObject fdo = new FlipDataObject();
            fdo.isFront = true;
            fdo.image_id_front = returnDrawable(eachNum);
            fdo.image_id_back = R.color.cardview_dark_background;
            fdo.imageNum = eachNum;
            listFlipData.add(fdo);
        }
        return listFlipData;
    }

    private void setRecyclerView(int totalNum) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        fdo = assignImages(totalNum);

        mAdapter = new FlipAdapter(this, fdo, this);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(mAdapter);
    }

    public void  runThisOnClick(int position) {
        EventData data = new EventData();
        FlipDataObject flipDataObject = mAdapter.getDataObject(position);
        if (flipDataObject.isFront) {
            data.setFront(false);
        } else {
            data.setFront(true);
        }
        data.isSaveData = false;
        data.setPosition(position);
        EventBus.getDefault().post(data);
    }

    public void playerOneChanceDone() {
        // Check for points here if to be added for not
        if (PlayerOne.openedTypes[0] == PlayerOne.openedTypes[1]) {
            PlayerOne.playerScore ++;
            // Disable clicks on those tiles now
            mAdapter.setIsDisable(PlayerOne.openedPlaces[0], true);
            mAdapter.setIsDisable(PlayerOne.openedPlaces[1], true);

            tvPlayer1Score.setText(PlayerOne.playerScore + "");
        } else {
            // unflip those
            runThisOnClick(PlayerOne.openedPlaces[0]);
            runThisOnClick(PlayerOne.openedPlaces[1]);

            mAdapter.setIsDisable(PlayerOne.openedPlaces[0], false);
            mAdapter.setIsDisable(PlayerOne.openedPlaces[1], false);
        }

        //Animation for chance of player 2
        tvPlayerTurnName.setText("Player 2");

        // reset player one
        PlayerOne.totalOpenedTiles = 0;
        PlayerOne.openedPlaces = new int[2];
        PlayerOne.openedTypes = new int[2];

        // Change chance to Player 2.
        PlayerOne.isTurn = false;
        PlayerTwo.isTurn = true;

        checkforGameOver();
    }

    public void playerTwoChancedone() {
        // Check for points here if to be added for not
        if (PlayerTwo.openedTypes[0] == PlayerTwo.openedTypes[1]) {
            PlayerTwo.playerScore ++;
            // Disable clicks on those tiles now
            mAdapter.setIsDisable(PlayerTwo.openedPlaces[0], true);
            mAdapter.setIsDisable(PlayerTwo.openedPlaces[1], true);

            tvPlayer2Score.setText(PlayerTwo.playerScore + "");
        } else {
            // unflip those
            runThisOnClick(PlayerTwo.openedPlaces[0]);
            runThisOnClick(PlayerTwo.openedPlaces[1]);

            mAdapter.setIsDisable(PlayerTwo.openedPlaces[0], false);
            mAdapter.setIsDisable(PlayerTwo.openedPlaces[1], false);
        }

        //Animation for chance of player 1
        tvPlayerTurnName.setText("Player 1");

        // reset player two
        PlayerTwo.totalOpenedTiles = 0;
        PlayerTwo.openedPlaces = new int[2];
        PlayerTwo.openedTypes = new int[2];

        // Change chance to Player 2.
        PlayerOne.isTurn = true;
        PlayerTwo.isTurn = false;

        checkforGameOver();
    }


    @Override
    public void onClickFlipIt(int position) {
        FlipDataObject flipDataObject = mAdapter.getDataObject(position);

        if (!flipDataObject.isDisable) {
            if (PlayerOne.isTurn) {
                runThisOnClick(position);
                PlayerOne.openedPlaces[PlayerOne.totalOpenedTiles] = position;
                PlayerOne.openedTypes[PlayerOne.totalOpenedTiles] = flipDataObject.imageNum;
                //Disable click on already flipped ones
                mAdapter.setIsDisable(PlayerOne.openedPlaces[PlayerOne.totalOpenedTiles], true);

                PlayerOne.totalOpenedTiles += 1;

                if (PlayerOne.totalOpenedTiles == 2) {
                    mHandler.postDelayed(mTask1, 1000);
                }
            }
            if (PlayerTwo.isTurn) {
                runThisOnClick(position);
                PlayerTwo.openedPlaces[PlayerTwo.totalOpenedTiles] = position;
                PlayerTwo.openedTypes[PlayerTwo.totalOpenedTiles] = flipDataObject.imageNum;
                //Disable click on already flipped ones
                mAdapter.setIsDisable(PlayerTwo.openedPlaces[PlayerTwo.totalOpenedTiles], true);

                PlayerTwo.totalOpenedTiles += 1;

                if (PlayerTwo.totalOpenedTiles == 2) {
                    mHandler.postDelayed(mtask2, 1000);
                }
            }

        } else {
            checkforGameOver();
        }
    }


    public void checkforGameOver() {
        if (mAdapter.isGameOver()) {
            flGameOver.setVisibility(View.VISIBLE);
            if (currentLevel==3) {
                btnLevel.setText("Exit");
            } else {
                btnLevel.setText("Next Level");
            }
            if (PlayerOne.playerScore > PlayerTwo.playerScore) {
                tvGameOver.setText("Player 1 Wins");
            } else if (PlayerTwo.playerScore > PlayerOne.playerScore){
                tvGameOver.setText("Player 2 Wins");
            } else {
                tvGameOver.setText("Game Over. No One wins");
            }
        }
    }
}
