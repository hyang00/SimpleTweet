package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity /*implements ImageView.OnClickListener*/ {

    Tweet tweet;

    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvName;
    TextView tvTimeStamp;
    ImageView ivMedia;
    TextView tvFavoriteCount;
    TextView tvRetweetCount;
    ImageView ivReply;
    ImageView ivRetweet;
    ImageView ivLike;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //switch to using viewbinding
        ActivityTweetDetailBinding binding = ActivityTweetDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //setContentView(R.layout.activity_tweet_detail);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        ivProfileImage = (binding.ivProfileImage);
        tvBody = binding.tvDescription;
        tvScreenName = binding.tvScreenName;
        tvName = binding.tvName;
        tvTimeStamp = binding.tvTimeStamp;
        ivMedia = (binding.ivMedia);
        tvFavoriteCount = binding.tvFavoriteCount;
        tvRetweetCount = binding.tvRetweetCount;
        ivReply = binding.ivReply;
        ivRetweet = binding.ivLike;

        DividerItemDecoration itemDecor = new DividerItemDecoration(TweetDetailActivity.this, DividerItemDecoration.VERTICAL);
        //binding..addItemDecoration(itemDecor);


        tvScreenName.setText("@" + tweet.user.screenName);
        tvBody.setText( tweet.getBody());
        tvName.setText(tweet.user.name);
        tvTimeStamp.setText(tweet.getFormattedAbsoluteTime());
        //Log.i("wtf", tweet.getFormattedAbsoluteTime());
        tvFavoriteCount.setText(tweet.getLikeCount() + " Likes");
        tvRetweetCount.setText(tweet.getRetweetCount() + " Retweets" );
        Glide.with(this).load(tweet.user.profileImageUrl).transform(new CircleCrop()).into(ivProfileImage);
        if(tweet.getMediaUrlHttps()!=null){
            Glide.with(this).load(tweet.mediaUrlHttps).into(ivMedia);
        }

    }

    /*@Override
    public void onClick(View v) {

    }*/
}