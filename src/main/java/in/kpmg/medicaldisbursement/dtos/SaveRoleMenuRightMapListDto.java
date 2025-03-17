package in.kpmg.medicaldisbursement.dtos;

import lombok.Data;

@Data
public class SaveRoleMenuRightMapListDto {

    private MenuDto menuDtl;

    private RoleDto roleDtls;
    private Integer createdBy;
    private Integer updatedBy;

    private Boolean isEdit;
    private Boolean isDownload;

    private Boolean isView;
    private Integer rightId;
}
