package org.jailbreak.service.core;

import java.util.List;

import org.jailbreak.api.representations.Representations.User;

import com.google.common.base.Optional;

public interface UsersManager {
	
	public Optional<User> getUser(long user_id);
	public Optional<User> getUserByEmail(String email);
	public List<User> getUsers();
	public User createUser(User user);

}
