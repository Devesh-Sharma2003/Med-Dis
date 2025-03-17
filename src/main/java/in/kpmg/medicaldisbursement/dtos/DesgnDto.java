package in.kpmg.medicaldisbursement.dtos;

import lombok.Data;

@Data
public class DesgnDto {
    private Integer id;
    private String name;
    private String nameTelugu;
    private String code;
    private Boolean isActive;
    private Integer createdBy;
    private Integer updatedBy;
}
