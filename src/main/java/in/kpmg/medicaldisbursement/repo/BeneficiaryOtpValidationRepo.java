package in.kpmg.medicaldisbursement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.constants.EmployeeConstants;
import in.kpmg.medicaldisbursement.models.BeneficiaryOtpValidation;
import in.kpmg.medicaldisbursement.models.GeneralTypeMst;
import in.kpmg.medicaldisbursement.models.UserMst;

@Repository
public interface BeneficiaryOtpValidationRepo extends JpaRepository<BeneficiaryOtpValidation, Integer>{
	
	BeneficiaryOtpValidation findFirstByUserAndValidationTypeOrderByOtpGeneratedAtDesc(UserMst user,Integer id);
	
	BeneficiaryOtpValidation findFirstByUserAndValidationTypeOrderByOtpGeneratedAtDesc(UserMst user,GeneralTypeMst validationType);
	
	@Query(value = "select o from BeneficiaryOtpValidation o where o.user=:userMst AND o.validationType.typeId=:validationId order by o.otpGeneratedAt DESC", nativeQuery = false)
	BeneficiaryOtpValidation findFirstByUserAndValidationTypeOrderByOtpGeneratedAtDes(UserMst userMst,
			int validationId);

}
