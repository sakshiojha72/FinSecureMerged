package com.ds.app.repository;

<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ds.app.entity.AppUser;
<<<<<<< HEAD
import com.ds.app.enums.UserRole;
=======
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e

@Repository
public interface iAppUserRepository extends JpaRepository<AppUser,Long>{

	public Optional<AppUser> findByUsername(String username);


	 boolean existsByUsername(String username);
<<<<<<< HEAD
	 
	 /* Code updated by Tarushi*/
	 public List<AppUser> findByRole(UserRole role);
=======
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
}
