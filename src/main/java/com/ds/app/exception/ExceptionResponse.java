package com.ds.app.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
	
	private String errorMsg;
	private LocalDate date;
	private LocalTime time;
	private String url;
	private String className;
	private String solution;

}
