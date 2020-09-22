package com.zikozee.communityproject.models;

public class User {
    private String name;
    private String phone;
    private String profile_image;
    private String user_id;

    public User(String name, String phone, String profile_image) {
        this.name = name;
        this.phone = phone;
        this.profile_image = profile_image;
    }

    private User(Builder builder) {
        name = builder.name;
        phone = builder.phone;
        profile_image = builder.profile_image;
        user_id = builder.user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public static final class Builder {
        private String name;
        private String phone;
        private String profile_image;
        private String user_id;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder profile_image(String val) {
            profile_image = val;
            return this;
        }

        public Builder user_id(String val) {
            user_id = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
