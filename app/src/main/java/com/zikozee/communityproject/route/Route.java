package com.zikozee.communityproject.route;

public class Route {
    private String startState;
    private String destinationCity;
    private double farePrice;

    public Route() {
    }

    private Route(Builder builder) {
        this.startState = builder.startState;
        this.destinationCity = builder.destinationCity;
        this.farePrice = builder.farePrice;
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

    public static class Builder {
        private String startState;
        private String destinationCity;
        private double farePrice;

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

        public Builder of(Route route) {
            this.startState = route.startState;
            this.destinationCity = route.destinationCity;
            this.farePrice = route.farePrice;
            return this;
        }

        public Route build() {
            return new Route(this);
        }
    }
}
