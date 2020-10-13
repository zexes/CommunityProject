package com.zikozee.communityproject.boarding;

public class BoardingLocation {

    private String stateName;
    private String locationStart;

    public BoardingLocation() {
    }

    private BoardingLocation(Builder builder) {
        stateName = builder.stateName;
        locationStart = builder.locationStart;
    }

    public String getStateName() {
        return stateName;
    }

    public String getLocationStart() {
        return locationStart;
    }

    public static final class Builder {
        private String stateName;
        private String locationStart;

        public Builder() {
        }

        public Builder stateName(String val) {
            stateName = val;
            return this;
        }

        public Builder locationStart(String val) {
            locationStart = val;
            return this;
        }

        public BoardingLocation build() {
            return new BoardingLocation(this);
        }
    }
}
