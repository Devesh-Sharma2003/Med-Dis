package in.kpmg.medicaldisbursement.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.dtos.responseDto.DropdownDataDto;
import in.kpmg.medicaldisbursement.models.MandalMaster;

import java.util.List;
import java.util.Optional;

@Repository
public interface MandalRepo extends JpaRepository<MandalMaster,Integer> {
	 @Query("SELECT m.mandalId AS typeId, m.mandalName AS typeName FROM MandalMaster m WHERE m.district.distId =:distId AND m.isActive = true Order by m.mandalName ASC")
	  List<DropdownDataDto> findMandalByDistId(@Param("distId") Integer distId);
}
