package in.kpmg.medicaldisbursement.dtos.RequestDto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class VendorActionReqDto {

    private String requestNumber;

    private Integer actionId;

    private Integer userId;

    @NotBlank(message = "Remarks must not be blank!")
//    @Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$")
    // @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "Remarks can only contain letters, numbers, spaces, hyphens, and periods")
    private String remarks;

    @NotNull(message = "File is mandatory")
    private FileSaveRqtDto vendorFileAttachment;

    @NotNull(message = "Drug list is mandatory")
    @NotEmpty(message = "Drug list is mandatory")
    List<@Valid VendorDrugActionDto> drugList;
}
