package com.example.secure.demo;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public class BaseUnitTest {
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
