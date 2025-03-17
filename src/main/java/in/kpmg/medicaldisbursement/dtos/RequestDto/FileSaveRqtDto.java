package in.kpmg.medicaldisbursement.dtos.RequestDto;

import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileSaveRqtDto {

	@NotNull(message = "Attachment Type is mandatory")
	private Integer attachmentType;

	@NotBlank(message = "File Name is mandatory")
	private String fileName;

	@NotBlank(message = "File Path is mandatory")
	private String filePath;

}
