package org.jailbreak.auth.db;

import java.util.List;

import org.jailbreak.auth.db.mappers.UsersMapper;
import org.jailbreak.auth.representations.Representations.User;
import org.jailbreak.common.db.BindProtobuf;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import com.google.common.base.Optional;

@RegisterMapper(UsersMapper.class)
public interface UsersDAO {

	@SqlQuery("SELECT * FROM users WHERE user_id = :user_id")
	@SingleValueResult(User.class)
	Optional<User> getUser(@Bind("user_id") long user_id);
	
	@SqlQuery("SELECT * FROM users LIMIT 20")
	List<User> getUsers();

	@SqlUpdate("INSERT INTO users VALUES(:user_id, UNIX_TIMESTAMP(UTC_TIMESTAMP()), :email, :user_level, :first_name, :last_name, :gender, :timezone, :locale, :facebook_link)")
	void createUser(@BindProtobuf User user);

}