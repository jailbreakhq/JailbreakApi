package org.jailbreak.service.base;

import java.util.List;

import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.service.core.UsersManager;
import org.jailbreak.service.db.dao.UsersDAO;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class UsersManagerImpl implements UsersManager {
	
	private final UsersDAO dao;
	
	@Inject
	public UsersManagerImpl(UsersDAO dao) {
		this.dao = dao;
	}

	@Override
	public Optional<User> getUser(long user_id) {
		return this.dao.getUser(user_id);
	}
	
	@Override
	public Optional<User> getUserByEmail(String email) {
		return this.dao.getUserByEmail(email);
	}

	@Override
	public List<User> getUsers() {
		return this.dao.getUsers();
	}

	@Override
	public User createUser(User user) {
		int new_id = this.dao.createUser(user);
		
		return dao.getUser(new_id).get();
	}

}
