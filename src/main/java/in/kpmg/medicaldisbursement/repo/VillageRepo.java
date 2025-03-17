package in.kpmg.medicaldisbursement.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.dtos.responseDto.DropdownDataDto;
import in.kpmg.medicaldisbursement.models.VillageMaster;

import java.util.List;
import java.util.Optional;

@Repository
public interface VillageRepo extends JpaRepository<VillageMaster,Integer> {
	 @Query("SELECT v.villageId AS typeId, v.villageName AS typeName FROM VillageMaster v WHERE v.mandal.mandalId=:mandalId order by v.villageName asc")
	  List<DropdownDataDto> findVillageByMandalId(@Param("mandalId") Integer mandalId);
}
