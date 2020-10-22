package com.zikozee.communityproject.route;

public class Route {
    private String startState;
    private String destinationCity;
    private double farePrice;
    private String vendorName;
    private String boardingLocation;

    public Route() {
    }

    private Route(Builder builder) {
        this.startState = builder.startState;
        this.destinationCity = builder.destinationCity;
        this.farePrice = builder.farePrice;
        this.vendorName = builder.vendorName;
        this.boardingLocation = builder.boardingLocation;
    }

    public static Builder builder() {
        return new Builder();
    }


    public String getStartState() {
        return startState;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public double getFarePrice() {
        return farePrice;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getBoardingLocation() {
        return boardingLocation;
    }

    public static class Builder {
        private String startState;
        private String destinationCity;
        private double farePrice;
        private String vendorName;
        private String boardingLocation;

        private Builder() {
        }

        public Builder setStartState(String startState) {
            this.startState = startState;
            return this;
        }

        public Builder setDestinationCity(String destinationCity) {
            this.destinationCity = destinationCity;
            return this;
        }

        public Builder setFarePrice(double farePrice) {
            this.farePrice = farePrice;
            return this;
        }

        public Builder setVendorName(String vendorName) {
            this.vendorName = vendorName;
            return this;
        }

        public Builder setBoardingLocation(String boardingLocation) {
            this.boardingLocation = boardingLocation;
            return this;
        }

        public Builder of(Route route) {
            this.startState = route.startState;
            this.destinationCity = route.destinationCity;
            this.farePrice = route.farePrice;
            this.vendorName = route.vendorName;
            this.boardingLocation = route.boardingLocation;
            return this;
        }

        public Route build() {
            return new Route(this);
        }
    }
}
