package com.routinenyang.backend.user.dto;

import lombok.Builder;
import lombok.Getter;
import com.routinenyang.backend.user.entity.User;

import java.time.LocalDate;

@Builder
@Getter
public class UserBasicInfoDto {
    private String name;
    private LocalDate dateOfBirth;
    private String gender;

    public static UserBasicInfoDto from(User user) {
        return UserBasicInfoDto.builder()
                .name(user.getName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }

}
