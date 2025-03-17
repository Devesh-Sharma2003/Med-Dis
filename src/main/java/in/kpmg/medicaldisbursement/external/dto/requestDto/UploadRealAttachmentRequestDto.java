package in.kpmg.medicaldisbursement.external.dto.requestDto;

import lombok.Data;

@Data
public class UploadRealAttachmentRequestDto {
    private String module;
    private String virtualPath;
    private String id;
}
