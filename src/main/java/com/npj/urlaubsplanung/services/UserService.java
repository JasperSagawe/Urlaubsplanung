package com.npj.urlaubsplanung.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.npj.urlaubsplanung.models.User;

@Service
public class UserService {

	public UserService() {
	}

	public Iterable<User> getUser() {
		return List.of(new User("Alice", "Krüger", 24, 6), new User("Bob", "Baumeister", 20, 10),
				new User("Charlie", "Brown", 12, 18));
	}

	public User getUserById() {
		return new User("Alice", "Krüger", 24, 6);
	}
}
