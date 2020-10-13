package com.zikozee.communityproject.models;

import java.util.Map;

public class State {
    private long id;
    private String name;
    private String startLocation;
    private String destinationState;
    private String destinationCity;
    private Double farePrice;


    public State() {
    }

    private State(Builder builder) {
        id = builder.id;
        name = builder.name;
        startLocation = builder.startLocation;
        destinationState = builder.destinationState;
        destinationCity = builder.destinationCity;
        farePrice = builder.farePrice;
    }


//    public Map<String, Object> toMap(){
//        Map<String, Object> stateMap = new LinkedHashMap<>();
//        stateMap.put("name", name);
//        stateMap.put("location", location);
//
//        return stateMap;
//    }

    public State fromMap(Object obj){
        Map<String, Object> stateObject = (Map<String, Object>)obj;
        return new Builder()
                .id((long)stateObject.get("id"))
                .name((String)stateObject.get("name"))
                .name((String)stateObject.get("startLocation"))
                .name((String)stateObject.get("destinationState"))
                .name((String)stateObject.get("destinationCIty"))
                .farePrice((Double) stateObject.get("farePrice"))
                .build();
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getDestinationState() {
        return destinationState;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public Double getFarePrice() {
        return farePrice;
    }

    public static final class Builder {
        private long id;
        private String name;
        private String startLocation;
        private String destinationState;
        private String destinationCity;
        private Double farePrice;

        public Builder() {
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder startLocation(String val) {
            startLocation = val;
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

        public Builder farePrice(Double val) {
            farePrice = val;
            return this;
        }

        public State build() {
            return new State(this);
        }
    }
}
