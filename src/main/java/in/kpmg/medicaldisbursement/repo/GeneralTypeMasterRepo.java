package in.kpmg.medicaldisbursement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.dtos.responseDto.DropdownDataDto;
import in.kpmg.medicaldisbursement.models.GeneralTypeMst;

@Repository
public interface GeneralTypeMasterRepo extends JpaRepository<GeneralTypeMst,Integer> {

	 @Query("SELECT g.typeId AS typeId, g.typeName As typeName FROM GeneralTypeMst g WHERE g.typeDesc = :typeDesc AND g.isActive = true")
	 List<DropdownDataDto> findTypeIdAndTypeNameByTypeDescAndIsActive(@Param("typeDesc") String typeDesc);
	 
	 @Query("SELECT g.typeId AS typeId, g.typeName As typeName FROM GeneralTypeMst g WHERE g.typeDesc =:typeDesc AND g.isActive = true")
	 List<DropdownDataDto> findDrugUnit(@Param("typeDesc") String typeDesc);
	 
	 @Query("SELECT g.typeId AS typeId, g.typeName As typeName FROM GeneralTypeMst g WHERE g.typeDesc =:typeDesc AND g.isActive = true")
	 List<DropdownDataDto> findByRelationId(@Param("typeDesc") String typeDesc);
	 
	@Query(nativeQuery = false, value="select g from GeneralTypeMst g where g.typeId=:mobileLoginTypeId AND g.isActive = true") 
	GeneralTypeMst findByTypeId(int mobileLoginTypeId);
	 
}
