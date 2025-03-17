package in.kpmg.medicaldisbursement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.dtos.RequestDto.DrugSaveRqtDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.DropdownDataDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.DrugRspDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.DrugSaveRspDto;
import in.kpmg.medicaldisbursement.models.BeneficiaryRequestDrug;

@Repository
public interface BeneficiaryRqtDrugsRepo extends JpaRepository<BeneficiaryRequestDrug, Integer>{

	@Query(value="select b from BeneficiaryRequestDrug b where b.requestNumber.requestNumber=:requestNo")
	List<BeneficiaryRequestDrug> findByRequestNo(String requestNo);

	@Query(value="select medical.md_dispatch_number_generation()", nativeQuery = true)
	String generateDisptachId();

}
