package com.routinenyang.backend.user;

import com.routinenyang.backend.global.exception.CustomException;
import com.routinenyang.backend.global.exception.ErrorCode;
import com.routinenyang.backend.routine.entity.RoutineGroup;
import com.routinenyang.backend.routine.repository.RoutineGroupRepository;
import com.routinenyang.backend.user.dto.*;
import com.routinenyang.backend.user.entity.User;
import com.routinenyang.backend.user.entity.UserSurvey;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final RoutineGroupRepository routineGroupRepository;

    // 유저 첫 온보딩 시 기본 정보 & 설문 정보 저장
    public UserResponse saveUserOnboarding(User user, UserRequest request) {
        if (!user.isOnBoardingFinished()) {
            user.setOnBoardingAsFinished();
        }
        user.updateUser(request.getName(), request.getDateOfBirth(), request.getGender());
        // 루틴 기본 그룹 생성
        routineGroupRepository.save(RoutineGroup.builder()
                .userId(user.getId())
                .name("기본")
                .build());
        return UserResponse.from(user, updateAnswers(user, request.getSurveys()));
    }

    // 사용자 기본 정보 조회
    public UserBasicInfoDto findBasicInfo(User user) {
        return UserBasicInfoDto.from(user);
    }

    // 사용자 설문 응답 목록 조회
    public SurveyResponse findSurveyAnswers(User user) {
        List<SurveyDto> surveyDtos = user.getUserSurveys().stream()
                .map(SurveyDto::from).toList();

        return SurveyResponse.builder()
                .totalCount(surveyDtos.size())
                .savedSurveys(surveyDtos)
                .build();
    }

    public UserBasicInfoDto updateBasicInfo(User user, UserBasicInfoDto request) {
        user.updateUser(request.getName(), request.getDateOfBirth(), request.getGender());
        return UserBasicInfoDto.from(user);
    }

    // 유저 설문 응답 저장 & 수정
    public SurveyResponse updateAnswers(User user, List<SurveyDto> requests) {
        Map<String, UserSurvey> currentSurveys = user.getUserSurveys().stream()
                .collect(Collectors.toMap(UserSurvey::getQuestion, a -> a));

        for (SurveyDto req : requests) {
            // 존재하는 설문 수정일 경우
            if (currentSurveys.containsKey(req.getQuestion())) {
                currentSurveys.get(req.getQuestion()).updateAnswer(req.getAnswer());
            }
            // 새로운 입력일 경우
            else {
                user.addSurveyAnswer(req.toEntity());
            }
        }
        List<SurveyDto> resultSurveys = user.getUserSurveys().stream().map(SurveyDto::from).toList();

        return SurveyResponse.builder()
                .totalCount(resultSurveys.size())
                .savedSurveys(resultSurveys)
                .build();
    }

    public UserLoginSuccessResponse loginSuccess(HttpServletRequest request) {
        Map<String, Object> result = (Map<String, Object>) request.getSession().getAttribute("loginResult");
        if (result == null) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        return UserLoginSuccessResponse.builder()
                .onboardingFinished((Boolean) result.get("onboardingFinished"))
                .token((String) result.get("token"))
                .userName((String) result.get("userName"))
                .build();
    }
}
