package com.routinenyang.backend.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.routinenyang.backend.global.base.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;

    private String name;

    private boolean onBoardingFinished;
    private LocalDate dateOfBirth;
    private String gender;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSurvey> userSurveys = new ArrayList<>();

    public void setOnBoardingAsFinished() {
        onBoardingFinished = true;
    }

    public void updateUser(String name, LocalDate dateOfBirth, String gender) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public void addSurveyAnswer(UserSurvey answer) {
        userSurveys.add(answer);
        answer.setUser(this);
    }

    public void removeSurveyAnswer(UserSurvey answer) {
        userSurveys.remove(answer);
        answer.setUser(null);
    }
}
