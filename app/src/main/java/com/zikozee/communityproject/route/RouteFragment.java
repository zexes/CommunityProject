package com.zikozee.communityproject.route;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zikozee.communityproject.ApiUtil;
import com.zikozee.communityproject.R;
import com.zikozee.communityproject.models.State;
import com.zikozee.communityproject.models.Vendor;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteFragment extends Fragment {

    public static final String TAG = "ROUTE_FRAGMENT";

    private ProgressBar mLoadingProgress;
    private RecyclerView myRecyclerView;
    private String chosenText;
    private String currentState;
    private TextView textView;

    View v;

    public RouteFragment() {
        // Required empty public constructor
    }

    public RouteFragment(String chosen, String currentState) {
        this.chosenText = chosen;
        this.currentState = currentState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_route, container, false);

        mLoadingProgress = v.findViewById(R.id.pb_loading_route);
        mLoadingProgress.setVisibility(View.VISIBLE);

        myRecyclerView = v.findViewById(R.id.route_recyclerview);
        LinearLayoutManager learningLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        myRecyclerView.setLayoutManager(learningLayoutManager);

        try {
            URL vendorsURL = ApiUtil.buildUrl("/vendors");
            new RouteFragment.VendorQueryTask().execute(vendorsURL);
        }catch (Exception e){
            Log.d("error", e.getMessage());
        }

        return v;
    }

    public class VendorQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String result = null;

            try{
                result = ApiUtil.getJson(searchURL);
            }catch (Exception e){
                Log.e("error", e.toString());
            }

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            TextView error = v.findViewById(R.id.error_route);
            mLoadingProgress.setVisibility(View.GONE);
            if(error == null){
                myRecyclerView.setVisibility(View.INVISIBLE);
                error.setVisibility(View.VISIBLE);
            }else{
                myRecyclerView.setVisibility(View.VISIBLE);
                error.setVisibility(View.INVISIBLE);
            }
            List<Vendor> vendors = ApiUtil.getVendorFromJson(result);


            List<Route> routes = null;
            for(Vendor vendor: vendors){
                if(vendor.getName().equals(chosenText)){

                    List<State> states = ApiUtil.getStatesFromJson(vendor.getState());

                    routes = states.stream()
                            .filter(state -> state.getName().equals(currentState))
                            .map(state -> Route.builder()
                                    .setStartState(state.getName())
                                    .setDestinationCity(state.getDestinationCity())
                                    .setFarePrice(state.getFarePrice())
                                    .setVendorName(chosenText)
                                    .setBoardingLocation(state.getStartLocation())
                                    .build())
                            .collect(Collectors.toList());
                    break;
                }

            }

            Log.d(TAG, routes.toString());


            RouteAdapter adapter = new RouteAdapter(routes);
            myRecyclerView.setAdapter(adapter);
        }
    }
}