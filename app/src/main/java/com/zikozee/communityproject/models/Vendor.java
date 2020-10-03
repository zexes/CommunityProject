package com.zikozee.communityproject.models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Vendor {
    private String vendorName;
    private List<State> state;
    private String headOfficeContact;

    private Vendor(Builder builder) {
        vendorName = builder.vendorName;
        state = builder.state;
        headOfficeContact = builder.headOfficeContact;
    }


    public Map<String, Object> toMap(){
        Map<String, Object> vendorMap = new LinkedHashMap<>();
        vendorMap.put("vendorName", vendorName);
        vendorMap.put("state", state);
        vendorMap.put("headOfficeContact", headOfficeContact);

        return vendorMap;
    }

    public Vendor fromMap(Object obj){
        Map<String, Object> stateObject = (Map<String, Object>)obj;
        return new Vendor.Builder()
                .vendorName((String)stateObject.get("name"))
                .state((List<State>)stateObject.get("state"))
                .headOfficeContact((String)stateObject.get("headOfficeContact"))
                .build();
    }


    public static final class Builder {
        private String vendorName;
        private List<State> state;
        private String headOfficeContact;

        public Builder() {
        }

        public Builder vendorName(String val) {
            vendorName = val;
            return this;
        }

        public Builder state(List<State> val) {
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
    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public List<State> getState() {
        return state;
    }

    public void setState(List<State> state) {
        this.state = state;
    }

    public String getHeadOfficeContact() {
        return headOfficeContact;
    }

    public void setHeadOfficeContact(String headOfficeContact) {
        this.headOfficeContact = headOfficeContact;
    }

}
