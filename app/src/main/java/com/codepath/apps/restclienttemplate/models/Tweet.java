package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

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

@Parcel
public class Tweet {
        public String body;
        public String createdAt;
        public User user;
        public String formattedRelativeTime;
        public String mediaUrlHttps;
        public String formattedAbsoluteTime;

        public Tweet(){}

        public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
            Tweet tweet = new Tweet();
            tweet.body = jsonObject.getString("text");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.formattedRelativeTime = TimeFormatter.getTimeDifference(tweet.createdAt);
            tweet.formattedAbsoluteTime = TimeFormatter.getTimeStamp(tweet.createdAt);
            JSONObject entitiesObject = jsonObject.getJSONObject("entities");
            Log.i("details", jsonObject.toString());
            if(entitiesObject.has("media")){
                tweet.mediaUrlHttps = entitiesObject.getJSONArray("media").getJSONObject(0).getString("media_url_https");
            }
            /*Log.i("tweet", "entities: " + entitiesObject.toString());
            JSONArray mediaArray = entitiesObject.optJSONArray("media");
            JSONObject mediaObj = null;
            if(mediaArray!=null){
                mediaObj = mediaArray.optJSONObject(0);
                Log.i("media", "media: " + mediaArray.toString());
            }
            if(mediaObj !=null){
                tweet.mediaUrlHttps = mediaObj.optString("media_url_https");
            }
            if(tweet.mediaUrlHttps!=null){
                Log.i("mediaurl", "mediaUrlHttps " + tweet.mediaUrlHttps);
            }*/

            //tweet.mediaUrlHttps = jsonObject.getJSONObject("entities").optJSONArray("media").optJSONObject(0).getString("media_url_https");
            Log.i("tweet", "tweet body:" + tweet.body + " mediaUrl: " + tweet.mediaUrlHttps);
            return tweet;
        }

        public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
            List<Tweet> tweets = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++){
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
/*public String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            String relativeDate = "";
            try {
                long dateMillis = sf.parse(rawJsonDate).getTime();
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return relativeDate;
        }*/
}
