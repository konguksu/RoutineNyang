package com.routinenyang.backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.routinenyang.backend.user.entity.UserSurvey;

public interface SurveyAnswerRepository extends JpaRepository<UserSurvey, Long> {
}
