package com.ds.app.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponseDTO<T> {
	
	
	private List<T> data;
	private int page;
	private int size;
	private Long totalElements;
	private int totalPages;
	private boolean first;
	private boolean last;
	
	 public static <T> PagedResponseDTO<T> of(List<T> content, Page<?> page) {
	        PagedResponseDTO<T> response = new PagedResponseDTO<>();
	        response.setData(content);
	        response.setPage(page.getNumber());
	        response.setSize(page.getSize());
	        response.setTotalElements(page.getTotalElements());
	        response.setTotalPages(page.getTotalPages());
	        response.setFirst(page.isFirst());
	        response.setLast(page.isLast());
	        return response;
	    }
	

}

/**
 * Static factory — builds PagedResponseDTO from a mapped list + Spring Page.
 *
 * Usage in service:
 *   Page<Employee> page = iEmployeeRepo.filterEmployees(..., pageable);
 *   List<EmployeeResponseDTO> data = page.getContent().stream()
 *           .map(this::mapToResponseDTO).toList();
 *   return PagedResponseDTO.of(data, page);
 */