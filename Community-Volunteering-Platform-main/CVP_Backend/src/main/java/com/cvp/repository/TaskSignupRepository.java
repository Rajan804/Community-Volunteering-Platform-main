package com.cvp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cvp.model.Task;
import com.cvp.model.TaskSignup;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskSignupRepository extends JpaRepository<TaskSignup, Long> {

    boolean existsByVolunteerNameAndTaskNameAndSignupDate(String volunteerName, String taskName, LocalDate signupDate);

    List<TaskSignup> findByVolunteerName(String volunteerName);

    @Query("SELECT ts FROM TaskSignup ts WHERE ts.taskName IN " +
            "(SELECT t.title FROM Task t WHERE t.org.id = :orgId)")
    List<TaskSignup> findVolunteersByOrganization(@Param("orgId") Long orgId);

    @Query("SELECT t FROM Task t JOIN TaskSignup ts ON t.id = ts.task.id WHERE ts.user.id = :userId")
    List<Task> findTasksByUserId(@Param("userId") Long userId);
    
    @Query("SELECT ts.task FROM TaskSignup ts WHERE ts.user.id = :userId AND ts.task.status = 'COMPLETED' AND ts.task.id NOT IN :ratedTaskIds")
    List<Task> findCompletedTasksNotRatedByUser(@Param("userId") Long userId, @Param("ratedTaskIds") List<Long> ratedTaskIds);

}
