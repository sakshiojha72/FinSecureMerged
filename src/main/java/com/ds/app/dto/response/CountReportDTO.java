package com.ds.app.dto.response;

import com.ds.app.enums.EmployeeExperience;
import com.ds.app.enums.EmploymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountReportDTO {
	
	
	 // ── Overall counts ─────────────────────────────────────────────
    private Long totalActive;        // isDeleted = false
    private Long totalDeleted;       // isDeleted = true

    // ── Experience counts ──────────────────────────────────────────
    private Long totalFreshers;      // FRESHER
    private Long totalExperienced;   // EXPERIENCED

    // ── Certification counts ───────────────────────────────────────
    private Long totalCertified;     // CERTIFIED
    private Long totalNonCertified;  // NON_CERTIFIED

}
