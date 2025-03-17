package in.kpmg.medicaldisbursement.dtos;

import lombok.Data;

@Data
public class DeptDto {
    private Integer deptId;
    private String name;
    private String deptNameTelugu;
    private String code;
    private Integer createdBy;
    private Integer updatedBy;
}
