package in.kpmg.medicaldisbursement.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.dtos.responseDto.DropdownDataDto;
import in.kpmg.medicaldisbursement.models.StateMaster;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepo extends JpaRepository<StateMaster,Integer> {

	@Query("SELECT s.stateId AS typeId, s.stateName AS typeName FROM StateMaster s WHERE s.isStateActive = true ORDER by s.stateName ASC")
    List<DropdownDataDto> findStateIdAndStateNameByIsStateActive();
}
