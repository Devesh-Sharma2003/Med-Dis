package in.kpmg.medicaldisbursement.dtos;

public interface RoleMenuRightProj {
     Integer getRightId();

     Integer getRoleId();

     Integer getMenuId();

     Boolean getIsView();

     Boolean getIsEdit();

     Boolean getIsDownload();

     String getLink();

     String getIcon();

     String getMenuName();

     String getMenuCode();

     Integer getParentMenuId();

     Integer getDisplayOrder();

}
