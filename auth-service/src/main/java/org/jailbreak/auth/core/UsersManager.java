package org.jailbreak.auth.core;

import java.util.List;

import org.jailbreak.auth.representations.Representations.User;

import com.google.common.base.Optional;

public interface UsersManager {
	
	public Optional<User> getUser(long user_id);
	public List<User> getUsers();
	public void createUser(User user);

}
