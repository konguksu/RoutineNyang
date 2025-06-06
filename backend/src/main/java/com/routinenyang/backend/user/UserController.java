package com.routinenyang.backend.user;

import com.routinenyang.backend.auth.resolver.CurrentUser;
import com.routinenyang.backend.global.response.ApiResponse;
import com.routinenyang.backend.global.response.ResponseFactory;
import com.routinenyang.backend.store.dto.ItemResponse;
import com.routinenyang.backend.store.service.ItemService;
import com.routinenyang.backend.user.dto.*;
import com.routinenyang.backend.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "유저 API")
public class UserController {

    private final UserService userService;
    private final ItemService itemService;

    @PostMapping("/onboarding")
    @Operation(summary = "User 온보딩 정보 저장", description = "온보딩 화면에서 입력받은 유저의 기본 정보 및 설문 결과 저장")
    public ResponseEntity<ApiResponse<UserResponse>> saveOnboarding(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestBody UserRequest request) {
        return ResponseFactory.ok(userService.saveUserOnboarding(user, request));
    }

    @GetMapping("/info/basics")
    @Operation(summary = "User 기본 정보 조회", description = "유저의 이름, 나이, 성별 조회")
    public ResponseEntity<ApiResponse<UserBasicInfoDto>> findBasicUserInfo(
            @Parameter(hidden = true) @CurrentUser User user) {
        return ResponseFactory.ok(userService.findBasicInfo(user));
    }

    @GetMapping("/info/survey-answers")
    @Operation(summary = "User 설문 정보 조회", description = "온보딩에서 입력받은 유저의 설문 결과 조회")
    public ResponseEntity<ApiResponse<SurveyResponse>> findSurveyAnswers(
            @Parameter(hidden = true) @CurrentUser User user) {
        return ResponseFactory.ok(userService.findSurveyAnswers(user));
    }

    @GetMapping("/items")
    @Operation(summary = "유저가 보유한 아이템 목록 조회", description = "유저가 구매하여 보유중인 아이템 목록 조회")
    public  ResponseEntity<ApiResponse<List<ItemResponse>>> findAllItemsByUser(
            @Parameter(hidden = true) @CurrentUser User user) {
        return ResponseFactory.ok(itemService.findAllByUser(user));
    }

    @PutMapping("/info/basics")
    @Operation(summary = "User 기본 정보 수정", description = "유저의 기본 정보 수정")
    public ResponseEntity<ApiResponse<UserBasicInfoDto>> updateUserInfo(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestBody UserBasicInfoDto request) {
        return ResponseFactory.ok(userService.updateBasicInfo(user, request));
    }


    @PutMapping("/info/survey-answers")
    @Operation(summary = "User 설문 정보 수정", description = "유저의 설문 정보 수정")
    public ResponseEntity<ApiResponse<SurveyResponse>> updateSurveyAnswers(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestBody List<SurveyDto> request) {
        return ResponseFactory.ok(userService.updateAnswers(user, request));
    }

    @GetMapping("/oauth2/login/success")
    @Operation(summary = "구글 로그인 성공 시 응답", description = "유저의 이름, 온보딩 완료 여부, jwt 토큰 반환")
    public UserLoginSuccessResponse oauthLoginSuccess(HttpServletRequest request) {
        return userService.loginSuccess(request);
    }
}
