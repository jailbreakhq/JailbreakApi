package org.jailbreak.auth.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jailbreak.auth.representations.Representations.ApiToken;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ApiTokensMapper implements ResultSetMapper<ApiToken> {

	@Override
	public ApiToken map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		String api_token = r.getString("api_token");
		long user_id = r.getLong("user_id");
		long expires = r.getLong("expires");
		
		return ApiToken.newBuilder()
				.setApiToken(api_token)
				.setUserId(user_id)
				.setExpires(expires)
				.build();
	}
	
}