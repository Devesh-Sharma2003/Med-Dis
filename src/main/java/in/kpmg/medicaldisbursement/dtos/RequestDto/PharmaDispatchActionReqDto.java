package in.kpmg.medicaldisbursement.dtos.RequestDto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class PharmaDispatchActionReqDto {

    private String requestNumber;

    private Integer actionId;

    private Integer userId;

    @NotBlank(message = "Remarks must not be blank!")
//    @Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$")
    // @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "Remarks can only contain letters, numbers, spaces, hyphens, and periods")
    private String remarks;

//    @NotBlank(message = "Pickup Name is mandatory")
    @Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", message = "Pickup Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    private String pickupName;

//    @NotBlank(message = "Pickup Mobile Number is mandatory")
    @Pattern(regexp = "^[1-9][0-9]{9}$", message = "Pickup Mobile number must be 10 digits and should not start with 0")
    private String pickupMobileNo;

//    @NotNull(message = "pickup Relation Id is mandatory")
    private Integer pickupRelation;

//    	@NotBlank(message = "Pickup Relation Name is Required")
    @Pattern(regexp = "^[A-Za-z][A-Za-z ]*$|^$", message = "Pickup Relation Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    private String pickupRelationName;

//    @NotNull(message = "File is mandatory")
//    @NotEmpty(message = "File is mandatory")
    private FileSaveRqtDto authorizationLetter;

    @NotNull(message = "Shipment Type is mandatory")
    private Integer shipmentType;

//    @NotBlank(message = "Postal Tracking Id is mandatory")
    private String postalTrackingId;

    @NotNull(message = "Drug list is mandatory")
    @NotEmpty(message = "Drug list is mandatory")
    List<@Valid PharmaDispatchDrugDto> drugList;

}
