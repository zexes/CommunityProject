package com.zikozee.communityproject.route;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zikozee.communityproject.PaymentActivity;
import com.zikozee.communityproject.R;
import com.zikozee.communityproject.boarding.BoardingLocationRecyclerItemClickListener;

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

        holder.itemView.setOnClickListener(v -> mRecyclerView.addOnItemTouchListener(
                new BoardingLocationRecyclerItemClickListener(context, mRecyclerView ,new BoardingLocationRecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position1) {
                        // do whatever
                        Toast.makeText(context, mRoutes.get(position1).getStartState() +" chosen", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onLongItemClick(View view, int position1) {
                        // do whatever
                    }
                })
        ));

    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final Context context;
        TextView routeView;
        double price;

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
            routeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Intent intent = new Intent(context, PaymentActivity.class);
            intent.putExtra("fare_price", price);

            context.startActivity(intent);
        }
    }
}
