package in.kpmg.medicaldisbursement.dtos;

public interface RoleMenuRightMapDto {
    Integer getMenuId();

    Integer getRightId();

    String getMenuName();

    Integer getParentMenuId();

    String getParentMenuName();

    Boolean getIsView();

    Boolean getIsEdit();

    Boolean getIsDownload();
}
