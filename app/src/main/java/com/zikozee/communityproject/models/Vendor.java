package com.zikozee.communityproject.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class Vendor {
    private long id;
    private String name;
    private JSONArray state;
    private String headOfficeContact;

    public Vendor() {}

    private Vendor(Builder builder) {
        id = builder.id;
        name = builder.name;
        state = builder.state;
        headOfficeContact = builder.headOfficeContact;
    }


    public Vendor fromMap(JSONObject obj){
        Map<String, Object> stateObject = (Map<String, Object>)obj;
        return new Builder()
                .id((long)stateObject.get("id"))
                .name((String)stateObject.get("name"))
                .state((JSONArray) stateObject.get("state"))
                .headOfficeContact((String)stateObject.get("headOfficeContact"))
                .build();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JSONArray getState() {
        return state;
    }

    public String getHeadOfficeContact() {
        return headOfficeContact;
    }


    public static final class Builder {
        private long id;
        private String name;
        private JSONArray state;
        private String headOfficeContact;

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

        public Builder state(JSONArray val) {
            state = val;
            return this;
        }

        public Builder headOfficeContact(String val) {
            headOfficeContact = val;
            return this;
        }

        public Vendor build() {
            return new Vendor(this);
        }
    }
}
