package in.kpmg.medicaldisbursement.dtos.RequestDto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PharmaDispatchDrugDto {
    @NotNull(message = "Drug Id is required")
    @Min(value = 0, message = "Drug Id must be greater than or equal to 0")
    private Integer drugId;

}
