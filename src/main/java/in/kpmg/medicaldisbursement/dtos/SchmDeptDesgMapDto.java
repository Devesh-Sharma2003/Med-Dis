package in.kpmg.medicaldisbursement.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchmDeptDesgMapDto {
    private Integer mappingId;
    private DeptDto department;
    private UserDto crtBy;
    private UserDto updBy;
    private SchemeDto scheme;
    private Boolean isActive;
    private RoleDto role;
}
