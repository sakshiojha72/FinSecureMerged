package com.ds.app;

import com.ds.app.dto.request.ClaimRequestDTO;
import com.ds.app.dto.request.ClaimStatusUpdateDTO;
import com.ds.app.dto.response.ClaimResponseDTO;
import com.ds.app.entity.*;
import com.ds.app.repository.EmployeeInsuranceRepository;
import com.ds.app.repository.EmployeeRepository;
import com.ds.app.repository.InsuranceClaimRepository;
import com.ds.app.service.impl.InsuranceClaimServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsuranceClaimServiceImplTest {

    @Mock
    private InsuranceClaimRepository insuranceClaimRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeInsuranceRepository employeeInsuranceRepository;

    @InjectMocks
    private InsuranceClaimServiceImpl insuranceClaimService;


    // ============================================================
    // Helper — builds a fake Employee
    // ============================================================
    private Employee buildEmployee(Long id, String username) {
        Employee emp = new Employee();
        emp.setUserId(id);
        emp.setUsername(username);
        emp.setFirstName("");
        emp.setLastName("");
        return emp;
    }

    // Helper — builds a fake InsurancePlan
    private InsurancePlan buildPlan(Long id, String name, Double coverage) {
        InsurancePlan plan = new InsurancePlan();
        plan.setId(id);
        plan.setPlanName(name);
        plan.setCoverageAmount(coverage);
        return plan;
    }

    // Helper — builds a fake EmployeeInsurance
    private EmployeeInsurance buildInsurance(Long id, Employee emp,
                                              InsurancePlan plan,
                                              InsuranceStatus status) {
        EmployeeInsurance ins = new EmployeeInsurance();
        ins.setId(id);
        ins.setEmployee(emp);
        ins.setInsurancePlan(plan);
        ins.setStatus(status);
        return ins;
    }


    // ============================================================
    // TEST 1: raiseClaim — happy path
    // ============================================================
    @Test
    void raiseClaim_ShouldReturnResponse_WhenAllChecksPass() {

        Employee emp = buildEmployee(1L, "emp01");
        InsurancePlan plan = buildPlan(1L, "Gold Plan", 500000.0);
        EmployeeInsurance insurance = buildInsurance(1L, emp, plan, InsuranceStatus.ACTIVE);

        ClaimRequestDTO dto = new ClaimRequestDTO();
        dto.setEmployeeInsuranceId(1L);
        dto.setClaimAmount(10000.0);
        dto.setReason("Hospitalization");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(employeeInsuranceRepository.findById(1L)).thenReturn(Optional.of(insurance));
        when(insuranceClaimRepository.existsByEmployee_UserIdAndStatus(1L, ClaimStatus.PENDING))
                .thenReturn(false);

        // fake saved claim returned from DB
        InsuranceClaim savedClaim = new InsuranceClaim();
        savedClaim.setId(1L);
        savedClaim.setEmployee(emp);
        savedClaim.setEmployeeInsurance(insurance);
        savedClaim.setClaimAmount(10000.0);
        savedClaim.setReason("Hospitalization");
        savedClaim.setStatus(ClaimStatus.PENDING);
        savedClaim.setRaisedAt(LocalDateTime.now());

        when(insuranceClaimRepository.save(any(InsuranceClaim.class))).thenReturn(savedClaim);

        ClaimResponseDTO result = insuranceClaimService.raiseClaim(dto, 1L);

        assertNotNull(result);
        assertEquals(ClaimStatus.PENDING, result.getStatus());
        assertEquals(10000.0, result.getClaimAmount());
        assertEquals(1L, result.getClaimId());
    }


    // ============================================================
    // TEST 2: raiseClaim — expired insurance
    // ============================================================
    @Test
    void raiseClaim_ShouldThrowException_WhenInsuranceIsExpired() {

        Employee emp = buildEmployee(1L, "emp01");
        InsurancePlan plan = buildPlan(1L, "Gold Plan", 500000.0);
        EmployeeInsurance insurance = buildInsurance(1L, emp, plan, InsuranceStatus.EXPIRED);

        ClaimRequestDTO dto = new ClaimRequestDTO();
        dto.setEmployeeInsuranceId(1L);
        dto.setClaimAmount(10000.0);
        dto.setReason("Surgery");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(employeeInsuranceRepository.findById(1L)).thenReturn(Optional.of(insurance));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                insuranceClaimService.raiseClaim(dto, 1L)
        );

        assertTrue(ex.getMessage().contains("expired"));
        verify(insuranceClaimRepository, never()).save(any());
    }


    // ============================================================
    // TEST 3: raiseClaim — employee already has a PENDING claim
    // ============================================================
    @Test
    void raiseClaim_ShouldThrowException_WhenPendingClaimAlreadyExists() {

        Employee emp = buildEmployee(1L, "emp01");
        InsurancePlan plan = buildPlan(1L, "Gold Plan", 500000.0);
        EmployeeInsurance insurance = buildInsurance(1L, emp, plan, InsuranceStatus.ACTIVE);

        ClaimRequestDTO dto = new ClaimRequestDTO();
        dto.setEmployeeInsuranceId(1L);
        dto.setClaimAmount(5000.0);
        dto.setReason("Medicine");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(employeeInsuranceRepository.findById(1L)).thenReturn(Optional.of(insurance));
        when(insuranceClaimRepository.existsByEmployee_UserIdAndStatus(1L, ClaimStatus.PENDING))
                .thenReturn(true); // already has one pending

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                insuranceClaimService.raiseClaim(dto, 1L)
        );

        assertTrue(ex.getMessage().contains("pending claim"));
    }


    // ============================================================
    // TEST 4: raiseClaim — employee not found
    // ============================================================
    @Test
    void raiseClaim_ShouldThrowException_WhenEmployeeNotFound() {

        ClaimRequestDTO dto = new ClaimRequestDTO();
        dto.setEmployeeInsuranceId(1L);
        dto.setClaimAmount(5000.0);

        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                insuranceClaimService.raiseClaim(dto, 99L)
        );
    }


    // ============================================================
    // TEST 5: getEmployeeClaims — returns list
    // ============================================================
    @Test
    void getEmployeeClaims_ShouldReturnList() {

        Employee emp = buildEmployee(1L, "emp01");
        InsurancePlan plan = buildPlan(1L, "Gold Plan", 500000.0);
        EmployeeInsurance insurance = buildInsurance(1L, emp, plan, InsuranceStatus.ACTIVE);

        InsuranceClaim claim = new InsuranceClaim();
        claim.setId(1L);
        claim.setEmployee(emp);
        claim.setEmployeeInsurance(insurance);
        claim.setClaimAmount(8000.0);
        claim.setReason("Lab tests");
        claim.setStatus(ClaimStatus.PENDING);
        claim.setRaisedAt(LocalDateTime.now());

        when(insuranceClaimRepository.findByEmployee_UserId(1L))
                .thenReturn(List.of(claim));

        List<ClaimResponseDTO> result = insuranceClaimService.getEmployeeClaims(1L);

        assertEquals(1, result.size());
        assertEquals(ClaimStatus.PENDING, result.get(0).getStatus());
    }


    // ============================================================
    // TEST 6: getAllClaims — with status filter
    // ============================================================
    @Test
    void getAllClaims_WithStatusFilter_ShouldReturnFilteredList() {

        Employee emp = buildEmployee(1L, "emp01");
        InsurancePlan plan = buildPlan(1L, "Gold Plan", 500000.0);
        EmployeeInsurance insurance = buildInsurance(1L, emp, plan, InsuranceStatus.ACTIVE);

        InsuranceClaim claim = new InsuranceClaim();
        claim.setId(1L);
        claim.setEmployee(emp);
        claim.setEmployeeInsurance(insurance);
        claim.setClaimAmount(3000.0);
        claim.setReason("Checkup");
        claim.setStatus(ClaimStatus.APPROVED);

        when(insuranceClaimRepository.findByStatus(ClaimStatus.APPROVED))
                .thenReturn(List.of(claim));

        List<ClaimResponseDTO> result =
                insuranceClaimService.getAllClaims(ClaimStatus.APPROVED);

        assertEquals(1, result.size());
        assertEquals(ClaimStatus.APPROVED, result.get(0).getStatus());
    }


    // ============================================================
    // TEST 7: getAllClaims — no filter (returns all)
    // ============================================================
    @Test
    void getAllClaims_WithNoFilter_ShouldReturnAll() {

        Employee emp = buildEmployee(1L, "emp01");
        InsurancePlan plan = buildPlan(1L, "Gold Plan", 500000.0);
        EmployeeInsurance insurance = buildInsurance(1L, emp, plan, InsuranceStatus.ACTIVE);

        InsuranceClaim c1 = new InsuranceClaim();
        c1.setId(1L); c1.setEmployee(emp);
        c1.setEmployeeInsurance(insurance);
        c1.setClaimAmount(1000.0); c1.setReason("A");
        c1.setStatus(ClaimStatus.PENDING);

        InsuranceClaim c2 = new InsuranceClaim();
        c2.setId(2L); c2.setEmployee(emp);
        c2.setEmployeeInsurance(insurance);
        c2.setClaimAmount(2000.0); c2.setReason("B");
        c2.setStatus(ClaimStatus.APPROVED);

        when(insuranceClaimRepository.findAll()).thenReturn(List.of(c1, c2));

        List<ClaimResponseDTO> result = insuranceClaimService.getAllClaims(null);

        assertEquals(2, result.size());
    }


    // ============================================================
    // TEST 8: updateClaimStatus — happy path (approve)
    // ============================================================
    @Test
    void updateClaimStatus_ShouldApprove_WhenClaimIsPending() {

        Employee emp = buildEmployee(1L, "emp01");
        InsurancePlan plan = buildPlan(1L, "Gold Plan", 500000.0);
        EmployeeInsurance insurance = buildInsurance(1L, emp, plan, InsuranceStatus.ACTIVE);

        InsuranceClaim claim = new InsuranceClaim();
        claim.setId(1L);
        claim.setEmployee(emp);
        claim.setEmployeeInsurance(insurance);
        claim.setClaimAmount(5000.0);
        claim.setReason("Surgery");
        claim.setStatus(ClaimStatus.PENDING);
        claim.setRaisedAt(LocalDateTime.now());

        ClaimStatusUpdateDTO dto = new ClaimStatusUpdateDTO();
        dto.setClaimId(1L);
        dto.setStatus(ClaimStatus.APPROVED);
        dto.setAdminRemarks("Verified and approved");
        dto.setResolvedBy("admin01");

        when(insuranceClaimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(insuranceClaimRepository.save(any())).thenReturn(claim);

        ClaimResponseDTO result = insuranceClaimService.updateClaimStatus(dto);

        assertEquals(ClaimStatus.APPROVED, result.getStatus());
        verify(insuranceClaimRepository, times(1)).save(claim);
    }


    // ============================================================
    // TEST 9: updateClaimStatus — claim already resolved
    // ============================================================
    @Test
    void updateClaimStatus_ShouldThrowException_WhenClaimAlreadyResolved() {

        Employee emp = buildEmployee(1L, "emp01");
        InsurancePlan plan = buildPlan(1L, "Gold Plan", 500000.0);
        EmployeeInsurance insurance = buildInsurance(1L, emp, plan, InsuranceStatus.ACTIVE);

        InsuranceClaim claim = new InsuranceClaim();
        claim.setId(1L);
        claim.setEmployee(emp);
        claim.setEmployeeInsurance(insurance);
        claim.setStatus(ClaimStatus.APPROVED); // already resolved!

        ClaimStatusUpdateDTO dto = new ClaimStatusUpdateDTO();
        dto.setClaimId(1L);
        dto.setStatus(ClaimStatus.REJECTED);
        dto.setAdminRemarks("Trying to reject already approved");

        when(insuranceClaimRepository.findById(1L)).thenReturn(Optional.of(claim));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                insuranceClaimService.updateClaimStatus(dto)
        );

        assertTrue(ex.getMessage().contains("PENDING"));
    }


    // ============================================================
    // TEST 10: updateClaimStatus — cannot set status back to PENDING
    // ============================================================
    @Test
    void updateClaimStatus_ShouldThrowException_WhenNewStatusIsPending() {

        Employee emp = buildEmployee(1L, "emp01");
        InsurancePlan plan = buildPlan(1L, "Gold Plan", 500000.0);
        EmployeeInsurance insurance = buildInsurance(1L, emp, plan, InsuranceStatus.ACTIVE);

        InsuranceClaim claim = new InsuranceClaim();
        claim.setId(1L);
        claim.setEmployee(emp);
        claim.setEmployeeInsurance(insurance);
        claim.setStatus(ClaimStatus.PENDING);

        ClaimStatusUpdateDTO dto = new ClaimStatusUpdateDTO();
        dto.setClaimId(1L);
        dto.setStatus(ClaimStatus.PENDING); // trying to set back to PENDING

        when(insuranceClaimRepository.findById(1L)).thenReturn(Optional.of(claim));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                insuranceClaimService.updateClaimStatus(dto)
        );

        assertTrue(ex.getMessage().contains("PENDING"));
    }
}