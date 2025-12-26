package org.example.service;

import org.example.dao.GenericDAO;
import org.example.model.Role;
import org.example.model.User;

import java.util.List;

public record UserService(GenericDAO<User> userGenericDAO) {

    public void createNewUser(User user) {
        if(user.getRole() == null){
            user.setRole(Role.DEFAULT_ROLE);
        }

        userGenericDAO.save(user);
    }

    public User findById(Long id) {
        return userGenericDAO.findById(id);
    }

    public void updateUserName(Long id, String newName) {
        User existingUser = userGenericDAO.findById(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("Пользователь с ID " + id + " не найден.");
        }
        existingUser.setName(newName);
        userGenericDAO.update(existingUser);
    }

    public List<User> findAllUsers() {
        return userGenericDAO.findAll();
    }
}
