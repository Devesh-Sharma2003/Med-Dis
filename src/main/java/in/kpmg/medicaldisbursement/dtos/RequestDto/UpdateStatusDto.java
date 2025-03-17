package in.kpmg.medicaldisbursement.dtos.RequestDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusDto {

    private String requestNumber;

    private Integer actionId;

    private Integer userId;

    //	@Pattern(regexp = "^[A-Za-z][A-Za-z ]*$", message = "Remarks must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    @NotBlank(message = "Remarks must not be blank!")
//    @Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$")
    // @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "Remarks can only contain letters, numbers, spaces, hyphens, and periods")
    private String remarks;

    //	@NotBlank(message = "Pickup Name is mandatory")
    @Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", message = "Pickup Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    private String pickupName;

    //	@NotBlank(message = "Pickup Mobile Number is mandatory")
    @Pattern(regexp = "^[1-9][0-9]{9}$", message = "Pickup Mobile number must be 10 digits and should not start with 0")
    private String pickupMobileNo;

    private Integer pickupRelationId;

    //	@NotBlank(message = "Pickup Relation Name is Required")
    @Pattern(regexp = "^[A-Za-z][A-Za-z ]*$|^$", message = "Pickup Relation Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    private String pickupRelationName;

    private FileSaveRqtDto authorizationLetter;

    private Integer shipmentType;

    private List<@Valid VendorDrugDetailDto> drugList;

    private FileSaveRqtDto vendorFileAttachment;

    private String postalTrackingId;
}
