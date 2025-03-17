package in.kpmg.medicaldisbursement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.kpmg.medicaldisbursement.models.ActionMst;

public interface ActionRepo extends JpaRepository<ActionMst , Integer>{

}
