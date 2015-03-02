package org.jailbreak.service.db.mappers.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.Twitter;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class TwitterEventsMapper implements ResultSetMapper<Twitter> {
	
	public Twitter map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		int id = r.getInt("id");
		long tweetId = r.getLong("tweet_id");
		String tweet = r.getString("tweet");
		String tweetHtml = r.getString("tweet_html");
		String photoUrl = r.getString("photo_url");
		String inReplyTo = r.getString("in_reply_to");
		int twitterUserId = r.getInt("twitter_user_id");
		String twitterUserName = r.getString("twitter_user_name");
		String twitterUserPhoto = r.getString("twitter_user_photo");
		long time = r.getLong("time");
		int teamId = r.getInt("team_id");
		
		Twitter.Builder builder = Twitter.newBuilder()
				.setId(id)
				.setTweetId(tweetId)
				.setTweet(tweet)
				.setTime(time)
				.setTwitterUserId(twitterUserId)
				.setTwitterUserName(twitterUserName);
		
		if (tweetHtml != null && !tweetHtml.isEmpty()) {
			builder.setTweetHtml(tweetHtml);
		}
		
		if (photoUrl != null && !photoUrl.isEmpty()) {
			builder.setPhotoUrl(photoUrl);
		}
		
		if (inReplyTo != null && !inReplyTo.isEmpty()) {
			builder.setInReplyTo(inReplyTo);
		}
		
		if (twitterUserPhoto != null && !twitterUserPhoto.isEmpty()) {
			builder.setTwitterUserPhoto(twitterUserPhoto);
		}
		
		if (teamId != 0) {
			builder.setTeamId(teamId);
		}
		
		return builder.build();
	}

}
