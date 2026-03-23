package com.ds.app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends AppUser{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "User ID is required")
	@Column(nullable = false, unique = true)
	private Integer userId;
	
	@NotNull(message = "Employee Code is Required")
	@Size(min = 3, max = 20, message = "Employee Code musr be between 3 and 20 characters")
	@Column(nullable = false, unique = true, length = 20)
	private String employeeCode;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@Column
	private Long departmentId;
	
	@Column
	private Long companyId;
	
	@Column
	private Long projectId;
	
	@NotNull(message = "Employee Type is required")
	@Enumerated(EnumType.STRING)
	 @Column(nullable = false, length = 20)
    private EmployeeExperience employeeExperience;

    @NotNull(message = "Certification status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CertificationStatus certificationStatus;

    @Column(nullable = false)
    private Boolean trainingRequired = false;

    @Column(nullable = false)
    private Boolean isEscalated = false;

    @NotNull(message = "Salary is required")
    @PositiveOrZero(message = "Salary must be zero or greater")
    @Column(nullable = false)
    private Double salary;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    @Column(nullable = false)
    private LocalDate joiningDate;

    @NotNull(message = "Employee status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmployeeStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeSkill employeeSkill;

    @Size(max = 255, message = "Profile photo URL is too long")
    @Column(length = 255)
    private String profilePhotoUrl;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private BankDetail bankDetail;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<InvestmentDetail> investmentDetail;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        applyBusinessRules();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        applyBusinessRules();
    }

    private void applyBusinessRules() {
        this.trainingRequired =
                this.certificationStatus == CertificationStatus.NON_CERTIFIED;
    }
	
}// end class
