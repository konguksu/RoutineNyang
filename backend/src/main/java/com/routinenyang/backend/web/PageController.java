package com.routinenyang.backend.web;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Hidden
@Controller
public class PageController {

    @GetMapping("/loginPage")
    public String login() {
        return "login";
    }

    @GetMapping("/result")
    public String resultPage(HttpServletRequest request, Model model) {
        Map<String, Object> result = (Map<String, Object>) request.getSession().getAttribute("loginResult");

        if (result != null) {
            model.addAttribute("userName", result.get("userName"));
            model.addAttribute("onBoardingFinished", Boolean.TRUE.equals(result.get("onBoardingFinished")));
            model.addAttribute("token", result.get("token"));
        } else {
            model.addAttribute("error", "로그인 결과가 없습니다.");
        }

        return "result";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/onboarding")
    public String onboarding(HttpServletRequest request, Model model) {
        Map<String, Object> result = (Map<String, Object>) request.getSession().getAttribute("loginResult");

        if (result != null) {
            model.addAttribute("userName", result.get("userName"));
        }

        return "onboarding";
    }

    @RequestMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {}
}