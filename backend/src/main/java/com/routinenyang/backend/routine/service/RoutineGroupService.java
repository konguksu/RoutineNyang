package com.routinenyang.backend.routine.service;

import com.routinenyang.backend.global.exception.CustomException;
import com.routinenyang.backend.routine.dto.RoutineGroupRequest;
import com.routinenyang.backend.routine.dto.RoutineGroupResponse;
import com.routinenyang.backend.routine.entity.Routine;
import com.routinenyang.backend.routine.entity.RoutineGroup;
import com.routinenyang.backend.routine.repository.RoutineGroupRepository;
import com.routinenyang.backend.routine.repository.RoutineRepository;
import com.routinenyang.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.routinenyang.backend.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineGroupService {

    private final RoutineGroupRepository routineGroupRepository;
    private final RoutineRepository routineRepository;
    private static final String DEFAULT_GROUP_NAME = "기본";


    public RoutineGroupResponse createGroup(User user, RoutineGroupRequest request) {
        RoutineGroup savedGroup = routineGroupRepository.save(RoutineGroup.builder()
                .userId(user.getId())
                .name(request.getName())
                .build());
        return RoutineGroupResponse.from(savedGroup);
    }

    public List<RoutineGroupResponse> findAllByLoginUser(User user) {
        List<RoutineGroup> groupList = routineGroupRepository.findByUserIdOrderByNameAsc(user.getId());
        return groupList.stream()
                .map(RoutineGroupResponse::from)
                .toList();

    }

    public RoutineGroupResponse updateById(User user, Long groupId, RoutineGroupRequest request) {
        RoutineGroup group = validateEditableGroup(user, groupId);
        group.update(request.getName());
        return RoutineGroupResponse.from(group);
    }

    public void deleteById(User user, Long groupId) {
        RoutineGroup group = validateEditableGroup(user, groupId);

        // 그룹에 소속된 루틴들 조회
        List<Routine> routines = routineRepository.findByGroupIdAndDeletedFalse(groupId);

        // 소속된 루틴 존재하면 기본 그룹으로 이동
        if (!routines.isEmpty()) {
            RoutineGroup defaultGroup = routineGroupRepository.findByUserIdAndName(user.getId(), DEFAULT_GROUP_NAME)
                    .orElseThrow(() -> new CustomException(ROUTINE_GROUP_NOT_FOUND));
            for (Routine routine : routines) {
                routine.moveToGroup(defaultGroup);
            }
        }
        // 그룹 삭제
        routineGroupRepository.delete(group);
    }

    private RoutineGroup validateEditableGroup(User user, Long groupId) {
        // 존재하는 루틴 그룹 ID인지 확인
        RoutineGroup group = routineGroupRepository.findById(groupId).orElseThrow(
                () -> new CustomException(ROUTINE_GROUP_NOT_FOUND)
        );
        // 로그인한 유저의 루틴 그룹인지 확인
        if (!Objects.equals(group.getUserId(), user.getId())) {
            throw new CustomException(UNAUTHORIZED_REQUEST);
        }
        // 기본 루틴 그룹은 수정/삭제 불가
        if (group.getName().equals(DEFAULT_GROUP_NAME)) {
            throw new CustomException(ROUTINE_GROUP_IMMUTABLE);
        }
        return group;
    }
}
