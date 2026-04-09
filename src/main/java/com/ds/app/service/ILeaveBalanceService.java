package com.ds.app.service;

import com.ds.app.dto.response.LeaveBalanceResponse;
import com.ds.app.entity.Employee;
import com.ds.app.enums.LeaveType;

import java.util.List;

public interface ILeaveBalanceService {

    void reserveLeaves(Long userId, int year, LeaveType type, int days);
    void releaseReservedLeaves(Long userId, int year, LeaveType type, int days);
    void applyApproval(Long userId, int year, LeaveType type, int days);
    void applyCancellationApproval(Long userId, int year, LeaveType type, int days);

    LeaveBalanceResponse getMyLeaveBalance(Integer year);

    LeaveBalanceResponse getEmployeeLeaveBalance(Long employeeId, Integer year);

    List<LeaveBalanceResponse> getTeamLeaveBalances(Integer year);
	void createLeaveBalanceForNewEmployee(Employee employee);
}