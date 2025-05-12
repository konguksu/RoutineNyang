package com.routinenyang.backend.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.routinenyang.backend.global.base.BaseEntity;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSurvey extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "survey_id")
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    // 사용자 맵핑
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void updateAnswer(String answer) {
        this.answer = answer;
    }

    void setUser(User user) {
        this.user = user;
    }
}
