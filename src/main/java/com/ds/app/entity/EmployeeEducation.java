package com.ds.app.entity;
 
import java.time.LocalDateTime;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "employee_education")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEducation {
 
		    @Id
		    @GeneratedValue(strategy = GenerationType.IDENTITY)
		    private Integer eduId;
		 
		    // ManyToOne — many education records belong to one employee
		    @ManyToOne(fetch = FetchType.LAZY)
		    @JoinColumn(name = "user_id", nullable = false)
		    private Employee employee;
		 
		    // e.g. B.Tech, MBA, MCA, B.Sc, 12th, 10th
		    @Column(nullable = false, length = 100)
		    private String degree;
		 
		    // e.g. IIT Bombay, Mumbai University
		    @Column(nullable = false, length = 200)
		    private String institution;
		 
		    // e.g. Computer Science, Information Technology
		    @Column(length = 100)
		    private String fieldOfStudy;
		 
		    // Year of passing e.g. 2022
		    private Integer passingYear;
		 
		    // Percentage e.g. 85.5
		    private Double percentage;
		 
		    // Grade e.g. A, B, First Class, Distinction
		    @Column(length = 50)
		    private String grade;
		 
		    // City/state of institution
		    @Column(length = 100)
		    private String location;
		 
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
}
