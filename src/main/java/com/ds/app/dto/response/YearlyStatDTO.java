package com.ds.app.dto.response;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * Used by   : HR / Admin
 * Endpoint  : GET /finsecure/hr/employees/stats/yearly
 *
 * Returns employee joining count per year.
 * HR uses this to see yearly hiring trends.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearlyStatDTO {
 
    private int year;    // e.g. 2024
    private Long count;  // number of employees joined
}