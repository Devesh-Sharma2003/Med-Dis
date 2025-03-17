package in.kpmg.medicaldisbursement.dtos.RequestDto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class VendorDrugActionDto {

    @NotNull(message = "Drug Id is required")
    @Min(value = 0, message = "Drug Id must be greater than or equal to 0")
    private Integer drugId;

    @NotBlank(message = "Batch Number is required")
    @NotNull
    private String batchNo;

    @NotBlank(message = "Manufactured Date is required")
    @NotNull
    private String manufacturedDate;

    @NotBlank(message = "Expiry Date is required")
    @NotNull
    private String expiryDate;

    @NotNull(message = "MRP is required")
    @Min(value = 1, message = "MRP must be greater than or equal to 1")
    private Integer mrp;

    @NotBlank(message = "Brand Name is required")
    @Pattern(regexp = "^[A-Za-z][A-Za-z ]*$", message = "Brand Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    private String brandName;
}
