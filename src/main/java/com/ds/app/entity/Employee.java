package com.ds.app.entity;

import com.ds.app.enums.CertificationStatus;
import com.ds.app.enums.EmployeeExperience;
import com.ds.app.enums.SkillStatus;
import com.ds.app.enums.Status;


import com.ds.app.enums.UserRole;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;


import lombok.Data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity

@Getter
@Setter

@Data

@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
@EqualsAndHashCode(
    callSuper = true,
    exclude = {
        "insurances",
        "topUps",
        "insuranceClaims",
        "company",
        "department",
        "project",
        "bankAccount",
        "investments",
        "cards",
        "salaryRecords",
        "escalations",
        "appraisals"
    }
)
@ToString(
    callSuper = true,
    exclude = {
        "insurances",
        "topUps",
        "insuranceClaims",
        "company",
        "department",
        "project",
        "bankAccount",
        "investments",
        "cards",
        "salaryRecords",
        "escalations",
        "appraisals"
    }
)
public class Employee extends AppUser {

    @Column(unique = true)
    private String employeeCode;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)

    private String email ;

  


    private Boolean isEscalated = Boolean.FALSE;

    @Column(nullable = false)
    private Double currentSalary = 0.0;

    private LocalDate joiningDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    private Boolean isDeleted = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    private EmployeeExperience employeeExperience;

    @Enumerated(EnumType.STRING)
    private CertificationStatus certificationStatus;
    
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private SkillStatus  skillStatus;

    /* ===================== Relationships ===================== */

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<EmployeeInsurance> insurances;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<EmployeeTopUp> topUps;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<InsuranceClaim> insuranceClaims;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties({
        "employees", "departments", "projects",
        "hibernateLazyInitializer", "handler"
    })
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({
        "employees", "projects", "company",
        "hibernateLazyInitializer", "handler"
    })
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties({
        "assignedEmployees", "company", "department",
        "hibernateLazyInitializer", "handler"
    })
    private Project project;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private EmployeeBankAccount bankAccount;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EmployeeInvestment> investments;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EmployeeCard> cards;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SalaryRecord> salaryRecords;

    @OneToMany(mappedBy = "targetEmployee", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Escalation> escalations = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appraisal> appraisals = new ArrayList<>();
    
    
    
	
	//---------------------------changes made by saurabh-----------------------
	@OneToMany(mappedBy = "employee",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<EmployeeTraining> employeeTrainings= new ArrayList<>();
	
	@OneToMany(mappedBy ="employee",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<Certification> certifications = new ArrayList<>();
	

    
    
 // ------------- Changes made for Tarushi Start -------------------------------
    
  //boolean isAssetEscalated=false; 
  	@Column(name="has_active_asset_escalation",nullable=false)
  	private Boolean hasActiveAssetEscalation=false;
  	
  	@OneToMany(mappedBy = "employee")
  	private List<AssetAllocation> assetAllocations;
      
  	@OneToMany(mappedBy = "employee")
      private List<AssetIssue> assetIssues;
      
  	
  	@OneToMany(mappedBy = "employee")
      private List<AssetEscalation> assetEscalations;
  	
  	
     

  	public Boolean isHasActiveAssetEscalation() {
  		return hasActiveAssetEscalation;
  	}

  	public void setHasActiveAssetEscalation(Boolean hasActiveAssetEscalation) {
  		this.hasActiveAssetEscalation = hasActiveAssetEscalation;
  	}
    
    
    
 // ------------- Changes made for Tarushi END -------------------------------
    
    
    
    
    
    
    
    
    
    
    
    


    /* ===================== Convenience Getters ===================== */
    public Long getCompanyId() {
        return company != null ? company.getId() : null;
    }

    public Long getDepartmentId() {
        return department != null ? department.getId() : null;
    }

    public Long getProjectId() {
        return project != null ? project.getId() : null;
    }

    
    
 // ADD THIS CONSTRUCTOR inside Employee class

    public Employee(String username,
                    String password,
                    boolean isDeleted,
                    UserRole role,
                    String firstName,
                    String lastName,
                    String employeeCode) {

        super(username, password, role);

        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeCode = employeeCode;
    }

}