package com.zikozee.communityproject;

import android.util.Log;

import com.zikozee.communityproject.models.State;
import com.zikozee.communityproject.models.Vendor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;


public class ApiUtil {
    private ApiUtil() {
    }

    public static final String BASE_API_URI = "https://vendor-android.herokuapp.com";

    public static URL buildUrl(String title) {
        String fullUrl = BASE_API_URI + title;

        URL url = null;
        try {
            url = new URL(fullUrl);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getJson(URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return null;
        } finally {
            connection.disconnect();
        }

    }

    public static List<Vendor> getVendorFromJson(String json) {
        final String _ID = "id";
        final String NAME = "name";
        final String STATE = "state";
        final String HEADOFFICE = "headOfficeContact";

        List<Vendor> vendors = new ArrayList<>();

        try {
//            JSONObject jsonSkills = new JSONObject(json);
            JSONArray arrayVendors = new JSONArray(json);
            int numberOfSkills = arrayVendors.length();

            for (int i = 0; i < numberOfSkills; i++) {
                JSONObject vendorJSON = arrayVendors.getJSONObject(i);
//                Vendor vendor = new Vendor(skillJSON.getString(NAME), skillJSON.getInt(SCORE),
//                        skillJSON.getString(COUNTRY), skillJSON.getString(URL));
                Vendor vendor  =  new Vendor.Builder()
                        .id(vendorJSON.getLong(_ID))
                        .name(vendorJSON.getString(NAME))
                        .state(vendorJSON.getJSONArray(STATE))
                        .headOfficeContact(vendorJSON.getString(HEADOFFICE))
                        .build();


                vendors.add(vendor);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vendors;
    }

    public static List<State> getStatesFromJson(JSONArray jsonArray) {
        final String _ID = "id";
        final String NAME = "name";
        final String START= "startLocation";
        final String DEST_STATE= "destinationState";
        final String DEST_CITY = "destinationCity";
        final String FARE_PRICE = "farePrice";

        List<State> states = new ArrayList<>();

        try {
            int numberOfStates = jsonArray.length();

            for (int i = 0; i < numberOfStates; i++) {
                JSONObject stateJSON = jsonArray.getJSONObject(i);

                State state  =  new State.Builder()
                        .id(stateJSON.getLong(_ID))
                        .name(stateJSON.getString(NAME))
                        .startLocation(stateJSON.getString(START))
                        .destinationState(stateJSON.getString(DEST_STATE))
                        .destinationCity(stateJSON.getString(DEST_CITY))
                        .farePrice(stateJSON.getDouble(FARE_PRICE))
                        .build();


                states.add(state);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return states;
    }


}
