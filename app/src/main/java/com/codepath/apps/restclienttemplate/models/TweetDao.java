package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {
    @Query("SELECT Tweet.body AS tweet_body, Tweet.createdAt As tweet_createdAt, Tweet.id AS tweet_id, Tweet.retweetCount AS tweet_retweetCount, Tweet.LikeCount AS tweet_likeCount, " +
            "Tweet.formattedRelativeTime AS tweet_formattedRelativeTime, Tweet.formattedAbsoluteTime AS tweet_formattedAbsoluteTime, Tweet.*, User.*" +
            "FROM Tweet INNER JOIN User ON Tweet.userId = User.id ORDER BY Tweet.createdAt DESC LIMIT 5")
    List<TweetWithUser> recentItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Tweet... tweets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User... users);
}
