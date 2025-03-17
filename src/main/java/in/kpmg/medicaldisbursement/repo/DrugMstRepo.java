package in.kpmg.medicaldisbursement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.dtos.responseDto.DropdownDataDto;
import in.kpmg.medicaldisbursement.models.DrugMst;

@Repository
public interface DrugMstRepo extends JpaRepository<DrugMst, Integer> {

	@Query(value="select d.drugId as typeId, d.drugName as typeName from DrugMst d", nativeQuery = false)
	List<DropdownDataDto> findDrugIdAndDrugName();
	
	
}
