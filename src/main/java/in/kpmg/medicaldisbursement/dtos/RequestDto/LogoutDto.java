package in.kpmg.medicaldisbursement.dtos.RequestDto;

import javax.validation.constraints.Null;

import lombok.Data;

@Data
public class LogoutDto {

//	@Null(message="User Id should not be null")
	private Integer userId;
	
//	@Null(message="Role Id should not be null")
	private Integer roleId;
}
