package in.kpmg.medicaldisbursement.controllers;


import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.netflix.discovery.converters.Auto;

import in.kpmg.medicaldisbursement.config.AesUtil;
import in.kpmg.medicaldisbursement.config.JwtTokenUtil;
import in.kpmg.medicaldisbursement.constants.EmployeeConstants;
import in.kpmg.medicaldisbursement.dtos.ApiResponse2;
import in.kpmg.medicaldisbursement.dtos.ForgetPassRequestDto;
import in.kpmg.medicaldisbursement.dtos.JwtRequest;
import in.kpmg.medicaldisbursement.dtos.RequestDto.MobileOtp;
import in.kpmg.medicaldisbursement.dtos.RequestDto.MobileRqtDto;
import in.kpmg.medicaldisbursement.exceptions.BadRequestException;
import in.kpmg.medicaldisbursement.models.BeneficiaryOtpValidation;
import in.kpmg.medicaldisbursement.models.GeneralTypeMst;
import in.kpmg.medicaldisbursement.models.OtpValidations;
import in.kpmg.medicaldisbursement.models.UserMst;
import in.kpmg.medicaldisbursement.repo.BeneficiaryOtpValidationRepo;
import in.kpmg.medicaldisbursement.repo.GeneralTypeMasterRepo;
import in.kpmg.medicaldisbursement.repo.OtpValidationsRepo;
import in.kpmg.medicaldisbursement.repo.UserLoginTrailRepo;
import in.kpmg.medicaldisbursement.repo.UserRepo;
import in.kpmg.medicaldisbursement.repo.UserRoleMapRepo;
import in.kpmg.medicaldisbursement.services.JwtUserDetailsService;
import in.kpmg.medicaldisbursement.services.MedicalDisbursementService;
import in.kpmg.medicaldisbursement.utils.OtpUtil;
import in.kpmg.medicaldisbursement.utils.Util;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@CrossOrigin(
//     origins = {"http://141.148.194.18:8080", "http://localhost:3000"},
//        origins = {"http://apmsidc.staging.ap.gov.in"},
		origins = {"http://103.129.72.100"},
        allowedHeaders = {"Content-Type", "Authorization", "X-Requested-With", "Accept"},

//	    allowedMethods = { "GET", "POST", "PUT", "DELETE" },
//	    exposedHeaders = { "Custom-Header1", "Custom-Header2" }
        allowCredentials = "true"
)
public class AuthenticationController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private MedicalDisbursementService medicalDisbursementService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserRoleMapRepo userRoleMapRepo;

    @Autowired
    private Util util;
    
    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private GeneralTypeMasterRepo generalMstRepo;
    
    @Autowired
    private BeneficiaryOtpValidationRepo otpValidationRepo;
    
    private final DefaultKaptcha defaultKaptcha;

    public AuthenticationController(DefaultKaptcha defaultKaptcha) {
        this.defaultKaptcha = defaultKaptcha;
    }

    //    @GetMapping("/testing")
//    public ResponseEntity<?> testFunction(){
//        return ResponseEntity.ok(userRepo.findAll());
//    }
    private final ConcurrentHashMap<String, String> captchaMap = new ConcurrentHashMap<>();


    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest,
                                                       HttpServletRequest req) throws Exception {
        try {
//            String captchaSession = (String) req.getSession().getAttribute("captcha");
//            HttpSession session = req.getSession();
//            String captchaSession = (String) session.getAttribute("captcha");
//
//            if (!authenticationRequest.getCaptcha().equals(captchaSession))
//                throw new BadRequestException("Invalid Captcha Entered");

            String storedCaptcha = captchaMap.get(authenticationRequest.getCaptchaId());

            if (!authenticationRequest.getCaptcha().equals(storedCaptcha)) {
                throw new BadRequestException("Invalid Captcha Entered");
            }

//            if (Boolean.FALSE.equals(userDetailsService.checkPasswordHash(authenticationRequest.getUsername(),
//                    authenticationRequest.getPassword()))) {
//                throw new Exception("Invalid Username or Password");
//            }
//        	String pass = "trust";
//        	String encodePass = Base64.getEncoder().encodeToString(pass.getBytes());
//        	System.out.println(encodePass);

            UserMst user = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            Map<String, Object> activeIpSessionData = this.userDetailsService.isLoggedInFromOtherIp(user, this.util.getClientIp(req));

            if ((boolean) activeIpSessionData.get("isLoggedInFromOtherIp")) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "You are already logged in from another session. Do you want to log out from that session and log in here?");
                response.put("isLoggedInFromOtherIp", true);
                response.put("status", true);
                return ResponseEntity.ok(response);
            }

            final String token = jwtTokenUtil.generateToken(authenticationRequest.getUsername());
            System.out.println("token generated!!");
            String MyUrl = "http://localhost:3000/medical-disbursement/raisedRequests/?userid=" + user.getLoginName();
            MyUrl += "&token=" + token;
            // String MyUrl1 = "http://10.96.38.28/medical-disbursement/?userid=" + user.getLoginName();
