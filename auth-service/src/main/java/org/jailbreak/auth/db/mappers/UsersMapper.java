package org.jailbreak.auth.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.auth.representations.Representations.User;
import org.jailbreak.auth.representations.Representations.User.Gender;
import org.jailbreak.auth.representations.Representations.User.UserLevel;
import org.jailbreak.auth.resources.Paths;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class UsersMapper implements ResultSetMapper<User> {
	
	public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		long user_id = r.getLong("user_id");
		long time_created = r.getLong("time_created");
		UserLevel user_level = UserLevel.valueOf(r.getInt("user_level"));
		String email = r.getString("email");
		String first_name = r.getString("first_name");
		String last_name = r.getString("last_name");
		Gender gender = Gender.valueOf(r.getInt("gender"));
		int timezone = r.getInt("timezone");
		String locale = r.getString("locale");
		String facebook_link = r.getString("facebook_link");
		
		return User.newBuilder()
				.setUserId(user_id)
				.setTimeCreated(time_created)
				.setUserLevel(user_level)
				.setEmail(email)
				.setFirstName(first_name)
				.setLastName(last_name)
				.setGender(gender)
				.setTimezone(timezone)
				.setLocale(locale)
				.setFacebookLink(facebook_link)
				.setHref(Paths.USERS_PATH + "/" + user_id)
				.build();
	}

}
