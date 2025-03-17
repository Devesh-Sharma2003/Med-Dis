package in.kpmg.medicaldisbursement.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import in.kpmg.medicaldisbursement.dtos.RequestDto.*;
import in.kpmg.medicaldisbursement.models.UserMst;
import in.kpmg.medicaldisbursement.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.kpmg.medicaldisbursement.dtos.ApiResponse2;
import in.kpmg.medicaldisbursement.dtos.EmployeeDetailsDto;
import in.kpmg.medicaldisbursement.services.JwtUserDetailsService;
import in.kpmg.medicaldisbursement.services.MedicalDisbursementService;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RequestMapping("/public")

@CrossOrigin(
//        origins = {"http://141.148.194.18:8080", "http://localhost:3000"},
		origins = {	"http://http://103.129.72.100"},
        allowedHeaders = {"Content-Type", "Authorization", "X-Requested-With", "Accept"},
//	    allowedMethods = { "GET", "POST", "PUT", "DELETE" },
//	    exposedHeaders = { "Custom-Header1", "Custom-Header2" }
        allowCredentials = "true"
)

public class MedicalDisbursementController {

    @Autowired
    private MedicalDisbursementService medicalDisbursementService;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private Util util;


    @PostMapping("/getempdetails")
    public ApiResponse2<?> getMedicalDisburse(@Valid @RequestBody EmployeeDetailsDto empDto) {
        return medicalDisbursementService.getMedicalDisbursement(empDto);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authenticationRequest,
                                   HttpServletRequest req) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String clientIpAddress = this.util.getClientIp(req);

