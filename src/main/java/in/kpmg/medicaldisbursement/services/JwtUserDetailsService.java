package in.kpmg.medicaldisbursement.services;

import in.kpmg.medicaldisbursement.constants.EmployeeConstants;
import in.kpmg.medicaldisbursement.dtos.ApiResponse2;
import in.kpmg.medicaldisbursement.dtos.RoleListDropDownDto;
import in.kpmg.medicaldisbursement.dtos.RoleMenuRightProj;
import in.kpmg.medicaldisbursement.dtos.UserDto;
import in.kpmg.medicaldisbursement.dtos.RequestDto.LogoutDto;
import in.kpmg.medicaldisbursement.models.RoleMst;
import in.kpmg.medicaldisbursement.models.UserLoginTrail;
//import in.kpmg.medicaldisbursement.mapper.UserMapper;
import in.kpmg.medicaldisbursement.models.UserMst;
import in.kpmg.medicaldisbursement.models.UserRoleMappingMst;
import in.kpmg.medicaldisbursement.repo.RoleMenuRightMapRepo;
import in.kpmg.medicaldisbursement.repo.RoleRepo;
import in.kpmg.medicaldisbursement.repo.UserLoginTrailRepo;
import in.kpmg.medicaldisbursement.repo.UserRepo;
import in.kpmg.medicaldisbursement.repo.UserRoleMapRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.sql.Timestamp;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;


//    private UserMapper userMapper;

    @Autowired
    private UserRoleMapRepo userRoleRepo;

    @Autowired
    private RoleMenuRightMapRepo menuRightMapRepo;
    
    @Autowired
    UserLoginTrailRepo userLoginTrailRepo;
    
    @Autowired
    RoleRepo roleRepo;
    
    @Autowired
    private MedicalDisbursementService medicalDisbursementService;

//    @Autowired
//    private LoginRepo loginRepo;



    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserMst userdata = userRepo.getUserDataByLoginName(username);
        if (userdata == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(userdata.getLoginName(), userdata.getPassword(),
                new ArrayList<>());
    }

    public Boolean isTokenLoggedOut(String token){
        Optional<UserLoginTrail> optionalUserLoginTrail = this.userLoginTrailRepo.findByApiTokenAndIsActiveIsFalse(token);

        return optionalUserLoginTrail.isPresent();
    }


//    public Boolean checkPasswordHash(String username, String password) {
//        Integer hashtimes = loginRepo.checkPasswordHash(username, password);
//        System.out.println(hashtimes+"--------------------------------");
//        return hashtimes > 0 ? false : true;
//
//    }


    @Transactional
