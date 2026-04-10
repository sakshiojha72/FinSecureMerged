package com.ds.app.dto.response;
 
import java.time.LocalDateTime;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEducationResponseDTO {
 
    private Integer eduId;
    private Long userId;
    private String degree;
    private String institution;
    private String fieldOfStudy;
    private Integer passingYear;
    private Double percentage;
    private String grade;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
