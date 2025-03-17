package in.kpmg.medicaldisbursement.dtos.RequestDto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class MedicalOfficerDrugDto {

    @NotNull(message = "Drug Id is required")
    @Min(value = 0, message = "Drug Id must be greater than or equal to 0")
    private Integer drugId;

    @NotBlank(message = "Drug Name is required")
    @Pattern(regexp = "^[A-Za-z][A-Za-z ]*$", message = "Drug Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    private String drugName;

    @NotNull(message = "Recommended Quantity is required")
    @Min(value = 1, message = "Recommended Quantity must be greater than 0")
    private Integer recommendedQuantity;

    @NotBlank(message = "Remarks are required")
    // @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "Remarks can only contain letters, numbers, spaces, hyphens, and periods")
    private String remarks;

    @NotNull(message = "Drug Type is required")
    @Min(value = 0, message = "Drug Type must be greater than or equal to 0")
    private Integer drugType;
}
