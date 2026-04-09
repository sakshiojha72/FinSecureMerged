package com.ds.app.service.impl;

import com.ds.app.dto.response.LeaveBalanceResponse;
import com.ds.app.entity.Employee;
import com.ds.app.entity.LeaveBalance;
import com.ds.app.enums.LeaveType;
import com.ds.app.exception.InsufficientLeaveBalanceException;
import com.ds.app.exception.InvalidLeaveStateException;
import com.ds.app.exception.ResourceNotFoundException2;
import com.ds.app.mapper.LeaveBalanceMapper;
import com.ds.app.repository.iEmployeeRepository;
import com.ds.app.repository.ILeaveBalanceRepository;
import com.ds.app.service.ILeaveBalanceService;
import com.ds.app.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveBalanceServiceImpl implements ILeaveBalanceService {

    private final ILeaveBalanceRepository leaveBalanceRepository;
    private final iEmployeeRepository employeeRepository;
    private final LeaveBalanceMapper leaveBalanceMapper;
    private final SecurityUtils securityUtils;
    
    private static final int FULL_SICK_LEAVES   = 12;
    private static final int FULL_CASUAL_LEAVES = 12;
    
    @Override
    @Transactional
    public void createLeaveBalanceForNewEmployee(Employee employee) {
        int currentYear       = Year.now().getValue();
        int registrationMonth = LocalDate.now().getMonthValue();

        if (leaveBalanceRepository.existsByEmployeeUserIdAndYear(
                employee.getUserId(), currentYear)) {
            return;
        }

        int sickLeaves = calculateRemainingLeaves(registrationMonth, FULL_SICK_LEAVES);
        int casualLeaves = calculateRemainingLeaves(registrationMonth, FULL_CASUAL_LEAVES);

        leaveBalanceRepository.save(
            LeaveBalance.builder()
                .employee(employee)
                .year(currentYear)
                .sickLeaveBalance(sickLeaves)
                .casualLeaveBalance(casualLeaves)
                .earnedLeaveBalance(BigDecimal.ZERO)  
                .reservedSickLeaves(0)
                .reservedCasualLeaves(0)
                .reservedEarnedLeaves(0)
                .sickLeavesConsumed(0)
                .casualLeavesConsumed(0)
                .earnedLeavesConsumed(0)
                .carriedForwardEarnedDays(BigDecimal.ZERO)
                .build()
        );
    }

    @Override
    public void reserveLeaves(Long userId, int year, LeaveType type, int days) {
        if (type == LeaveType.UNPAID) return;

        LeaveBalance lb = findByEmployeeAndYear(userId, year);

        switch (type) {
            case SICK -> {
                int available = lb.getSickLeaveBalance() - lb.getReservedSickLeaves();
                if (available < days)
                    throw new InsufficientLeaveBalanceException("Insufficient sick leave balance");
                lb.setReservedSickLeaves(lb.getReservedSickLeaves() + days);
            }
            case CASUAL -> {
                int available = lb.getCasualLeaveBalance() - lb.getReservedCasualLeaves();
                if (available < days)
                    throw new InsufficientLeaveBalanceException("Insufficient casual leave balance");
                lb.setReservedCasualLeaves(lb.getReservedCasualLeaves() + days);
            }
            case EARNED -> {
                int available = lb.getEarnedLeaveBalance().intValue() - lb.getReservedEarnedLeaves();
                if (available < days)
                    throw new InsufficientLeaveBalanceException("Insufficient earned leave balance");
                lb.setReservedEarnedLeaves(lb.getReservedEarnedLeaves() + days);
            }
            default -> { }
        }
    }

    @Override
    public void releaseReservedLeaves(Long userId, int year, LeaveType type, int days) {
        if (type == LeaveType.UNPAID) return;

        LeaveBalance lb = findByEmployeeAndYear(userId, year);

        switch (type) {
            case SICK   -> lb.setReservedSickLeaves(Math.max(0, lb.getReservedSickLeaves() - days));
            case CASUAL -> lb.setReservedCasualLeaves(Math.max(0, lb.getReservedCasualLeaves() - days));
            case EARNED -> lb.setReservedEarnedLeaves(Math.max(0, lb.getReservedEarnedLeaves() - days));
            default -> { }
        }
    }

    @Override
    public void applyApproval(Long userId, int year, LeaveType type, int days) {
        if (type == LeaveType.UNPAID) return;

        LeaveBalance lb = findByEmployeeAndYear(userId, year);

        switch (type) {
            case SICK -> {
                if (lb.getReservedSickLeaves() < days)
                    throw new InvalidLeaveStateException(
                            "Invalid leave state: reserved sick leaves less than requested days");
                lb.setReservedSickLeaves(lb.getReservedSickLeaves() - days);
                lb.setSickLeaveBalance(lb.getSickLeaveBalance() - days);
                lb.setSickLeavesConsumed(lb.getSickLeavesConsumed() + days);
            }
            case CASUAL -> {
                if (lb.getReservedCasualLeaves() < days)
                    throw new InvalidLeaveStateException(
                            "Invalid leave state: reserved casual leaves less than requested days");
                lb.setReservedCasualLeaves(lb.getReservedCasualLeaves() - days);
                lb.setCasualLeaveBalance(lb.getCasualLeaveBalance() - days);
                lb.setCasualLeavesConsumed(lb.getCasualLeavesConsumed() + days);
            }
            case EARNED -> {
                if (lb.getReservedEarnedLeaves() < days)
                    throw new InvalidLeaveStateException(
                            "Invalid leave state: reserved earned leaves less than requested days");
                lb.setReservedEarnedLeaves(lb.getReservedEarnedLeaves() - days);
                lb.setEarnedLeaveBalance(lb.getEarnedLeaveBalance().subtract(BigDecimal.valueOf(days)));
                lb.setEarnedLeavesConsumed(lb.getEarnedLeavesConsumed() + days);
            }
            default -> { }
        }
    }

    @Override
    public void applyCancellationApproval(Long userId, int year, LeaveType type, int days) {
        if (type == LeaveType.UNPAID) return;

        LeaveBalance lb = findByEmployeeAndYear(userId, year);

        switch (type) {
            case SICK -> {
                lb.setSickLeaveBalance(lb.getSickLeaveBalance() + days);
                lb.setSickLeavesConsumed(lb.getSickLeavesConsumed() - days);
            }
            case CASUAL -> {
                lb.setCasualLeaveBalance(lb.getCasualLeaveBalance() + days);
                lb.setCasualLeavesConsumed(lb.getCasualLeavesConsumed() - days);
            }
            case EARNED -> {
                lb.setEarnedLeaveBalance(lb.getEarnedLeaveBalance().add(BigDecimal.valueOf(days)));
                lb.setEarnedLeavesConsumed(lb.getEarnedLeavesConsumed() - days);
            }
            default -> { }
        }
    }

    @Override
    public LeaveBalanceResponse getMyLeaveBalance(Integer year) {
        Employee me = securityUtils.getLoggedInEmployee();
        int targetYear = (year != null) ? year : Year.now().getValue();

        LeaveBalance lb = findByEmployeeAndYear(me.getUserId(), targetYear);
        return leaveBalanceMapper.mapToResponse(lb);
    }

    @Override
    public LeaveBalanceResponse getEmployeeLeaveBalance(Long employeeId, Integer year) {
        int targetYear = (year != null) ? year : Year.now().getValue();

        // verify employee exists
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException2(
                        "Employee not found with id: " + employeeId));

        LeaveBalance lb = findByEmployeeAndYear(employeeId, targetYear);
        return leaveBalanceMapper.mapToResponse(lb);
    }

    @Override
    public List<LeaveBalanceResponse> getTeamLeaveBalances(Integer year) {
        Employee manager = securityUtils.getLoggedInEmployee();
        int targetYear = (year != null) ? year : Year.now().getValue();

        return leaveBalanceRepository
                .findByEmployeeManagerUserIdAndYear(manager.getUserId(), targetYear)
                .stream()
                .map(leaveBalanceMapper::mapToResponse)
                .toList();
    }

    private LeaveBalance findByEmployeeAndYear(Long userId, int year) {
        return leaveBalanceRepository.findByEmployeeUserIdAndYear(userId, year)
                .orElseThrow(() -> new ResourceNotFoundException2(
                        "Leave balance not found for employee/year"));
    }
    
    private int calculateRemainingLeaves(int registrationMonth, int fullLeaves) {
    	int remainingMonths = 13 - registrationMonth;
        return (int) Math.ceil(remainingMonths / 12.0 * fullLeaves);
    }
}