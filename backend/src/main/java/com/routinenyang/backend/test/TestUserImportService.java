package com.routinenyang.backend.test;

import com.routinenyang.backend.user.UserService;
import com.routinenyang.backend.user.dto.SurveyDto;
import com.routinenyang.backend.user.dto.UserRequest;
import com.routinenyang.backend.user.entity.User;
import com.routinenyang.backend.user.entity.UserSurvey;
import com.routinenyang.backend.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Profile({"local", "docker-dev"})
public class TestUserImportService {
    private final UserService userService;
    private final UserRepository userRepository;

    private static final String TEST_EMAIL = "test_user@gmail.com";

    public void saveTestUser() {
        if (userRepository.findByEmail(TEST_EMAIL).isPresent()) return;
        User testUser = User.builder()
                .email(TEST_EMAIL)
                .name("테스트 유저")
                .build();
        userRepository.save(testUser);

        SurveyDto testSurvey1 = SurveyDto.builder()
                .question("테스트 유저의 설문 질문1")
                .answer("테스트 유저의 설문 응답1")
                .build();
        SurveyDto testSurvey2 = SurveyDto.builder()
                .question("테스트 유저의 설문 질문2")
                .answer("테스트 유저의 설문 응답2")
                .build();
        UserRequest request = UserRequest.builder()
                .name("테스트 유저")
                .dateOfBirth(LocalDate.of(1999,9,18))
                .gender("FEMALE")
                .surveys(List.of(testSurvey1, testSurvey2))
                .build();
        userService.saveUserOnboarding(testUser, request);
    }
}
