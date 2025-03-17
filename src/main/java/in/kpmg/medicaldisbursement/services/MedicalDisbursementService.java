package in.kpmg.medicaldisbursement.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map.Entry;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import in.kpmg.medicaldisbursement.dtos.ForgetPassRequestDto;

import in.kpmg.medicaldisbursement.dtos.RequestDto.*;
import in.kpmg.medicaldisbursement.exceptions.BadRequestException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.PDDestinationOrAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.netflix.discovery.converters.Auto;


import freemarker.core.ParseException;
import in.kpmg.medicaldisbursement.config.AesUtil;
import in.kpmg.medicaldisbursement.config.JwtTokenUtil;
import in.kpmg.medicaldisbursement.constants.EmployeeConstants;
import in.kpmg.medicaldisbursement.controllers.AuthenticationController;
import in.kpmg.medicaldisbursement.dtos.ApiResponse2;
import in.kpmg.medicaldisbursement.dtos.ApiResponseStatus;
import in.kpmg.medicaldisbursement.dtos.EmployeeDetailsDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.CountResponseDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.DashboardTableResponse;
import in.kpmg.medicaldisbursement.dtos.responseDto.DropdownDataDto;

import in.kpmg.medicaldisbursement.dtos.responseDto.DrugRspDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.DrugSaveRspDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.StepActionResponse;
import in.kpmg.medicaldisbursement.dtos.responseDto.FileRspDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.InboxResponseDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.PhysicianRspDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.RemarksOutputDto;
import in.kpmg.medicaldisbursement.exceptions.NoRecordFoundException;
import in.kpmg.medicaldisbursement.exceptions.UnauthorizedException;
//import in.kpmg.medicaldisbursement.mapper.BeneficiaryToDtoMapper;
import in.kpmg.medicaldisbursement.models.BeneficiaryMst;
import in.kpmg.medicaldisbursement.models.BeneficiaryOtpValidation;
import in.kpmg.medicaldisbursement.models.UserMst;
import in.kpmg.medicaldisbursement.models.UserRoleMappingMst;
import in.kpmg.medicaldisbursement.models.VillageMaster;
import in.kpmg.medicaldisbursement.repo.ActionRepo;
import in.kpmg.medicaldisbursement.repo.AuditRepo;
import in.kpmg.medicaldisbursement.repo.BeneficiaryMstRepo;
import in.kpmg.medicaldisbursement.repo.BeneficiaryOtpValidationRepo;
import in.kpmg.medicaldisbursement.models.ActionMst;
import in.kpmg.medicaldisbursement.models.BeneficiaryAttachments;
import in.kpmg.medicaldisbursement.models.BeneficiaryRequestDetails;
import in.kpmg.medicaldisbursement.models.BeneficiaryRequestDrug;
import in.kpmg.medicaldisbursement.models.BeneficiaryRequestDrugAudit;
import in.kpmg.medicaldisbursement.models.DistrictMaster;
import in.kpmg.medicaldisbursement.models.GeneralTypeMst;
import in.kpmg.medicaldisbursement.models.MandalMaster;
import in.kpmg.medicaldisbursement.models.MenuMst;
import in.kpmg.medicaldisbursement.models.OtpValidations;
import in.kpmg.medicaldisbursement.models.RoleMenuMappingMst;
import in.kpmg.medicaldisbursement.models.RoleMst;
import in.kpmg.medicaldisbursement.models.StateMaster;
import in.kpmg.medicaldisbursement.models.StepMaster;
import in.kpmg.medicaldisbursement.models.StepRoleMapping;
import in.kpmg.medicaldisbursement.models.UserMst;
import in.kpmg.medicaldisbursement.repo.BeneficiaryAttachmentRepo;
import in.kpmg.medicaldisbursement.repo.BeneficiaryDrugAuditRepo;
import in.kpmg.medicaldisbursement.repo.BeneficiaryRqtDetailsRepo;
import in.kpmg.medicaldisbursement.repo.BeneficiaryRqtDrugsRepo;
import in.kpmg.medicaldisbursement.repo.GeneralTypeMasterRepo;

import in.kpmg.medicaldisbursement.repo.DistrictRepo;
import in.kpmg.medicaldisbursement.repo.DrugMstRepo;
import in.kpmg.medicaldisbursement.repo.GeneralTypeMasterRepo;
import in.kpmg.medicaldisbursement.repo.MandalRepo;
import in.kpmg.medicaldisbursement.repo.MenuMstRepo;
import in.kpmg.medicaldisbursement.repo.OtpValidationsRepo;
import in.kpmg.medicaldisbursement.repo.RoleMenuRightMapRepo;
import in.kpmg.medicaldisbursement.repo.RoleRepo;
import in.kpmg.medicaldisbursement.repo.StateRepo;
import in.kpmg.medicaldisbursement.repo.StepMasterRepo;
import in.kpmg.medicaldisbursement.repo.StepRoleMappingRepo;
import in.kpmg.medicaldisbursement.repo.UserRepo;
import in.kpmg.medicaldisbursement.repo.UserRoleMapRepo;
import in.kpmg.medicaldisbursement.repo.VillageRepo;
import in.kpmg.medicaldisbursement.utils.OtpUtil;
import lombok.extern.slf4j.Slf4j;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

@Slf4j
@Service
public class MedicalDisbursementService {

    @Autowired
    UserRepo userRepo;
    
    @Autowired
    private DrugMstRepo drugRepo;

    @Autowired
    BeneficiaryMstRepo beneficiaryMstRepo;

    @Autowired
    private BeneficiaryRqtDetailsRepo beneficiaryRqtDetailsRepo;

    @Autowired
    private BeneficiaryRqtDrugsRepo beneficiaryRqtDrugsRepo;

    @Autowired
    ActionRepo actionRepo;

    @Autowired
    StepMasterRepo stepMasterRepo;

    @Autowired
    EntityManager em;

    @Autowired
    GeneralTypeMasterRepo generalTypeMasterRepo;

    @Autowired
    private StepRoleMappingRepo stepRoleRepo;

    @Autowired
    DistrictRepo districtRepo;

//	@Autowired
//	SmsService services;

    @Autowired
    StateRepo stateRepo;

    @Autowired
    MandalRepo mandalRepo;

    @Autowired
    VillageRepo villageRepo;

    @Autowired
    UserRoleMapRepo userRoleMapRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    OtpUtil otpUtil;

    @Autowired
    private BeneficiaryDrugAuditRepo beneficiaryDrugAuditRepo;

    @Autowired
    BeneficiaryOtpValidationRepo beneficiaryOtpValidationRepo;

    @Autowired
    AuditRepo auditRepo;

    @Autowired
    OtpValidationsRepo otpValidationsRepo;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private MenuMstRepo menuMstRepo;

    @Autowired
    private RoleMenuRightMapRepo roleMenuRightMapRepo;


//	@Autowired
//	private BeneficiaryToDtoMapper beneficiaryToDtoMapper; 

    @Autowired
    private BeneficiaryAttachmentRepo beneficiaryAttachmentRepo;

    @Value("${file.temp.path}")
    private String tempPath;

    AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();

    private static final double DISCOUNT_PERCENTAGE = 24.0;

    public static final List<String> CONTENT_TYPE = Arrays.asList("application/pdf", "image/jpeg", "image/png", "image/jpg");

    private static final Pattern XSS_PATTERN = Pattern.compile("<script>(.*?)</script>|javascript:|on\\w+=");
    @Value("${file.path}")
    private String docPath;

    private static final DateTimeFormatter DATE_FOMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    static private String hashPassword = "U2FsdGVkX1/rGDtQ5/vtQ9D6RWYsf5+yUj6+mX3I8Qs=";


    public MedicalDisbursementService() {
        aesEncryptor.setPassword(hashPassword);
    }

    public ApiResponse2<?> getMedicalDisbursement(EmployeeDetailsDto empDto) {
        Optional<BeneficiaryMst> pft = beneficiaryMstRepo.findById(empDto.getBeneId());
        if (pft == null) {
            throw new NoRecordFoundException(ApiResponseStatus.noRecords);
        }
        return new ApiResponse2<>(true, ApiResponseStatus.fetch, pft, HttpStatus.OK.value());
    }

    public String decryptUrl(String encriptPath) {
        return aesEncryptor.decrypt(encriptPath);
    }


