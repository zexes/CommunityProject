package com.zikozee.communityproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zikozee.communityproject.boarding.BoardingLocationFragment;
import com.zikozee.communityproject.models.Vendor;
import com.zikozee.communityproject.route.RouteFragment;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class SignedInActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "SignedInActivity";
    private String chosenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
        try {
            URL vendorURL = ApiUtil.buildUrl("/vendors");
            new VendorQueryTask().execute(vendorURL);
        }catch (Exception e){
            Log.d("error", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.optionSignOut:
                signOut();
                intent = new Intent(SignedInActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.optionAccountSettings:
                intent = new Intent(SignedInActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        Log.d(TAG, "signOut: signing out");
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
//        populateFireStore();
    }


    //place in every activity to ensure user is authenticated
    private void checkAuthenticationState() {
        Log.d(TAG, "checkAuthenticationState: checking authentication state.");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Log.d(TAG, "checkAuthenticationState: user is null, navigating back to login screen.");

            Intent intent = new Intent(SignedInActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//THIS FLAG CLEARS OUT THE ACTIVITY STACK SO USER CANNOT PRESS BACK BUTTON
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "checkAuthenticationState: user is authenticated.");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosenText = parent.getItemAtPosition(position).toString();
        Log.d(TAG, "data is " + chosenText);
        Toast.makeText(parent.getContext(), chosenText, Toast.LENGTH_LONG).show();


        BoardingLocationFragment boardingLocationFragment = new BoardingLocationFragment(chosenText);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.location_holder_fragment, boardingLocationFragment, boardingLocationFragment.getTag())
                .commit();



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

            List<Vendor> vendors = ApiUtil.getVendorFromJson(result);

            List<String> vendorNames = vendors.stream()
                    .map(Vendor::getName)
                    .collect(Collectors.toList());

            Spinner spinner = findViewById(R.id.spinner);
            spinner.setOnItemSelectedListener(SignedInActivity.this);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(SignedInActivity.this, android.R.layout.simple_spinner_item, vendorNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }
}