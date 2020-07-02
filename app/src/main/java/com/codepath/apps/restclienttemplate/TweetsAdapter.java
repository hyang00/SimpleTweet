package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Movie;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;
    TwitterClient client;
    // Pass in the context and list of tweets
    public  TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
        this.client = TwitterApplication.getRestClient(context);
    }
    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("myApp", "On Create");
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("myApp", "On Bind " +getItemCount());
        // Get the data
        Tweet tweet = tweets.get(position);
        // Bind w/ viewholder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }


    //Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvName;
        TextView tvTimeStamp;
        ImageView ivMedia;
        ImageView ivReply;
        ImageView ivRetweet;
        ImageView ivLike;
        TextView tvReply;
        TextView tvRetweetCount;
        TextView tvLikeCount;
        // passing in one "tweet"
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            ivReply = itemView.findViewById(R.id.ivReply);
            ivRetweet = itemView.findViewById(R.id.ivRetweet);
            ivLike = itemView.findViewById(R.id.ivLike);
            tvReply = itemView.findViewById(R.id.tvReplyCount);
            tvRetweetCount = itemView.findViewById(R.id.tvRetweetCount);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            itemView.setOnClickListener(this);

        }

        public void bind(final Tweet tweet) {
            tvScreenName.setText("@" + tweet.user.screenName);
            tvBody.setText( tweet.getBody());
            tvName.setText(tweet.user.name);
            tvTimeStamp.setText("| " + tweet.formattedRelativeTime);
            tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount()));
            tvLikeCount.setText(Integer.toString(tweet.getLikeCount()));
            Glide.with(context).load(tweet.user.profileImageUrl).transform(new CircleCrop()).into(ivProfileImage);
            if(tweet.getMediaUrlHttps()!=null){
                Glide.with(context).load(tweet.mediaUrlHttps).transform(new RoundedCornersTransformation(30, 10)).into(ivMedia);
                ivMedia.setVisibility(View.VISIBLE);
            } else {
                ivMedia.setVisibility(View.GONE);
            }
            //Glide.with(context).load(R.drawable.)
            ivRetweet.setImageResource(R.drawable.ic_retweet_twitter);
            ivLike.setSelected(tweet.isFavorited());
            ivLike.setImageResource(R.drawable.ic_favorite_twitter);
            final Long tweetId = tweet.getId();

            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "" + tweet.isFavorited(), Toast.LENGTH_SHORT).show();
                    Log.i("mytweet", "" + tweet.isFavorited());
                    client.likeTweet(ivLike.isSelected(), tweetId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i("mytweet", "onSuccess to like tweet");
                            try {
                                Tweet myTweet = Tweet.fromJson(json.jsonObject);
                                Log.i("mytweet", "tweet favorited" + myTweet.isFavorited());
                                ivLike.setSelected(myTweet.isFavorited());
                                // how do i fix the sketchy count?
                                tvLikeCount.setText(Integer.toString(myTweet.getLikeCount()));
                                //setResult(RESULT_OK, intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("mytweet", "onFailure to like tweet", throwable);
                        }
                    });
                }
            });
            //Glide.with(context).load(R.drawable.ic_vector_retweet_stroke).into(ivRetweet);
            //Glide.with(context).load(R.drawable.ic_vector_heart_stroke).into(ivLike);
        }
        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            //Log.i("myApp", "on click");
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet tweet = tweets.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, TweetDetailActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                //reset the activity we are going to
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                // show the activity
                context.startActivity(intent);
            }
        }
    }


}
