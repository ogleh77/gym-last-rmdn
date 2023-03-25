package com.example.gymproject.dto;

import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CustomException;
import com.example.gymproject.model.UserModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class UserService {
    private static UserModel userModel;
    private static ObservableList<Users> users = null;

    static {
        if (userModel == null) {
            userModel = new UserModel();
        }
    }

    public static void insertUser(Users user) throws SQLException {
        try {
            userModel.insert(user);
        } catch (SQLException e) {
            if (e.getMessage().contains("(UNIQUE constraint failed: users.username")) {
                throw new CustomException("username ka " + user.getUsername() + " horaa lo isticmalay");
            } else if (e.getMessage().contains("(UNIQUE constraint failed: users.phone")) {
                throw new CustomException("lanbar ka " + user.getPhone() + " horaa lo isticmalay");
            } else {
                throw e;
            }
        }
    }
}
