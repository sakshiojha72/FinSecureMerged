package com.ds.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Used by   : HR / Admin
* Endpoint  : GET /finsecure/hr/employees/stats/monthly
*
* Returns employee joining count per month.
* HR uses this to see hiring trends.
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyStatDTO {

   private int year;          // e.g. 2024
   private int month;         // e.g. 3
   private String monthName;  // e.g. "March"
   private Long count;        // number of employees joined
}