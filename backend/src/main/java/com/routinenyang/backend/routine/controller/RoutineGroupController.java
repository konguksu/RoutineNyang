package com.routinenyang.backend.routine.controller;

import com.routinenyang.backend.auth.resolver.CurrentUser;
import com.routinenyang.backend.global.response.ApiResponse;
import com.routinenyang.backend.global.response.ResponseFactory;
import com.routinenyang.backend.routine.dto.RoutineGroupRequest;
import com.routinenyang.backend.routine.dto.RoutineGroupResponse;
import com.routinenyang.backend.routine.service.RoutineGroupService;
import com.routinenyang.backend.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routines/groups")
@Tag(name = "Routine Group", description = "루틴 그룹 API")
public class RoutineGroupController {

    private final RoutineGroupService routineGroupService;

    @PostMapping
    @Operation(summary = "Routine Group 생성", description = "루틴 그룹을 생성합니다.")
    public ResponseEntity<ApiResponse<RoutineGroupResponse>> create(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestBody RoutineGroupRequest request) {
        return ResponseFactory.created(routineGroupService.createGroup(user, request));
    }

    @GetMapping
    @Operation(summary = "Routine Group 목록 조회", description = "로그인한 유저의 루틴 그룹 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<RoutineGroupResponse>>> findAll(
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        return ResponseFactory.ok(routineGroupService.findAllByLoginUser(user));
    }

    @PutMapping("{routine-group-id}")
    @Operation(summary = "Routine Group 수정", description = "루틴 그룹의 이름을 수정합니다.")
    public ResponseEntity<ApiResponse<RoutineGroupResponse>> update(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestBody RoutineGroupRequest request,
            @PathVariable("routine-group-id") Long groupId) {
        return ResponseFactory.ok(routineGroupService.updateById(user, groupId, request));
    }

    @DeleteMapping("{routine-group-id}")
    @Operation(summary = "Routine Group 삭제", description = "루틴 그룹을 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("routine-group-id") Long groupId) {
        routineGroupService.deleteById(user, groupId);
        return ResponseFactory.noContent();
    }

}
