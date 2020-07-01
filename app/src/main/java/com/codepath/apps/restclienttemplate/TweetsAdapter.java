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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;
    // Pass in the context and list of tweets
    public  TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
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
        // passing in one "tweet"
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            itemView.setOnClickListener(this);
        }

        public void bind(Tweet tweet) {
            tvScreenName.setText("@" + tweet.user.screenName);
            tvBody.setText( tweet.body);
            tvName.setText(tweet.user.name);
            tvTimeStamp.setText("| " + tweet.formattedRelativeTime);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
            Glide.with(context).load(tweet.mediaUrlHttps).into(ivMedia);
        }
        @Override
        public void onClick(View view) {

            // gets item position
            int position = getAdapterPosition();
            Log.i("myApp", "on click");
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
