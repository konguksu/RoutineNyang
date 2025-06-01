package com.routinenyang.backend.routine.controller;

import com.routinenyang.backend.auth.resolver.CurrentUser;
import com.routinenyang.backend.global.response.ApiResponse;
import com.routinenyang.backend.global.response.ResponseFactory;
import com.routinenyang.backend.routine.dto.*;
import com.routinenyang.backend.routine.service.RoutineExecutionService;
import com.routinenyang.backend.routine.service.RoutineService;
import com.routinenyang.backend.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routines")
@Tag(name = "Routine", description = "루틴 API")
public class RoutineController {

    private final RoutineService routineService;
    private final RoutineExecutionService routineExecutionService;

    @PostMapping
    @Operation(summary = "Routine 생성", description = "새로운 루틴을 생성")
    public ResponseEntity<ApiResponse<RoutineSummaryResponse>> create(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestBody RoutineRequest request) {
        return ResponseFactory.created(routineService.create(user.getId(), request));
    }

    @GetMapping("/filter")
    @Operation(summary = "Routine 목록 조회 (필터)", description = "전체, 그룹별, 종료 여부 필터링해서 루틴 조회 + 페이징")
    public ResponseEntity<ApiResponse<Page<RoutineSummaryResponse>>> findAllWithFilter(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestParam(required = false) Long groupId,
            @RequestParam(defaultValue = "false") boolean isActive,
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = DESC)
            Pageable pageable
    ) {
        return ResponseFactory.ok(routineService.findAllWithFilter(user.getId(), groupId, isActive, pageable));
    }

    @GetMapping
    @Operation(summary = "Routine Status 목록 조회 (날짜)", description = "특정 날짜에 수행해야 하는 루틴의 수행 상태 목록 조회")
    public ResponseEntity<ApiResponse<List<RoutineStatusResponse>>> findAllStatusByDate(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestParam LocalDate date
    ) {
        return ResponseFactory.ok(routineService.findAllStatusByDate(user.getId(), date));
    }

    @GetMapping("{routine-id}")
    @Operation(summary = "Routine 속성 정보 조회", description = "루틴의 고유 식별자(id)로 수정 가능한 루틴 속성 조회")
    public ResponseEntity<ApiResponse<RoutineResponse>> find(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("routine-id") Long id
    ) {
        return ResponseFactory.ok(routineService.findById(user.getId(), id));
    }

    @PutMapping("{routine-id}")
    @Operation(summary = "Routine 수정", description = "루틴의 세부 정보 수정")
    public ResponseEntity<ApiResponse<RoutineSummaryResponse>> update(
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

    @PatchMapping("/{routine-id}/toggle")
    @Operation(summary = "Routine Execution 상태 토글", description = "루틴을 수행 완료한 상태라면 실패로, 실패한 상태라면 완료로 변경")
    public ResponseEntity<ApiResponse<Void>> toggleExecution(
            @PathVariable("routine-id") Long routineId,
            @RequestParam LocalDate date
    ) {
        routineExecutionService.toggleExecution(routineId, date);
        return ResponseFactory.ok((Void) null);
    }
}
