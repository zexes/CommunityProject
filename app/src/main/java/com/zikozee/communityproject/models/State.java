package com.zikozee.communityproject.models;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class State {
    private String name;
    private List<Location> location;

    private State(Builder builder) {
        name = builder.name;
        location = builder.location;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> stateMap = new LinkedHashMap<>();
        stateMap.put("name", name);
        stateMap.put("location", location);

        return stateMap;
    }

    public State fromMap(Object obj){
        Map<String, Object> stateObject = (Map<String, Object>)obj;
        return new Builder()
                .name((String)stateObject.get("name"))
                .location((List<Location>)stateObject.get("location"))
                .build();
    }

    public static final class Builder {
        private String name;
        private List<Location> location;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder location(List<Location> val) {
            location = val;
            return this;
        }

        public State build() {
            return new State(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }
}
