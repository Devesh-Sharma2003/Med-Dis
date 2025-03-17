package in.kpmg.medicaldisbursement.dtos.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugSaveRspDto {
	
	private String requestNo;
	
	private String drugName;
	
	private Integer noOfTablets;
	
	private String remarks;
	
	private Long status;
	
	private Integer crtBy;
	
	private Integer updBy;

}
