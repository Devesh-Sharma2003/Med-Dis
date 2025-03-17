package in.kpmg.medicaldisbursement.dtos.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugRspDto {
	
	private Integer drugId;
	
	private String drugName;
	
	private Integer recommendedQuantity;
	
	private Integer indentedQuantity;
	
	private Integer drugType;
	
	private String drugTypeName;
	
	private String remarks;
	
	private Long status;
	
	private String batchNo;
	
	private String manufacturedDate;
	
	private String expiryDate;
	
	private Integer mrp;
	
	private String postalId;
	
	private String dispatchDate;
	
	private Boolean isShipped;
	
	private Boolean isAcknowledged;

	private String strength;
	
	private String brandName;
	
	private Integer discountedPrice;
	
	private Integer drugUnitId;
	
	private String drugUnitName;
}
