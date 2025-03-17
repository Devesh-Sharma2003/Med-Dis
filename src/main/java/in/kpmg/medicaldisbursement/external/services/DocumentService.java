//package in.kpmg.medicaldisbursement.external.services;
//
//import in.kpmg.medicaldisbursement.external.dto.requestDto.UploadRealAttachmentRequestDto;
//import in.kpmg.medicaldisbursement.external.dto.responseDto.ApiResponseDto;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.HashMap;
//import java.util.List;
//
//@FeignClient(name = "PAYROLL-DOCUMENT-SERVICE")
//public interface DocumentService {
//    @PostMapping(value = "/payroll-documents/upload-document", consumes = "multipart/form-data")
//    ApiResponseDto getTempFilePath(@RequestPart("file") List<MultipartFile> file);
//
//    @PostMapping("/payroll-documents/upload-on-real-path")
//    HashMap<String, String > getRealFilePath(@RequestBody UploadRealAttachmentRequestDto uploadRealAttachmentRequestDto);
//
//}