    private Map<Boolean, String> scanAv(MultipartFile file) throws IOException {
        Map<Boolean, String> avScan = new HashMap<>();
        try {


            ClamavClient client = new ClamavClient("127.0.0.1", 3310);
            ScanResult scanResult = client.scan(file.getInputStream());

            if (scanResult instanceof ScanResult.OK) {
                avScan.put(Boolean.TRUE, "No malicious content found");
            } else if (scanResult instanceof ScanResult.VirusFound) {
                avScan.put(Boolean.FALSE, "Malicious content found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return avScan;
    }


    public static Map<Boolean, String> scanForJavaScript(PDDocument document) throws Exception {
        Map<Boolean, String> jsParser = new HashMap<>();
        try {

            PDDocumentCatalog catalog = document.getDocumentCatalog();
            PDDestinationOrAction openAction = catalog.getOpenAction();
            if (openAction instanceof PDActionJavaScript) {
                PDActionJavaScript jsAction = (PDActionJavaScript) openAction;
                jsParser.put(Boolean.FALSE, "JavaScript found: " + jsAction.getAction());
            } else {
                System.out.println("No JavaScript found in the document actions.");
                jsParser.put(Boolean.TRUE, "No JavaScript found in the document actions.");
            }
            return jsParser;
        } catch (Exception e) {
            e.printStackTrace();
            jsParser.put(Boolean.FALSE, "Internal Server Error");
            return jsParser;
        }

    }

    public static Map<Boolean, String> scanForHTMLandURLs(PDDocument document) throws Exception {
        // Extract the text from the PDF
        Map<Boolean, String> htmlParser = new HashMap<>();
        try {


            PDFTextStripper stripper = new PDFTextStripper();
            String pdfText = stripper.getText(document);

            // Regular expression to detect HTML tags
            String htmlPattern = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";

            // Regular expression to detect URLs
            String urlPattern = "(http|https|ftp|mailto):\\S+";

            // Compile patterns
            Pattern htmlTagPattern = Pattern.compile(htmlPattern, Pattern.CASE_INSENSITIVE);
            Pattern urlPatternCompiled = Pattern.compile(urlPattern);

            // Check for HTML
            Matcher htmlMatcher = htmlTagPattern.matcher(pdfText);
            if (htmlMatcher.find()) {

                System.out.println("HTML tags detected in the PDF text.");
                htmlParser.put(Boolean.FALSE, "HTML tags detected in the PDF text");
//            throw new RuntimeException("HTML tags detected in the PDF text");
//            return true;
            } else {
                System.out.println("No HTML tags found.");
                htmlParser.put(Boolean.TRUE, "No HTML tags found");
            }

//        Matcher urlMatcher = urlPatternCompiled.matcher(pdfText);
//        if (urlMatcher.find()) {
//            throw new RuntimeException("URLs detected in the PDF text");
////            return true;
//        } else {
//            System.out.println("No URLs found.");
//            return false;
//        }
            return htmlParser;
        } catch (Exception e) {
            e.printStackTrace();
            htmlParser.put(Boolean.FALSE, "Internal Server Error");
            return htmlParser;
        }
    }
    
//    public static boolean isValidFileName(String fileName) {      
//        String regex = "^[\\w-]+(v\\d+\\.\\d+)?\\.[a-zA-Z0-9]+$";
//        
//        return fileName.matches(regex);
//    }
    
    public static boolean isValidFileName(String fileName) {
        String regex = "^[a-zA-Z0-9 _-]+\\.[a-zA-Z0-9]+$";
        if (!fileName.matches(regex)) {
            System.out.println("Invalid file name: Either multiple extensions or invalid characters in the file name.");
            return false;
        }
        return true;
    }

    public HashMap<String, String> uploadFiles(List<MultipartFile> uploadfiles, Authentication authentication) throws Exception {
//		log.info("--start of uploadFiles---");
        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());
        Integer roleId = userRoleMapRepo.findRoleIdByUserId(authenticatedUser.getUserId());

//        RoleMst role = roleRepo.findByRoleId(roleId);
        HashMap<String, String> pathResponse = new HashMap<>();
        if (EmployeeConstants.Medical_officer_Role_Id != roleId) {

            String encodedpath = "";
            String fileName = "";
            String pathDirectory = System.getProperty("java.io.tmpdir");
            pathDirectory += tempPath;

            for (MultipartFile uploadfile : uploadfiles) {

                fileName = StringUtils.cleanPath(uploadfile.getOriginalFilename()).replaceAll("//s", "");
                String extension = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                int l = fileName.length() - extension.length();
                String fileExt = fileName.substring(0, l);
                System.out.println(fileExt);
                System.out.println(fileName);
                char c = ' ';
                int flag = 0;
//                for (int i = 0; i < fileExt.length(); i++) {
//                    c = fileExt.charAt(i);
//                    if (c == '*' || c == '/' || c == '"' || c == '|' || c == '\\' || c == ':' || c == '<' || c == '?' || c == '>') {
//                        flag = 1;
//                    }
//                }

                System.out.println(isValidFileName(fileExt));
                if ((isValidFileName(fileName)==true) && (extension.contains(".pdf"))) {
                    log.info("pdf file check");
                    Map<String, Map<Boolean, String>> result = new HashMap<>();
                    Map<Boolean, String> avScanner = scanAv(uploadfile);
//
                    System.out.println(avScanner);
                    InputStream inputStream = uploadfile.getInputStream();
                    PDDocument document = PDDocument.load(inputStream);

                    Map<Boolean, String> js = scanForJavaScript(document);
                    Map<Boolean, String> html = scanForHTMLandURLs(document);
//                    result.put("AntiVirus", avScanner);
                    result.put("JavaScript Parser", js);
                    result.put("HTML Parser", html);
                    System.out.println(result.toString());

                    System.out.println(js.containsKey(Boolean.TRUE));
                    if (js.containsKey(Boolean.TRUE) && html.containsKey(Boolean.TRUE) && avScanner.containsKey(Boolean.TRUE)) {
                        log.info("file is ok");
                        long currentTime = System.currentTimeMillis();
                        Path path = Paths.get(pathDirectory + String.valueOf(currentTime) + fileName);
                        try {
                            Files.createDirectories(Paths.get(pathDirectory + File.separator));
                            System.out.println("before copy file path " + path);
                            Files.copy(uploadfile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        encodedpath = aesEncryptor.encrypt(path.toString());

                        pathResponse.put(fileName, encodedpath);
//     					log.info("----end of uploadfiles----");
//                        return pathResponse;
                    } else {
                        log.info("malicious pdf");
                        pathResponse.put("invalid_file", "Upload Restricted: The document contains potentially harmful content.");
                        return pathResponse;
                    }
                } else if ((isValidFileName(fileName)==true) && (extension.contains(".png") || extension.contains(".jpeg") || extension.contains(".jpg"))) {
                    log.info("img file check");
                    long currentTime = System.currentTimeMillis();
                    Path path = Paths.get(pathDirectory + String.valueOf(currentTime) + fileName);
                    try {
                        Files.createDirectories(Paths.get(pathDirectory + File.separator));
                        System.out.println("before copy file path " + path);
                        Files.copy(uploadfile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    encodedpath = aesEncryptor.encrypt(path.toString());

                    pathResponse.put(fileName, encodedpath);
//		log.info("----end of uploadfiles----");
//                    return pathResponse;
                } else {
                    log.info("invalid file");
                    pathResponse.put("invalid_file", "invalid file name");
                    return pathResponse;
                }
            }
            return pathResponse;
        }

//        pathResponse.put("invalid_user", "Unauthorized Access");
        return pathResponse;
    }


    /**
     * public HashMap<String, String> uploadFiles(List<MultipartFile> uploadfiles, Authentication authentication) {
     * //		log.info("--start of uploadFiles---");
     * UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());
     * Integer roleId = userRoleMapRepo.findRoleIdByUserId(authenticatedUser.getUserId());
     * <p>
     * //        RoleMst role = roleRepo.findByRoleId(roleId);
     * HashMap<String, String> pathResponse = new HashMap<>();
     * if (EmployeeConstants.Medical_officer_Role_Id != roleId) {
     * String encodedpath = "";
     * String fileName = "";
     * String pathDirectory = System.getProperty("java.io.tmpdir");
     * pathDirectory += tempPath;
     * <p>
     * for (MultipartFile uploadfile : uploadfiles) {
     * fileName = StringUtils.cleanPath(uploadfile.getOriginalFilename()).replaceAll("//s", "");
     * String extension = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
     * int l = fileName.length() - extension.length();
     * String fileExt = fileName.substring(0, l);
     * char c = ' ';
     * int flag = 0;
     * for (int i = 0; i < fileExt.length(); i++) {
     * c = fileExt.charAt(i);
     * if (c == '*' || c == '/' || c == '"' || c == '|' || c == '\\' || c == ':' || c == '<' || c == '?' || c == '>') {
     * flag = 1;
     * }
     * }
     * <p>
     * if (flag != 1 && (extension.contains(".pdf"))){
     * log.info("pdf file check");
     * try (InputStream fis =uploadfile.getInputStream()) {
     * byte[] buffer = new byte[2048];
     * int bytesRead;
     * <p>
     * StringBuilder rawContent = new StringBuilder();
     * while ((bytesRead = fis.read(buffer)) != -1) {
     * <p>
     * rawContent.append(new String(buffer, 0, bytesRead, "ISO-8859-1"));
     * }
     * <p>
     * String content = rawContent.toString();
     * <p>
     * if (!containsMaliciousPatterns(content)) {
     * log.info("file is ok");
     * long currentTime = System.currentTimeMillis();
     * Path path = Paths.get(pathDirectory + String.valueOf(currentTime) + fileName);
     * try {
     * Files.createDirectories(Paths.get(pathDirectory + File.separator));
     * System.out.println("before copy file path " + path);
     * Files.copy(uploadfile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * encodedpath = aesEncryptor.encrypt(path.toString());
     * <p>
     * pathResponse.put(fileName, encodedpath);
     * //     					log.info("----end of uploadfiles----");
     * return pathResponse;
     * }
     * else {
     * log.info("malicious pdf");
     * pathResponse.put("invalid_file","Upload Restricted: The document contains potentially harmful content.");
     * return pathResponse;
     * }
     * <p>
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * }
     * else if (flag != 1 && (extension.contains(".png") || extension.contains(".jpeg") || extension.contains(".jpg"))) {
     * log.info("img file check");
     * long currentTime = System.currentTimeMillis();
     * Path path = Paths.get(pathDirectory + String.valueOf(currentTime) + fileName);
     * try {
     * Files.createDirectories(Paths.get(pathDirectory + File.separator));
     * System.out.println("before copy file path " + path);
     * Files.copy(uploadfile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * encodedpath = aesEncryptor.encrypt(path.toString());
     * <p>
     * pathResponse.put(fileName, encodedpath);
     * //		log.info("----end of uploadfiles----");
     * return pathResponse;
     * } else {
     * log.info("invalid file");
     * pathResponse.put("invalid_file", "invalid file name");
     * return pathResponse;
     * }
     * }
     * }
     * <p>
     * pathResponse.put("invalid_user", "Unauthorized Access");
     * return pathResponse;
     * }
     * <p>
     * private static boolean containsMaliciousPatterns(String content) {
     * log.info("in containsMaliciousPatterns");
     * String[] maliciousPatterns = {
     * "/JavaScript",
     * "/JS",
     * "app.alert",
     * "document.domain"
     * };
     * <p>
     * for (String pattern : maliciousPatterns) {
     * if (content.contains(pattern)) {
     * return true;
     * }
     * }
     * return false;
     * }
     **/

    public String uploadOnRealPath(FileDto fileDto) throws FileNotFoundException {
//		log.info("--start of uploadOnRealPath---");
        String tempDocPath = this.decryptUrl(fileDto.getVirtualPath());
        System.out.println(tempDocPath);
        if (new File(tempDocPath).isFile()) {
            System.out.println("temp path" + new File(tempDocPath).isFile());
            String filePath = docPath + File.separator;
            LocalDate date = LocalDate.now();

            filePath += fileDto.getAppNo() + File.separator + date.getYear() + File.separator + date.getMonthValue()
                    + File.separator
                    + date.getDayOfMonth();

            try {
                Files.createDirectories(Paths.get(filePath + File.separator));
                System.out.println(tempDocPath.substring(tempDocPath.lastIndexOf(File.separator) + 1));
                filePath += tempDocPath.substring(tempDocPath.lastIndexOf(File.separator) + 1);
                System.out.println("The server path: " + filePath);
                Path temp = Files.move(Paths.get(tempDocPath), Paths.get(filePath));
                if (temp != null)
                    return aesEncryptor.encrypt(filePath);
            } catch (Exception e) {
                System.out.println("exception " + e);
                return e.getMessage();
            }
//			log.info("--end of uploadOnRealPath---");
            return aesEncryptor.encrypt(filePath);
        } else {
            throw new FileNotFoundException("File already moved");
        }
    }

    public UserMst getAuthenticatedUser(String username) {
        return this.userRepo.findByLoginName(username).orElseThrow(UnauthorizedException::new);
    }

    public UserMst loadUserByUsername(String jwtToken) {
//      System.out.println(authentication);

        jwtToken = jwtToken.substring(7);
        String username = this.jwtUtil.getUsernameFromToken(jwtToken);

        if (username != null) {
//          System.out.println(authentication.getName());

            Optional<UserMst> optionalAuthenticationUser = this.userRepo.findByLoginName(username);

            if (!optionalAuthenticationUser.isPresent())
                throw new UnauthorizedException("Unauthorized Access!");

            return optionalAuthenticationUser.get();

        } else {
//      user = null;
            throw new UnauthorizedException("Unauthorized Access!");
        }
    }


    public Object savePhysicianAndDrugHandler(BeneficiaryPhysicianDrugFileSaveRqtDto dto, Authentication authentication) throws Exception {
        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());
        BeneficiaryMst beneObj = null;
        if (dto.getBeneficiaryId() == null) {
            BeneficiaryMst saveBeneficiaryMst = new BeneficiaryMst();

            saveBeneficiaryMst.setBeneficiaryName(dto.getName());
            saveBeneficiaryMst.setBeneficiaryPhoneNo(Long.parseLong(dto.getMobileNo()));
            if(!dto.getMobileAltNo().isEmpty()){
            	saveBeneficiaryMst.setAlternateContactNumber(Long.parseLong(dto.getMobileAltNo()));
            }
            saveBeneficiaryMst.setParentBeneficiaryId(dto.getParentId());
            if (dto.getDob() != null || (!dto.getDob().equals(""))){
            	LocalDate currDate = LocalDate.now();
            	if(dateConversion(dto.getDob()).compareTo(currDate) < 0) {
                saveBeneficiaryMst.setBeneficiaryDob(dateConversion(dto.getDob()));}
            	else {
            	throw new BadRequestException("Date of birth should be less than today's date!");}
            }
            
            if (dto.getRelation() != null) {
                Optional<GeneralTypeMst> generalObjRealtion = this.generalTypeMasterRepo.findById(dto.getRelation());
                if (generalObjRealtion != null)
                    saveBeneficiaryMst.setRelation(generalObjRealtion.get());
            }
            if (dto.getDesignationId() != null) {
                Optional<GeneralTypeMst> generalObjType = this.generalTypeMasterRepo.findById(dto.getDesignationId());
                if (generalObjType != null)
                    saveBeneficiaryMst.setBeneficiaryType(generalObjType.get());
            } else {
                saveBeneficiaryMst.setBeneficiaryType(null);
            }
            if (dto.getGender() != null) {
                Optional<GeneralTypeMst> generalObjGender = this.generalTypeMasterRepo.findById(dto.getGender());
                if (generalObjGender != null)
                    saveBeneficiaryMst.setGender(generalObjGender.get());
            }
            saveBeneficiaryMst.setEmail(dto.getEmail());
            saveBeneficiaryMst.setHouseNo(dto.getHouseNo());
            saveBeneficiaryMst.setLocality(dto.getStreet());
            if (dto.getLandmark() != null)
                saveBeneficiaryMst.setLandmark(dto.getLandmark());
            saveBeneficiaryMst.setIsActive(true);
            Optional<StateMaster> state = this.stateRepo.findById(dto.getState());
            if (state != null)
                saveBeneficiaryMst.setStateId(state.get());
            Optional<DistrictMaster> dist = this.districtRepo.findById(dto.getDistrict());
            if (dist != null)
                saveBeneficiaryMst.setDistrictId(dist.get());
            Optional<MandalMaster> man = this.mandalRepo.findById(dto.getMandal());
            if (man != null)
                saveBeneficiaryMst.setMandalId(man.get());
            Optional<VillageMaster> vill = this.villageRepo.findById(dto.getVillage());
            if (vill != null)
                saveBeneficiaryMst.setVillageId(vill.get());
            saveBeneficiaryMst.setPincode(dto.getPincode());
            beneObj = this.beneficiaryMstRepo.save(saveBeneficiaryMst);
        } else {
            Optional<BeneficiaryMst> beneficiaryMstObj = this.beneficiaryMstRepo.findById(dto.getBeneficiaryId());

            BeneficiaryRequestDetails saveBrDetailsObject = null;

            if (dto != null && beneficiaryMstObj.isPresent()) {
                if (dto.getName() != null)
                    beneficiaryMstObj.get().setBeneficiaryName(dto.getName());
                beneficiaryMstObj.get().setBeneficiaryPhoneNo(Long.parseLong(dto.getMobileNo()));

                if (dto.getMobileAltNo() != null && !dto.getMobileAltNo().isEmpty())
                    beneficiaryMstObj.get().setAlternateContactNumber(Long.parseLong(dto.getMobileAltNo()));

                	if (dto.getDob() != null || (!dto.getDob().equals(""))){
                    	LocalDate currDate = LocalDate.now();
                    	if(dateConversion(dto.getDob()).compareTo(currDate) < 0) {
                    		beneficiaryMstObj.get().setBeneficiaryDob(dateConversion(dto.getDob()));}
                    	else {
                    	throw new BadRequestException("Date of birth should be less than today's date!");}
                    }
                    
                if (dto.getRelation() != null) {
                    Optional<GeneralTypeMst> generalObjRealtion = this.generalTypeMasterRepo.findById(dto.getRelation());
                    if (generalObjRealtion != null)
                        beneficiaryMstObj.get().setRelation(generalObjRealtion.get());
                }
                if (dto.getDesignationId() != null) {
                    Optional<GeneralTypeMst> generalObjType = this.generalTypeMasterRepo.findById(dto.getDesignationId());
                    if (generalObjType != null)
                        beneficiaryMstObj.get().setBeneficiaryType(generalObjType.get());
                } else {
                    beneficiaryMstObj.get().setBeneficiaryType(null);
                }
                if (dto.getGender() != null) {
                    Optional<GeneralTypeMst> generalObjGender = this.generalTypeMasterRepo.findById(dto.getGender());
                    if (generalObjGender != null)
                        beneficiaryMstObj.get().setGender(generalObjGender.get());
                }
                beneficiaryMstObj.get().setEmail(dto.getEmail());
                beneficiaryMstObj.get().setHouseNo(dto.getHouseNo());
                beneficiaryMstObj.get().setLocality(dto.getStreet());
                Optional<StateMaster> state = this.stateRepo.findById(dto.getState());
                if (state != null)
                    beneficiaryMstObj.get().setStateId(state.get());
                Optional<DistrictMaster> dist = this.districtRepo.findById(dto.getDistrict());
                if (dist != null)
                    beneficiaryMstObj.get().setDistrictId(dist.get());
                Optional<MandalMaster> man = this.mandalRepo.findById(dto.getMandal());
                if (man != null)
                    beneficiaryMstObj.get().setMandalId(man.get());
                Optional<VillageMaster> vill = this.villageRepo.findById(dto.getVillage());
                if (vill != null)
                    beneficiaryMstObj.get().setVillageId(vill.get());
                beneficiaryMstObj.get().setPincode(dto.getPincode());
                beneObj = this.beneficiaryMstRepo.save(beneficiaryMstObj.get());
            }
        }
        String generatedSeq = this.beneficiaryRqtDetailsRepo.generateMergedSequence();
        BeneficiaryRequestDetails brDetailsObject = new BeneficiaryRequestDetails();

        brDetailsObject.setRequestNumber(generatedSeq);

        Optional<BeneficiaryMst> beneObject = this.beneficiaryMstRepo.findById(beneObj.getId());
        if (beneObject.isPresent())
            brDetailsObject.setBeneficiaryId(beneObject.get());
        brDetailsObject.setDoctorName(dto.getDoctorName());
        brDetailsObject.setDoctorMobileNo(Long.parseLong(dto.getDoctorPhoneNo()));
        brDetailsObject.setRemarks(dto.getRemarks());
        Optional<StepRoleMapping> stepRoleObj = this.stepRoleRepo.findById(dto.getStatus());
        if (stepRoleObj.isPresent())
            brDetailsObject.setStatus(stepRoleObj.get());
        Optional<UserMst> userMstObj = this.userRepo.findById(authenticatedUser.getUserId());
        if (userMstObj.isPresent()) {
            brDetailsObject.setCreatedBy(userMstObj.get());
            brDetailsObject.setUpdatedBy(userMstObj.get());
        }
        if (dto.getAuthorizedName() != null)
            brDetailsObject.setAuthorizedPersonName(dto.getAuthorizedName());
        if (dto.getAuthorizedPersonMob() != null)
            brDetailsObject.setAuthorizedPersonNo(Long.parseLong(dto.getAuthorizedPersonMob()));
        Optional<GeneralTypeMst> generalObj = this.generalTypeMasterRepo.findById(dto.getRequestedPersonType());
        if (generalObj != null)
            brDetailsObject.setRequestedPersonType(generalObj.get());

        BeneficiaryRequestDetails saveBrDetailsObject = this.beneficiaryRqtDetailsRepo.save(brDetailsObject);

//		saving aadhar card and id card file
        if (saveBrDetailsObject != null && dto != null && dto.getAuthorizationLetter() != null) {
            BeneficiaryAttachments authorizeLetter = new BeneficiaryAttachments();
            FileSaveRqtDto letter = dto.getAuthorizationLetter();
            authorizeLetter.setAttachmentType(letter.getAttachmentType());
            authorizeLetter.setFileName(letter.getFileName());
            authorizeLetter.setFilePath(this.uploadOnRealPath(
                    new FileDto(letter.getFilePath(), String.valueOf(saveBrDetailsObject.getRequestNumber()))));
            Optional<UserMst> user = this.userRepo.findById(authenticatedUser.getUserId());
            if (user.isPresent())
                authorizeLetter.setCreatedBy(user.get());
            Optional<BeneficiaryRequestDetails> obj = this.beneficiaryRqtDetailsRepo
                    .findById(saveBrDetailsObject.getRequestNumber());
            authorizeLetter.setRequestNumber(obj.get());

            this.beneficiaryAttachmentRepo.save(authorizeLetter);
        }

        if (dto.getDrugList() != null) {
            List<BeneficiaryRequestDrug> drugRspList = new ArrayList<>();

            for (DrugSaveRqtDto drug : dto.getDrugList()) {
                BeneficiaryRequestDrug obj = new BeneficiaryRequestDrug();

                if (saveBrDetailsObject != null) {
                    Optional<BeneficiaryRequestDetails> ob = beneficiaryRqtDetailsRepo.findById(saveBrDetailsObject.getRequestNumber());
                    obj.setRequestNumber(ob.get());
                    System.out.println(ob.get());
                }
                if (drug.getDrugUnitId() != null) {
                    obj.setUnit(drug.getDrugUnitId());
                    if (drug.getDrugUnitId() == 59){
                    	if(drug.getDrugUnitName() == null ||(drug.getDrugUnitName().equals(""))) {
                    		throw new BadRequestException("Drug unit name is Mandatory!");                        
                    }
                    	else
                    	{
                    		obj.setUnitText(drug.getDrugUnitName());
                    	}
                    }
                    
                    else {
                        Optional<GeneralTypeMst> genObj = this.generalTypeMasterRepo.findById(drug.getDrugUnitId());
                        if (genObj.isPresent())
                            obj.setUnitText(genObj.get().getTypeName());
                    }
                }
                obj.setStrength(drug.getStrength());
                obj.setCreatedBy(saveBrDetailsObject.getCreatedBy());
                obj.setUpdatedBy(saveBrDetailsObject.getUpdatedBy());
                obj.setDrugName(drug.getDrugName());
                obj.setQuantity(drug.getNoOfTablets());
                obj.setStatus(saveBrDetailsObject.getStatus());
                obj.setDrugType(drug.getDrugTypeId());

                drugRspList.add(obj);
            }

            this.beneficiaryRqtDrugsRepo.saveAll(drugRspList);
        }

        if (dto.getFileList() != null) {
            List<BeneficiaryAttachments> beneficiaryAttachList = new ArrayList<>();

            for (FileSaveRqtDto fileDto : dto.getFileList()) {
                BeneficiaryAttachments beneficiaryAttachObj = new BeneficiaryAttachments();

                if (saveBrDetailsObject != null) {
                    Optional<BeneficiaryRequestDetails> obj = this.beneficiaryRqtDetailsRepo.findById(saveBrDetailsObject.getRequestNumber());
                    beneficiaryAttachObj.setRequestNumber(obj.get());
                }
                beneficiaryAttachObj.setAttachmentType(fileDto.getAttachmentType());
                if (saveBrDetailsObject != null) {
                    Optional<UserMst> userObj = this.userRepo.findById(saveBrDetailsObject.getCreatedBy().getUserId());
                    beneficiaryAttachObj.setCreatedBy(userObj.get());
                }
                beneficiaryAttachObj.setFileName(fileDto.getFileName());
                beneficiaryAttachObj.setFilePath(this
                        .uploadOnRealPath(new FileDto(fileDto.getFilePath(), String.valueOf(saveBrDetailsObject.getRequestNumber()))));

                beneficiaryAttachList.add(beneficiaryAttachObj);
            }
            this.beneficiaryAttachmentRepo.saveAll(beneficiaryAttachList);
        }

//		if(dto.getRequestedPersonType()==25) {
//			String message = EmployeeConstants.dear+dto.getName()+"\n"+EmployeeConstants.initiating+"\n"+saveBrDetailsObject.getRequestNumber()+" on "+saveBrDetailsObject.getCreatedOn().toString()+"\n"+EmployeeConstants.govt;
//			sendsms(dto.getMobileNo().toString() , message, "1407172404908798481");
//		}else if(dto.getRequestedPersonType()==24) {
//			String message = EmployeeConstants.dear+dto.getName()+"\n"+EmployeeConstants.initiating+"\n"+saveBrDetailsObject.getRequestNumber()+" on "+saveBrDetailsObject.getCreatedOn().toString()+" through authorised person "+dto.getAuthorizedName()+"\n"+EmployeeConstants.govt; 
//			sendsms(dto.getMobileNo().toString() , message, "1407172404923710042");
//		}

        return saveBrDetailsObject.getRequestNumber();
    }

    public Object getPhysicianDrugFileHandler(String requestNo, String jwtToken) {
        UserMst user = this.loadUserByUsername(jwtToken);
        //System.out.println(user.getUserId());
        Integer role = userRoleMapRepo.findRoleIdByUserId(user.getUserId());

        BeneficiaryRequestDetails beneficiaryObj = this.beneficiaryRqtDetailsRepo.findByRequestNo(requestNo);
        if (beneficiaryObj == null)
            return "No such id exists!";
        Optional<BeneficiaryMst> beneficiaryMstObj = this.beneficiaryMstRepo.findById(beneficiaryObj.getBeneficiaryId().getId());
        System.out.println("Object is: " + beneficiaryObj);
        System.out.println("beneficiaryMstObj is: " + beneficiaryMstObj);
        if (beneficiaryObj != null) {
            PhysicianRspDto physicianRspDto = new PhysicianRspDto();
            System.out.println(role);
            System.out.println(EmployeeConstants.Medco_Role_Id);
            System.out.println(beneficiaryObj.getCreatedBy().getCreatedBy());
            System.out.println(user.getUserId());

            if (role.equals(EmployeeConstants.Medco_Role_Id)) {
                if (beneficiaryObj.getCreatedBy().getUserId().equals(user.getUserId())) {
//			physicianRspDto = beneficiaryToDtoMapper.beneficiaryMstToDto(beneficiaryMstObj.get());

                    physicianRspDto.setIndentId(requestNo);
                    physicianRspDto.setBeneficiaryId(beneficiaryMstObj.get().getId());
                    if (beneficiaryMstObj.isPresent()) {
                        physicianRspDto.setBeneficiaryName(beneficiaryMstObj.get().getBeneficiaryName());

                        if (beneficiaryMstObj.get().getBeneficiaryType() != null) {
                            physicianRspDto.setBeneficiaryDesignationId(beneficiaryMstObj.get().getBeneficiaryType().getTypeId());
                            Optional<GeneralTypeMst> generalDesignation = this.generalTypeMasterRepo.findById(beneficiaryMstObj.get().getBeneficiaryType().getTypeId());
                            if (generalDesignation != null)
                                physicianRspDto.setDesigntion(generalDesignation.get().getTypeName());
                        }
                        if (beneficiaryMstObj.get().getParentBeneficiaryId() != null) {
                            physicianRspDto.setEmpId(this.beneficiaryMstRepo.findById(beneficiaryMstObj.get().
                                    getParentBeneficiaryId()).get().getBeneficiaryId());
                            physicianRspDto.setEmpName(this.beneficiaryMstRepo.findById(beneficiaryMstObj.get().
                                    getParentBeneficiaryId()).get().getBeneficiaryName());
                        }
                        physicianRspDto.setPickupPersonName(beneficiaryObj.getPickupPersonName());
                        physicianRspDto.setPickupPersonMobileNo(beneficiaryObj.getPickupContactNo());
                        if (beneficiaryObj.getPickupRelation() != null) {
                            Optional<GeneralTypeMst> genObj = this.generalTypeMasterRepo.findById(beneficiaryObj.getPickupRelation().getTypeId());
                            if (genObj != null)
                                physicianRspDto.setPickupRelation(beneficiaryObj.getPickupRelation().getTypeId());
                        }
                        physicianRspDto.setBeneficiarymobileNo(beneficiaryMstObj.get().getBeneficiaryPhoneNo());
                        physicianRspDto.setBeneficiaryMobileAltNo(beneficiaryMstObj.get().getAlternateContactNumber());
                        physicianRspDto.setDob(beneficiaryMstObj.get().getBeneficiaryDob().toString());
                        physicianRspDto.setGenderId(beneficiaryMstObj.get().getGender().getTypeId());
                        Optional<GeneralTypeMst> generalGender = this.generalTypeMasterRepo.findById(beneficiaryMstObj.get().getGender().getTypeId());
                        if (generalGender != null)
                            physicianRspDto.setGender(generalGender.get().getTypeName());
                        physicianRspDto.setRelationId(beneficiaryMstObj.get().getRelation().getTypeId());
                        Optional<GeneralTypeMst> generalRelation = this.generalTypeMasterRepo.findById(beneficiaryMstObj.get().getRelation().getTypeId());
                        if (generalRelation != null)
                            physicianRspDto.setRelation(generalRelation.get().getTypeName());
                        physicianRspDto.setEmail(beneficiaryMstObj.get().getEmail());
                        physicianRspDto.setHouseNo(beneficiaryMstObj.get().getHouseNo());
                        if (beneficiaryMstObj.get().getLandmark() != null)
                            physicianRspDto.setLandmark(beneficiaryMstObj.get().getLandmark());
                        physicianRspDto.setLocality(beneficiaryMstObj.get().getLocality());
                        physicianRspDto.setDistrictId(beneficiaryMstObj.get().getDistrictId().getDistId());
                        Optional<DistrictMaster> district = this.districtRepo.findById(beneficiaryMstObj.get().getDistrictId().getDistId());
                        if (district != null)
                            physicianRspDto.setDistrict(district.get().getDistrictName());
                        physicianRspDto.setStateId(beneficiaryMstObj.get().getStateId().getStateId());
                        Optional<StateMaster> state = this.stateRepo.findById(beneficiaryMstObj.get().getStateId().getStateId());
                        if (district != null)
                            physicianRspDto.setState(state.get().getStateName());
                        physicianRspDto.setMandalId(beneficiaryMstObj.get().getMandalId().getMandalId());
                        Optional<MandalMaster> mandal = this.mandalRepo.findById(beneficiaryMstObj.get().getMandalId().getMandalId());
                        if (mandal != null)
                            physicianRspDto.setMandal(mandal.get().getMandalName());
                        physicianRspDto.setPincode(beneficiaryMstObj.get().getPincode());
                        physicianRspDto.setVillageId(beneficiaryMstObj.get().getVillageId().getVillageId());
                        Optional<VillageMaster> village = this.villageRepo.findById(beneficiaryMstObj.get().getVillageId().getVillageId());
                        if (village != null)
                            physicianRspDto.setVillage(village.get().getVillageName());
                    }
                    physicianRspDto.setPhysicianName(beneficiaryObj.getDoctorName());
                    physicianRspDto.setCrtBy(beneficiaryObj.getCreatedBy().getUserId());
                    physicianRspDto.setPhysicianEmpAltNo(beneficiaryObj.getEmpAltContact());
                    physicianRspDto.setPhysicianMobileNo(beneficiaryObj.getDoctorMobileNo());
                    physicianRspDto.setRemarks(beneficiaryObj.getRemarks());
                    physicianRspDto.setStatus(beneficiaryObj.getStatus().getStepRoleId());
                    physicianRspDto.setUptBy(beneficiaryObj.getUpdatedBy().getUserId());
                    if (beneficiaryObj.getAuthorizedPersonName() != null)
                        physicianRspDto.setAuthorizedPersonName(beneficiaryObj.getAuthorizedPersonName());
                    if (beneficiaryObj.getAuthorizedPersonNo() != null)
                        physicianRspDto.setAuthorizedPersonMobNo(beneficiaryObj.getAuthorizedPersonNo());
                    physicianRspDto.setRequestedPersonType(beneficiaryObj.getRequestedPersonType().getTypeId());
                    physicianRspDto.setShipmentType(beneficiaryObj.getShipmentType());

                    List<BeneficiaryRequestDrug> drugObj = this.beneficiaryRqtDrugsRepo.findByRequestNo(requestNo);
                    List<BeneficiaryRequestDrugAudit> drugAuditList = this.beneficiaryDrugAuditRepo.findRoleBycrtBy(requestNo);
                    List<DrugRspDto> drugList = new ArrayList<>();
                    for (BeneficiaryRequestDrug obj : drugObj) {
                        DrugRspDto drugDto = new DrugRspDto();

                        drugDto.setDrugId(obj.getId());
                        drugDto.setDrugName(obj.getDrugName());
                        drugDto.setRecommendedQuantity(obj.getQuantity());
                        drugDto.setStatus(obj.getStatus().getStepRoleId());
                        drugDto.setDrugType(obj.getDrugType());
                        drugDto.setStrength(obj.getStrength());
                        drugDto.setBrandName(obj.getBrandName());
                        drugDto.setDiscountedPrice(obj.getDiscountedPrice());
                        if (obj.getDrugType() != null) {
                            Optional<GeneralTypeMst> generalObj = this.generalTypeMasterRepo.findById(obj.getDrugType());
                            if (generalObj != null)
                                drugDto.setDrugTypeName(generalObj.get().getTypeName());
                        }
                        if (drugAuditList != null) {
                            for (BeneficiaryRequestDrugAudit drugAudit : drugAuditList) {
                                if (drugAudit.getId().equals(obj.getId()))
                                    drugDto.setIndentedQuantity(drugAudit.getQuantity());
                            }
                        }
                        if (obj.getPostalTrackingId() != null)
                            drugDto.setPostalId(obj.getPostalTrackingId());
                        if (obj.getDispatchDate() != null)
                            drugDto.setDispatchDate(obj.getDispatchDate().toString());
                        drugDto.setRemarks(obj.getRemarks());
                        if (obj.getBatchNo() != null)
                            drugDto.setBatchNo(obj.getBatchNo());
                        if (obj.getManufacturedDate() != null)
                            drugDto.setManufacturedDate(obj.getManufacturedDate().toString());
                        if (obj.getUnit() != null) {
                            drugDto.setDrugUnitId(obj.getUnit());
                            Optional<GeneralTypeMst> gen = this.generalTypeMasterRepo.findById(obj.getUnit());
                            if (gen.isPresent())
                                drugDto.setDrugUnitName(gen.get().getTypeName());
                        }
                        if (obj.getUnitText() != null)
                            drugDto.setDrugUnitName(obj.getUnitText());
                        if (obj.getExpiryDate() != null)
                            drugDto.setExpiryDate(obj.getExpiryDate().toString());
                        if (obj.getMrp() != null)
                            drugDto.setMrp(obj.getMrp());
                        if (obj.getIsAcknowledged() != null)
                            drugDto.setIsAcknowledged(obj.getIsAcknowledged());
                        if (obj.getIsShipped() != null)
                            drugDto.setIsShipped(obj.getIsShipped());
                        drugList.add(drugDto);
                    }
                    physicianRspDto.setDrugList(drugList);

                    List<BeneficiaryAttachments> attachObj = this.beneficiaryAttachmentRepo.findByRequestNo(requestNo);
                    List<FileRspDto> fileList = new ArrayList<>();
                    if (!attachObj.isEmpty()) {
                        for (BeneficiaryAttachments obj : attachObj) {
                            FileRspDto fileDto = new FileRspDto();

                            fileDto.setAttachmentType(obj.getAttachmentType());
                            fileDto.setFileName(obj.getFileName());
                            fileDto.setFilePath(obj.getFilePath());

                            fileList.add(fileDto);
                        }
                    }
                    physicianRspDto.setFileList(fileList);

                    return physicianRspDto;
                } else {
                    return null;
                }

            } else {
                physicianRspDto.setIndentId(requestNo);
                physicianRspDto.setBeneficiaryId(beneficiaryMstObj.get().getId());
                if (beneficiaryMstObj.isPresent()) {
                    physicianRspDto.setBeneficiaryName(beneficiaryMstObj.get().getBeneficiaryName());

                    if (beneficiaryMstObj.get().getBeneficiaryType() != null) {
                        physicianRspDto.setBeneficiaryDesignationId(beneficiaryMstObj.get().getBeneficiaryType().getTypeId());
                        Optional<GeneralTypeMst> generalDesignation = this.generalTypeMasterRepo.findById(beneficiaryMstObj.get().getBeneficiaryType().getTypeId());
                        if (generalDesignation != null)
                            physicianRspDto.setDesigntion(generalDesignation.get().getTypeName());
                    }
                    if (beneficiaryMstObj.get().getParentBeneficiaryId() != null) {
                        physicianRspDto.setEmpId(this.beneficiaryMstRepo.findById(beneficiaryMstObj.get().
                                getParentBeneficiaryId()).get().getBeneficiaryId());
                        physicianRspDto.setEmpName(this.beneficiaryMstRepo.findById(beneficiaryMstObj.get().
                                getParentBeneficiaryId()).get().getBeneficiaryName());
                    }
                    physicianRspDto.setPickupPersonName(beneficiaryObj.getPickupPersonName());
                    physicianRspDto.setPickupPersonMobileNo(beneficiaryObj.getPickupContactNo());
                    if (beneficiaryObj.getPickupRelation() != null) {
                        Optional<GeneralTypeMst> genObj = this.generalTypeMasterRepo.findById(beneficiaryObj.getPickupRelation().getTypeId());
                        if (genObj != null)
                            physicianRspDto.setPickupRelation(beneficiaryObj.getPickupRelation().getTypeId());
                    }
                    physicianRspDto.setBeneficiarymobileNo(beneficiaryMstObj.get().getBeneficiaryPhoneNo());
                    physicianRspDto.setBeneficiaryMobileAltNo(beneficiaryMstObj.get().getAlternateContactNumber());
                    physicianRspDto.setDob(beneficiaryMstObj.get().getBeneficiaryDob().toString());
                    physicianRspDto.setGenderId(beneficiaryMstObj.get().getGender().getTypeId());
                    Optional<GeneralTypeMst> generalGender = this.generalTypeMasterRepo.findById(beneficiaryMstObj.get().getGender().getTypeId());
                    if (generalGender != null)
                        physicianRspDto.setGender(generalGender.get().getTypeName());
                    physicianRspDto.setRelationId(beneficiaryMstObj.get().getRelation().getTypeId());
                    Optional<GeneralTypeMst> generalRelation = this.generalTypeMasterRepo.findById(beneficiaryMstObj.get().getRelation().getTypeId());
                    if (generalRelation != null)
                        physicianRspDto.setRelation(generalRelation.get().getTypeName());
                    physicianRspDto.setEmail(beneficiaryMstObj.get().getEmail());
                    physicianRspDto.setHouseNo(beneficiaryMstObj.get().getHouseNo());
                    if (beneficiaryMstObj.get().getLandmark() != null)
                        physicianRspDto.setLandmark(beneficiaryMstObj.get().getLandmark());
                    physicianRspDto.setLocality(beneficiaryMstObj.get().getLocality());
                    physicianRspDto.setDistrictId(beneficiaryMstObj.get().getDistrictId().getDistId());
                    Optional<DistrictMaster> district = this.districtRepo.findById(beneficiaryMstObj.get().getDistrictId().getDistId());
                    if (district != null)
                        physicianRspDto.setDistrict(district.get().getDistrictName());
                    physicianRspDto.setStateId(beneficiaryMstObj.get().getStateId().getStateId());
                    Optional<StateMaster> state = this.stateRepo.findById(beneficiaryMstObj.get().getStateId().getStateId());
                    if (district != null)
                        physicianRspDto.setState(state.get().getStateName());
                    physicianRspDto.setMandalId(beneficiaryMstObj.get().getMandalId().getMandalId());
                    Optional<MandalMaster> mandal = this.mandalRepo.findById(beneficiaryMstObj.get().getMandalId().getMandalId());
                    if (mandal != null)
                        physicianRspDto.setMandal(mandal.get().getMandalName());
                    physicianRspDto.setPincode(beneficiaryMstObj.get().getPincode());
                    physicianRspDto.setVillageId(beneficiaryMstObj.get().getVillageId().getVillageId());
                    Optional<VillageMaster> village = this.villageRepo.findById(beneficiaryMstObj.get().getVillageId().getVillageId());
                    if (village != null)
                        physicianRspDto.setVillage(village.get().getVillageName());
                }
                physicianRspDto.setPhysicianName(beneficiaryObj.getDoctorName());
                physicianRspDto.setCrtBy(beneficiaryObj.getCreatedBy().getUserId());
                physicianRspDto.setPhysicianEmpAltNo(beneficiaryObj.getEmpAltContact());
                physicianRspDto.setPhysicianMobileNo(beneficiaryObj.getDoctorMobileNo());
                physicianRspDto.setRemarks(beneficiaryObj.getRemarks());
                physicianRspDto.setStatus(beneficiaryObj.getStatus().getStepRoleId());
                physicianRspDto.setUptBy(beneficiaryObj.getUpdatedBy().getUserId());
                if (beneficiaryObj.getAuthorizedPersonName() != null)
                    physicianRspDto.setAuthorizedPersonName(beneficiaryObj.getAuthorizedPersonName());
                if (beneficiaryObj.getAuthorizedPersonNo() != null)
                    physicianRspDto.setAuthorizedPersonMobNo(beneficiaryObj.getAuthorizedPersonNo());
                physicianRspDto.setRequestedPersonType(beneficiaryObj.getRequestedPersonType().getTypeId());
                physicianRspDto.setShipmentType(beneficiaryObj.getShipmentType());

                List<BeneficiaryRequestDrug> drugObj = this.beneficiaryRqtDrugsRepo.findByRequestNo(requestNo);
                List<BeneficiaryRequestDrugAudit> drugAuditList = this.beneficiaryDrugAuditRepo.findRoleBycrtBy(requestNo);
                List<DrugRspDto> drugList = new ArrayList<>();
                for (BeneficiaryRequestDrug obj : drugObj) {
                    DrugRspDto drugDto = new DrugRspDto();

                    drugDto.setDrugId(obj.getId());
                    drugDto.setDrugName(obj.getDrugName());
                    drugDto.setRecommendedQuantity(obj.getQuantity());
                    drugDto.setStatus(obj.getStatus().getStepRoleId());
                    drugDto.setDrugType(obj.getDrugType());
                    drugDto.setStrength(obj.getStrength());
                    drugDto.setBrandName(obj.getBrandName());
                    drugDto.setDiscountedPrice(obj.getDiscountedPrice());
                    if (obj.getDrugType() != null) {
                        Optional<GeneralTypeMst> generalObj = this.generalTypeMasterRepo.findById(obj.getDrugType());
                        if (generalObj != null)
                            drugDto.setDrugTypeName(generalObj.get().getTypeName());
                    }
                    if (drugAuditList != null) {
                        for (BeneficiaryRequestDrugAudit drugAudit : drugAuditList) {
                            if (drugAudit.getId().equals(obj.getId()))
                                drugDto.setIndentedQuantity(drugAudit.getQuantity());
                        }
                    }
                    if (obj.getPostalTrackingId() != null)
                        drugDto.setPostalId(obj.getPostalTrackingId());
                    if (obj.getDispatchDate() != null)
                        drugDto.setDispatchDate(obj.getDispatchDate().toString());
                    drugDto.setRemarks(obj.getRemarks());
                    if (obj.getBatchNo() != null)
                        drugDto.setBatchNo(obj.getBatchNo());
                    if (obj.getManufacturedDate() != null)
                        drugDto.setManufacturedDate(obj.getManufacturedDate().toString());
                    if (obj.getUnit() != null) {
                        drugDto.setDrugUnitId(obj.getUnit());
                        Optional<GeneralTypeMst> gen = this.generalTypeMasterRepo.findById(obj.getUnit());
                        if (gen.isPresent())
                            drugDto.setDrugUnitName(gen.get().getTypeName());
                    }
                    if (obj.getUnitText() != null)
                        drugDto.setDrugUnitName(obj.getUnitText());
                    if (obj.getExpiryDate() != null)
                        drugDto.setExpiryDate(obj.getExpiryDate().toString());
                    if (obj.getMrp() != null)
                        drugDto.setMrp(obj.getMrp());
                    if (obj.getIsAcknowledged() != null)
                        drugDto.setIsAcknowledged(obj.getIsAcknowledged());
                    if (obj.getIsShipped() != null)
                        drugDto.setIsShipped(obj.getIsShipped());
                    drugList.add(drugDto);
                }
                physicianRspDto.setDrugList(drugList);

                List<BeneficiaryAttachments> attachObj = this.beneficiaryAttachmentRepo.findByRequestNo(requestNo);
                List<FileRspDto> fileList = new ArrayList<>();
                if (!attachObj.isEmpty()) {
                    for (BeneficiaryAttachments obj : attachObj) {
                        FileRspDto fileDto = new FileRspDto();

                        fileDto.setAttachmentType(obj.getAttachmentType());
                        fileDto.setFileName(obj.getFileName());
                        fileDto.setFilePath(obj.getFilePath());

                        fileList.add(fileDto);
                    }
                }
                physicianRspDto.setFileList(fileList);

                return physicianRspDto;
            }

        }
        return null;
    }

    //workflow method
    public ApiResponse2<Object> getStepAction(String requestNumber) {
        try {
            Optional<BeneficiaryRequestDetails> medicalCase = beneficiaryRqtDetailsRepo.findById(requestNumber);
            if (!medicalCase.isPresent()) {
                return new ApiResponse2<>(true, "No medical case found", Collections.emptyList(), HttpStatus.OK.value());
            }

            Query query = em.createNativeQuery("select medical.next_step_and_actions_determination(1,'" + requestNumber + "')");
            List<String> result = query.getResultList();

            if (result.isEmpty()) {
                return new ApiResponse2<>(true, "No action found", Collections.emptyList(), HttpStatus.OK.value());
            }

            HashMap<Integer, Integer> stepAction = getNextStepByAction(result.get(0));
            List<StepActionResponse> stepAndAction = getStepsWithAction(stepAction);

            if (stepAndAction.isEmpty()) {
                return new ApiResponse2<>(true, "No action found", stepAndAction, HttpStatus.OK.value());
            }

            return new ApiResponse2<>(true, ApiResponseStatus.fetch, stepAndAction, HttpStatus.OK.value());
        } catch (Exception e) {
            return new ApiResponse2<>(false, ApiResponseStatus.exception, null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private List<StepActionResponse> getStepsWithAction(HashMap<Integer, Integer> stepAction) {
        List<StepActionResponse> response = new ArrayList<>();
        for (Entry<Integer, Integer> entry : stepAction.entrySet()) {
            StepActionResponse stepActionResponse = new StepActionResponse();

            Optional<ActionMst> action = actionRepo.findById(entry.getValue());
            if (!action.isPresent()) {
                return Collections.emptyList();
            }

            stepActionResponse.setActionId(action.get().getActionId());
            stepActionResponse.setAction(action.get().getActionDesc());

            System.out.println("Entry key :" + entry.getKey());
            Optional<StepMaster> step = stepMasterRepo.findById(entry.getKey());
            System.out.println("Step Obj: " + step);
            if (step.isPresent()) {
                stepActionResponse.setStepId(step.get().getStepId());
                stepActionResponse.setStepName(step.get().getStepName());
            }

            response.add(stepActionResponse);
        }
        return response;
    }

    private HashMap<Integer, Integer> getNextStepByAction(String result) {
        HashMap<Integer, Integer> actionStep = new HashMap<>();
        String[] pairs = result.split(",");
        for (String pair : pairs) {
            String[] keyVal = pair.split(":");
            int key = Integer.parseInt(keyVal[0].replaceAll("[^0-9]", ""));
            int value = Integer.parseInt(keyVal[1].replaceAll("[^0-9]", ""));
            actionStep.put(key, value);
        }
        return actionStep;
    }

    public static LocalDate dateConversion(String dob) throws java.text.ParseException {
        return LocalDate.parse(dob, DATE_FOMATTER);
    }

    //Dropdown APIs
    public ApiResponse2<Object> districtList(Integer stateId) {
        List<DropdownDataDto> response = districtRepo.findDistIdAndDistrictNameByStateId(stateId);
        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());
    }

    public ApiResponse2<Object> mandalList(Integer districtId) {
        List<DropdownDataDto> response = mandalRepo.findMandalByDistId(districtId);
        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());
    }

    public ApiResponse2<Object> villageList(Integer mandalId) {
        List<DropdownDataDto> response = villageRepo.findVillageByMandalId(mandalId);
        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());
    }

    public ApiResponse2<Object> independentDropdown() {
        Map<String, List<DropdownDataDto>> response = new TreeMap<>();
        response.put("designation", generalTypeMasterRepo.findTypeIdAndTypeNameByTypeDescAndIsActive(EmployeeConstants.FETCH_DESIGNATION));
        response.put("relation", generalTypeMasterRepo.findTypeIdAndTypeNameByTypeDescAndIsActive(EmployeeConstants.FETCH_RELATION));
        response.put("gender", generalTypeMasterRepo.findTypeIdAndTypeNameByTypeDescAndIsActive(EmployeeConstants.FETCH_GENDER));
        response.put("shipmentType", generalTypeMasterRepo.findTypeIdAndTypeNameByTypeDescAndIsActive(EmployeeConstants.FETCH_SHIPMENT_TYPE));
        response.put("stateList", stateRepo.findStateIdAndStateNameByIsStateActive());
        response.put("drugType", generalTypeMasterRepo.findTypeIdAndTypeNameByTypeDescAndIsActive(EmployeeConstants.FETCH_DRUG_TYPE));
        response.put("requestedPersonType", generalTypeMasterRepo.findTypeIdAndTypeNameByTypeDescAndIsActive(EmployeeConstants.FETCH_PERSON_TYPE));
        response.put("drugUnit", generalTypeMasterRepo.findDrugUnit(EmployeeConstants.FETCH_DRUG_UNITS));
        response.put("pickUpRelation", generalTypeMasterRepo.findByRelationId(EmployeeConstants.FETCH_PICKUP_RELATION));
        response.put("drugs", drugRepo.findDrugIdAndDrugName());
        
        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());
    }

    public ApiResponse2<Object> getParentBeneficiaryWithChildren(EmployeeDetailsDto empDto) {
        List<BeneficiaryMst> beneficiaries = new ArrayList<>();
        BeneficiaryMst parentBeneficiary = beneficiaryMstRepo.findParentBeneficiaryByBeneficiaryId(empDto.getEmpId(), empDto.getEmpMobileNo());
        if (parentBeneficiary != null) {
            beneficiaries.add(parentBeneficiary);
            List<BeneficiaryMst> childBeneficiaries = beneficiaryMstRepo.findChildBeneficiariesByParentId(parentBeneficiary.getId());
            beneficiaries.addAll(childBeneficiaries);
        }

        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, beneficiaries, HttpStatus.OK.value());
    }

    public ApiResponse2<Object> getInbox(Integer userId, Authentication authentication) {
        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());
        List<InboxResponseDto> response = new ArrayList<>();
        Integer role = userRoleMapRepo.findRoleIdByUserId(authenticatedUser.getUserId());
        if (role == EmployeeConstants.Medco_Role_Id) {
            List<InboxResponseDto> respon = beneficiaryRqtDetailsRepo.getInboxDataMedco(authenticatedUser.getUserId());
            response.addAll(respon);
        } else {
            List<InboxResponseDto> respon = beneficiaryRqtDetailsRepo.getInboxData(role);
            response.addAll(respon);
        }
        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());

    }

    public static Date convertString(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try {
            d = dateFormat.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public ApiResponse2<Object> getStatus(String reqNo, String jwtToken) {


        UserMst user = this.loadUserByUsername(jwtToken);


        Integer role = userRoleMapRepo.findRoleIdByUserId(user.getUserId());


        BeneficiaryRequestDetails beneficiaryObj = this.beneficiaryRqtDetailsRepo.findByRequestNo(reqNo);

        if (beneficiaryObj != null) {

            if (role.equals(EmployeeConstants.Medco_Role_Id)) {

                if (beneficiaryObj.getCreatedBy().getUserId().equals(user.getUserId())) {

                    List<RemarksOutputDto> response = auditRepo.getAudit(reqNo);


                    if (response != null && !response.isEmpty()) {
                        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());
                    } else {
                        return new ApiResponse2<>(true, EmployeeConstants.NO_RECORDS, response, HttpStatus.OK.value());
                    }
                } else {

                    return new ApiResponse2<>(false, "Unauthorized Access", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
            } else {
                // For non-Medco users
                List<RemarksOutputDto> response = auditRepo.getAudit(reqNo);


                if (response != null && !response.isEmpty()) {
                    return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());
                } else {
                    return new ApiResponse2<>(true, EmployeeConstants.NO_RECORDS, response, HttpStatus.OK.value());
                }
            }
        } else {

            return new ApiResponse2<>(false, "Request not found", null, HttpStatus.NOT_FOUND.value());
        }
    }


    public ApiResponse2<Object> getCount(Integer userId) {
        Map<String, List<CountResponseDto>> response = new LinkedHashMap<>();
        Integer role = userRoleMapRepo.findRoleIdByUserId(userId);
        if (role == EmployeeConstants.Medco_Role_Id) {
            response.put("MedcoInitiatedcount", beneficiaryRqtDetailsRepo.getMedcoInitiatedCount(userId));
            response.put("MedcoApprovedcount", beneficiaryRqtDetailsRepo.getMedcoApprovedCount(userId));
        } else if (role == EmployeeConstants.Medical_officer_Role_Id) {
            response.put("RequestIntitiatedCount", beneficiaryRqtDetailsRepo.getMORequestInitiatedCount());
            response.put("RequestAppByMOCount", beneficiaryRqtDetailsRepo.getMORequestApprovedCount());
            response.put("RequestDispatchedByVendorCount", beneficiaryRqtDetailsRepo.getMORequestDispatchedToPharmacistCount());
            response.put("RequestDispatchedToBeneficiaryCount", beneficiaryRqtDetailsRepo.getMORequestDispatchedToBeneficiaryCount());
            response.put("DeliveredToBeneCount", beneficiaryRqtDetailsRepo.getMORequestDeliveredCount());
        } else if (role == EmployeeConstants.Vendor_Role_Id) {
            response.put("VendorRequestRaisedCount", beneficiaryRqtDetailsRepo.getVendorRequestsRaisedCount());
            response.put("VendorRequestDispatchedCount", beneficiaryRqtDetailsRepo.getVendorRequestsDispatchedCount());
            response.put("VendorRequestDeliveredCount", beneficiaryRqtDetailsRepo.getVendorRequestsDeliveredCount());
        } else {
            response.put("PharmaRequestIniCount", beneficiaryRqtDetailsRepo.getPharmacistRequestInitiatedCount(userId));
            response.put("RequestAppByMOCount", beneficiaryRqtDetailsRepo.getPharmacistRequestApprovedCount(userId));
            response.put("RequestPendingWithVendorCount", beneficiaryRqtDetailsRepo.getPharmacistRequestApprovedCount(userId));
            response.put("RequestDispatchedByVendorCount", beneficiaryRqtDetailsRepo.getPharmacistRequestDispatchedToPharmacistCount());
            response.put("RequestDispatchedToBeneCount", beneficiaryRqtDetailsRepo.getPharmacistRequestDispatchedToBeneficiaryCount());
            response.put("RequestDeliveredCount", beneficiaryRqtDetailsRepo.getPharmacistRequestDeliveredCount());
        }
        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());
    }

    public ApiResponse2<Object> getDashboardTable(FilterRequestDto filDto, Authentication authentication) {

        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());

        List<DashboardTableResponse> response = new ArrayList<>();
        Integer role = userRoleMapRepo.findRoleIdByUserId(authenticatedUser.getUserId());
        if (role == EmployeeConstants.Medco_Role_Id && filDto.getStatus() == 1) {
            List<DashboardTableResponse> medcoIni = beneficiaryRqtDetailsRepo.getMedcoInitiatedDetails(authenticatedUser.getUserId(), filDto.getStatus(), filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(medcoIni);
        } else if (role == EmployeeConstants.Medco_Role_Id && filDto.getStatus() == 3) {
            List<DashboardTableResponse> medcoApp = beneficiaryRqtDetailsRepo.getMedcoApprovedDetails(authenticatedUser.getUserId(), filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(medcoApp);
        } else if (role == EmployeeConstants.Medical_officer_Role_Id && filDto.getStatus() == 1) {
            List<DashboardTableResponse> moini = beneficiaryRqtDetailsRepo.getMORequestInitiatedDetails(filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(moini);
        } else if (role == EmployeeConstants.Medical_officer_Role_Id && filDto.getStatus() == 3) {
            List<DashboardTableResponse> moapp = beneficiaryRqtDetailsRepo.getMORequestApprovedDetails(filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(moapp);
        } else if (role == EmployeeConstants.Medical_officer_Role_Id && filDto.getStatus() == 7) {
            List<DashboardTableResponse> modisVen = beneficiaryRqtDetailsRepo.getMORequestDispatchedToPharmacistDetails(filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(modisVen);
        } else if (role == EmployeeConstants.Medical_officer_Role_Id && filDto.getStatus() == 15) {
            List<DashboardTableResponse> modis = beneficiaryRqtDetailsRepo.getMORequestDispatchedToBeneficiaryDetails(filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(modis);
        } else if (role == EmployeeConstants.Medical_officer_Role_Id && filDto.getStatus() == 17) {
            List<DashboardTableResponse> model = beneficiaryRqtDetailsRepo.getMORequestDeliveredDetails(filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(model);
        } else if (role == EmployeeConstants.Vendor_Role_Id && filDto.getStatus() == 3) {
            List<DashboardTableResponse> venapp = beneficiaryRqtDetailsRepo.getVendorRequestsRaisedDetails(filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(venapp);
        } else if (role == EmployeeConstants.Vendor_Role_Id && filDto.getStatus() == 15) {
            List<DashboardTableResponse> vendis = beneficiaryRqtDetailsRepo.getVendorRequestsDispatchedDetails(filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(vendis);
        } else if (role == EmployeeConstants.Vendor_Role_Id && filDto.getStatus() == 17) {
            List<DashboardTableResponse> vendeli = beneficiaryRqtDetailsRepo.getVendorRequestsDeliveredDetails(filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(vendeli);
        } else if (role == EmployeeConstants.Pharma_Role_Id && filDto.getStatus() == 2) {
            List<DashboardTableResponse> pharini = beneficiaryRqtDetailsRepo.getPharmacistRequestInitiatedDetails(authenticatedUser.getUserId(), filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(pharini);
        } else if (role == EmployeeConstants.Pharma_Role_Id && filDto.getStatus() == 5) {
            List<DashboardTableResponse> pharapp = beneficiaryRqtDetailsRepo.getPharmacistRequestApprovedDetails(authenticatedUser.getUserId(), filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(pharapp);
        } else if (role == EmployeeConstants.Pharma_Role_Id && filDto.getStatus() == 7) {
            List<DashboardTableResponse> phardisven = beneficiaryRqtDetailsRepo.getPharmacistRequestDispatchedToPharmacistDetails(filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(phardisven);
        } else if (role == EmployeeConstants.Pharma_Role_Id && filDto.getStatus() == 15) {
            List<DashboardTableResponse> phardisbene = beneficiaryRqtDetailsRepo.getPharmacistRequestDispatchedToBeneficiaryDetails(filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(phardisbene);
        } else if (role == EmployeeConstants.Pharma_Role_Id && filDto.getStatus() == 17) {
            List<DashboardTableResponse> phardele = beneficiaryRqtDetailsRepo.getPharmacistRequestDeliveredDetails(filDto.getRequestNumber(), filDto.getBeneficiaryId(), filDto.getStartDate(), filDto.getEndDate());
            response.addAll(phardele);
        }
        //	if(response != null) {
        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());
//	    	}
//	    	
//	    	return new ApiResponse2<>(true, EmployeeConstants.NO_RECORDS, response, HttpStatus.OK.value());

    }

    @SuppressWarnings("unused")
    public ApiResponse2<Object> takeAction(UpdateStatusDto actDto, Authentication authentication) throws Exception {

        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());
        Integer roleId = userRoleMapRepo.findRoleIdByUserId(authenticatedUser.getUserId());

//        RoleMst role = roleRepo.findByRoleId(roleId);

        BeneficiaryRequestDetails model = beneficiaryRqtDetailsRepo.findByRequestNo(actDto.getRequestNumber());
        StepRoleMapping statusId = stepRoleRepo.findByStepRoleId(model.getStatus().getStepRoleId());

        System.out.println(statusId.getRoleId().getRoleId());
        System.out.println(roleId);
        System.out.println(model.getStatus().getStepRoleId());

        if ((statusId.getRoleId().getRoleId()) == roleId) {

            Query query = em.createNativeQuery("select medical.next_step_and_actions_determination(1,'" + actDto.getRequestNumber() + "')");
            List<String> result = query.getResultList();
            if (result.get(0) == null || result.get(0).equals("")) {
                return new ApiResponse2<>(false, "Required action already taken for this case", null, HttpStatus.OK.value());
            }

//        HashMap<Integer, Integer> stepAction = getNextStepByAction(result.get(0));
//        Optional<ActionMst> action = actionRepo.findById(actDto.getActionId());
//        for (Entry<Integer, Integer> entry : stepAction.entrySet()) {
//            if (entry.getValue() == (long) (action.get().getActionId())) {
//                StepRoleMapping status = new StepRoleMapping();
//                status.setStepRoleId(entry.getKey().longValue());
//                if (status == null) {
//                    return new ApiResponse2<>(false, "Step not available", null, HttpStatus.OK.value());
//                }
//
//                model.setStatus(status);
//            }
//        }
            model.setRemarks(actDto.getRemarks());
            if (actDto.getPickupMobileNo() != null)
                model.setPickupContactNo(Long.parseLong(actDto.getPickupMobileNo()));
            if (actDto.getPickupName() != null)
                model.setPickupPersonName(actDto.getPickupName());
            if (actDto.getPickupRelationId() != null) {
                Optional<GeneralTypeMst> gen = this.generalTypeMasterRepo.findById(actDto.getPickupRelationId());
                if (gen.isPresent()) {
                    model.setPickupRelation(gen.get());
                    if (gen.get().getTypeId() == 57)
                        model.setPickupRelationName(gen.get().getTypeName());
                    else if (gen.get().getTypeId() == 58) {
                        if (actDto.getPickupRelationName() != null)
                            model.setPickupRelationName(actDto.getPickupRelationName());
                    }

                }
            }
            BeneficiaryMst benfObj = beneficiaryRqtDetailsRepo.findBene(actDto.getRequestNumber());
            int otp = 0;
//					if(actDto.getShipmentType()==18) {
//						otp = otpUtil.generateOTP();
//						
//						BeneficiaryOtpValidation beneficiaryOtp = new BeneficiaryOtpValidation();
//						
//						String message = EmployeeConstants.dear+benfObj.getBeneficiaryName()+"\n"+EmployeeConstants.shipmentTypePickUp+actDto.getRequestNumber()+" "+String.valueOf(otp)+".\n"+EmployeeConstants.govt;
//						sendsms(benfObj.getBeneficiaryPhoneNo().toString() , message, "1407172405003614055");
//					}else if(actDto.getShipmentType()==19) {	
//						String message = EmployeeConstants.dear+benfObj.getBeneficiaryName()+"\n"+"Postal tracking ID "+actDto.getPostalTrackingId()+" for your medicines with indent no.:"+actDto.getRequestNumber()+". "+EmployeeConstants.postal+"\n\n"+EmployeeConstants.govt;	
//						sendsms(benfObj.getBeneficiaryPhoneNo().toString() , message, "1407172405052774978");
//					}
            model.setShipmentType(actDto.getShipmentType());
            if (actDto.getAuthorizationLetter() != null) {
                BeneficiaryAttachments uploadAuthorizedLetter = new BeneficiaryAttachments();

                uploadAuthorizedLetter.setAttachmentType(actDto.getAuthorizationLetter().getAttachmentType());
                uploadAuthorizedLetter.setFileName(actDto.getAuthorizationLetter().getFileName());
                uploadAuthorizedLetter.setRequestNumber(model);
                Optional<UserMst> user = this.userRepo.findById(authenticatedUser.getUserId());
                if (user != null)
                    uploadAuthorizedLetter.setCreatedBy(user.get());
                uploadAuthorizedLetter.setFilePath(this.uploadOnRealPath(
                        new FileDto(actDto.getAuthorizationLetter().getFilePath(), String.valueOf(model.getRequestNumber()))));

                this.beneficiaryAttachmentRepo.save(uploadAuthorizedLetter);
            }
            UserMst updBy = userRepo.findById(authenticatedUser.getUserId()).orElse(null);
            if (updBy == null) {
                return new ApiResponse2<>(false, "User not found", null, HttpStatus.OK.value());
            }
            model.setUpdatedBy(updBy);

            BeneficiaryRequestDetails requestDetailsObj = this.beneficiaryRqtDetailsRepo.save(model);
//					if(requestDetailsObj.getStatus().getStepId().getStepId() ==9 || requestDetailsObj.getStatus().getStepId().getStepId()==11) {
//						String message = EmployeeConstants.dear+benfObj.getBeneficiaryName()+"\n"+"Your medicines with Indent No:"+actDto.getRequestNumber()+" has received at Civil Dispensary Dt:"+requestDetailsObj.getUpdatedOn()+"\n"+EmployeeConstants.govt;
//						sendsms(benfObj.getBeneficiaryPhoneNo().toString() , message, "1407172404978760682");
//					}

            String dispatchId = this.beneficiaryRqtDrugsRepo.generateDisptachId();
            if (!actDto.getDrugList().isEmpty()) {
                for (VendorDrugDetailDto drug : actDto.getDrugList()) {

                    Optional<BeneficiaryRequestDrug> drugObj = this.beneficiaryRqtDrugsRepo.findById(drug.getDrugId());
                    drugObj.get().setStatus(requestDetailsObj.getStatus());
                    if (drug.getDrugName() != null)
                        drugObj.get().setDrugName(drug.getDrugName());
                    if (drug.getBatchNo() != null)
                        drugObj.get().setBatchNo(drug.getBatchNo());

                    LocalDate today = LocalDate.now();

                    if (drug.getManufacturedDate() != null) {

                        LocalDate inputDate = convertString(drug.getManufacturedDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate oneYearBack = today.minusYears(1);
                        LocalDate exactOneYear = oneYearBack.minusDays(1);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String formattedDate = exactOneYear.format(formatter);
                        if (inputDate.compareTo(exactOneYear) >= 0 && inputDate.compareTo(today) <= 0) {
                            drugObj.get().setManufacturedDate(convertString(drug.getManufacturedDate()));
                        } else {
                            return new ApiResponse2<>(false, "Manufacturing date should be between current date and 1yr before current date", drug.getManufacturedDate(), HttpStatus.OK.value());
                        }
                    }

                    if (drug.getExpiryDate() != null) {
                        LocalDate expiryDate = convertString(drug.getExpiryDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate afterThreeMonths = today.plusMonths(3);
                        if (expiryDate.compareTo(afterThreeMonths) >= 0) {
                            drugObj.get().setExpiryDate(convertString(drug.getExpiryDate()));
                        } else {
                            return new ApiResponse2<>(false, "Expiry date should be more than 3 months from current date", drug.getExpiryDate(), HttpStatus.OK.value());
                        }
                    }

                    if (drug.getMrp() != null) {
                        drugObj.get().setMrp(drug.getMrp());
                        drugObj.get().setDiscountedPrice((int) (drug.getMrp() - ((DISCOUNT_PERCENTAGE * drug.getMrp()) / 100)));
                    }
                    if (drug.getRecommendedQuantity() != null) {
                        if (drug.getRecommendedQuantity() <= drugObj.get().getQuantity()) {
                            drugObj.get().setQuantity(drug.getRecommendedQuantity());
                        } else {
                            return new ApiResponse2<>(false, "Recommended quantity should be less than or equal to quantity", drugObj.get().getQuantity(), HttpStatus.OK.value());
                        }
                    }
                    if (drug.getRemarks() != null)
                        drugObj.get().setRemarks(drug.getRemarks());
                    if (requestDetailsObj.getStatus() != null)
                        drugObj.get().setStatus(requestDetailsObj.getStatus());
                    if (drug.getDrugType() != null)
                        drugObj.get().setDrugType(drug.getDrugType());
                    if (drug.getIsShipped() != null)
                        drugObj.get().setIsShipped(drug.getIsShipped());
                    if (drug.getIsAcknowledged() != null)
                        drugObj.get().setIsAcknowledged(drug.getIsAcknowledged());
                    if (drug.getStrength() != null)
                        drugObj.get().setStrength(drug.getStrength());
                    if (drug.getBrandName() != null)
                        drugObj.get().setBrandName(drug.getBrandName());
//                if (drug.getDiscountedPrice() != null)

                    UserMst updatedBy = new UserMst();
                    updatedBy.setUserId(authenticatedUser.getUserId());
                    drugObj.get().setUpdatedBy(updatedBy);
                    drugObj.get().setDispatchId(dispatchId);
                    drugObj.get().setDispatchDate(new Date());
                    drugObj.get().setPostalTrackingId(actDto.getPostalTrackingId());

                    if (actDto.getShipmentType() != null) {
                        if (actDto.getShipmentType() == 18) {
                            BeneficiaryOtpValidation beneOtp = new BeneficiaryOtpValidation();
                            if (drugObj.get() != null) {
                                beneOtp.setDrug(drugObj.get());
                                beneOtp.setUser(model.getCreatedBy());
                                beneOtp.setOtp(otp);
                            }
                            this.beneficiaryOtpValidationRepo.save(beneOtp);
                        }
                    }
                    this.beneficiaryRqtDrugsRepo.save(drugObj.get());
                }
            }
            if (actDto.getVendorFileAttachment() != null) {
                Optional<BeneficiaryAttachments> vendorAttach = this.beneficiaryAttachmentRepo.
                        findByRequestNAndAttachType(actDto.getRequestNumber(),
                                actDto.getVendorFileAttachment().getAttachmentType());
                if (vendorAttach.isPresent()) {
                    vendorAttach.get().setFileName(actDto.getVendorFileAttachment().getFileName());
                    vendorAttach.get().setFilePath(this
                            .uploadOnRealPath(new FileDto(actDto.getVendorFileAttachment().getFilePath(),
                                    String.valueOf(requestDetailsObj.getRequestNumber()))));
                } else {
                    vendorAttach = Optional.of(new BeneficiaryAttachments());
                    vendorAttach.get().setAttachmentType(actDto.getVendorFileAttachment().getAttachmentType());
                    vendorAttach.get().setFileName(actDto.getVendorFileAttachment().getFileName());
                    vendorAttach.get().setFilePath(
                            this.uploadOnRealPath(new FileDto(actDto.getVendorFileAttachment().getFilePath(),
                                    String.valueOf(requestDetailsObj.getRequestNumber()))));
                }
                UserMst updatedBy = new UserMst();
                updatedBy.setUserId(authenticatedUser.getUserId());
                Optional<UserMst> userObj = this.userRepo.findById(updatedBy.getUserId());
                if (userObj.isPresent())
                    vendorAttach.get().setCreatedBy(userObj.get());
                Optional<BeneficiaryRequestDetails> requestDetails = this.beneficiaryRqtDetailsRepo.findById(actDto.getRequestNumber());
                if (requestDetails.isPresent())
                    vendorAttach.get().setRequestNumber(requestDetails.get());
                BeneficiaryAttachments attach = this.beneficiaryAttachmentRepo.save(vendorAttach.get());
            }

            HashMap<Integer, Integer> stepAction = getNextStepByAction(result.get(0));
            Optional<ActionMst> action = actionRepo.findById(actDto.getActionId());
            for (Entry<Integer, Integer> entry : stepAction.entrySet()) {
                if (entry.getValue() == (long) (action.get().getActionId())) {
                    StepRoleMapping status = new StepRoleMapping();
                    status.setStepRoleId(entry.getKey().longValue());
                    if (status == null) {
                        return new ApiResponse2<>(false, "Step not available", null, HttpStatus.OK.value());
                    }

                    model.setStatus(status);
                }
            }
            this.beneficiaryRqtDetailsRepo.save(model);
            
            return new ApiResponse2<>(true, "Status updated successfully", dispatchId, HttpStatus.OK.value());
        } else {
            return new ApiResponse2<>(false, "Unauthorized Access !!", null, HttpStatus.UNAUTHORIZED.value());
        }
    }


    public ApiResponse2<?> saveMedicalOfficerAction(MedicalOfficerAppReqDto medicalOfficerAppReqDto, Authentication authentication) {
        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());
        Integer roleId = userRoleMapRepo.findRoleIdByUserId(authenticatedUser.getUserId());

//        RoleMst role = roleRepo.findByRoleId(roleId);

        BeneficiaryRequestDetails model = beneficiaryRqtDetailsRepo.findByRequestNo(medicalOfficerAppReqDto.getRequestNumber());
        StepRoleMapping statusId = stepRoleRepo.findByStepRoleId(model.getStatus().getStepRoleId());

        System.out.println(statusId.getRoleId().getRoleId());
        System.out.println(roleId);
        System.out.println(model.getStatus().getStepRoleId());

        if ((statusId.getRoleId().getRoleId()) == roleId) {

            Query query = em.createNativeQuery("select medical.next_step_and_actions_determination(1,'" + medicalOfficerAppReqDto.getRequestNumber() + "')");
            List<String> result = query.getResultList();
            if (result.get(0) == null || result.get(0).isEmpty())
                return new ApiResponse2<>(false, "Required action already taken for this case", null, HttpStatus.OK.value());

            model.setRemarks(medicalOfficerAppReqDto.getRemarks());

            UserMst updBy = userRepo.findById(authenticatedUser.getUserId()).orElse(null);
            if (updBy == null) {
                return new ApiResponse2<>(false, "User not found", null, HttpStatus.OK.value());
            }
            model.setUpdatedBy(updBy);

            BeneficiaryRequestDetails requestDetailsObj = this.beneficiaryRqtDetailsRepo.save(model);

            for (MedicalOfficerDrugDto drug : medicalOfficerAppReqDto.getDrugList()) {

                Optional<BeneficiaryRequestDrug> drugObj = this.beneficiaryRqtDrugsRepo.findById(drug.getDrugId());
                drugObj.get().setStatus(requestDetailsObj.getStatus());

                if (drug.getRecommendedQuantity() <= drugObj.get().getQuantity()) {
                    drugObj.get().setQuantity(drug.getRecommendedQuantity());
                } else {
                    return new ApiResponse2<>(false, "Recommended quantity should be less than or equal to indented quantity", drugObj.get().getQuantity(), HttpStatus.OK.value());
                }

                drugObj.get().setRemarks(drug.getRemarks());
                drugObj.get().setDrugName(drug.getDrugName());
                drugObj.get().setDrugType(drug.getDrugType());

                UserMst updatedBy = new UserMst();
                updatedBy.setUserId(authenticatedUser.getUserId());
                drugObj.get().setUpdatedBy(updatedBy);

                this.beneficiaryRqtDrugsRepo.save(drugObj.get());
            }

            HashMap<Integer, Integer> stepAction = getNextStepByAction(result.get(0));
            Optional<ActionMst> action = actionRepo.findById(medicalOfficerAppReqDto.getActionId());
            for (Entry<Integer, Integer> entry : stepAction.entrySet()) {
                if (entry.getValue() == (long) (action.get().getActionId())) {
                    StepRoleMapping status = new StepRoleMapping();
                    status.setStepRoleId(entry.getKey().longValue());
                    if (status == null) {
                        return new ApiResponse2<>(false, "Step not available", null, HttpStatus.OK.value());
                    }

                    requestDetailsObj.setStatus(status);
                }
            }
            this.beneficiaryRqtDetailsRepo.save(requestDetailsObj);
            return new ApiResponse2<>(true, "Status updated successfully", null, HttpStatus.OK.value());
        } else {
            return new ApiResponse2<>(false, "Unauthorized Access !!", null, HttpStatus.UNAUTHORIZED.value());
        }
    }

    public ApiResponse2<?> saveVendorAction(VendorActionReqDto vendorActionReqDto, Authentication authentication) throws FileNotFoundException {
        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());
        Integer roleId = userRoleMapRepo.findRoleIdByUserId(authenticatedUser.getUserId());

//        RoleMst role = roleRepo.findByRoleId(roleId);

        BeneficiaryRequestDetails model = beneficiaryRqtDetailsRepo.findByRequestNo(vendorActionReqDto.getRequestNumber());
        StepRoleMapping statusId = stepRoleRepo.findByStepRoleId(model.getStatus().getStepRoleId());

        System.out.println(statusId.getRoleId().getRoleId());
        System.out.println(roleId);
        System.out.println(model.getStatus().getStepRoleId());

        if ((statusId.getRoleId().getRoleId()) == roleId) {

            Query query = em.createNativeQuery("select medical.next_step_and_actions_determination(1,'" + vendorActionReqDto.getRequestNumber() + "')");
            List<String> result = query.getResultList();
            if (result.get(0) == null || result.get(0).isEmpty())
                return new ApiResponse2<>(false, "Required action already taken for this case", null, HttpStatus.OK.value());

            model.setRemarks(vendorActionReqDto.getRemarks());

            UserMst updBy = userRepo.findById(authenticatedUser.getUserId()).orElse(null);
            if (updBy == null) {
                return new ApiResponse2<>(false, "User not found", null, HttpStatus.OK.value());
            }
            model.setUpdatedBy(updBy);

            BeneficiaryRequestDetails requestDetailsObj = this.beneficiaryRqtDetailsRepo.save(model);

            for (VendorDrugActionDto drug : vendorActionReqDto.getDrugList()) {

                Optional<BeneficiaryRequestDrug> drugObj = this.beneficiaryRqtDrugsRepo.findById(drug.getDrugId());
                drugObj.get().setStatus(requestDetailsObj.getStatus());


                drugObj.get().setBatchNo(drug.getBatchNo());

                LocalDate today = LocalDate.now();


                LocalDate inputDate = convertString(drug.getManufacturedDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate oneYearBack = today.minusYears(1);
                LocalDate exactOneYear = oneYearBack.minusDays(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDate = exactOneYear.format(formatter);
                if (inputDate.compareTo(exactOneYear) >= 0 && inputDate.compareTo(today) <= 0) {
                    drugObj.get().setManufacturedDate(convertString(drug.getManufacturedDate()));
                } else {
                    return new ApiResponse2<>(false, "Manufacturing date should be between current date and 1yr before current date", drug.getManufacturedDate(), HttpStatus.OK.value());
                }


                LocalDate expiryDate = convertString(drug.getExpiryDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate afterThreeMonths = today.plusMonths(3);
                if (expiryDate.compareTo(afterThreeMonths) >= 0) {
                    drugObj.get().setExpiryDate(convertString(drug.getExpiryDate()));
                } else {
                    return new ApiResponse2<>(false, "Expiry date should be more than 3 months from current date", drug.getExpiryDate(), HttpStatus.OK.value());
                }


                drugObj.get().setBrandName(drug.getBrandName());

                if (drug.getMrp() != null) {
                    drugObj.get().setMrp(drug.getMrp());
                    drugObj.get().setDiscountedPrice((int) (drug.getMrp() - ((DISCOUNT_PERCENTAGE * drug.getMrp()) / 100)));
                }

                UserMst updatedBy = new UserMst();
                updatedBy.setUserId(authenticatedUser.getUserId());
                drugObj.get().setUpdatedBy(updatedBy);

                this.beneficiaryRqtDrugsRepo.save(drugObj.get());
            }

            if (vendorActionReqDto.getVendorFileAttachment() != null) {
                Optional<BeneficiaryAttachments> vendorAttach = this.beneficiaryAttachmentRepo.
                        findByRequestNAndAttachType(vendorActionReqDto.getRequestNumber(),
                                vendorActionReqDto.getVendorFileAttachment().getAttachmentType());
                if (vendorAttach.isPresent()) {
                    vendorAttach.get().setFileName(vendorActionReqDto.getVendorFileAttachment().getFileName());
                    vendorAttach.get().setFilePath(this
                            .uploadOnRealPath(new FileDto(vendorActionReqDto.getVendorFileAttachment().getFilePath(),
                                    String.valueOf(requestDetailsObj.getRequestNumber()))));
                } else {
                    vendorAttach = Optional.of(new BeneficiaryAttachments());
                    vendorAttach.get().setAttachmentType(vendorActionReqDto.getVendorFileAttachment().getAttachmentType());
                    vendorAttach.get().setFileName(vendorActionReqDto.getVendorFileAttachment().getFileName());
                    vendorAttach.get().setFilePath(
                            this.uploadOnRealPath(new FileDto(vendorActionReqDto.getVendorFileAttachment().getFilePath(),
                                    String.valueOf(requestDetailsObj.getRequestNumber()))));
                }
                UserMst updatedBy = new UserMst();
                updatedBy.setUserId(authenticatedUser.getUserId());
                Optional<UserMst> userObj = this.userRepo.findById(updatedBy.getUserId());
                if (userObj.isPresent())
                    vendorAttach.get().setCreatedBy(userObj.get());
                Optional<BeneficiaryRequestDetails> requestDetails = this.beneficiaryRqtDetailsRepo.findById(vendorActionReqDto.getRequestNumber());
                if (requestDetails.isPresent())
                    vendorAttach.get().setRequestNumber(requestDetails.get());
                BeneficiaryAttachments attach = this.beneficiaryAttachmentRepo.save(vendorAttach.get());
            }

            HashMap<Integer, Integer> stepAction = getNextStepByAction(result.get(0));
            Optional<ActionMst> action = actionRepo.findById(vendorActionReqDto.getActionId());
            for (Entry<Integer, Integer> entry : stepAction.entrySet()) {
                if (entry.getValue() == (long) (action.get().getActionId())) {
                    StepRoleMapping status = new StepRoleMapping();
                    status.setStepRoleId(entry.getKey().longValue());
                    if (status == null) {
                        return new ApiResponse2<>(false, "Step not available", null, HttpStatus.OK.value());
                    }

                    requestDetailsObj.setStatus(status);
                }
            }
            this.beneficiaryRqtDetailsRepo.save(requestDetailsObj);
            return new ApiResponse2<>(true, "Status updated successfully", null, HttpStatus.OK.value());
        } else {
            return new ApiResponse2<>(false, "Unauthorized Access !!", null, HttpStatus.UNAUTHORIZED.value());
        }
    }

    public ApiResponse2<?> savePharmaReceiveAction(PharmacistReceiveActionReqDto pharmacistReceiveActionReqDto, Authentication authentication) throws FileNotFoundException {
        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());
        Integer roleId = userRoleMapRepo.findRoleIdByUserId(authenticatedUser.getUserId());

//        RoleMst role = roleRepo.findByRoleId(roleId);

        BeneficiaryRequestDetails model = beneficiaryRqtDetailsRepo.findByRequestNo(pharmacistReceiveActionReqDto.getRequestNumber());
        StepRoleMapping statusId = stepRoleRepo.findByStepRoleId(model.getStatus().getStepRoleId());

        System.out.println(statusId.getRoleId().getRoleId());
        System.out.println(roleId);
        System.out.println(model.getStatus().getStepRoleId());

        if ((statusId.getRoleId().getRoleId()) == roleId) {

            Query query = em.createNativeQuery("select medical.next_step_and_actions_determination(1,'" + pharmacistReceiveActionReqDto.getRequestNumber() + "')");
            List<String> result = query.getResultList();
            if (result.get(0) == null || result.get(0).isEmpty())
                return new ApiResponse2<>(false, "Required action already taken for this case", null, HttpStatus.OK.value());

            model.setRemarks(pharmacistReceiveActionReqDto.getRemarks());

            UserMst updBy = userRepo.findById(authenticatedUser.getUserId()).orElse(null);
            if (updBy == null) {
                return new ApiResponse2<>(false, "User not found", null, HttpStatus.OK.value());
            }
            model.setUpdatedBy(updBy);

            BeneficiaryRequestDetails requestDetailsObj = this.beneficiaryRqtDetailsRepo.save(model);

            HashMap<Integer, Integer> stepAction = getNextStepByAction(result.get(0));
            Optional<ActionMst> action = actionRepo.findById(pharmacistReceiveActionReqDto.getActionId());
            for (Entry<Integer, Integer> entry : stepAction.entrySet()) {
                if (entry.getValue() == (long) (action.get().getActionId())) {
                    StepRoleMapping status = new StepRoleMapping();
                    status.setStepRoleId(entry.getKey().longValue());
                    if (status == null) {
                        return new ApiResponse2<>(false, "Step not available", null, HttpStatus.OK.value());
                    }

                    requestDetailsObj.setStatus(status);
                }
            }
            this.beneficiaryRqtDetailsRepo.save(requestDetailsObj);
            return new ApiResponse2<>(true, "Status updated successfully", null, HttpStatus.OK.value());
        } else {
            return new ApiResponse2<>(false, "Unauthorized Access !!", null, HttpStatus.UNAUTHORIZED.value());
        }
    }

    public ApiResponse2<?> savePharmaDispatchAction(PharmaDispatchActionReqDto pharmaDispatchActionReqDto, Authentication authentication) throws FileNotFoundException {
        if (pharmaDispatchActionReqDto.getShipmentType() != 18 && pharmaDispatchActionReqDto.getShipmentType() != 19)
            throw new BadRequestException("Invalid Shipment Type Id");

        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());
        Integer roleId = userRoleMapRepo.findRoleIdByUserId(authenticatedUser.getUserId());

//        RoleMst role = roleRepo.findByRoleId(roleId);

        BeneficiaryRequestDetails model = beneficiaryRqtDetailsRepo.findByRequestNo(pharmaDispatchActionReqDto.getRequestNumber());
        StepRoleMapping statusId = stepRoleRepo.findByStepRoleId(model.getStatus().getStepRoleId());

        System.out.println(statusId.getRoleId().getRoleId());
        System.out.println(roleId);
        System.out.println(model.getStatus().getStepRoleId());

        if ((statusId.getRoleId().getRoleId()) == roleId) {

            Query query = em.createNativeQuery("select medical.next_step_and_actions_determination(1,'" + pharmaDispatchActionReqDto.getRequestNumber() + "')");
            List<String> result = query.getResultList();
            if (result.get(0) == null || result.get(0).isEmpty())
                return new ApiResponse2<>(false, "Required action already taken for this case", null, HttpStatus.OK.value());

            model.setRemarks(pharmaDispatchActionReqDto.getRemarks());
            model.setShipmentType(pharmaDispatchActionReqDto.getShipmentType());

            if (pharmaDispatchActionReqDto.getShipmentType() == 18) {

                if (pharmaDispatchActionReqDto.getPickupName() == null || pharmaDispatchActionReqDto.getPickupName().isEmpty())
                    throw new BadRequestException("Pickup Name is mandatory");
                if (pharmaDispatchActionReqDto.getPickupMobileNo() == null || pharmaDispatchActionReqDto.getPickupMobileNo().isEmpty())
                    throw new BadRequestException("Pickup Mobile Number is mandatory");
                if (pharmaDispatchActionReqDto.getPickupRelation() == null )
                    throw new BadRequestException("Pickup Relation ID is mandatory");

                model.setPickupContactNo(Long.parseLong(pharmaDispatchActionReqDto.getPickupMobileNo()));
                model.setPickupPersonName(pharmaDispatchActionReqDto.getPickupName());
                Optional<GeneralTypeMst> gen = this.generalTypeMasterRepo.findById(pharmaDispatchActionReqDto.getPickupRelation());
                if (gen.isPresent()) {
                    model.setPickupRelation(gen.get());
                    if (gen.get().getTypeId() == 57)
                        model.setPickupRelationName(gen.get().getTypeName());
                    else if (gen.get().getTypeId() == 58) {
                        if (null == pharmaDispatchActionReqDto.getPickupRelationName()||pharmaDispatchActionReqDto.getPickupRelationName().equals(""))
                            throw new BadRequestException("Pickup Relation Name is mandatory if Others Option is selected!");

                        model.setPickupRelationName(pharmaDispatchActionReqDto.getPickupRelationName());
                    }

                }

//                if (pharmaDispatchActionReqDto.getAuthorizationLetter() == null)
//                    throw new BadRequestException("Authorization Letter is mandatory");

                if (pharmaDispatchActionReqDto.getAuthorizationLetter() != null) {
                BeneficiaryAttachments uploadAuthorizedLetter = new BeneficiaryAttachments();

                uploadAuthorizedLetter.setAttachmentType(pharmaDispatchActionReqDto.getAuthorizationLetter().getAttachmentType());
                uploadAuthorizedLetter.setFileName(pharmaDispatchActionReqDto.getAuthorizationLetter().getFileName());
                uploadAuthorizedLetter.setRequestNumber(model);
                Optional<UserMst> user = this.userRepo.findById(authenticatedUser.getUserId());
                if (user != null)
                    uploadAuthorizedLetter.setCreatedBy(user.get());
                uploadAuthorizedLetter.setFilePath(this.uploadOnRealPath(
                        new FileDto(pharmaDispatchActionReqDto.getAuthorizationLetter().getFilePath(), String.valueOf(model.getRequestNumber()))));

                this.beneficiaryAttachmentRepo.save(uploadAuthorizedLetter);
            }}


            UserMst updBy = userRepo.findById(authenticatedUser.getUserId()).orElse(null);
            if (updBy == null) {
                return new ApiResponse2<>(false, "User not found", null, HttpStatus.OK.value());
            }
            model.setUpdatedBy(updBy);

            BeneficiaryRequestDetails requestDetailsObj = this.beneficiaryRqtDetailsRepo.save(model);

            String dispatchId = this.beneficiaryRqtDrugsRepo.generateDisptachId();
            if (!pharmaDispatchActionReqDto.getDrugList().isEmpty()) {
                for (PharmaDispatchDrugDto drug : pharmaDispatchActionReqDto.getDrugList()) {

                    Optional<BeneficiaryRequestDrug> drugObj = this.beneficiaryRqtDrugsRepo.findById(drug.getDrugId());
                    drugObj.get().setStatus(requestDetailsObj.getStatus());
                    UserMst updatedBy = new UserMst();
                    updatedBy.setUserId(authenticatedUser.getUserId());
                    drugObj.get().setUpdatedBy(updatedBy);
                    drugObj.get().setDispatchId(dispatchId);
                    drugObj.get().setDispatchDate(new Date());
                    if (pharmaDispatchActionReqDto.getShipmentType() == 19) {
                        if (pharmaDispatchActionReqDto.getPostalTrackingId()==null || pharmaDispatchActionReqDto.getPostalTrackingId().equals(""))
                            throw new BadRequestException("Postal Tracking ID is mandatory");

                        drugObj.get().setPostalTrackingId(pharmaDispatchActionReqDto.getPostalTrackingId());
                    }
                    if (pharmaDispatchActionReqDto.getShipmentType() != null) {
                        if (pharmaDispatchActionReqDto.getShipmentType() == 18) {
                            BeneficiaryOtpValidation beneOtp = new BeneficiaryOtpValidation();
                            if (drugObj.get() != null) {
                                beneOtp.setDrug(drugObj.get());
                                beneOtp.setUser(model.getCreatedBy());
                                beneOtp.setOtp(123456);
                            }
                            this.beneficiaryOtpValidationRepo.save(beneOtp);
                        }
                    }
                    this.beneficiaryRqtDrugsRepo.save(drugObj.get());
                }
            }
            HashMap<Integer, Integer> stepAction = getNextStepByAction(result.get(0));
            Optional<ActionMst> action = actionRepo.findById(pharmaDispatchActionReqDto.getActionId());
            for (Entry<Integer, Integer> entry : stepAction.entrySet()) {
                if (entry.getValue() == (long) (action.get().getActionId())) {
                    StepRoleMapping status = new StepRoleMapping();
                    status.setStepRoleId(entry.getKey().longValue());
                    if (status == null) {
                        return new ApiResponse2<>(false, "Step not available", null, HttpStatus.OK.value());
                    }

                    requestDetailsObj.setStatus(status);
                }
            }
            this.beneficiaryRqtDetailsRepo.save(requestDetailsObj);
            return new ApiResponse2<>(true, "Status updated successfully", dispatchId, HttpStatus.OK.value());
        } else {
            return new ApiResponse2<>(false, "Unauthorized Access !!", null, HttpStatus.UNAUTHORIZED.value());
        }
    }

    public Object uploadPostalConfirmByReqNoHandler(UploadPostalDto dto, Authentication authentication) throws FileNotFoundException {

        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());

        BeneficiaryRequestDetails model = beneficiaryRqtDetailsRepo.findByRequestNo(dto.getRequestNo());
        if (model == null)
            return new ApiResponse2<>(false, "No such request id exist!", null, HttpStatus.NOT_FOUND.value());
        Query query = em.createNativeQuery("select medical.next_step_and_actions_determination(1,'" + dto.getRequestNo() + "')");
        List<String> result = query.getResultList();
        if (result.get(0) == null || result.get(0).equals("")) {
            return new ApiResponse2<>(false, "Required action already taken for this case", null,
                    HttpStatus.OK.value());
        }
        HashMap<Integer, Integer> stepAction = getNextStepByAction(result.get(0));
        Optional<ActionMst> action = actionRepo.findById(dto.getActionId());
        for (Entry<Integer, Integer> entry : stepAction.entrySet()) {
            if (entry.getValue() == (long) (action.get().getActionId())) {

                StepRoleMapping status = new StepRoleMapping();
                status.setStepRoleId(entry.getKey().longValue());
                if (status == null) {
                    return new ApiResponse2<>(false, "Step not available", null, HttpStatus.OK.value());
                }

                model.setStatus(status);
            }
        }
        List<BeneficiaryRequestDrug> drugList = this.beneficiaryRqtDrugsRepo.findByRequestNo(dto.getRequestNo());
        UserMst updBy = userRepo.findById(authenticatedUser.getUserId()).orElse(null);
        if (updBy == null) {
            return new ApiResponse2<>(false, "User not found", null, HttpStatus.OK.value());
        }
        Optional<StepRoleMapping> stepRole = this.stepRoleRepo.findById(17L);
        model.setUpdatedBy(updBy);
        BeneficiaryAttachments attachment = null;
        if (model.getShipmentType() == 19) {
            if (dto.getPostalUpload() != null) {
                BeneficiaryAttachments uploadPostal = new BeneficiaryAttachments();

                uploadPostal.setAttachmentType(dto.getPostalUpload().getAttachmentType());
                uploadPostal.setFileName(dto.getPostalUpload().getFileName());
                uploadPostal.setRequestNumber(model);
                Optional<UserMst> user = this.userRepo.findById(authenticatedUser.getUserId());
                if (user != null)
                    uploadPostal.setCreatedBy(user.get());
                uploadPostal.setFilePath(this.uploadOnRealPath(
                        new FileDto(dto.getPostalUpload().getFilePath(), String.valueOf(model.getRequestNumber()))));


                for (BeneficiaryRequestDrug drug : drugList) {
                    if (stepRole != null) {
                        drug.setStatus(stepRole.get());
                        this.beneficiaryRqtDrugsRepo.save(drug);
                    }
                }
                attachment = this.beneficiaryAttachmentRepo.save(uploadPostal);
                if (stepRole != null) {
                    model.setStatus(stepRole.get());
                    this.beneficiaryRqtDetailsRepo.save(model);
                }
                return new ApiResponse2<>(true, "File uploaded successfully!", attachment.getId(),
                        HttpStatus.OK.value());
            } else {
                return new ApiResponse2<>(false, "Attachment is Mandatory!", null, HttpStatus.BAD_REQUEST.value());
            }
        }
        if (model.getShipmentType() == 18) {
        	if(dto.getOtp()==null || dto.getOtp().equals(""))
        	{
        		return new ApiResponse2<>(false, "otp must not be blank!", null, HttpStatus.NOT_ACCEPTABLE.value());
        	}
        	else if (dto.getOtp() != null && !dto.getOtp().isEmpty() && Integer.parseInt(dto.getOtp()) == 123456) {
                for (BeneficiaryRequestDrug drug : drugList) {
                    if (stepRole != null) {
                        drug.setStatus(stepRole.get());
                        this.beneficiaryRqtDrugsRepo.save(drug);
                    }
                }
                if (stepRole != null) {
                    model.setStatus(stepRole.get());
                    this.beneficiaryRqtDetailsRepo.save(model);
                }
                return new ApiResponse2<>(true, "OTP validated successfully!", null, HttpStatus.OK.value());
            } else {
                return new ApiResponse2<>(false, "OTP Mismatched!", null, HttpStatus.NOT_ACCEPTABLE.value());
            }
        } else {
            return new ApiResponse2<>(false, "ShipmentType is null", null, HttpStatus.BAD_REQUEST.value());
        }
    }

    public ApiResponse2<?> recieveMedicine(EmployeeDetailsDto dropDto, Authentication authentication) {
        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());

        List<InboxResponseDto> response = new ArrayList<>();
        Integer role = userRoleMapRepo.findRoleIdByUserId(authenticatedUser.getUserId());
        if (role == EmployeeConstants.Pharma_Role_Id) {
            List<InboxResponseDto> respon = beneficiaryRqtDetailsRepo.getRecievedMedicine(role,
                    dropDto.getRequestNumber(), dropDto.getStartDate(), dropDto.getEndDate());
            response.addAll(respon);
        }

        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> dispatchMedicine(EmployeeDetailsDto dropDto, Authentication authentication) {

        UserMst authenticatedUser = getAuthenticatedUser(authentication.getName());

        List<InboxResponseDto> response = new ArrayList<>();
        Integer role = userRoleMapRepo.findRoleIdByUserId(authenticatedUser.getUserId());
        if (role == EmployeeConstants.Pharma_Role_Id) {
            List<InboxResponseDto> respon = beneficiaryRqtDetailsRepo.getDispatchMedicines(role,
                    dropDto.getRequestNumber(), dropDto.getStartDate(), dropDto.getEndDate());
            response.addAll(respon);
        }

        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());
    }


    public ApiResponse2<?> dispatchMedicine(EmployeeDetailsDto dropDto) {
        List<InboxResponseDto> response = new ArrayList<>();
        Integer role = userRoleMapRepo.findRoleIdByUserId(dropDto.getUserId());
        if (role == EmployeeConstants.Pharma_Role_Id) {
            List<InboxResponseDto> respon = beneficiaryRqtDetailsRepo.getDispatchMedicines(role,
                    dropDto.getRequestNumber(), dropDto.getStartDate(), dropDto.getEndDate());
            response.addAll(respon);
        }

        return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> validateUser(EmployeeDetailsDto dropDto, String jwtToken) {
        // Load the user based on the JWT token
        UserMst user = this.loadUserByUsername(jwtToken);
        if (user == null) {

            return new ApiResponse2<>(false, "User not found", null, HttpStatus.UNAUTHORIZED.value());
        }


        Integer roleId = userRoleMapRepo.findRoleIdByUserId(user.getUserId());

        RoleMst role = roleRepo.findByRoleId(roleId); // Assumes this method returns RoleMst or null
        if (role == null) {

            return new ApiResponse2<>(false, "Role not found", null, HttpStatus.BAD_REQUEST.value());
        }

        // Fetch MenuMst by link
        Optional<MenuMst> menuOpt = menuMstRepo.findByLink(dropDto.getEndpoint());
        if (!menuOpt.isPresent()) {

            return new ApiResponse2<>(false, "Endpoint not found", null, HttpStatus.BAD_REQUEST.value());
        }
        MenuMst menu = menuOpt.get(); // Retrieve the MenuMst object


        Optional<RoleMenuMappingMst> mappingOpt = roleMenuRightMapRepo.findByRoleDtls_RoleIdAndMenuDtl_MenuId(role.getRoleId(), menu.getMenuId());
        if (mappingOpt.isPresent()) {
            RoleMenuMappingMst mapping = mappingOpt.get();
            if (Boolean.TRUE.equals(mapping.getIsActive())) {

                return new ApiResponse2<>(true, "Access Granted", true, HttpStatus.OK.value());
            } else {

                return new ApiResponse2<>(false, "Access Denied: Inactive Mapping", false, HttpStatus.FORBIDDEN.value());
            }
        } else {

            return new ApiResponse2<>(false, "Access Denied: No Mapping Found", false, HttpStatus.FORBIDDEN.value());
        }
    }

//		if(dropDto.getEndpoint().equals(EmployeeConstants.Form_End_Point)) {
//		if (role == EmployeeConstants.Medco_Role_Id || role == EmployeeConstants.Pharma_Role_Id)
//		{
//			return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, true, HttpStatus.OK.value());
//		}
//		else {
//    	return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, false, HttpStatus.OK.value());
//		}
//	}
//		else {
//		return new ApiResponse2<>(true, EmployeeConstants.FETCH_SUCCESSFULL, false, HttpStatus.OK.value());
//		}
//	}


//	private void sendsms(String contactNumber, String message, String templateId) throws Exception{
//		try {
//			String result ="";
//			
//			result = services.sendSingleSMS("YSRAHCT", "DrYsrahct@123456", message, "YSRAST", contactNumber, "bc8e21ee-4b85-43f2-a9f1-482aa285f5b6", templateId);
//
//			System.out.print(result);
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}


    public ResponseEntity<?> forgetPasswordOtp(String loginId) {

        Optional<UserMst> optionalUser = this.userRepo.findByLoginNameIgnoreCaseAndIsActiveIsTrue(loginId);

        if (!optionalUser.isPresent())
            throw new EntityNotFoundException("User Id doesn't exists!");

        UserMst user = optionalUser.get();

//        YsrGeneralTypeMaster validationType = this.ysrGeneralTypeMasterRepo.findByTypeId(140);

//        if (validationType == null)
//            throw new EntityNotFoundException("Validation Type doesn't exists!");

        /** for time being hardcoded otp 123456 for testing purpose */

        Integer otp = 123456;

        if (user.getMobileNo() == null)
            throw new EntityNotFoundException("Mobile Number doesn't exists for this user!");

        String contactNo = user.getMobileNo().toString();

        BeneficiaryOtpValidation otpModel = new BeneficiaryOtpValidation();
        otpModel.setOtp(otp);

        UserMst userId = new UserMst();
        userId.setUserId(user.getUserId());
        otpModel.setUser(userId);
        
        GeneralTypeMst generalType = this.generalTypeMasterRepo.findByTypeId(EmployeeConstants.Forget_Otp_Valid);
  
        otpModel.setValidationType(generalType);
        otpModel.setFailedAttempts(0);
        otpModel.setIsValidated(false);

        Timestamp generatedAt = new Timestamp(System.currentTimeMillis());

        // Convert to Local Date Time in UTC
        LocalDateTime utcDateTime = generatedAt.toLocalDateTime();

        // Convert to Local Date Time in IST
        LocalDateTime istDateTime = utcDateTime.atZone(ZoneOffset.UTC)
                .withZoneSameInstant(ZoneId.of("Asia/Kolkata"))
                .toLocalDateTime();

        // Convert Local Date time to Timestamp
        Timestamp timestamp = Timestamp.valueOf(istDateTime);

        otpModel.setOtpGeneratedAt(timestamp);

        beneficiaryOtpValidationRepo.save(otpModel);

        if (!contactNo.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse2<>(true,
                    "OTP Sent to registered Mobile No. - XXXXXX" + contactNo.substring(6, 10), null,
                    HttpStatus.OK.value()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ApiResponse2<>(false, "Mobile Number is not registered!", null, HttpStatus.OK.value()),
                    HttpStatus.OK);

        }


    }

    public static String generateEncriptedString(String lStrPassword) {
        String lStrEncriptedPassword = "";
        byte[] defaultBytes = lStrPassword.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            lStrEncriptedPassword = hexString + "";

        } catch (Exception e) {
            //throw Exception (e.getMessage());
        }

        return lStrEncriptedPassword;
    }

    public ResponseEntity<Object> forgetPasswordValidateOtp(ForgetPassRequestDto forgetPassRequestDto) {

        String username = forgetPassRequestDto.getUsername();
        Integer otp = forgetPassRequestDto.getOtp();

        Optional<UserMst> optionalUser = this.userRepo.findByLoginNameIgnoreCaseAndIsActiveIsTrue(username);

        if (!optionalUser.isPresent())
            throw new EntityNotFoundException("User Id doesn't exists!");

        UserMst user = optionalUser.get();

        BeneficiaryOtpValidation otpValidations = beneficiaryOtpValidationRepo.findFirstByUserAndValidationTypeOrderByOtpGeneratedAtDesc(user, EmployeeConstants.Forget_Otp_Valid);

//                .findFirstByUserIdAndValidationType_typeIdOrderByOtpGeneratedAtDesc(user, 140);

        if (otpValidations == null)
            throw new EntityNotFoundException("Please generate OTP first!");

        Instant otpGeneratedAt = otpValidations.getOtpGeneratedAt().toInstant();
        Instant currentInstant = Instant.now();

        Duration duration = Duration.between(otpGeneratedAt, currentInstant);

        if (Boolean.TRUE.equals(duration.toMinutes() < 10 && otpValidations.getFailedAttempts() < 5
                && !otpValidations.getIsValidated()) && Objects.equals(otpValidations.getOtp(), otp)) {

            otpValidations.setIsValidated(true);
            beneficiaryOtpValidationRepo.save(otpValidations);

            byte[] decodedBytes = Base64.getDecoder().decode(forgetPassRequestDto.getPassword());
            String decodedPwdString = new String(decodedBytes);
//            System.out.println(decodedPwdString);

            String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W]).{8,}$";

            if (!decodedPwdString.matches(passwordPattern)) {
                throw new BadRequestException("Password does not meet the required criteria.");
            }


            String hashedPassword = generateEncriptedString(decodedPwdString);
//            System.out.println(hashedPassword);
            user.setPassword(hashedPassword);
//        	this.aes.encrypt(forgetPassRequestDto.getPassword())
            userRepo.save(user);
            return new ResponseEntity<>(
                    new ApiResponse2<>(true, "Password Updated Successfully!", null, HttpStatus.OK.value()),
                    HttpStatus.OK);

        } else {

            if (duration.toMinutes() >= 10 || Boolean.TRUE.equals(otpValidations.getIsValidated()))
                throw new IllegalStateException("OTP has expired!");

            if (otpValidations.getFailedAttempts() >= 5)
                throw new IllegalStateException("Entered wrong OTP too many times! Please generate otp again!");

            otpValidations.setFailedAttempts(otpValidations.getFailedAttempts() + 1);
            beneficiaryOtpValidationRepo.save(otpValidations);
            throw new EntityNotFoundException("Wrong OTP!");

        }

    }


}
