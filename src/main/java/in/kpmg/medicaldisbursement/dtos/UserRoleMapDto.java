package in.kpmg.medicaldisbursement.dtos;

import lombok.Data;

@Data
public class UserRoleMapDto {

    private Integer roleMapId;


    private RoleDto role;

    private Boolean isActive;
}
