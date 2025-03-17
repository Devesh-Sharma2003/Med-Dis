package in.kpmg.medicaldisbursement.dtos.responseDto;

public interface InboxResponseDto {
	String getRequestNumber();
	String getBeneficiaryId();
	String getBeneficiaryName();
	String getRaisedOn();
	String getStatus();
	String getRelation();
	Integer getShipmentType();
	String getShipmentTypeName();
}
