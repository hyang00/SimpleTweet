package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.databinding.ActivityProfileDetailBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class ProfileDetailActivity extends AppCompatActivity {

    User user;
    ImageView ivProfileImage;
    TextView tvDescription;
    TextView tvScreenName;
    TextView tvName;
    TextView tvLocation;
    TextView tvFollowerCount;
    TextView tvFollowingCount;
    TwitterClient client;
    RecyclerView rvFollowers;
    List<User> followers;
    UsersAdapter adapter;
    String wanted;
    TextView tvWanted;
    public static final String TAG = "ProfileDetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileDetailBinding binding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //setContentView(R.layout.activity_profile_detail);

        client = TwitterApplication.getRestClient(this);

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));
        wanted = "Followers";
        ivProfileImage = binding.ivProfileImage;
        tvDescription = binding.tvDescription;
        tvScreenName = binding.tvScreenName;
        tvName = binding.tvName;
        tvLocation = binding.tvLocation;
        tvFollowerCount = binding.tvFollowerCount;
        tvFollowingCount = binding.tvFollowingCount;
        tvWanted = binding.tvWanted;

        tvScreenName.setText(user.getScreenName());
        tvDescription.setText( user.getDescription());
        tvName.setText(user.name);
        tvWanted.setText(wanted);
        tvLocation.setText(user.getLocation());
        tvFollowerCount.setText(NumberFormat.getNumberInstance(Locale.US).format(user.getFollowersCount()) + " Followers");
        tvFollowingCount.setText(NumberFormat.getNumberInstance(Locale.US).format(user.getFollowingCount()) + " Following");
        //tvFollowerCount.setText(Long.toString(user.getFollowersCount()) + " Followers");
        Glide.with(this).load(user.profileImageUrl).transform(new CircleCrop()).into(ivProfileImage);

        tvFollowingCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wanted = "Following";
                tvWanted.setText(wanted);
                populateUsers(wanted);
            }
        });

        tvFollowerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wanted = "Followers";
                tvWanted.setText(wanted);
                populateUsers(wanted);
            }
        });

        //Find the recycler view
        rvFollowers = binding.rvFollowers;

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
        populateUsers(wanted);
    }

    private void populateUsers(String wanted) {
        client.getUsersList(wanted, user.getScreenName(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                try {
                    adapter.clear();
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