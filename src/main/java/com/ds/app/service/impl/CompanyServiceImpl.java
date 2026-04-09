
package com.ds.app.service.impl;

import com.ds.app.dto.request.CompanyRequestDTO;
import com.ds.app.dto.response.CompanyResponseDTO;
import com.ds.app.entity.Company;
import com.ds.app.exception.HrDuplicateResourceException;
import com.ds.app.exception.HrException;
import com.ds.app.exception.HrResourceNotFoundException;
import com.ds.app.repository.iCompanyRepository;
import com.ds.app.service.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired private iCompanyRepository companyRepo;

    //  entity → response DTO---------
<<<<<<< HEAD
=======
    
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
    public CompanyResponseDTO toResponse(Company c) {
        CompanyResponseDTO res = new CompanyResponseDTO();
        res.setId(c.getId());
        res.setName(c.getName());
        res.setCode(c.getCode());
        res.setRestrictsInvestment(c.getRestrictsInvestment());
        res.setStatus(c.getStatus());
        return res;
    }

    // -- create-------------------
<<<<<<< HEAD
=======
    @Override
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
    public CompanyResponseDTO create(CompanyRequestDTO req) {
        if (companyRepo.existsByCode(req.getCode()))
            throw new HrDuplicateResourceException("Company","Code",req.getCode());

        Company company = new Company();
        company.setName(req.getName());
        company.setCode(req.getCode());
        company.setRestrictsInvestment(req.getRestrictsInvestment() != null
                ? req.getRestrictsInvestment() : false);
        company.setStatus(req.getStatus() != null ? req.getStatus() : "ACTIVE");

        return toResponse(companyRepo.save(company));
    }

    //update-----------------
<<<<<<< HEAD
=======
    @Override
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
    public CompanyResponseDTO update(Long id, CompanyRequestDTO req) {
        Company company = findOrThrow(id);
        if (req.getName()               != null) company.setName(req.getName());
        if (req.getCode()               != null) company.setCode(req.getCode());
        if (req.getRestrictsInvestment() != null) company.setRestrictsInvestment(req.getRestrictsInvestment());
        if (req.getStatus()             != null) company.setStatus(req.getStatus());
        return toResponse(companyRepo.save(company));
    }

    // ---- get all ---------------------
<<<<<<< HEAD
=======
    @Override
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
    public List<CompanyResponseDTO> getAll() {
        return companyRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ---- get by id -----------------------
<<<<<<< HEAD
=======
    @Override
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
    public CompanyResponseDTO getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    
    //update status-------------------------------
<<<<<<< HEAD
=======
    @Override
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
    public CompanyResponseDTO updateStatus(Long id, String status) {
        if (!status.equals("ACTIVE") && !status.equals("INACTIVE"))
            throw new HrException("Status must be ACTIVE or INACTIVE");
        Company company = findOrThrow(id);
        company.setStatus(status);
        return toResponse(companyRepo.save(company));
    }

    // ---- internal helper — returns raw entity for other services ------------─
<<<<<<< HEAD
=======
<<<<<<< HEAD
    
=======
>>>>>>> 460b80319683eda7f335758b4df8c84147c8d2fe
    @Override
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
    public Company findOrThrow(Long id) {
        return companyRepo.findById(id)
                .orElseThrow(() -> new HrResourceNotFoundException("Company " , id));
    }
}

