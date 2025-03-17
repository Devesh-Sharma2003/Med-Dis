package in.kpmg.medicaldisbursement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.models.StepRoleMapping;

@Repository
public interface StepRoleMappingRepo extends JpaRepository<StepRoleMapping, Long> {
	
	StepRoleMapping findByStepRoleId(Long id);
}
