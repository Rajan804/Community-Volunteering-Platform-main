package com.cvp.controller;

import com.cvp.service.TaskSignupService;
import com.cvp.model.Task;
import com.cvp.model.TaskSignup;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasksignup")
public class TaskSignupController {

    private final TaskSignupService taskSignupService;

    @Autowired
    public TaskSignupController(TaskSignupService taskSignupService) {
        this.taskSignupService = taskSignupService;
    }

    @GetMapping("/all")
    public List<TaskSignup> getAllSignups() {
        return taskSignupService.getAllSignups();
    }

    @PostMapping("/register")
    public String registerTask(@Valid @RequestBody TaskSignup taskSignup, BindingResult result) {
        if (result.hasErrors()) {
            return result.getFieldErrors().get(0).getDefaultMessage(); // Returns the first validation error message
        }

        return taskSignupService.registerForTask(taskSignup);
    }

    @GetMapping("/user/signedup/{userId}")
    public ResponseEntity<List<Task>> getSignedUpTasks(@PathVariable Long userId) {
        List<Task> signedUpTasks = taskSignupService.getSignedUpTasksByUserId(userId);
        return ResponseEntity.ok(signedUpTasks);
    }


    // Get Tasks by UserId
    @GetMapping("/user/{userid}")
    public ResponseEntity<List<Task>> getUserTasks(@PathVariable Long userid) {
        return ResponseEntity.ok(taskSignupService.getTasksByUserId(userid));
    }
}
