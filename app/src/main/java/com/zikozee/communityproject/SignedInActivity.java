package com.zikozee.communityproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zikozee.communityproject.location.LocationFragment;
import com.zikozee.communityproject.models.Location;
import com.zikozee.communityproject.models.State;
import com.zikozee.communityproject.models.Vendor;
import com.zikozee.communityproject.route.RouteFragment;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SignedInActivity extends AppCompatActivity{
    public static final String TAG = "SignedInActivity";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        LocationFragment locationFragment = new LocationFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.location_holder_fragment, locationFragment, locationFragment.getTag())
                .commit();

        RouteFragment routeFragment = new RouteFragment();
        manager.beginTransaction()
                .replace(R.id.route_holder_fragment, routeFragment, routeFragment.getTag())
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.optionSignOut:
                signOut();
                return true;
            case R.id.optionAccountSettings:
                intent = new Intent(SignedInActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut(){
        Log.d(TAG, "signOut: signing out");
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
//        PopulateFireStore();
        getVendorsByName();
    }


    //place in every activity to ensure user is authenticated
    private void checkAuthenticationState(){
        Log.d(TAG, "checkAuthenticationState: checking authentication state.");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Log.d(TAG, "checkAuthenticationState: user is null, navigating back to login screen.");

            Intent intent = new Intent(SignedInActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//THIS FLAG CLEARS OUT THE ACTIVITY STACK SO USER CANNOT PRESS BACK BUTTON
            startActivity(intent);
            finish();
        }else{
            Log.d(TAG, "checkAuthenticationState: user is authenticated.");
        }
    }

    private void PopulateFireStore(){
        Location location = new Location.Builder()
                .start("yaba")
                .destinationCity("benin")
                .destinationState("Edo")
                .fare_price(5000.0)
                .build();

        Location location2 = new Location.Builder()
                .start("Ikorodu")
                .destinationCity("Maiduguri")
                .destinationState("Edo")
                .fare_price(4800.05)
                .build();

        State state = new State.Builder()
                .location(new ArrayList<>(Arrays.asList(location, location2)))
                .name("Lagos")
                .build();

        State state2 = new State.Builder()
                .location(new ArrayList<>(Arrays.asList(location, location2)))
                .name("Edo")
                .build();
        Vendor vendor1 = new Vendor.Builder()
                .vendorName("God is Good")
                .state(new ArrayList<>(Arrays.asList(state, state2)))
                .headOfficeContact("24 sakpomba road benin-city Edo state")
                .build();


        Location location3 = new Location.Builder()
                .start("yaba")
                .destinationCity("benin")
                .destinationState("Edo")
                .fare_price(5000.0)
                .build();

        Location location4 = new Location.Builder()
                .start("Ikorodu")
                .destinationCity("Maiduguri")
                .destinationState("Edo")
                .fare_price(4800.05)
                .build();

        State state3 = new State.Builder()
                .location(new ArrayList<>(Arrays.asList(location3, location4)))
                .name("Lagos")
                .build();

        State state4 = new State.Builder()
                .location(new ArrayList<>(Arrays.asList(location, location2)))
                .name("Edo")
                .build();

        Vendor vendor2 = new Vendor.Builder()
                .vendorName("Ifesinachi Transport Ltd")
                .state(new ArrayList<>(Arrays.asList(state3, state4)))
                .headOfficeContact("23 Godwills road yaba lagos")
                .build();

        List<Vendor> vendors = new ArrayList<>(Arrays.asList(vendor1,vendor2));

        for(Vendor vendor: vendors){
            mDatabase.child("vendors")
//                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .push()
                    .setValue(vendor);
        }

    }

    private void getVendorsByName() {
        Query query1 = mDatabase.child("vendors/names")
                .orderByKey();
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> values = (Map<String, String>)snapshot.getValue();
                for(Entry<String, String> entry: values.entrySet()){
                    Log.d(TAG, entry.getValue());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}