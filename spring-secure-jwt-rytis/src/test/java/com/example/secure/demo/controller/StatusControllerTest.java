package com.example.secure.demo.controller;

import com.example.secure.demo.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class StatusControllerTest extends BaseUnitTest {

    @InjectMocks
    StatusController statusController;

    @Test
    void greeting() {
        assertEquals("Hello World !",statusController.greeting());
    }

    @Test
    void status() {
    }
}