package com.zikozee.communityproject.route;

public class Route {
    private String startState;
    private String destinationCity;

    public Route() {
    }

    private Route(Builder builder) {
        this.startState = builder.startState;
        this.destinationCity = builder.destinationCity;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String startState;
        private String destinationCity;

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

        public Builder of(Route route) {
            this.startState = route.startState;
            this.destinationCity = route.destinationCity;
            return this;
        }

        public Route build() {
            return new Route(this);
        }
    }

    public String getStartState() {
        return startState;
    }

    public String getDestinationCity() {
        return destinationCity;
    }
}
