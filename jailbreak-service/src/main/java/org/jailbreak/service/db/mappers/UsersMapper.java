package org.jailbreak.service.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.api.representations.Representations.User.Gender;
import org.jailbreak.api.representations.Representations.User.UserLevel;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class UsersMapper implements ResultSetMapper<User> {
	
	public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		long user_id = r.getLong("user_id");
		long time_created = r.getLong("time_created");
		UserLevel user_level = UserLevel.valueOf(r.getInt("user_level"));
		String email = r.getString("email");
		String firstName = r.getString("first_name");
		String lastName = r.getString("last_name");
		Gender gender = Gender.valueOf(r.getInt("gender"));
		int timezone = r.getInt("timezone");
		String locale = r.getString("locale");
		String facebookLink = r.getString("facebook_link");
		String password = r.getString("password");
		
		User.Builder builder = User.newBuilder()
				.setUserId(user_id)
				.setTimeCreated(time_created)
				.setUserLevel(user_level)
				.setEmail(email)
				.setFirstName(firstName)
				.setLastName(lastName)
				.setGender(gender)
				.setTimezone(timezone);
		
		if (locale != null && !locale.isEmpty()) {
			builder.setLocale(locale);
		}
		
		if (facebookLink != null && !facebookLink.isEmpty()) {
			builder.setFacebookLink(facebookLink);
		}
		
		if (password != null && !password.isEmpty()) {
			builder.setPassword(password);
		}
		
		return builder.build();
	}

}
