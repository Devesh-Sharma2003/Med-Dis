package in.kpmg.medicaldisbursement.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.models.RoleMst;

@Repository
public interface RoleRepo extends JpaRepository<RoleMst, Integer> {

	RoleMst findByRoleId(Integer roleId);
	
}
