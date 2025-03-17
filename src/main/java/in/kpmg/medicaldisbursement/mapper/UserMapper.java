//package in.kpmg.medicaldisbursement.mapper;
//
//
//import in.kpmg.medicaldisbursement.dtos.UserDto;
//import in.kpmg.medicaldisbursement.models.UserMst;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.factory.Mappers;
//
//@Mapper(componentModel = "spring")
//public interface UserMapper {
//
//	UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
//
//	// @Mapping(target = "schmDeptDsgMaps", source = "userDto.schmDeptDsgMaps")
//	// @Mapping(target = "crtBy.schmDeptDsgMaps", ignore = true)
////	UserMst mapToModel(UserDto userDto);
////
////	UserDto modelToDto(UserMst user);
//
//	// @Named("mapSchDept")
//	// default List<UserDeptDesgMap> mapSchDept(UserSchmDeptDesgMapDto
//	// schmDeptDesgMapDto) {
//	// List<UserDeptDesgMap> scheDeps = new ArrayList<>();
//	// for (UserDeptDesgMap sDesgMap : scheDeps) {
//	// sDesgMap.setStartDate(java.sql.Date.valueOf(schmDeptDesgMapDto.getStartDate()));
//	// sDesgMap.setEndDate(java.sql.Date.valueOf(schmDeptDesgMapDto.getEndDate()));
//	// }
//	// return scheDeps;
//	// }
//
//}
