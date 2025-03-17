package in.kpmg.medicaldisbursement.repo;

import in.kpmg.medicaldisbursement.dtos.responseDto.DropdownDataDto;
import in.kpmg.medicaldisbursement.models.DistrictMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DistrictRepo extends JpaRepository<DistrictMaster,Integer> {

	 @Query("SELECT d.distId AS typeId, d.districtName AS typeName FROM DistrictMaster d WHERE d.state.stateId =:stateId AND d.isDistrictActive = true ORDER by d.districtName ASC")
	  List<DropdownDataDto> findDistIdAndDistrictNameByStateId(@Param("stateId") Integer stateId);
}
