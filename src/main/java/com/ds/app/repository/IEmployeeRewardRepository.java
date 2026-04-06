package com.ds.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ds.app.entity.EmployeeReward;
import com.ds.app.enums.RewardCategory;
import com.ds.app.enums.RewardType;

@Repository
public interface IEmployeeRewardRepository extends JpaRepository<EmployeeReward, Integer> {
	
		@Query("SELECT r FROM EmployeeReward r " +
		           "WHERE r.employee.userId = :userId " +
		           "ORDER BY r.rewardDate DESC")
		    List<EmployeeReward> findByEmployeeUserIdOrderByRewardDateDesc(@Param("userId") Long userId);
	
		
		Optional<EmployeeReward>  findByRewardIdAndEmployeeUserId(Integer rewardId, Long userId);
		
		//count rewards for employee
		long countByEmployeeUserId(Long userId);
		
		//find rewards by type
		List<EmployeeReward> findByEmployeeUserIdAndRewardType(Long userId, RewardType rewardType);
		
		//find rewards by category
		
		 // rewardCategory matches entity field ✅
	    List<EmployeeReward> findByEmployeeUserIdAndRewardCategory(Long userId, RewardCategory rewardCategory);
		
		//find tip rewarded employees
		@Query("SELECT r.employee.userId, COUNT(r) as rewardCount " +
				"FROM EmployeeReward r " + 
				"GROUP BY r.employee.userId " +
				"ORDER BY rewardCount DESC ")
		List<Object[]> findTopRewardedEmployee();
		
		

}//..end class
