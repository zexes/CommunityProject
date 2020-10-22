package com.zikozee.communityproject.route;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zikozee.communityproject.PaymentActivity;
import com.zikozee.communityproject.R;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    List<Route> mRoutes;
    RecyclerView mRecyclerView;
    Context context;

    public RouteAdapter(List<Route> routes) {
        this.mRoutes = routes;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RouteAdapter.RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
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

    public static class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final Context context;
        TextView routeView;
        double price;
        private String vendor;
        private String startLocation;
        private String destinationCity;
        private String destinationState;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            routeView = itemView.findViewById(R.id.routeView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Route route){
            routeView.setText(route.getStartState() + " -->> " + route.getDestinationCity()
                + "         \u20A6" + route.getFarePrice());

            price = route.getFarePrice();
            vendor = route.getVendorName();
            startLocation =route.getBoardingLocation();
            destinationCity = route.getDestinationCity();
            destinationState = route.getStartState();
            routeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Intent intent = new Intent(context, PaymentActivity.class);
            intent.putExtra("fare_price", price);
            intent.putExtra("vendor", vendor);
            intent.putExtra("startLocation", startLocation);
            intent.putExtra("destinationCity", destinationCity);
            intent.putExtra("destinationState", destinationState);

            context.startActivity(intent);
        }
    }
}
