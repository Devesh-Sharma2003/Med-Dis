package in.kpmg.medicaldisbursement.dtos.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorDrugDetailDto {

    @NotNull(message = "Drug Id is required")
    @Min(value = 0, message = "Drug Id must be greater than or equal to 0")
    private Integer drugId;

    @NotBlank(message = "Drug Name is required")
    @Pattern(regexp = "^[A-Za-z][A-Za-z ]*$", message = "Drug Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    private String drugName;

    @NotBlank(message = "Remarks are required")
    // @Pattern(regexp = "^[a-zA-Z0-9\\s.-]+$", message = "Remarks can only contain letters, numbers, spaces, hyphens, and periods")
    private String remarks;

    @NotNull(message = "Drug Type is required")
    @Min(value = 0, message = "Drug Type must be greater than or equal to 0")
    private Integer drugType;

    @NotNull(message = "Recommended Quantity is required")
    @Min(value = 0, message = "Recommended Quantity must be greater than or equal to 0")
    private Integer recommendedQuantity;

    @NotBlank(message = "Batch Number is required")
    private String batchNo;

    @NotBlank(message = "Manufactured Date is required")
    private String manufacturedDate;

    @NotBlank(message = "Expiry Date is required")
    private String expiryDate;

    @NotNull(message = "MRP is required")
    @Min(value = 1, message = "MRP must be greater than or equal to 1")
    private Integer mrp;

    @NotNull(message = "Is Shipped is required")
    private Boolean isShipped;

    @NotBlank(message = "Strength is required")
    private String strength;

    @NotBlank(message = "Brand Name is required")
    @Pattern(regexp = "^[A-Za-z][A-Za-z ]*$", message = "Brand Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
    private String brandName;

    // private Integer discountedPrice;
    @NotNull(message = "Is Acknowledged is required")
    private Boolean isAcknowledged;
}
