package in.kpmg.medicaldisbursement.dtos.RequestDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeneficiaryPhysicianDrugFileSaveRqtDto {

    private String requestNumber;

    private Integer beneficiaryId;    //rd , beneficiaryMst

    @Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", message = "Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    private String name;

    private Integer parentId;

    @NotNull(message = "Relation must not be null!")
    private Integer relation;

    @NotBlank(message = "Contact Number must not be blank!")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile number must be 10 digits and should start with a digit between 6 to 9")
    private String mobileNo;

    @Pattern(regexp = "^$|^[6-9][0-9]{9}$", message = "Mobile number must be 10 digits and should start with a digit between 6 to 9")
    private String mobileAltNo;
    
//    @NotNull(message = "Designation Id is mandatory")
//    @Min(value = 0, message = "Designation Id must be greater than or equal to 0")
    private Integer designationId;

    @NotBlank(message = "DOB must not be blank!")
    private String dob;

    @NotNull(message = "Gender must not be blank!")
    private Integer gender;

    @Pattern( regexp = "^$|^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email address")
    private String email;

    @NotBlank(message = "House Number must not be blank!")
//    @Pattern(regexp = "^[a-zA-Z0-9\\s./-]+$", message = "House No name can only contain letters, numbers, spaces, hyphens, and periods")
    private String houseNo;

    @NotBlank(message = "street must not be blank!")
//	@Pattern(regexp = "^[A-Za-z0-9/, ]$")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "Street name can only contain letters, numbers, spaces, hyphens, and periods")
    private String street;

    //	@Pattern(regexp = "^[A-Za-z0-9/,-. ]$")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "Landmark name can only contain letters, numbers, spaces, hyphens, and periods")
    private String landmark;

    @NotNull(message = "State must not be blank!")
    private Integer state;

    @NotNull(message = "District must not be blank!")
    private Integer district;

    @NotNull(message = "Mandal must not be blank!")
    private Integer mandal;

    @NotNull(message = "Village must not be blank!")
    private Integer village;

    @NotNull(message = "Pincode must not be blank!")
    private Long pincode;

    FileSaveRqtDto authorizationLetter;

    @Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", message = "Doctor Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    private String doctorName;         //rd

    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile number must be 10 digits and should start with a digit between 6 to 9")
    private String doctorPhoneNo;       //rd

    //	@Pattern(regexp = "^[A-Za-z][A-Za-z ]*$", message = "Remarks must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    @NotBlank(message = "Remarks must not be blank!")
//    @Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$")
    // @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "Remarks can only contain letters, numbers, spaces, hyphens, and periods")
    private String remarks;          //rd

    private Long status;             //rd

    private Integer crtBy;           //rd

    private Integer uptBy;           //rd

    private Integer shipmentType;      //rd

    @Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", message = "Authorized Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    private String authorizedName;

    private Integer requestedPersonType;

    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile number must be 10 digits and should start with a digit between 6 to 9")
    private String authorizedPersonMob;

    @NotNull(message = "Drug Details are mandatory")
    @NotEmpty(message = "Drug Details are mandatory")
    List<@Valid DrugSaveRqtDto> drugList;

    @NotNull(message = "File is mandatory")
    @NotEmpty(message = "File is mandatory")
    List<@Valid FileSaveRqtDto> fileList;
}
