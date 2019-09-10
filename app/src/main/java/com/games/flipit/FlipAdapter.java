package com.games.flipit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class FlipAdapter extends RecyclerView.Adapter<FlipAdapter.FlipViewHolder> {

    public ArrayList<FlipDataObject> allObjects;
    private Context context;
    RelativeLayout[] card_back_arr, card_front_arr;
    FlipInterface flipInterfaceInAdapter;

    public FlipAdapter(Context context, ArrayList<FlipDataObject> flipDataObjects, FlipInterface flipHere) {
        this.flipInterfaceInAdapter = flipHere;
        this.allObjects = flipDataObjects;
        this.context = context;
        card_back_arr = new RelativeLayout[flipDataObjects.size()];
        card_front_arr = new RelativeLayout[flipDataObjects.size()];
        EventBus.getDefault().register(this);
    }

    public boolean isGameOver() {
        for (FlipDataObject fdo: allObjects) {
            if (!fdo.isDisable) {
                return false;
            }
        }
        return true;
    }

    public void setIsDisable(int position, boolean isDisable) {
        allObjects.get(position).isDisable = isDisable;
    }

    public FlipDataObject getDataObject(int position) {
        return allObjects.get(position);
    }

    @NonNull
    @Override
    public FlipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("onCreateVH", "Creatin ciews");
        return new FlipViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flip_view, parent, false), flipInterfaceInAdapter);

    }

    @Override
    public void onBindViewHolder(@NonNull FlipViewHolder holder, int position) {
        holder.iv_front.setImageResource(allObjects.get(position).image_id_front);
        holder.iv_back.setImageResource(allObjects.get(position).image_id_back);
        card_back_arr[position] = holder.card_back;
        card_front_arr[position] = holder.card_front;
    }

    @Override
    public int getItemCount() {
        return allObjects.size();
    }

    @Subscribe
    public void onEvent(EventData data) {
        Log.e("onEvent", "Recieved " + data.isSaveData + data.isFront());
        if (data.isSaveData) {
            allObjects.get(data.getPosition()).isFront = data.isFront();
        } else {
            allObjects.get(data.getPosition()).isFront = data.isFront();
            FlipAnimator.flipView(context, card_back_arr[data.getPosition()], card_front_arr[data.getPosition()], data.isFront(), data.getPosition());
        }
    }

    public static class FlipViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        AppCompatImageView iv_front, iv_back;
        RelativeLayout card_back, card_front;
        FlipInterface flipInterface;
        CardView cardView;
        public FlipViewHolder(@NonNull View itemView, FlipInterface flipHere) {
            super(itemView);
            iv_front = itemView.findViewById(R.id.iv_item_front);
            iv_back = itemView.findViewById(R.id.iv_item_back);
            card_front = (RelativeLayout) itemView.findViewById(R.id.card_front);
            card_back = (RelativeLayout) itemView.findViewById(R.id.card_back);
            flipInterface = flipHere;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.e(FlipViewHolder.class.getSimpleName(), "Clicked in VH");
            flipInterface.onClickFlipIt(getAdapterPosition());
        }
    }
}
