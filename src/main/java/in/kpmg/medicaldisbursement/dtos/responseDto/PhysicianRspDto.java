package in.kpmg.medicaldisbursement.dtos.responseDto;

import java.util.List;

import in.kpmg.medicaldisbursement.dtos.RequestDto.FileSaveRqtDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhysicianRspDto {
	
	private Integer beneficiaryId;
	
	private String beneficiaryName;
	
	private String indentId;
	
	private String empId;
	
	private String empName;
	
	private Long BeneficiarymobileNo;
	
	private Long BeneficiaryMobileAltNo;
	
	private Integer BeneficiaryDesignationId; 
	
	private String designtion;
	
	private String dob;
	
	private Integer genderId;
	
	private String gender;
	
	private Integer relationId;
	
	private String relation;

	private String email;
	
	private String houseNo;
	
	private String landmark;
	
	private String locality;
	
	private Integer stateId;
	
	private String state;
	
	private Integer districtId;
	
	private String district;
	
	private Integer mandalId;
	
	private String mandal;
	
	private Integer villageId;
	
	private String village;
	
	private Long pincode;
	
	private String physicianName;
	
	private Long physicianMobileNo;
	
	private Long physicianEmpAltNo;
	
	private String remarks;
	
	private Long status;
	
	private Integer crtBy;
	
	private Integer uptBy;
	
	private Integer shipmentType;      //rd
	
	private Integer requestedPersonType;
	
	private String authorizedPersonName;
	
	private Long authorizedPersonMobNo;
	
	private String pickupPersonName;
	
	private Long pickupPersonMobileNo;
	
	private Integer pickupRelation;
	
	private List<DrugRspDto> drugList;
	
	private List<FileRspDto> fileList;
	
}
