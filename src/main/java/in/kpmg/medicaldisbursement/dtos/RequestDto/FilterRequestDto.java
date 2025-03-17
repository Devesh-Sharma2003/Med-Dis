package in.kpmg.medicaldisbursement.dtos.RequestDto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequestDto {
	
	private Integer userId;
	private Integer status;
	private String requestNumber;
	private String beneficiaryId;
	private String startDate;
	private String endDate;

}
