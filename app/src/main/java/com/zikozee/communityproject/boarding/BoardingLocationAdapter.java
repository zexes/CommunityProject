package com.zikozee.communityproject.boarding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zikozee.communityproject.R;
import com.zikozee.communityproject.route.RouteFragment;

import java.util.List;

public class BoardingLocationAdapter extends RecyclerView.Adapter<BoardingLocationAdapter.BoardingLocationViewHolder> {

    List<BoardingLocation> mBoardingLocations;
    Context context;
    String chosenText;
    RecyclerView mRecyclerView;

    public BoardingLocationAdapter(List<BoardingLocation> boardingLocations, String chosenText){
        this.mBoardingLocations =boardingLocations;
        this.chosenText = chosenText;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public BoardingLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_location, parent, false);
        return new BoardingLocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardingLocationViewHolder holder, int position) {
        BoardingLocation boardingLocation = mBoardingLocations.get(position);
        holder.bind(boardingLocation);

        holder.itemView.setOnClickListener(v -> mRecyclerView.addOnItemTouchListener(
                new BoardingLocationRecyclerItemClickListener(context, mRecyclerView ,new BoardingLocationRecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position1) {
                        // do whatever
                        Toast.makeText(context, chosenText + " " + mBoardingLocations.get(position1).getStateName(), Toast.LENGTH_SHORT).show();

                        RouteFragment routeFragment = new RouteFragment(chosenText, mBoardingLocations.get(position1).getStateName());
                        FragmentManager manager =  ((AppCompatActivity) context).getSupportFragmentManager();
                        manager.beginTransaction()
                                .replace(R.id.route_holder_fragment, routeFragment, routeFragment.getTag())
                                .commit();
                    }

                    @Override public void onLongItemClick(View view, int position1) {
                        // do whatever
                    }
                })
        ));
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
