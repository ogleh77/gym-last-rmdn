package com.example.gymproject.dto;

import com.example.gymproject.dto.main.UserService;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class UserServiceTest {

    @Test
    void predictNextId() throws SQLException {
        System.out.println(UserService.predictNextId());
    }
}