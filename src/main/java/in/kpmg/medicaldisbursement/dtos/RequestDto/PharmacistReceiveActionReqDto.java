package in.kpmg.medicaldisbursement.dtos.RequestDto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PharmacistReceiveActionReqDto {

    private String requestNumber;

    private Integer actionId;

    private Integer userId;

    @NotBlank(message = "Remarks must not be blank!")
//    @Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$")
    // @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "Remarks can only contain letters, numbers, spaces, hyphens, and periods")
    private String remarks;

}
