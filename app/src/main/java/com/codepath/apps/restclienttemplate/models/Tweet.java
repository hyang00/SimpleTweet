package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.TimeFormatter;
import com.facebook.stetho.common.ArrayListAccumulator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity(foreignKeys = @ForeignKey(entity=User.class, parentColumns="id", childColumns="userId"))
@Parcel
public class Tweet {
    @ColumnInfo
    @PrimaryKey
    public long id;
    @ColumnInfo
    public String body;
    @ColumnInfo
    public String createdAt;
    @ColumnInfo
    public String formattedRelativeTime;
    @ColumnInfo
    public String mediaUrlHttps;
    @ColumnInfo
    public String formattedAbsoluteTime;
    @ColumnInfo
    public Integer retweetCount;
    @ColumnInfo
    public Integer likeCount;
    @ColumnInfo
    public boolean favorited;
    @ColumnInfo
    public boolean retweeted;
    @ColumnInfo
    public long userId;
    @Ignore
    public User user;


    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.formattedRelativeTime = TimeFormatter.getTimeDifference(tweet.createdAt);
        tweet.formattedAbsoluteTime = TimeFormatter.getTimeStamp(tweet.createdAt);
        tweet.retweetCount = jsonObject.getInt("retweet_count");
        tweet.likeCount = jsonObject.getInt("favorite_count");
        tweet.id = jsonObject.getLong("id");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.userId = tweet.user.id;
        //JSONObject entitiesObject = jsonObject.getJSONObject("entities");
        //Log.i("details", jsonObject.toString());
        tweet.mediaUrlHttps = null;
        if (jsonObject.getJSONObject("entities").has("media") && jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).has("media_url_https")) {
            tweet.mediaUrlHttps = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");
            Log.i("tweet", " hi" + jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https"));
            Log.i("tweet", "tweet body:" + tweet.body + " mediaUrl: " + tweet.mediaUrlHttps);
        }

        //tweet.mediaUrlHttps = jsonObject.getJSONObject("entities").optJSONArray("media").optJSONObject(0).getString("media_url_https");
        //Log.i("tweet", "tweet body:" + tweet.body + " mediaUrl: " + tweet.mediaUrlHttps);
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getFormattedAbsoluteTime() {
        return formattedAbsoluteTime;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public String getFormattedRelativeTime() {
        return formattedRelativeTime;
    }

    public String getMediaUrlHttps() {
        return mediaUrlHttps;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public long getId() {
        return id;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }
}
