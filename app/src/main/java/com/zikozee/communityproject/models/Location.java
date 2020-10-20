package com.zikozee.communityproject.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class Location {
    private String start;
    private String destinationState;
    private String destinationCity;
    private String fare_price;

    public Location() {
    }

    private Location(Builder builder) {
        start = builder.start;
        destinationState = builder.destinationState;
        destinationCity = builder.destinationCity;
        fare_price = builder.fare_price;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> locationMap = new LinkedHashMap<>();
        locationMap.put("start", start);
        locationMap.put("destinationState", destinationState);
        locationMap.put("destinationCity", destinationCity);
        locationMap.put("fare_price", fare_price);
        return locationMap;
    }

    public Location fromMap(Object obj){
        Map<String, Object> locationObject = (Map<String, Object>)obj;
        String farePrice = (String)locationObject.get("fare_price");

        return new Builder()
                .start((String)locationObject.get("start"))
                .destinationState((String)locationObject.get("destinationState"))
                .destinationCity((String)locationObject.get("destinationCity"))
                .fare_price(farePrice)
                .build();
    }

    public static final class Builder {
        private String start;
        private String destinationState;
        private String destinationCity;
        private String fare_price;

        public Builder() {
        }

        public Builder start(String val) {
            start = val;
            return this;
        }

        public Builder destinationState(String val) {
            destinationState = val;
            return this;
        }

        public Builder destinationCity(String val) {
            destinationCity = val;
            return this;
        }

        public Builder fare_price(String val) {
            fare_price = val;
            return this;
        }

        public Location build() {
            return new Location(this);
        }
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDestinationState() {
        return destinationState;
    }

    public void setDestinationState(String destinationState) {
        this.destinationState = destinationState;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getFare_price() {
        return fare_price;
    }

    public void setFare_price(String fare_price) {
        this.fare_price = fare_price;
    }
}
