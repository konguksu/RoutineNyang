package com.routinenyang.backend.routine.controller;

import com.routinenyang.backend.auth.resolver.CurrentUser;
import com.routinenyang.backend.global.response.ApiResponse;
import com.routinenyang.backend.global.response.ResponseFactory;
import com.routinenyang.backend.routine.dto.DailyNoteRequest;
import com.routinenyang.backend.routine.dto.DailyNoteResponse;
import com.routinenyang.backend.routine.service.DailyNoteService;
import com.routinenyang.backend.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routines")
@Tag(name = "Daily Note", description = "루틴의 날짜별 메모 API")
public class DailyNoteController {

    private final DailyNoteService dailyNoteService;

    @PutMapping("/{routine-id}/daily-notes")
    @Operation(summary = "메모 수정", description = "해당 날짜에 메모가 존재한다면 수정, 없으면 생성")
    public ResponseEntity<ApiResponse<Void>> createOrUpdate(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("routine-id") Long routineId,
            @RequestBody DailyNoteRequest request
    ) {
        dailyNoteService.update(user.getId(), routineId, request);
        return ResponseFactory.ok(null);
    }

    @GetMapping("/{routine-id}/daily-notes")
    @Operation(summary = "메모 조회", description = "루틴의 날짜별 메모를 루틴 id와 날짜로 조회. 해당 날짜에 메모를 작성한적 없다면 빈 문자열 반환.")
    public ResponseEntity<ApiResponse<DailyNoteResponse>> findByRoutineIdAndDate(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("routine-id") Long routineId,
            @RequestParam LocalDate date
    ) {
        return ResponseFactory.ok(dailyNoteService.findDailyNoteByRoutineAndDate(user.getId(), routineId, date));
    }
}