        result.put("userdetails", userDetailsService.getLoginUserData(authenticationRequest, clientIpAddress));
        return ResponseEntity.status(201).body(result).ok(result);
    }

    @GetMapping("/signout")
    public ApiResponse2<?> logout(Authentication authentication) {
        UserMst authenticatedUser = this.medicalDisbursementService.getAuthenticatedUser(authentication.getName());
        if (authenticatedUser == null) {

            return new ApiResponse2<>(false, "User not found", null, HttpStatus.UNAUTHORIZED.value());
        }
        return userDetailsService.logout(authenticatedUser.getUserId());
    }

    @PostMapping("/uploadAttachment")
    public ApiResponse2<Object> uploadAttachment(@RequestBody List<MultipartFile> file, Authentication authentication) throws IOException {
        try {
            if (medicalDisbursementService.uploadFiles(file, authentication).containsKey("invalid_file")) {
                return new ApiResponse2<>(false, "Invalid File Name", medicalDisbursementService.uploadFiles(file, authentication),
                        HttpStatus.NOT_ACCEPTABLE.value());
            }
            if (medicalDisbursementService.uploadFiles(file, authentication).containsKey("invalid_user")) {
                return new ApiResponse2<>(false, "Invalid User", medicalDisbursementService.uploadFiles(file, authentication),
                        HttpStatus.UNAUTHORIZED.value());
            } else {
                return new ApiResponse2<>(true, "Success", medicalDisbursementService.uploadFiles(file, authentication),
                        HttpStatus.OK.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse2<>(false, "Something went wrong!", e.getMessage(),
                    HttpStatus.BAD_REQUEST.value());
        }
    }


    @PostMapping(value = "/savePhysicianDrugs")
    public ApiResponse2<?> savePhysicianAndDrugs(@Valid @RequestBody BeneficiaryPhysicianDrugFileSaveRqtDto
                                                         physicianDto, Authentication authentication) throws Exception {
        return new ApiResponse2<>(true, "Details saved successfully!",
                this.medicalDisbursementService.savePhysicianAndDrugHandler(physicianDto, authentication),
                HttpStatus.OK.value());
    }

    @PostMapping("/getStepAction")
    public ApiResponse2<Object> getStepAction(@RequestBody HashMap<String, String> request) {
        return medicalDisbursementService.getStepAction(request.get("requestNumber"));
    }
//	 @PostMapping("/updateStatus")
//		public ApiResponse2<Object> updateStatus(@Valid @RequestBody NabhSaveDto nabhSaveDto) {
//	        return nabhServices.updateStatus(nabhSaveDto);
//	    }

    @PostMapping("/download")
    public ResponseEntity<?> downloadDocument(@RequestParam("filePath") String filePath) {
//	        log.info("--end of downloadDocument---");
        String actualPath = medicalDisbursementService.decryptUrl(filePath);
        Path path = Paths.get(actualPath);
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//	        log.info("--end of downloadDocument---");
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }


    @PostMapping("/getPhysicianDrugFile")
    public ApiResponse2<?> getPhysicianDrugFileByReqNo(@RequestBody HashMap<String, String> request, @RequestHeader("Authorization") String authenticationRequest) {

        if (this.medicalDisbursementService.getPhysicianDrugFileHandler(request.get("requestNumber"),
                authenticationRequest) != null) {
            return new ApiResponse2<>(true, "Details fetched successfully!", this.medicalDisbursementService
                    .getPhysicianDrugFileHandler(request.get("requestNumber"), authenticationRequest),
                    HttpStatus.OK.value());
        } else {
            return new ApiResponse2<>(
                    false, "Unauthorized Access", this.medicalDisbursementService
                    .getPhysicianDrugFileHandler(request.get("requestNumber"), authenticationRequest),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());

        }

    }

    @PostMapping("/uploadPostalConfirmation")
    public Object uploadPostalConfirmByReqNo(@Valid @RequestBody UploadPostalDto dto, Authentication authentication) throws FileNotFoundException {

        return this.medicalDisbursementService.uploadPostalConfirmByReqNoHandler(dto, authentication);

    }

    //dropdown APIs

    @GetMapping("/independent-dropdown")
    public ApiResponse2<?> initiateApplication() {
        return medicalDisbursementService.independentDropdown();
    }

    @PostMapping("/districtlist")
    public ApiResponse2<?> districtList(@RequestBody DropdownRequestDto dropDto) {
        return medicalDisbursementService.districtList(dropDto.getStateId());
    }

    @PostMapping("/mandallist")
    public ApiResponse2<?> mandalList(@RequestBody DropdownRequestDto dropDto) {
        return medicalDisbursementService.mandalList(dropDto.getDistId());
    }

    @PostMapping("/villagelist")
    public ApiResponse2<?> villageList(@RequestBody DropdownRequestDto dropDto) {
        return medicalDisbursementService.villageList(dropDto.getMandalId());
    }

    @PostMapping("/getbeneficiaries")
    public ApiResponse2<?> getBene(@Valid @RequestBody EmployeeDetailsDto dropDto) {
        return medicalDisbursementService.getParentBeneficiaryWithChildren(dropDto);
    }

    @PostMapping("/getinbox")
    public ApiResponse2<?> getInbox(@Valid @RequestBody EmployeeDetailsDto dropDto, Authentication authentication) {
        return medicalDisbursementService.getInbox(dropDto.getUserId(), authentication);
    }

    @PostMapping("/count")
    public ApiResponse2<?> getCount(@Valid @RequestBody EmployeeDetailsDto dropDto, Authentication authentication) {
        UserMst authenticatedUser = this.medicalDisbursementService.getAuthenticatedUser(authentication.getName());
        return medicalDisbursementService.getCount(authenticatedUser.getUserId());
    }

    @PostMapping("/trackstatus")
    public ApiResponse2<?> getStatus(@RequestBody HashMap<String, String> request, @RequestHeader("Authorization") String authenticationRequest) {
        return medicalDisbursementService.getStatus(request.get("requestNumber"), authenticationRequest);
    }

    @PostMapping("/dashboardtable")
    public ApiResponse2<?> getDashTable(@Valid @RequestBody FilterRequestDto dropDto, Authentication authentication) {
        return medicalDisbursementService.getDashboardTable(dropDto, authentication);
    }

    @PostMapping("/takeaction")
    public ApiResponse2<?> getTakeAction(@Valid @RequestBody UpdateStatusDto actDto, Authentication authentication) throws Exception {
        return medicalDisbursementService.takeAction(actDto, authentication);
    }

    @PostMapping("/medical-officer/takeaction")
    public ApiResponse2<?> getMedicalOfficerTakeAction(@Valid @RequestBody MedicalOfficerAppReqDto actDto, Authentication authentication) throws Exception {
        return medicalDisbursementService.saveMedicalOfficerAction(actDto, authentication);
    }

    @PostMapping("/vendor/takeaction")
    public ApiResponse2<?> getVendorTakeAction(@Valid @RequestBody VendorActionReqDto actDto, Authentication authentication) throws Exception {
        return medicalDisbursementService.saveVendorAction(actDto, authentication);
    }

    @PostMapping("/pharmacist-receive/takeaction")
    public ApiResponse2<?> getPharmaMedTakeAction(@Valid @RequestBody PharmacistReceiveActionReqDto actDto, Authentication authentication) throws Exception {
        return medicalDisbursementService.savePharmaReceiveAction(actDto, authentication);
    }

    @PostMapping("/pharmacist-dispatch/takeaction")
    public ApiResponse2<?> getPharmaMedTakeAction(@Valid @RequestBody PharmaDispatchActionReqDto actDto, Authentication authentication) throws Exception {
        return medicalDisbursementService.savePharmaDispatchAction(actDto, authentication);
    }

    @PostMapping("/receiveMedicine")
    public ApiResponse2<?> recieveMedicine(@Valid @RequestBody EmployeeDetailsDto dropDto, Authentication authentication) {
        return medicalDisbursementService.recieveMedicine(dropDto, authentication);
    }

    @PostMapping("/dispatchMedicine")
    public ApiResponse2<?> dispatchMedicine(@Valid @RequestBody EmployeeDetailsDto dropDto, Authentication authentication) {
        return medicalDisbursementService.dispatchMedicine(dropDto, authentication);
    }

    @PostMapping("/validate")
    public ApiResponse2<?> validate(@Valid @RequestBody EmployeeDetailsDto dropDto, @RequestHeader("Authorization") String authenticationRequest) {
        return medicalDisbursementService.validateUser(dropDto, authenticationRequest);
    }

}
