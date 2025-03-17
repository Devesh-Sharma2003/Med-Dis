package in.kpmg.medicaldisbursement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.models.UserMst;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserMst, Integer> {

	@Query(nativeQuery = false, value="SELECT u FROM UserMst u WHERE u.loginName=:username AND u.isActive='true'")
    UserMst getUserDataByLoginName(String username);

    Optional<UserMst> findByLoginNameIgnoreCaseAndIsActiveIsTrue(String username);
    
    public Optional<UserMst> findByUserId(Integer userId);

    Optional<UserMst> findByLoginName(String username);
    
    @Query(value="select u from UserMst u where u.mobileNo=:mobileNo AND u.isActive='true'", nativeQuery = false)
	UserMst findByMobileNo(Long mobileNo);
    
    @Query(value="select u from UserMst u where u.loginName=:empCode AND u.isActive='true'", nativeQuery = false)
    UserMst findByEmpName(String empCode);
}
