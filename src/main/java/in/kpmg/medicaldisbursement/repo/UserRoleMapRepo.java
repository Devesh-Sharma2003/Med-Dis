package in.kpmg.medicaldisbursement.repo;

import in.kpmg.medicaldisbursement.dtos.RoleListDropDownDto;
import in.kpmg.medicaldisbursement.models.RoleMst;
import in.kpmg.medicaldisbursement.models.UserMst;
import in.kpmg.medicaldisbursement.models.UserRoleMappingMst;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleMapRepo extends JpaRepository<UserRoleMappingMst, Integer> {

    @Query("SELECT r.role.roleId as roleId,r.role.name as roleName FROM UserRoleMappingMst r WHERE r.user.userId=:uid and r.isActive = true order by roleMapId desc")
    List<RoleListDropDownDto> getRoleDtls(Integer uid);
    
    @Query("SELECT u.role.roleId FROM UserRoleMappingMst u WHERE u.user.userId = :userId")
    Integer findRoleIdByUserId(Integer userId);
    
    UserRoleMappingMst findByUserAndRole(UserMst userId,RoleMst roleId);

}
