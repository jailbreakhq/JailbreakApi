package org.jailbreak.auth.base;

import java.util.List;

import org.jailbreak.auth.core.UsersManager;
import org.jailbreak.auth.db.UsersDAO;
import org.jailbreak.auth.representations.Representations.User;

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
	public List<User> getUsers() {
		return this.dao.getUsers();
	}

	@Override
	public void createUser(User user) {
		this.dao.createUser(user);
	}

}
