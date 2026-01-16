package com.yorku.controller;

import com.yorku.pojo.Emp;
import com.yorku.pojo.LoginInfo;
import com.yorku.pojo.Result;
import com.yorku.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login Controller
 * Handles user authentication and login requests.
 */
@RestController
@Slf4j
public class LoginController {

    @Autowired
    private EmpService empService;

    /**
     * Login Endpoint
     * Authenticates the user based on provided credentials.
     */
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
        // Log the login attempt
        // Note: In a real production env, be careful not to log passwords!
        log.info("Login request received: {}", emp);

        LoginInfo info = empService.login(emp);

        if (info != null){
            return Result.success(info);
        }

        // Standard English error message for failed authentication
        return Result.error("Invalid username or password");
    }

}