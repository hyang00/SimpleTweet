package com.codepath.apps.restclienttemplate.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class User {

    public String name;
    public String screenName;
    public String profileImageUrl;
    public String description;
    public Long followersCount;
    public String location;

    public User() {}

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        user.description = jsonObject.optString("description");
        user.location = jsonObject.getString("location");
        user.followersCount = jsonObject.getLong("followers_count");
        return user;
    }

    public static List<User> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            users.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return users;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return "@" +screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public Long getFollowersCount() {
        return followersCount;
    }

    public String getLocation() {
        return location;
    }
}
