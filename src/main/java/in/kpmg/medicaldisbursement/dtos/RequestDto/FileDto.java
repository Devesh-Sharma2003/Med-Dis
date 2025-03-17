package in.kpmg.medicaldisbursement.dtos.RequestDto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
	
	@NotNull(message = "virtual path should not be null")
	String virtualPath;

	@NotNull(message = "appNo should not be null")
	String appNo;

}
