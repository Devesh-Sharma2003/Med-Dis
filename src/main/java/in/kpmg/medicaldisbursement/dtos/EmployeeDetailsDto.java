package in.kpmg.medicaldisbursement.dtos;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailsDto {

	private String empId;
	private Integer beneId;
	private Integer userId;
	private Integer actionId;
	private Long empMobileNo;
	private String requestNumber;
	private String startDate;
	private String endDate;
	private String endpoint;
	
}
