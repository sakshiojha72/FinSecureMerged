package com.ds.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "investment_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Employee is required")
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private InvestmentType investmentType;

    @NotBlank(message = "Investment name is required")
    @Size(max = 100, message = "Investment name is too long")
    @Column(nullable = false, length = 100)
    private String investmentName;

    @NotNull(message = "Amount is required")
    @PositiveOrZero(message = "Amount must be zero or positive")
    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Boolean declaredToSystem = false;

    @Column(nullable = false)
    private Boolean directInvestment = false;

    @Column(nullable = false)
    private Boolean whitelistedFund = false;

    @Column(nullable = false)
    private Boolean restrictedByClient = false;

    @Size(max = 255, message = "Compliance remark is too long")
    @Column(length = 255)
    private String complianceRemark;

    @PrePersist
    @PreUpdate
    public void applyBusinessRules() {
        if (Boolean.FALSE.equals(declaredToSystem)) {
            this.complianceRemark = "Investment must be declared to the system";
        } else if (Boolean.TRUE.equals(restrictedByClient) && Boolean.TRUE.equals(directInvestment)) {
            this.complianceRemark = "Direct investment is restricted for this employee/client";
        } else if (Boolean.FALSE.equals(directInvestment) && Boolean.FALSE.equals(whitelistedFund)) {
            this.complianceRemark = "Only whitelisted funds are allowed";
        } else {
            this.complianceRemark = "Compliant";
        }
    }

}//end class
