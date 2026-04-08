package com.ds.app;

import com.ds.app.dto.response.InsuranceSummaryDTO;
import com.ds.app.entity.*;
import com.ds.app.enums.InsuranceStatus;
import com.ds.app.repository.EmployeeInsuranceRepository;
import com.ds.app.repository.EmployeeRepository;
import com.ds.app.repository.EmployeeTopUpRepository;
import com.ds.app.service.impl.InsuranceSummaryServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsuranceSummaryServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeInsuranceRepository employeeInsuranceRepository;

    @Mock
    private EmployeeTopUpRepository employeeTopUpRepository;

    @InjectMocks
    private InsuranceSummaryServiceImpl insuranceSummaryService;


    // ============================================================
    // Helpers
    // ============================================================
    private Employee buildEmployee(Long id, String username) {
        Employee emp = new Employee();
        emp.setUserId(id);
        emp.setUsername(username);
        emp.setFirstName("");
        emp.setLastName("");
        return emp;
    }

    private InsurancePlan buildPlan(String name, Double coverage) {
        InsurancePlan plan = new InsurancePlan();
        plan.setPlanName(name);
        plan.setCoverageAmount(coverage);
        return plan;
    }

    private EmployeeInsurance buildInsurance(Long id, Employee emp,
                                              InsurancePlan plan) {
        EmployeeInsurance ins = new EmployeeInsurance();
        ins.setId(id);
        ins.setEmployee(emp);
        ins.setInsurancePlan(plan);
        ins.setStatus(InsuranceStatus.ACTIVE);
        ins.setExpiryDate(LocalDate.now().plusYears(1));
        return ins;
    }

    private TopUpPlan buildTopUpPlan(String name, Double additional, Double price) {
        TopUpPlan plan = new TopUpPlan();
        plan.setTopUpName(name);
        plan.setAdditionalCoverage(additional);
        plan.setPrice(price);
        return plan;
    }

    private EmployeeTopUp buildTopUp(Long id, Employee emp, TopUpPlan topUpPlan) {
        EmployeeTopUp topUp = new EmployeeTopUp();
        topUp.setId(id);
        topUp.setEmployee(emp);
        topUp.setTopUpPlan(topUpPlan);
        topUp.setPurchasedDate(LocalDate.now());
        topUp.setExpiryDate(LocalDate.now().plusYears(1));
        topUp.setStatus(InsuranceStatus.ACTIVE);
        return topUp;
    }


    // ============================================================
    // TEST 1: with active top-ups — checks total coverage math
    // ============================================================
    @Test
    void getInsuranceSummary_ShouldReturnCorrectTotalCoverage_WithTopUps() {

        Employee emp = buildEmployee(1L, "emp01");
        InsurancePlan plan = buildPlan("Gold Plan", 500000.0);
        EmployeeInsurance insurance = buildInsurance(1L, emp, plan);

        TopUpPlan topUpPlan1 = buildTopUpPlan("Dental Cover", 100000.0, 1500.0);
        TopUpPlan topUpPlan2 = buildTopUpPlan("Vision Cover", 50000.0, 800.0);
        EmployeeTopUp topUp1 = buildTopUp(1L, emp, topUpPlan1);
        EmployeeTopUp topUp2 = buildTopUp(2L, emp, topUpPlan2);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(emp));
        when(employeeInsuranceRepository.findByEmployee_UserIdAndStatus(
                1L, InsuranceStatus.ACTIVE))
                .thenReturn(Optional.of(insurance));
        when(employeeTopUpRepository.findByEmployee_UserIdAndStatus(
                1L, InsuranceStatus.ACTIVE))
                .thenReturn(List.of(topUp1, topUp2));

        InsuranceSummaryDTO result = insuranceSummaryService.getInsuranceSummary(1L);

        assertNotNull(result);
        assertEquals(1L, result.getEmployeeId());
        assertEquals("Gold Plan", result.getBasePlanName());
        assertEquals(500000.0, result.getBaseCoverageAmount());
        assertEquals(2, result.getActiveTopUps().size());
        assertEquals(650000.0, result.getTotalCoverageAmount()); // 500000+100000+50000
    }


    // ============================================================
    // TEST 2: no top-ups — total = base only
    // ============================================================
    @Test
    void getInsuranceSummary_ShouldReturnBaseCoverageOnly_WhenNoTopUps() {

        Employee emp = buildEmployee(1L, "emp01");
        InsurancePlan plan = buildPlan("Silver Plan", 300000.0);
        EmployeeInsurance insurance = buildInsurance(1L, emp, plan);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(emp));
        when(employeeInsuranceRepository.findByEmployee_UserIdAndStatus(
                1L, InsuranceStatus.ACTIVE))
                .thenReturn(Optional.of(insurance));
        when(employeeTopUpRepository.findByEmployee_UserIdAndStatus(
                1L, InsuranceStatus.ACTIVE))
                .thenReturn(List.of());

        InsuranceSummaryDTO result = insuranceSummaryService.getInsuranceSummary(1L);

        assertEquals(300000.0, result.getTotalCoverageAmount());
        assertEquals(0, result.getActiveTopUps().size());
    }


    // ============================================================
    // TEST 3: employee not found
    // ============================================================
    @Test
    void getInsuranceSummary_ShouldThrowException_WhenEmployeeNotFound() {

        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                insuranceSummaryService.getInsuranceSummary(99L)
        );

        assertTrue(ex.getMessage().contains("not found"));
    }


    // ============================================================
    // TEST 4: no active insurance
    // ============================================================
    @Test
    void getInsuranceSummary_ShouldThrowException_WhenNoActiveInsurance() {

        Employee emp = buildEmployee(1L, "emp01");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(emp));
        when(employeeInsuranceRepository.findByEmployee_UserIdAndStatus(
                1L, InsuranceStatus.ACTIVE))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                insuranceSummaryService.getInsuranceSummary(1L)
        );

        assertTrue(ex.getMessage().contains("No active insurance"));
    }


 
}