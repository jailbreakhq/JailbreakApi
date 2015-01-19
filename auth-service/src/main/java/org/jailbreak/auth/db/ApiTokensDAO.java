package org.jailbreak.auth.db;

import java.util.List;

import org.jailbreak.auth.db.mappers.ApiTokensMapper;
import org.jailbreak.auth.representations.Representations.ApiToken;
import org.jailbreak.common.db.BindProtobuf;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(ApiTokensMapper.class)
public interface ApiTokensDAO {
	
	@SqlQuery("SELECT * FROM api_tokens WHERE user_id = :user_id AND api_token = :api_token")
	@SingleValueResult(ApiToken.class)
	Optional<ApiToken> getApiToken(@Bind("user_id") long user_id, @Bind("api_token") String api_token);
	
	@SqlQuery("SELECT * FROM api_tokens WHERE user_id = :user_id")
	List<ApiToken> getApiTokensForUser(@Bind("user_id") long user_id);
	
	@SqlUpdate("INSERT INTO api_tokens VALUES(:api_token, :user_id, :expires)")
	void createApiToken(@BindProtobuf ApiToken token);

}
