package com.example.gymproject.dto;

import com.example.gymproject.dto.main.BoxService;
import com.example.gymproject.entity.Box;
import com.example.gymproject.helpers.CustomException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class BoxServiceTest {

    @Test
    void nextBoxID() throws CustomException {
        System.out.println(BoxService.nextBoxID());
    }

    @Test
    void insertBox() {
    }

    @Test
    void updateBox() throws SQLException {
        Box box = BoxService.fetchBoxes().get(0);

        System.out.println(BoxService.fetchBoxes());
        BoxService.updateBox(box);
        System.out.println(BoxService.fetchBoxes());
    }

    @Test
    void deleteBox() {
    }

    @Test
    void fetchBoxes() {
    }

    @Test
    void findBoxIndex() {
    }
}