package com.example.gymproject.dto;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void predictNextId() throws SQLException {
        System.out.println(UserService.predictNextId());
    }
}