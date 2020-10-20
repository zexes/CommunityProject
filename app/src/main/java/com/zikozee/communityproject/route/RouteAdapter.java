package com.zikozee.communityproject.route;

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

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    List<Route> mRoutes;

    public RouteAdapter(List<Route> routes) {
        this.mRoutes = routes;
    }

    @NonNull
    @Override
    public RouteAdapter.RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_route, parent, false);
        return new RouteAdapter.RouteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteAdapter.RouteViewHolder holder, int position) {
        Route route = mRoutes.get(position);
        holder.bind(route);
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder{
        TextView routeView;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            routeView = itemView.findViewById(R.id.routeView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Route route){
            routeView.setText(route.getStartState() + " -->> " + route.getDestinationCity());
        }
    }
}
