package com.conner.service;

import java.util.List;

import com.conner.model.User;

public interface UserService {
    public User create(User user);

    public List<User> getAllUsers();

    public User getUser(String userId);

}
