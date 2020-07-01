package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;
import org.w3c.dom.Text;

public class TweetDetailActivity extends AppCompatActivity /*implements ImageView.OnClickListener*/ {

    Tweet tweet;

    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvName;
    TextView tvTimeStamp;
    ImageView ivMedia;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvName = (TextView) findViewById(R.id.tvName);
        tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
        ivMedia = (ImageView) findViewById(R.id.ivMedia);


        tvScreenName.setText("@" + tweet.user.screenName);
        tvBody.setText( tweet.getBody());
        tvName.setText(tweet.user.name);
        tvTimeStamp.setText( tweet.getFormattedAbsoluteTime());
        Glide.with(this).load(tweet.user.profileImageUrl).transform(new CircleCrop()).into(ivProfileImage);
        if(tweet.getMediaUrlHttps()!=null){
            Glide.with(this).load(tweet.mediaUrlHttps).into(ivMedia);
        }

    }

    /*@Override
    public void onClick(View v) {

    }*/
}