//    public Integer saveUserLogin(String username, Integer status, String passowrd, String ipAddress) {
////        UserLogin loginDetails = new UserLogin();
//        int k = 0;
//        try {
//            loginDetails.setUserName(username);
//            loginDetails.setStatus(status);
//            loginDetails.setPassword_hash(passowrd);
//            loginDetails.setIpAddress(ipAddress);
//            loginRepo.save(loginDetails);
//
//
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
//
//        return k;
//    }
    

    public Map<String, Object> getLoginUserData(String jwtToken, String ipAddress) {
    	UserMst user = this.medicalDisbursementService.loadUserByUsername(jwtToken);
    	System.out.println(jwtToken);
        Map<String, Object> response = new HashMap<>();
 
        UserMst userData = userRepo.getUserDataByLoginName(user.getLoginName());
//        UserDto useDto = userMapper.modelToDto(userData);
        UserDto userDto= new UserDto();
        userDto.setUserId(userData.getUserId());
        userDto.setEmail(userData.getEmail());
        userDto.setUserName(userData.getLoginName());
        userDto.setMobileNo(userData.getMobileNo());
        userDto.setFullname(userData.getUserName());
        
        List<RoleListDropDownDto> roleMaps = userRoleRepo.getRoleDtls(userData.getUserId());
        
        RoleMst roleMstModel=roleRepo.findByRoleId(roleMaps.get(0).getRoleId());
        String lastLoggedInTime="";
        if(userData !=null && roleMstModel!=null) 
        {
        UserRoleMappingMst usrRoleMapId = userRoleRepo.findByUserAndRole(userData,roleMstModel);
        lastLoggedInTime = userLoginTrailRepo.getlastLoggedInTime(usrRoleMapId.getRoleMapId());
        UserLoginTrail userLoginTrailModel=new UserLoginTrail();
        userLoginTrailModel.setUserRoleId(usrRoleMapId);
        userLoginTrailModel.setIsActive(true);
        userLoginTrailModel.setApiToken(jwtToken.substring(jwtToken.indexOf(' ')+1,jwtToken.length()));
        userLoginTrailModel.setIpAddress(ipAddress);

        userLoginTrailRepo.save(userLoginTrailModel);
        }
        
        
        List<RoleMenuRightProj> parentMenu = menuRightMapRepo.getMainMenuList(userData.getUserId());

        Set<Integer> parentMenuSetIds = new HashSet<>();
        List<RoleMenuRightProj> parentMenuWOduplicate1 = parentMenu.stream()
                .filter(e -> parentMenuSetIds.add(e.getMenuId()))
                .collect(Collectors.toList());
        List<RoleMenuRightProj> subMenu = menuRightMapRepo.getSubMenuList(userData.getUserId());
        List<RoleMenuRightProj> subMenuWoDuplicate = subMenu.stream().collect(collectingAndThen(
                toCollection(() -> new TreeSet<>(comparingInt(RoleMenuRightProj::getMenuId))), ArrayList::new));
        
        response.put("user", userDto);

        response.put("parentMenuList", parentMenuWOduplicate1);
        response.put("subMenuList", subMenuWoDuplicate);
        response.put("lastLoggedInTime", lastLoggedInTime);
        response.put("roleMap", roleMaps);
        return response;
    }

    public Optional<UserMst> getUserDetailsByUserName(String username) {
        return  userRepo.findByLoginNameIgnoreCaseAndIsActiveIsTrue(username);
    }

    public Map<String, Object> isLoggedInFromOtherIp(UserMst user, String ipAddress){

        Map<String, Object> response = new HashMap<>();

        List<RoleListDropDownDto> roleMaps = userRoleRepo.getRoleDtls(user.getUserId());

        RoleMst roleMstModel=roleRepo.findByRoleId(roleMaps.get(0).getRoleId());
        UserRoleMappingMst usrRoleMapId = userRoleRepo.findByUserAndRole(user,roleMstModel);

        List<UserLoginTrail> existingLoginTrailList = userLoginTrailRepo.findActiveSessionByUserRoleId(usrRoleMapId.getRoleMapId());

        if (existingLoginTrailList != null && !existingLoginTrailList.isEmpty()) {

            for (UserLoginTrail userLoginTrail : existingLoginTrailList)
            {
                String storedIpAddress = userLoginTrail.getIpAddress();

                if (storedIpAddress == null || !storedIpAddress.equals(ipAddress)) {
                    response.put("isLoggedInFromOtherIp", true);
                    response.put("otherIp", storedIpAddress != null ? storedIpAddress : "Unknown IP (null)");
                    return response;
                }
            }
        }

        response.put("isLoggedInFromOtherIp", false);
        return response;
    }

    @Transactional  // Add @Transactional here if you're calling the repository from this service
    public String logoutFromOtherSessions(String username) {
        UserMst userMst = this.userRepo.findByLoginName(username).orElseThrow(() -> new EntityNotFoundException("username not found"));

        userLoginTrailRepo.invalidateOtherSessions(userMst.getUserId());

        return "Logged out from other session";
    }

    public ResponseEntity<?> validateTokenUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {

                return new ResponseEntity<>(
                        new ApiResponse2<>(true, "Validation Successful", null, HttpStatus.OK.value()), HttpStatus.OK);

            } else
                throw new SecurityException("Unauthorized Access!");
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse2<>(false, e.getMessage(), null, HttpStatus.UNAUTHORIZED.value()),
                    HttpStatus.UNAUTHORIZED);
        }
    }


    public Map<String, Object> getDynamicMenu(Integer userId) {
        Map<String, Object> response = new HashMap<>();

        UserMst user;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Optional<UserMst> optionalAuthenticationUser = this.userRepo.findByLoginName( authentication.getName());

            if (!optionalAuthenticationUser.isPresent())
                throw new SecurityException("Unauthorized Access!");

            user = optionalAuthenticationUser.get();

        } else {
            throw new SecurityException("Unauthorized Access!");
        }

        List<RoleMenuRightProj> parentMenu = menuRightMapRepo.getMainMenuList(user.getUserId());

        List<RoleMenuRightProj> parentMenuWOduplicate = parentMenu.stream().collect(
                collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(RoleMenuRightProj::getMenuId))),
                        ArrayList::new));
        List<RoleMenuRightProj> subMenu = menuRightMapRepo.getSubMenuList(user.getUserId());
        List<RoleMenuRightProj> subMenuWoDuplicate = subMenu.stream().collect(collectingAndThen(
                toCollection(() -> new TreeSet<>(comparingInt(RoleMenuRightProj::getMenuId))), ArrayList::new));
        response.put("parentMenuList", parentMenuWOduplicate);
        response.put("subMenuList", subMenuWoDuplicate);

        return response;
    }
    
    public ApiResponse2<Object> logout(Integer userId) {
    	Optional<UserMst> userData = userRepo.findByUserId(userId);
        Integer roleId = userRoleRepo.findRoleIdByUserId(userData.get().getUserId());
     
        RoleMst roleMaps = roleRepo.findByRoleId(roleId); // Assumes this method returns RoleMst or null
        if (roleMaps == null) {
            
            return new ApiResponse2<>(false, "Role not found", null, HttpStatus.BAD_REQUEST.value());
        }
//    	RoleMst roleMaps=roleRepo.findByRoleId(roleId);
//    	List<RoleListDropDownDto> roleMaps = userRoleRepo.getRoleDtls(userData.get().getUserId());
    	UserRoleMappingMst usrRoleMapId = userRoleRepo.findByUserAndRole(userData.get(),roleMaps);
        List<UserLoginTrail> userLoginTrailModel=userLoginTrailRepo.findByUserRoleIdOrderByUserLoginIdDesc(usrRoleMapId);
        if(userLoginTrailModel.get(0)!=null && userLoginTrailModel.get(0).getIsActive()==true)
        {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        userLoginTrailModel.get(0).setIsActive(false);
        userLoginTrailModel.get(0).setLogoutTime(current);
        userLoginTrailRepo.save(userLoginTrailModel.get(0));
        return new ApiResponse2<>(true, "Logged Out Successfully !!", null, HttpStatus.OK.value());
        }
        else
        {
        	return new ApiResponse2<>(true, "Already Logged Out", null, HttpStatus.OK.value());
        }
    }
}
