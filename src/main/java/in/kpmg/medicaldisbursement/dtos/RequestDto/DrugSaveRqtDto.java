package in.kpmg.medicaldisbursement.dtos.RequestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugSaveRqtDto {

	@NotBlank(message = "Drug Name is required")
	@Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 ]*$", message = "Drug name must start with a letter or number and can contain letters, numbers, and spaces")
	private String drugName;

	@NotNull(message = "No. of Tablets is required")
	@Min(value = 1, message = "No. of Tablets must be greater than 0")
	private Integer noOfTablets;
	
	@NotNull(message = "Drug Type Id is required")
	@Min(value = 0, message = "Drug Type Id must be greater than or equal to 0")
	private Integer drugTypeId;
	
	@NotBlank(message = "Strength is required")
	private String strength;
	
	@NotNull(message = "Drug Unit Id is required")
	@Min(value = 0, message = "Drug Unit Id must be greater than or equal to 0")
	private Integer drugUnitId;

//	@NotBlank(message = "Drug Unit Name is required")
	private String drugUnitName;

}
