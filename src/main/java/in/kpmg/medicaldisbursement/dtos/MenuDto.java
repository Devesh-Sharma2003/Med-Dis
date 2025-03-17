package in.kpmg.medicaldisbursement.dtos;

import lombok.Data;

@Data
public class MenuDto {

    private String link;
    private Integer menuId;
    private String icon;

    private String name;
    private String nameTelugu;
    private Integer parentMenuId;
    private Boolean isDefault;
    private Integer createdBy;
    private Integer updatedBy;
    private Integer displayOrder;
    private Boolean isActive;
}
