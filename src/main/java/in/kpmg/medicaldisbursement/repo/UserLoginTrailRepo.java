package in.kpmg.medicaldisbursement.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.models.UserLoginTrail;
import in.kpmg.medicaldisbursement.models.UserRoleMappingMst;

import javax.transaction.Transactional;

@Repository
public interface UserLoginTrailRepo extends JpaRepository<UserLoginTrail,Integer>{

	List<UserLoginTrail> findByUserRoleIdOrderByUserLoginIdDesc(UserRoleMappingMst id);
	
	@Query(nativeQuery = true, value = "SELECT to_char(login_time,'DD-MM-YYYY HH12:MI:SS AM') login_time from medical.md_user_login_trail WHERE user_role_id=:usrRoleId Order by user_login_id desc limit 1")
	String getlastLoggedInTime(Integer usrRoleId);

	Optional<UserLoginTrail> findByApiTokenAndIsActiveIsFalse(String token);

	@Query("SELECT u FROM UserLoginTrail u WHERE u.userRoleId.roleMapId = :roleMapId AND u.isActive = true")
	List<UserLoginTrail> findActiveSessionByUserRoleId(@Param("roleMapId") Integer roleMapId);

	@Modifying
	@Query("UPDATE UserLoginTrail u " +
			"SET u.isActive = false, u.logoutTime = CURRENT_TIMESTAMP " +
			"WHERE u.userRoleId IN (SELECT r FROM UserRoleMappingMst r WHERE r.user.userId = :userId) " +
			"AND u.isActive = true")
	@Transactional
	void invalidateOtherSessions(@Param("userId") Integer userId);


}
