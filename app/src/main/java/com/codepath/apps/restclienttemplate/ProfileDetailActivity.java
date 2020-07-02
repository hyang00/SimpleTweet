package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.Headers;

public class ProfileDetailActivity extends AppCompatActivity {

    User user;
    ImageView ivProfileImage;
    TextView tvDescription;
    TextView tvScreenName;
    TextView tvName;
    TextView tvLocation;
    TextView tvFollowerCount;
    TwitterClient client;
    RecyclerView rvFollowers;
    List<User> followers;
    UsersAdapter adapter;
    public static final String TAG = "ProfileDetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        client = TwitterApplication.getRestClient(this);

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvName = (TextView) findViewById(R.id.tvName);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvFollowerCount = (TextView) findViewById(R.id.tvFollowerCount);

        tvScreenName.setText(user.getScreenName());
        tvDescription.setText( user.getDescription());
        tvName.setText(user.name);
        tvLocation.setText(user.getLocation());
        tvFollowerCount.setText(Long.toString(user.getFollowersCount()) + " Followers");
        Glide.with(this).load(user.profileImageUrl).transform(new CircleCrop()).into(ivProfileImage);

        //Find the recycler view
        rvFollowers = findViewById(R.id.rvFollowers);

        // Init the list of tweets + adapter
        followers = new ArrayList<>();
        adapter = new UsersAdapter(this, followers);

        // Recycler view setup: layout manager and the adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFollowers.setLayoutManager(linearLayoutManager);
        rvFollowers.setAdapter(adapter);

        //Adding horizontal lines b/t items
        DividerItemDecoration itemDecor = new DividerItemDecoration(ProfileDetailActivity.this, DividerItemDecoration.VERTICAL);
        rvFollowers.addItemDecoration(itemDecor);

        populateFollowers();
    }

    private void populateFollowers() {
        client.getFollowersList(user.getScreenName(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                try {
                    JSONArray jsonArray = json.jsonObject.getJSONArray("users");
                    followers.addAll(User.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, jsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure" + response, throwable);
            }
        });
    }
}