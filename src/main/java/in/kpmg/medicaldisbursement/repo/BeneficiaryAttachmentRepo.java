package in.kpmg.medicaldisbursement.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.models.BeneficiaryAttachments;

@Repository
public interface BeneficiaryAttachmentRepo extends JpaRepository<BeneficiaryAttachments, Integer>{

	@Query(value="select b from BeneficiaryAttachments b where b.requestNumber.requestNumber=:requestNo")
	List<BeneficiaryAttachments> findByRequestNo(String requestNo);

	@Query(value="select b from BeneficiaryAttachments b where b.requestNumber.requestNumber=:requestNo and b.attachmentType=:attachType")
	Optional<BeneficiaryAttachments> findByRequestNAndAttachType(String requestNo, Integer attachType);

}
