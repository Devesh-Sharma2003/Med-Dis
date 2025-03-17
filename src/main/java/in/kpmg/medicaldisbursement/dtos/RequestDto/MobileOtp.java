package in.kpmg.medicaldisbursement.dtos.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileOtp {

	private Integer otp;
	
	private Integer userId;
}
