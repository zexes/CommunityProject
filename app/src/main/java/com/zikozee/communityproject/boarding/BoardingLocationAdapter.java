package com.zikozee.communityproject.boarding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zikozee.communityproject.R;
import java.util.List;

public class BoardingLocationAdapter extends RecyclerView.Adapter<BoardingLocationAdapter.BoardingLocationViewHolder> {

    List<BoardingLocation> mBoardingLocations;

    public BoardingLocationAdapter(List<BoardingLocation> boardingLocations){
        this.mBoardingLocations =boardingLocations;
    }

    @NonNull
    @Override
    public BoardingLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_location, parent, false);
        return new BoardingLocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardingLocationViewHolder holder, int position) {
        BoardingLocation boardingLocation = mBoardingLocations.get(position);
        holder.bind(boardingLocation);
    }

    @Override
    public int getItemCount() {
        return mBoardingLocations.size();
    }

    public static class BoardingLocationViewHolder extends RecyclerView.ViewHolder{

        TextView stateName;
        TextView startLocations;

        public BoardingLocationViewHolder(@NonNull View itemView) {
            super(itemView);

            stateName = itemView.findViewById(R.id.stateName);
            startLocations = itemView.findViewById(R.id.startLocations);
//            imageView = itemView.findViewById(R.id.img_learning);
        }

        @SuppressLint("SetTextI18n")
        public void bind(BoardingLocation boardingLocation){
            stateName.setText(boardingLocation.getStateName());
            startLocations.setText(boardingLocation.getLocationStart());
        }
    }
}
