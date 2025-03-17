package in.kpmg.medicaldisbursement.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse2<T>{
    private Boolean status;
    private String message;
    private Object result;
    private Integer statusCode;

    public ApiResponse2(Boolean status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }
}

