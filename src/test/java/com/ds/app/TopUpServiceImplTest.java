package com.ds.app;

import com.ds.app.dto.request.BuyTopUpRequestDTO;
import com.ds.app.dto.request.CreateTopUpPlanRequestDTO;
import com.ds.app.dto.response.EmployeeTopUpResponseDTO;
import com.ds.app.dto.response.TopUpPlanResponseDTO;
import com.ds.app.entity.*;
import com.ds.app.repository.EmployeeInsuranceRepository;
import com.ds.app.repository.EmployeeRepository;
import com.ds.app.repository.EmployeeTopUpRepository;
import com.ds.app.repository.TopUpPlanRepository;
import com.ds.app.service.impl.TopUpServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopUpServiceImplTest {

    @Mock
    private TopUpPlanRepository topUpPlanRepository;

    @Mock
    private EmployeeTopUpRepository employeeTopUpRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeInsuranceRepository employeeInsuranceRepository;

    @InjectMocks
    private TopUpServiceImpl topUpService;


    // ============================================================
    // Helper methods
    // ============================================================
    private Employee buildEmployee(Long id, String username) {
        Employee emp = new Employee();
        emp.setUserId(id);
        emp.setUsername(username);
        emp.setFirstName("");
        emp.setLastName("");
        return emp;
    }

    private TopUpPlan buildTopUpPlan(Long id, String name, boolean isActive) {
        TopUpPlan plan = new TopUpPlan();
        plan.setId(id);
        plan.setTopUpName(name);
        plan.setAdditionalCoverage(100000.0);
        plan.setPrice(2000.0);
        plan.setIsActive(isActive);
        return plan;
    }


    // ============================================================
    // TEST 1: createTopUpPlan — happy path
    // ============================================================
    @Test
    void createTopUpPlan_ShouldReturnResponse_WhenNameIsUnique() {

        CreateTopUpPlanRequestDTO dto = new CreateTopUpPlanRequestDTO();
        dto.setTopUpName("Dental Cover");
        dto.setAdditionalCoverage(100000.0);
        dto.setPrice(1500.0);
        dto.setDescription("Covers dental procedures");

        when(topUpPlanRepository.existsByTopUpName("Dental Cover")).thenReturn(false);

        TopUpPlan saved = buildTopUpPlan(1L, "Dental Cover", true);
        saved.setDescription("Covers dental procedures");
        saved.setCreatedBy("admin01");

        when(topUpPlanRepository.save(any(TopUpPlan.class))).thenReturn(saved);

        TopUpPlanResponseDTO result = topUpService.createTopUpPlan(dto, "admin01");

        assertNotNull(result);
        assertEquals("Dental Cover", result.getTopUpName());
        assertEquals(1L, result.getTopUpPlanId());
    }


    // ============================================================
    // TEST 2: createTopUpPlan — duplicate name
    // ============================================================
    @Test
    void createTopUpPlan_ShouldThrowException_WhenNameAlreadyExists() {

        CreateTopUpPlanRequestDTO dto = new CreateTopUpPlanRequestDTO();
        dto.setTopUpName("Dental Cover");

        when(topUpPlanRepository.existsByTopUpName("Dental Cover")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                topUpService.createTopUpPlan(dto, "admin01")
        );

        assertTrue(ex.getMessage().contains("already exists"));
        verify(topUpPlanRepository, never()).save(any());
    }


    // ============================================================
    // TEST 3: getAllTopUpPlans — returns active plans only
    // ============================================================
    @Test
    void getAllTopUpPlans_ShouldReturnOnlyActivePlans() {

        TopUpPlan p1 = buildTopUpPlan(1L, "Dental Cover", true);
        TopUpPlan p2 = buildTopUpPlan(2L, "Vision Cover", true);

        when(topUpPlanRepository.findByIsActiveTrue()).thenReturn(List.of(p1, p2));

        List<TopUpPlanResponseDTO> result = topUpService.getAllTopUpPlans();

        assertEquals(2, result.size());
    }


    // ============================================================
    // TEST 4: deactivateTopUpPlan — happy path
    // ============================================================
    @Test
    void deactivateTopUpPlan_ShouldSetIsActiveFalse() {

        TopUpPlan plan = buildTopUpPlan(1L, "Dental Cover", true);
        when(topUpPlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(topUpPlanRepository.save(any())).thenReturn(plan);

        topUpService.deactivateTopUpPlan(1L);

        assertFalse(plan.getIsActive());
        verify(topUpPlanRepository, times(1)).save(plan);
    }


    // ============================================================
    // TEST 5: deactivateTopUpPlan — plan not found
    // ============================================================
    @Test
    void deactivateTopUpPlan_ShouldThrowException_WhenPlanNotFound() {

        when(topUpPlanRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                topUpService.deactivateTopUpPlan(99L)
        );
    }


    // ============================================================
    // TEST 6: buyTopUp — happy path
    // ============================================================
    @Test
    void buyTopUp_ShouldReturnResponse_WhenAllChecksPass() {

        Employee emp = buildEmployee(1L, "emp01");
        TopUpPlan plan = buildTopUpPlan(1L, "Dental Cover", true);

        BuyTopUpRequestDTO dto = new BuyTopUpRequestDTO();
        dto.setTopUpPlanId(1L);
        dto.setExpiryDate(LocalDate.now().plusYears(1));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(topUpPlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(employeeInsuranceRepository.existsByEmployee_UserIdAndStatus(
                1L, InsuranceStatus.ACTIVE)).thenReturn(true);
        when(employeeTopUpRepository.existsByEmployee_UserIdAndTopUpPlan_Id(1L, 1L))
                .thenReturn(false);

        EmployeeTopUp saved = new EmployeeTopUp();
        saved.setId(1L);
        saved.setEmployee(emp);
        saved.setTopUpPlan(plan);
        saved.setPurchasedDate(LocalDate.now());
        saved.setExpiryDate(dto.getExpiryDate());
        saved.setStatus(InsuranceStatus.ACTIVE);

        when(employeeTopUpRepository.save(any(EmployeeTopUp.class))).thenReturn(saved);

        EmployeeTopUpResponseDTO result = topUpService.buyTopUp(dto, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getEmployeeTopUpId());
        assertEquals(InsuranceStatus.ACTIVE, result.getStatus());
    }


    // ============================================================
    // TEST 7: buyTopUp — no active base insurance
    // ============================================================
    @Test
    void buyTopUp_ShouldThrowException_WhenNoActiveBaseInsurance() {

        Employee emp = buildEmployee(1L, "emp01");
        TopUpPlan plan = buildTopUpPlan(1L, "Dental Cover", true);

        BuyTopUpRequestDTO dto = new BuyTopUpRequestDTO();
        dto.setTopUpPlanId(1L);
        dto.setExpiryDate(LocalDate.now().plusYears(1));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(topUpPlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(employeeInsuranceRepository.existsByEmployee_UserIdAndStatus(
                1L, InsuranceStatus.ACTIVE)).thenReturn(false); // no base insurance!

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                topUpService.buyTopUp(dto, 1L)
        );

        assertTrue(ex.getMessage().contains("active base insurance"));
    }


    // ============================================================
    // TEST 8: buyTopUp — deactivated top-up plan
    // ============================================================
    @Test
    void buyTopUp_ShouldThrowException_WhenTopUpPlanIsInactive() {

        Employee emp = buildEmployee(1L, "emp01");
        TopUpPlan plan = buildTopUpPlan(1L, "Dental Cover", false); // inactive!

        BuyTopUpRequestDTO dto = new BuyTopUpRequestDTO();
        dto.setTopUpPlanId(1L);
        dto.setExpiryDate(LocalDate.now().plusYears(1));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(topUpPlanRepository.findById(1L)).thenReturn(Optional.of(plan));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                topUpService.buyTopUp(dto, 1L)
        );

        assertTrue(ex.getMessage().contains("no longer available"));
    }


    // ============================================================
    // TEST 9: buyTopUp — duplicate purchase
    // ============================================================
    @Test
    void buyTopUp_ShouldThrowException_WhenAlreadyPurchased() {

        Employee emp = buildEmployee(1L, "emp01");
        TopUpPlan plan = buildTopUpPlan(1L, "Dental Cover", true);

        BuyTopUpRequestDTO dto = new BuyTopUpRequestDTO();
        dto.setTopUpPlanId(1L);
        dto.setExpiryDate(LocalDate.now().plusYears(1));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(topUpPlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(employeeInsuranceRepository.existsByEmployee_UserIdAndStatus(
                1L, InsuranceStatus.ACTIVE)).thenReturn(true);
        when(employeeTopUpRepository.existsByEmployee_UserIdAndTopUpPlan_Id(1L, 1L))
                .thenReturn(true); // already bought!

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                topUpService.buyTopUp(dto, 1L)
        );

        assertTrue(ex.getMessage().contains("already purchased"));
    }


    // ============================================================
    // TEST 10: getEmployeeTopUps — returns full history
    // ============================================================
    @Test
    void getEmployeeTopUps_ShouldReturnList() {

        Employee emp = buildEmployee(1L, "emp01");
        TopUpPlan plan = buildTopUpPlan(1L, "Dental Cover", true);

        EmployeeTopUp topUp = new EmployeeTopUp();
        topUp.setId(1L);
        topUp.setEmployee(emp);
        topUp.setTopUpPlan(plan);
        topUp.setPurchasedDate(LocalDate.now());
        topUp.setExpiryDate(LocalDate.now().plusYears(1));
        topUp.setStatus(InsuranceStatus.ACTIVE);

        when(employeeTopUpRepository.findByEmployee_UserId(1L))
                .thenReturn(List.of(topUp));

        List<EmployeeTopUpResponseDTO> result = topUpService.getEmployeeTopUps(1L);

        assertEquals(1, result.size());
        assertEquals("Dental Cover", result.get(0).getTopUpName());
    }
}