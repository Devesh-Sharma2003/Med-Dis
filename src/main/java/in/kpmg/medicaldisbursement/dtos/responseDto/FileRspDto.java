package in.kpmg.medicaldisbursement.dtos.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileRspDto {
	
	private Integer attachmentType;
	
	private String fileName;
	
	private String filePath;

}
