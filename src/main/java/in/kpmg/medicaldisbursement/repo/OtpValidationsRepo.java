package in.kpmg.medicaldisbursement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.models.OtpValidations;
import in.kpmg.medicaldisbursement.models.UserMst;

@Repository
public interface OtpValidationsRepo extends JpaRepository<OtpValidations,Integer>{

	OtpValidations findFirstByUserOrderByOtpGeneratedAtDesc(UserMst user);
}
