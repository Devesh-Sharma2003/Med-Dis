package in.kpmg.medicaldisbursement.external.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {
    private String message;
    private Object result;
    private Boolean status;
    private Integer statusCode;

}