package in.kpmg.medicaldisbursement.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralMasterSaveDto {

    private Long typeId;
    private String typeName;
    private String typeNameRegLang;
    private String typeCode;
    private Integer generalTypeId;
    private Long parentTypeId;
    private Boolean isActive;
    private Integer createdBy;
    private Integer updatedBy;
}
