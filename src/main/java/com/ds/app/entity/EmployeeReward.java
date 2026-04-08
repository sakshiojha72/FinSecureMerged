package com.ds.app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;


import com.ds.app.enums.RewardCategory;
import com.ds.app.enums.RewardType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "employee_reward")
public class EmployeeReward {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rewardId;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private RewardType rewardType;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private RewardCategory rewardCategory;
	
	@Column(nullable = false, length = 200)
	private String title;
	
	@Column(length = 500)
	private String description;
	
	@Column(nullable = false)
	private LocalDate rewardDate;
	
	@Column(nullable = false, length = 100)
	private String givenBy; 
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void prePersist() {
		
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private Employee employee;
	
	

}//end class
