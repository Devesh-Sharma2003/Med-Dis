package in.kpmg.medicaldisbursement.dtos.RequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadPostalDto {
	
	private String requestNo;
	
//	@NotBlank(message = "otp must not be blank!")
	@Pattern(regexp = "^[0-9]{6}$", message = "otp must be 6 digits")
	private String otp;
	
	private Integer actionId;   //4
	
	private Integer UserId;

	private FileSaveRqtDto postalUpload;
}
