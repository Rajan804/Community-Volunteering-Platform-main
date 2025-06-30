package com.cvp.service;

import com.cvp.repository.TaskRepository;
import com.cvp.repository.TaskSignupRepository;
import com.cvp.model.Task;
import com.cvp.model.TaskSignup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaskSignupService {
    private final TaskSignupRepository taskSignupRepository;

    @Autowired
    private EmailService mailSender;

    @Autowired
    public TaskSignupService(TaskSignupRepository taskSignupRepository) {
        this.taskSignupRepository = taskSignupRepository;
    }

    // Get all tasks signed up by a particular user
    public List<Task> getSignedUpTasksByUserId(Long userId) {
        return taskSignupRepository.findTasksByUserId(userId);
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskSignupRepository.findTasksByUserId(userId);
    }

    public List<TaskSignup> getAllSignups() {
        return taskSignupRepository.findAll();
    }

    // Add TaskRepository to fetch Task by ID

    @Autowired
    private TaskRepository taskRepository; // Add TaskRepository

    public String registerForTask(TaskSignup taskSignup) {
        if (taskSignup.getSignupDate() == null) {
            taskSignup.setSignupDate(LocalDate.now());
        }

        // Fetch Task from Database using taskName
        Task task = taskRepository.findByTitleIgnoreCase(taskSignup.getTaskName())
                                  .stream().findFirst().orElse(null);

        if (task == null) {
            return "Task not found. Please check the task name.";
        }

        // Set the Task object in TaskSignup
        taskSignup.setTask(task);

        boolean alreadyRegistered = taskSignupRepository.existsByVolunteerNameAndTaskNameAndSignupDate(
                taskSignup.getVolunteerName(), taskSignup.getTaskName(), taskSignup.getSignupDate());

        if (alreadyRegistered) {
            return "You have already registered for this task on the selected date.";
        } else {
            taskSignupRepository.save(taskSignup);
            
            String recipientEmail =taskSignup.getUser().getEmail();

            
            mailSender.sendEmailForTaskSignUp(
                    recipientEmail,
                    taskSignup.getTaskName(),
                    taskSignup.getVolunteerName(),
                    taskSignup.getSignupDate());

            return "Registration Successful!";
        }
    }



    

    public List<TaskSignup> getVolunteersByOrganization(Long orgId) {
        return taskSignupRepository.findVolunteersByOrganization(orgId);
    }

}
