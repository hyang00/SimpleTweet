package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    Context context;
    List<User> users;
    TwitterClient client;
    // Pass in the context and list of Users
    public  UsersAdapter(Context context, List<User> users){
        this.context = context;
        this.users = users;
        this.client = TwitterApplication.getRestClient(context);
    }
    // For each row, inflate the layout
    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("myApp", "On Create");
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UsersAdapter.ViewHolder(view);
    }

    //Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        Log.i("myApp", "On Bind " +getItemCount());
        // Get the data
        User User = users.get(position);
        // Bind w/ viewholder
        holder.bind(User);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<User> list) {
        users.addAll(list);
        notifyDataSetChanged();
    }


    //Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{

        ImageView ivProfileImage;
        TextView tvScreenName;
        TextView tvName;
        TextView tvDescription;
        private final int REQUEST_CODE = 20;

        // passing in one "user profile"
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            //itemView.setOnClickListener(this);

        }

        public void bind(User user) {
            tvScreenName.setText( user.getScreenName());
            tvName.setText(user.getName());
            if (user.getDescription()!= null){
                tvDescription.setText(user.getDescription());
            }
            Glide.with(context).load(user.profileImageUrl).transform(new CircleCrop()).into(ivProfileImage);
        }
//        @Override
//        public void onClick(View view) {
//            // gets item position
//            int position = getAdapterPosition();
//            //Log.i("myApp", "on click");
//            // make sure the position is valid, i.e. actually exists in the view
//            if (position != RecyclerView.NO_POSITION) {
//                // get the movie at the position, this won't work if the class is static
//                Tweet tweet = tweets.get(position);
//                // create intent for the new activity
//                Intent intent = new Intent(context, TweetDetailActivity.class);
//                // serialize the movie using parceler, use its short name as a key
//                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
//                //reset the activity we are going to
//                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
//                // show the activity
//                context.startActivity(intent);
//            }
//        }

    }



}