//            String MyUrl1 = "http://apmsidc.staging.ap.gov.in/medical-disbursement/?userid=" + user.getLoginName();
            // UserMst users = this.medicalDisbursementService.loadUserByUsername(token);
            //System.out.println(user.getUserId());

            Optional<UserMst> optionalAuthenticationUser = this.userRepo.findByLoginName(authenticationRequest.getUsername());
            Integer role = userRoleMapRepo.findRoleIdByUserId(optionalAuthenticationUser.get().getUserId());
            System.out.println(role);
            String MyUrl1 = null;
            if (role == EmployeeConstants.Medco_Role_Id) {
                MyUrl1 = "http://103.129.72.100/raisedRequests/?userid=" + user.getLoginName();
            } else if (role == EmployeeConstants.Medical_officer_Role_Id) {
                MyUrl1 = "http://103.129.72.100/medicalOfficerRequestsSummary/?userid=" + user.getLoginName();
            } else if (role == EmployeeConstants.Pharma_Role_Id) {
                MyUrl1 = "http://103.129.72.100/pharmacistRequestsSummary/?userid=" + user.getLoginName();
            } else if (role == EmployeeConstants.Vendor_Role_Id) {
                MyUrl1 = "http://103.129.72.100/vendorRequestsSummary/?userid=" + user.getLoginName();
            }

//            if (role == EmployeeConstants.Medco_Role_Id) {
//                MyUrl1 = "http://apmsidc.staging.ap.gov.in/raisedRequests/?userid=" + user.getLoginName();
//            } else if (role == EmployeeConstants.Medical_officer_Role_Id) {
//                MyUrl1 = "http://apmsidc.staging.ap.gov.in/medicalOfficerRequestsSummary/?userid=" + user.getLoginName();
//            } else if (role == EmployeeConstants.Pharma_Role_Id) {
//                MyUrl1 = "http://apmsidc.staging.ap.gov.in/pharmacistRequestsSummary/?userid=" + user.getLoginName();
//            } else if (role == EmployeeConstants.Vendor_Role_Id) {
//                MyUrl1 = "http://apmsidc.staging.ap.gov.in/vendorRequestsSummary/?userid=" + user.getLoginName();
//            }

            MyUrl1 += "&token=" + token;
            HashMap<String, Object> result = new HashMap<String, Object>();

//            userDetailsService.saveUserLogin(authenticationRequest.getUsername(), 1,authenticationRequest.getPassword(), req.getRemoteAddr());
//            result.put("userdetails", userDetailsService.getLoginUserData(authenticationRequest.getUsername()));
//               result.put("url",MyUrl);
            result.put("url1", MyUrl1);
            result.put("status", true);
            // Remove the captcha from the map after successful validation
            captchaMap.remove(authenticationRequest.getCaptchaId());
            return ResponseEntity.ok(result);
        } catch (BadRequestException e) {

            return ResponseEntity.status(400).body(new ApiResponse2<>(false, e.getMessage(), null, HttpStatus.BAD_REQUEST.value()));
        } catch (Exception ex) {

            ex.printStackTrace();
            int k = 4;
//            int k = userDetailsService.saveUserLogin(authenticationRequest.getUsername(), 0,
//                    authenticationRequest.getPassword(), req.getRemoteAddr());

            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("status", false);
            result.put("message", "Invalid Username or Password");
            if (k > 2) {
                result.put("message", "Inactive User");
                return ResponseEntity.status(400).body(result);

            } else {
                return ResponseEntity.status(201).body(result).ok(result);
            }
        }
    }


    @PostMapping("/logout-other-session")
    public ResponseEntity<?> logoutOtherSession(@RequestParam String username) {
        return ResponseEntity.ok(this.userDetailsService.logoutFromOtherSessions(username));
    }

