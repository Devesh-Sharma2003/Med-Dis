package in.kpmg.medicaldisbursement.dtos.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepActionResponse {
	private Integer actionId;
	private String action;
	private Integer stepId;
	private String stepName;

}
