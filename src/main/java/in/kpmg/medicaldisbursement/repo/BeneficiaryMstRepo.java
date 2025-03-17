package in.kpmg.medicaldisbursement.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import in.kpmg.medicaldisbursement.models.BeneficiaryMst;

public interface BeneficiaryMstRepo extends JpaRepository<BeneficiaryMst, Integer> {

	
	Optional<BeneficiaryMst> findById(Integer beneficiaryId);
	
	@Query("SELECT b FROM BeneficiaryMst b WHERE b.beneficiaryId =:beneficiaryId AND b.isActive=true OR b.beneficiaryPhoneNo=:empMobileNo")
    BeneficiaryMst findParentBeneficiaryByBeneficiaryId(@Param("beneficiaryId") String beneficiaryId, Long empMobileNo);

    @Query("SELECT b FROM BeneficiaryMst b WHERE b.parentBeneficiaryId =:parentId AND b.isActive=true")
    List<BeneficiaryMst> findChildBeneficiariesByParentId(@Param("parentId") Integer parentId);

	@Query(value="select b from BeneficiaryMst b where b.beneficiaryId=:empId")
	public Optional<BeneficiaryMst> findByBeneficiaryId(String empId);

}
