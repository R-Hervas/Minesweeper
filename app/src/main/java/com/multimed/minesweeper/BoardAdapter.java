package com.multimed.minesweeper;

import static com.multimed.minesweeper.R.*;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.mineBoxViewHolder> {

    private int bombDrawable;
    private List<Box> boxes;
    private OnBoxClickListener boxListener;

    public BoardAdapter(List<Box> boxes, OnBoxClickListener boxListener, int bombDrawable) {
        this.boxes = boxes;
        this.boxListener = boxListener;
        this.bombDrawable = bombDrawable;
    }

    @NonNull
    @Override
    public mineBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layout.item_box, parent, false);
        return new mineBoxViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull mineBoxViewHolder holder, int position) {
        holder.bind(boxes.get(position));
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return boxes.size();
    }

    public void setBoxes(List<Box> boxes) {
        this.boxes = boxes;
        notifyDataSetChanged();
    }

    class mineBoxViewHolder extends RecyclerView.ViewHolder {

        TextView valueTextView;

        public mineBoxViewHolder(@NonNull View itemView) {
            super(itemView);
            valueTextView = itemView.findViewById(id.item_box_value);
        }

        @SuppressLint("ResourceAsColor")
        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void bind(final Box box) {

            itemView.setBackgroundResource(drawable.metallic_background);

            itemView.setOnClickListener(View -> {
                boxListener.onBoxClick(box);
            });

            itemView.setOnLongClickListener(View -> {
                boxListener.onBoxLongClick(box);
                return true;
            });

            if (box.isRevealed()) {

                if (box.getValue() == Box.BOMB) {
                    itemView.setBackgroundResource(bombDrawable);
                } else if (box.getValue() == Box.BLANK) {
                    valueTextView.setText("");
                    itemView.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    valueTextView.setText(String.valueOf(box.getValue()));
                    switch (box.getValue()) {
                        case 1:
                            valueTextView.setTextColor(Color.parseColor("#3FA4FF"));
                            itemView.setBackgroundResource(drawable.blast_crater);
                            break;
                        case 2:
                            valueTextView.setTextColor(Color.GREEN);
                            itemView.setBackgroundResource(drawable.blast_crater);
                            break;
                        default:
                            valueTextView.setTextColor(Color.parseColor("#FF1333"));
                            itemView.setBackgroundResource(drawable.blast_crater);
                            break;
                    }
                }

            } else if  (box.isFlagged()) {
                valueTextView.setBackgroundResource(drawable.secret_icon);
            }
        }


    }
}
