package com.cvp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cvp.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByTaskId(Long taskId);
    @Query("SELECT r.task.id FROM Rating r WHERE r.user.id = :userId")
    List<Long> findRatedTaskIdsByUserId(@Param("userId") Long userId);
    
    
    boolean existsByUserIdAndTaskId(Long userId, Long taskId);


}