package in.kpmg.medicaldisbursement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.models.BeneficiaryRequestDrugAudit;

@Repository
public interface BeneficiaryDrugAuditRepo extends JpaRepository<BeneficiaryRequestDrugAudit, Integer> {

	@Query(value="select * from medical.md_beneficiary_request_drug_details_audit as b inner join medical.md_user_mst as u on b.crt_by=u.user_id inner join medical.md_user_role_mapping as r on r.user_id=u.user_id inner join medical.md_role_mst ro on ro.role_id=r.role_id where ro.role_id IN (1,3) and b.request_number=:requestNo and b.status IN (1,2)", nativeQuery = true)
	List<BeneficiaryRequestDrugAudit> findRoleBycrtBy(String requestNo);

}
