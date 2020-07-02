package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 280;
    EditText etCompose;
    TextView tvCharacterCount;
    Button btnTweet;
    boolean isReply;
    String replyScreenName;

    TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient(this);
        isReply = getIntent().getBooleanExtra("isReply", false);
        replyScreenName = getIntent().getStringExtra("replyScreenName");
        etCompose = findViewById(R.id.etCompose);
        tvCharacterCount = findViewById(R.id.tvCharacterCount);
        btnTweet = findViewById(R.id.btnTweet);
        tvCharacterCount.setText("" +MAX_TWEET_LENGTH);
        if(isReply){
            etCompose.setHint("Replying to");
            etCompose.setText("@" +replyScreenName);
        }

        // character counter
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int diff = MAX_TWEET_LENGTH - s.length();
                tvCharacterCount.setText("" +diff);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });


        //Set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannont be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tweetContent.length()> MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_SHORT).show();

                // Make an API call to Twitter to publish the tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says: " + tweet);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            //set result code +bundle data for response
                            setResult(RESULT_OK, intent);
                            //close activity + pass data to parent
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);
                    }
                });
            }
        });

    }
}