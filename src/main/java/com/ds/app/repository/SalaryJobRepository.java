package com.ds.app.repository;

import com.ds.app.entity.SalaryJob;
import com.ds.app.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryJobRepository extends JpaRepository<SalaryJob, Long> {

    // ✅ Seeder / idempotent check
    boolean existsByTargetMonth(YearMonth targetMonth);

    // ✅ Optional: useful if you want to read that job again
    Optional<SalaryJob> findByTargetMonth(YearMonth targetMonth);

    // ✅ Scheduler uses this to find jobs ready to run
    List<SalaryJob> findByJobStatusAndScheduledDateTimeBefore(JobStatus status, LocalDateTime dateTime);
}