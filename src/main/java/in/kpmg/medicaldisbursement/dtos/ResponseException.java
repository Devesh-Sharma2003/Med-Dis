package in.kpmg.medicaldisbursement.dtos;

import lombok.Data;

@Data
public class ResponseException {
	
	private String msg;
	private String code;

}
