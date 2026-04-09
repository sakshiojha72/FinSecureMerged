package com.ds.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ds.app.entity.AppUser;
import com.ds.app.enums.UserRole;

@Repository
public interface iAppUserRepository extends JpaRepository<AppUser,Long>{

	public Optional<AppUser> findByUsername(String username);


	 boolean existsByUsername(String username);
	 
	 /* Code updated by Tarushi*/
	 public List<AppUser> findByRole(UserRole role);
}
