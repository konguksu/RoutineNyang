package com.routinenyang.backend.routine.controller;

import com.routinenyang.backend.auth.resolver.CurrentUser;
import com.routinenyang.backend.routine.dto.RoutineDetailResponse;
import com.routinenyang.backend.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.routinenyang.backend.global.response.ApiResponse;
import com.routinenyang.backend.global.response.ResponseFactory;
import com.routinenyang.backend.routine.dto.RoutineRequest;
import com.routinenyang.backend.routine.dto.RoutineResponse;
import com.routinenyang.backend.routine.dto.RoutineUpdateRequest;
import com.routinenyang.backend.routine.service.RoutineService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routines")
@Tag(name = "Routine", description = "루틴 API")
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping
    @Operation(summary = "Routine 생성", description = "새로운 루틴을 생성")
    public ResponseEntity<ApiResponse<RoutineResponse>> create(
            @CurrentUser User user,
            @RequestBody RoutineRequest request) {
        return ResponseFactory.created(routineService.createRoutine(user, request));
    }

    @GetMapping("/filter")
    @Operation(summary = "Routine 목록 조회 (필터)", description = "전체, 그룹별, 종료되지 않은 루틴 조회 + 페이징")
    public ResponseEntity<ApiResponse<Page<RoutineResponse>>> findAllWithFilter(
            @CurrentUser User user,
            @RequestParam(required = false) Long groupId,
            @RequestParam(defaultValue = "false") boolean activeOnly,
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = DESC)
            Pageable pageable
    ) {
        return ResponseFactory.ok(routineService.findAllWithFilter(user, groupId, activeOnly, pageable));
    }

    @GetMapping
    @Operation(summary = "Routine 목록 조회 (날짜)", description = "특정 날짜에 수행해야 하는 루틴 목록 조회")
    public ResponseEntity<ApiResponse<List<RoutineResponse>>> findAllByDate(
            @CurrentUser User user,
            @RequestParam LocalDate date
    ) {
        return ResponseFactory.ok(routineService.findAllByDate(user, date));
    }

    @GetMapping("{routine-id}")
    @Operation(summary = "Routine 조회 (id)", description = "루틴의 고유 식별자(id)로 루틴 상세 정보 조회")
    public ResponseEntity<ApiResponse<RoutineDetailResponse>> find(
            @PathVariable("routine-id") Long id
    ) {
        return ResponseFactory.ok(routineService.findRoutineById(id));
    }

    @PutMapping("{routine-id}")
    @Operation(summary = "Routine 수정", description = "루틴의 세부 정보 수정")
    public ResponseEntity<ApiResponse<RoutineResponse>> update(
            @RequestBody RoutineUpdateRequest request, @PathVariable("routine-id") Long routineId) {
        return ResponseFactory.ok(routineService.updateById(routineId, request));
    }

    @DeleteMapping("{routine-id}")
    @Operation(summary = "Routine 삭제", description = "루틴 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable("routine-id") Long routineId
    ) {
        routineService.deleteById(routineId);
        return ResponseFactory.noContent();
    }
}
