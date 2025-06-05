package com.routinenyang.backend.test;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestUserRunner implements CommandLineRunner {

    private final TestUserImportService testUserImportService;

    @Override
    public void run(String... args) {
        testUserImportService.saveTestUser();
    }
}

