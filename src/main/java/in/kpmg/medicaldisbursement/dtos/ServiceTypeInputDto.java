package in.kpmg.medicaldisbursement.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTypeInputDto {


    private Integer id;
    private String serviceType;
    private String serviceTypeRegLang;
    private Integer employeeTypeCode;
    private Integer employeeTypeName;
    private String serviceTypeCode;
    private String description;
    private String descRegLang;
    private String remarksEng;
    private String remarksRegLang;
    private Timestamp effectiveFrom;
    private Timestamp effectiveTo;
    private Integer displayOrder;
    private Boolean isActive;
}
