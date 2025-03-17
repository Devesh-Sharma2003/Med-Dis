//package in.kpmg.medicaldisbursement.mapper;
//
//import java.time.LocalDate;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Named;
//import org.mapstruct.factory.Mappers;
//
//import in.kpmg.medicaldisbursement.dtos.responseDto.PhysicianRspDto;
//import in.kpmg.medicaldisbursement.models.BeneficiaryMst;
//
//@Mapper(componentModel = "spring")
//public interface BeneficiaryToDtoMapper {
//	
//	BeneficiaryToDtoMapper MAPPER = Mappers.getMapper(BeneficiaryToDtoMapper.class);
//	
//	@Mapping(source="villageId.villageId", target="villageId")
//	@Mapping(source="mandalId.mandalId", target="mandalId")
//	@Mapping(source="stateId.stateId", target="stateId")
//	@Mapping(source="districtId.distId", target="districtId")
//	@Mapping(source="relation.typeId", target="relationId")
//	@Mapping(source="gender.typeId", target="genderId")
//	@Mapping(source="beneficiaryDob", target="dob", qualifiedByName="dateToString")
//	@Mapping(source="alternateContactNumber", target="beneficiaryMobileAltNo")
//	@Mapping(source="beneficiaryPhoneNo", target="beneficiarymobileNo")
//	@Mapping(source="id", target="beneficiaryId")
//	PhysicianRspDto beneficiaryMstToDto(BeneficiaryMst obj);
//	
//	
//	@Named("dateToString")
//	default String dateToString(LocalDate date) {
//		return date.toString();
//	}
//	
//	
//}