//    @GetMapping("/get-secure-captcha")
//    public void getCaptcha(HttpServletResponse response, HttpServletRequest request) throws IOException {
//
//        String text = defaultKaptcha.createText();
//        BufferedImage image = defaultKaptcha.createImage(text);
//
////        request.getSession().setAttribute("captcha", text);
//        HttpSession session = request.getSession();
//        session.setAttribute("captcha", text);
//
//        response.setContentType("image/jpeg");
//        OutputStream outputStream = response.getOutputStream();
//        ImageIO.write(image, "jpg", outputStream);
//        outputStream.close();
//    }

    @GetMapping("/get-secure-captcha")
    public Map<String, String> getCaptcha(HttpServletResponse response) throws IOException {
        String text = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);

        String captchaId = UUID.randomUUID().toString();
        captchaMap.put(captchaId, text);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("captchaId", captchaId);
        responseData.put("captchaImage", "data:image/jpeg;base64," + base64Image);

        return responseData;
    }

    @GetMapping("/menu/{userId}")
    public ResponseEntity<?> getDynamicMenu(@PathVariable Integer userId) throws Exception {

        Map<String, Object> dynamicMenu = this.userDetailsService.getDynamicMenu(userId);

        if (dynamicMenu.isEmpty())
            throw new Exception("User Id doesn't exists!");

        return ResponseEntity.ok(dynamicMenu);

    }

    @GetMapping(value = "/checklogin")
    public ResponseEntity<?> checklogin() throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        result.put("status", true);
        result.put("message", "Session continued...");
        return ResponseEntity.ok(result);
    }

    private UserMst authenticate(String username, String password) throws Exception {

        String usrName = username.toUpperCase();
        Optional<UserMst> userData = userDetailsService.getUserDetailsByUserName(usrName);
        if (userData.get() == null) {
            throw new Exception("INVALID_CREDENTIALS");
        }
        byte[] decodedBytes = Base64.getDecoder().decode(password);
        String decodedPwdString = new String(decodedBytes);
        System.out.println(decodedPwdString);
        String hashedPassword = MedicalDisbursementService.generateEncriptedString(decodedPwdString);
        System.out.println(hashedPassword);
        if (!hashedPassword.equals(userData.get().getPassword())) {
            throw new Exception("INVALID_CREDENTIALS");
        }
        return userData.get();

    }


    public static String getSecurePassword(String password, String salt) throws InvalidKeyException {

        String generatedPassword = null;
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretkey = new SecretKeySpec(salt.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256HMAC.init(secretkey);

            generatedPassword = Hex.encodeHexString(sha256HMAC.doFinal(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }


    @GetMapping("/validate-token")
    public ResponseEntity<?> validateTokenUsername() {
        return this.userDetailsService.validateTokenUsername();
    }

    @PostMapping("/forget-password/sendotp")
    public ResponseEntity<?> forgetPasswordOtp(@RequestBody ForgetPassRequestDto forgetPassRequestDto) {
        return this.medicalDisbursementService.forgetPasswordOtp(forgetPassRequestDto.getUsername());
    }

    @PostMapping("/forget-password/validateotp")
    public ResponseEntity<?> forgetPasswordValidateOtp(@Valid @RequestBody ForgetPassRequestDto forgetPassRequestDto, HttpServletRequest req) {
//        HttpSession session = req.getSession();
//        String captchaSession = (String) session.getAttribute("captcha");
//
//        if (!forgetPassRequestDto.getCaptcha().equals(captchaSession))
//            throw new BadRequestException("Invalid Captcha Entered");

        String storedCaptcha = captchaMap.get(forgetPassRequestDto.getCaptchaId());

        if (!forgetPassRequestDto.getCaptcha().equals(storedCaptcha)) {
            throw new BadRequestException("Invalid Captcha Entered");
        }

        // Remove the captcha from the map after successful validation
        captchaMap.remove(forgetPassRequestDto.getCaptchaId());

        return this.medicalDisbursementService.forgetPasswordValidateOtp(forgetPassRequestDto);
    }
    
    
    @PostMapping("/mobile-login")
    public ResponseEntity<?> createOTP(@RequestBody MobileRqtDto mobileRqtDto,
            HttpServletRequest req) throws Exception {
    	try {
    		Map<String, Object> result=new HashMap<>();
    		UserMst user=null;
    		
	    	if(mobileRqtDto.getMobileNo()!=null) {
	    		 user=this.userRepo.findByMobileNo(mobileRqtDto.getMobileNo());	
	    	}else if(mobileRqtDto.getEmpCode()!=null) {
	    		user=this.userRepo.findByEmpName(mobileRqtDto.getEmpCode());
	    		if(user!=null)
	    			result.put("mobileNo",user.getMobileNo());
	    	}
	    	if(user!=null) {
    			Integer otp=otpUtil.generateOTP();
    			BeneficiaryOtpValidation otpValidation=new BeneficiaryOtpValidation();
    			
    			otpValidation.setOtp(otp);
    			otpValidation.setUser(user);
    			otpValidation.setIsValidated(false);
    			GeneralTypeMst generalType = this.generalMstRepo.findByTypeId(EmployeeConstants.Mobile_Login_Otp);
    			otpValidation.setValidationType(generalType);
    			otpValidation.setFailedAttempts(0);
    			
    			otpValidation=this.otpValidationRepo.save(otpValidation);
    			
    			result.put("OTP", otp);
    			result.put("userId",user.getUserId());
//    			medicalDisbursementService.sendsms(user.getMobileNo().toString() , "OTP: "+otp, "1407172405052774978");
    			return ResponseEntity.status(200).body(new ApiResponse2<>(true, 
    					"OTP sent successfully!", result, HttpStatus.OK.value()));
    		}
	    	return ResponseEntity.status(400).body(new ApiResponse2<>(false, 
	    					"No such user exist!", null, HttpStatus.BAD_REQUEST.value()));
    	}catch (Exception e) {
    		return ResponseEntity.status(400).body(e.getMessage());
    	}	
    }
    
    @PostMapping("/verify-mobile-otp")
    public ResponseEntity<?> verifyAndCreateToken(@RequestBody MobileOtp otp){
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	try {
    		Optional<UserMst> user=this.userRepo.findByUserId(otp.getUserId());
    		if(user.isPresent()) {	
    			GeneralTypeMst validationType = this.generalMstRepo.findByTypeId(EmployeeConstants.Mobile_Login_Otp);
    			BeneficiaryOtpValidation beneficiaryOtp=null;
    			if(validationType!=null) {
    				beneficiaryOtp = this.otpValidationRepo.findFirstByUserAndValidationTypeOrderByOtpGeneratedAtDesc(user.get(), validationType);
    			}
    			if(beneficiaryOtp!=null) {
	    			if(beneficiaryOtp.getOtp().toString().equals(otp.getOtp().toString())) {
	    				final String token = jwtTokenUtil.generateToken(user.get().getLoginName());
	//    	            String MyUrl = "http://localhost:3000/medical-disbursement/raisedRequests/?userid=" + user.get().getLoginName();
	    	            String myUrl = token;
	    	            
	    	            beneficiaryOtp.setIsValidated(true);
	    	            otpValidationRepo.save(beneficiaryOtp);
	    	            result.put("token", myUrl);
	    	            result.put("status", true);
	    	            
	    	            return ResponseEntity.status(200).body(new ApiResponse2<>(true, "User logged in successfully!", result, HttpStatus.OK.value()));
	    			}else {
	    				return ResponseEntity.status(200).body(new ApiResponse2<>(false, "OTP mismatched!", null, HttpStatus.BAD_REQUEST.value()));
	    			}
    			}else {
    				return ResponseEntity.status(200).body(new ApiResponse2<>(false, "Generate otp first!", null, HttpStatus.BAD_REQUEST.value()));
    			}
    		}else {
    			return ResponseEntity.status(200).body(new ApiResponse2<>(false, "no such user exist!", null, HttpStatus.BAD_REQUEST.value()));
    		}
    	}catch(Exception ex) {
    		return ResponseEntity.status(200).body(ex.getMessage());
    	}
    }
    
    


}
