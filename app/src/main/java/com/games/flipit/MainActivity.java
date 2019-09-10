package com.games.flipit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FlipInterface{

    private RecyclerView recyclerView;
    FlipAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<FlipDataObject> fdo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        fdo = new ArrayList<>();
        for (int i =0;i < 12; i++) {
            FlipDataObject obj = new FlipDataObject();
            obj.isFront = false;
            obj.image_id_back = R.drawable.ic_launcher_foreground;
            obj.image_id_front = R.drawable.a1;

            fdo.add(obj);
        }
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

    @Override
    public void onClickFlipIt(int position) {
        Log.e(MainActivity.class.getSimpleName(), "Isnside onclick interface");
        runThisOnClick(position);
    }
}
