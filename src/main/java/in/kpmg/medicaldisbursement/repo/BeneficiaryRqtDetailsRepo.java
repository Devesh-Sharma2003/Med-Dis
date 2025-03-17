package in.kpmg.medicaldisbursement.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.dtos.RequestDto.BeneficiaryPhysicianDrugFileSaveRqtDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.CountResponseDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.DashboardTableResponse;
import in.kpmg.medicaldisbursement.dtos.responseDto.InboxResponseDto;
import in.kpmg.medicaldisbursement.dtos.responseDto.PhysicianRspDto;
import in.kpmg.medicaldisbursement.models.BeneficiaryMst;
import in.kpmg.medicaldisbursement.models.BeneficiaryRequestDetails;

@Repository
public interface BeneficiaryRqtDetailsRepo extends JpaRepository<BeneficiaryRequestDetails, String>{

	@Query(value="select medical.md_request_number_generation()", nativeQuery = true)
	String generateMergedSequence();

	@Query(value="select b from BeneficiaryRequestDetails b where b.requestNumber=:requestNo")
	BeneficiaryRequestDetails findByRequestNo(String requestNo);
	
	@Query(nativeQuery = true,value="SELECT mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,\r\n"
			+ "mb.beneficiary_name AS beneficiaryName,mr.crt_on AS raisedOn, mr.shipment_type AS shipmentType, mgt.type_name AS shipmentTypeName,\r\n"
			+ "mg.type_name AS relation,sr.case_status_name AS status\r\n"
			+ "FROM medical.md_beneficiary_request_details mr\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst mb ON mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sr ON sr.step_role_id = mr.status\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mgt on mgt.type_id = mr.shipment_type \r\n"
			+ "JOIN medical.md_step_mst sm ON sm.step_id = sr.step_id AND sm.workflow_id = '1'\r\n"
			+ "WHERE sr.role_id=:roleId ORDER BY mr.crt_on ASC")
	List<InboxResponseDto> getInboxData(@Param("roleId") Integer roleId);
	
	@Query(value="SELECT mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,\r\n"
			+ "mb.beneficiary_name AS beneficiaryName,mr.crt_on AS raisedOn, mr.shipment_type AS shipmentType, mgt.type_name AS shipmentTypeName,\r\n"
			+ "mg.type_name AS relation,sr.case_status_name AS status\r\n"
			+ "FROM medical.md_beneficiary_request_details mr\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst mb ON mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sr ON sr.step_role_id = mr.status\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mgt on mgt.type_id = mr.shipment_type \r\n"
			+ "JOIN medical.md_step_mst sm ON sm.step_id = sr.step_id AND sm.workflow_id = '1'\r\n"
			+ "WHERE "
			+ "(:requestNumber is null or mr.request_number= CAST(:requestNumber AS VARCHAR))"
			+ "and (:startDate is null or TO_CHAR(mr.crt_on,'YYYY-MM-DD')>=CAST(CAST(:startDate AS TEXT) AS VARCHAR))"
			+ "and (:endDate is null or TO_CHAR(mr.crt_on,'YYYY-MM-DD')<=CAST(CAST(:endDate AS TEXT) AS VARCHAR)) "
			+ "and sr.role_id=:roleId AND mr.status in (7,8,22,24,26,28 ) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<InboxResponseDto> getRecievedMedicine(@Param("roleId") Integer roleId,
			@Param("requestNumber") String requestNumber,
			@Param("startDate") String startDate,
			@Param("endDate") String endDate);
	
	@Query(value="SELECT mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,\r\n"
			+ "mb.beneficiary_name AS beneficiaryName,mr.crt_on AS raisedOn, mr.shipment_type AS shipmentType, mgt.type_name AS shipmentTypeName,\r\n"
			+ "mg.type_name AS relation,sr.case_status_name AS status\r\n"
			+ "FROM medical.md_beneficiary_request_details mr\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst mb ON mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sr ON sr.step_role_id = mr.status\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mgt on mgt.type_id = mr.shipment_type \r\n"
			+ "JOIN medical.md_step_mst sm ON sm.step_id = sr.step_id AND sm.workflow_id = '1'\r\n"
			+ "WHERE "
			+ "(:requestNumber is null or mr.request_number= CAST(:requestNumber AS VARCHAR))"
			+ "and (:startDate is null or TO_CHAR(mr.crt_on,'YYYY-MM-DD')>=CAST(CAST(:startDate AS TEXT) AS VARCHAR))"
			+ "and (:endDate is null or TO_CHAR(mr.crt_on,'YYYY-MM-DD')<=CAST(CAST(:endDate AS TEXT) AS VARCHAR)) "
			+ "and sr.role_id=:roleId AND mr.status in (9,11,15,16) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<InboxResponseDto> getDispatchMedicines(@Param("roleId") Integer roleId,
			@Param("requestNumber") String requestNumber,
			@Param("startDate") String startDate,
			@Param("endDate") String endDate);
	
	@Query(nativeQuery = true,value="select mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName,\r\n"
			+ "mr.crt_on AS raisedOn,mr.shipment_type AS shipmentType, mgt.type_name AS shipmentTypeName, \r\n"
			+ "mg.type_name AS relation,sr.case_status_name AS status\r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mgt on mgt.type_id = mr.shipment_type \r\n"
			+ "left join medical.md_step_role_mapping sr on sr.step_role_id = mr.status\r\n"
			+ "where crt_by=:userId ORDER BY mr.crt_on ASC ")
	List<InboxResponseDto> getInboxDataMedco(@Param("userId") Integer userId);
	
	// Medco initiated count
	@Query(value = "select count(distinct mr.request_number) AS count, 1 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status = 1 or mra.status = 1)\r\n"
			+ "and coalesce(mr.crt_by,mra.crt_by)=:crtBy", nativeQuery = true)
	List<CountResponseDto> getMedcoInitiatedCount(@Param("crtBy") Integer userId);

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,dm.district_name AS district,mg.type_name AS relation,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status=:status or mra.status=:status)\r\n"
			+ "and coalesce(mr.crt_by,mra.crt_by)=:crtBy and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getMedcoInitiatedDetails(@Param("crtBy") Integer userId,
		    @Param("status") Integer status,
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Medco approved count
	@Query(value = "select distinct count(distinct mr.request_number) AS count,3 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status = 3 or mra.status = 3)\r\n"
			+ "and mr.crt_by=:crtBy", nativeQuery = true)
	List<CountResponseDto> getMedcoApprovedCount(@Param("crtBy") Integer userId);

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status = 3 or mra.status = 3) "
			+ "and coalesce(mr.crt_by,mra.crt_by)=:crtBy and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) "
			, nativeQuery = true)
	List<DashboardTableResponse> getMedcoApprovedDetails(@Param("crtBy") Integer userId,
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Medical Officer - Request initiated
	@Query(value = "select count(distinct mr.request_number)AS count,1 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status in (1,2) or mra.status in (1,2))", nativeQuery = true)
	List<CountResponseDto> getMORequestInitiatedCount();

	@Query(value = "select distinct mr.request_number AS requestNumber,mb.beneficiary_id AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status in (1,2) or mra.status in (1,2)) and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getMORequestInitiatedDetails(
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Medical Officer - Request approved by Medical officer
	@Query(value = "select count(distinct mr.request_number) AS count,3 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status in (3,5) or mra.status in (3,5))\r\n", nativeQuery = true)
	List<CountResponseDto> getMORequestApprovedCount();

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status in (3,5) or mra.status in (3,5)) and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getMORequestApprovedDetails(
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Medical Officer - Request dispatched by vendor to pharmacist
	@Query(value = "select count(distinct mr.request_number) AS count,7 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status in (7,8) or mra.status in (7,8)) ", nativeQuery = true)
	List<CountResponseDto> getMORequestDispatchedToPharmacistCount();

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status in (7,8) or mra.status in (7,8)) and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getMORequestDispatchedToPharmacistDetails(
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Medical Officer - Request dispatched to beneficiary
	@Query(value = "select count(distinct mr.request_number) AS count,15 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status in (15,16) or mra.status in (15,16))", nativeQuery = true)
	List<CountResponseDto> getMORequestDispatchedToBeneficiaryCount();

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id where 1=1 and (mr.status in (15,16) or mra.status in (15,16)) and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getMORequestDispatchedToBeneficiaryDetails(
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Medical Officer - Request delivered
	@Query(value = "select count(distinct mr.request_number) AS count,17 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status in (17,18) or mra.status in (17,18)) ", nativeQuery = true)
	List<CountResponseDto> getMORequestDeliveredCount();

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId, mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status in (17,18) or mra.status in (17,18)) and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getMORequestDeliveredDetails(
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Vendor - Requests Raised
	@Query(value = "select count(distinct mr.request_number) AS count,3 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status in (3,5) or mra.status in (3,5)) ", nativeQuery = true)
	List<CountResponseDto> getVendorRequestsRaisedCount();

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status in (3,5) or mra.status in (3,5)) and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getVendorRequestsRaisedDetails(
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Vendor - Requests Dispatched
	@Query(value = "select count(distinct mr.request_number) AS count,15 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status in (15,16) or mra.status in (15,16)) ", nativeQuery = true)
	List<CountResponseDto> getVendorRequestsDispatchedCount();

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status in (15,16) or mra.status in (15,16)) and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getVendorRequestsDispatchedDetails(
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Vendor - Requests Delivered
	@Query(value = "select count(distinct mr.request_number) AS count,17 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status in (17,18) or mra.status in (17,18))", nativeQuery = true)
	List<CountResponseDto> getVendorRequestsDeliveredCount();

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status in (17,18) or mra.status in (17,18)) and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getVendorRequestsDeliveredDetails(
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Pharmacist - Request initiated
	@Query(value = "select count(distinct mr.request_number) AS count,2 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status = 2 or mra.status = 2)\r\n"
			+ "and mr.crt_by=:crtBy", nativeQuery = true)
	List<CountResponseDto> getPharmacistRequestInitiatedCount(@Param("crtBy") Integer userId);

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status = 2 or mra.status = 2) AND coalesce(mr.crt_by,mra.crt_by)=:crtBy and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getPharmacistRequestInitiatedDetails(@Param("crtBy") Integer userId,
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Pharmacist - Request approved by Medical officer
	@Query(value = "select count(distinct mr.request_number) AS count,5 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status = 5 or mra.status = 5)\r\n"
			+ "and mr.crt_by=:crtBy", nativeQuery = true)
	List<CountResponseDto> getPharmacistRequestApprovedCount(@Param("crtBy") Integer userId);

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status = 5 or mra.status = 5) AND coalesce(mr.crt_by,mra.crt_by)=:crtBy and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getPharmacistRequestApprovedDetails(@Param("crtBy") Integer userId,
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Pharmacist - Request dispatched by vendor to pharmacist
	@Query(value = "select count(distinct mr.request_number) AS count,7 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status in (7,8) or mra.status in (7,8))", nativeQuery = true)
	List<CountResponseDto> getPharmacistRequestDispatchedToPharmacistCount();

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status in (7,8) or mra.status in (7,8)) and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getPharmacistRequestDispatchedToPharmacistDetails(
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Pharmacist - Requests Dispatched to beneficiary
	@Query(value = "select count(distinct mr.request_number) AS count,15 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status in (15,16) or mra.status in (15,16))", nativeQuery = true)
	List<CountResponseDto> getPharmacistRequestDispatchedToBeneficiaryCount();

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status in (15,16) or mra.status in (15,16)) and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getPharmacistRequestDispatchedToBeneficiaryDetails(
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);

	// Pharmacist - Requests Delivered
	@Query(value = "select count(distinct mr.request_number) AS count,17 AS status from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "where 1=1 and (mr.status in (17,18) or mra.status in (17,18))", nativeQuery = true)
	List<CountResponseDto> getPharmacistRequestDeliveredCount();

	@Query(value = "select distinct mr.request_number AS requestNumber,COALESCE(mb.beneficiary_id, parent_mb.beneficiary_id) AS beneficiaryId,mb.beneficiary_name AS beneficiaryName, mr.status AS statusId, sm.case_status_name AS status,\r\n"
			+ "mb.beneficiary_phone_no AS beneMobileNo,mg.type_name AS relation,dm.district_name AS district,mr.crt_on AS raisedOn \r\n"
			+ "from medical.md_beneficiary_request_details mr\r\n"
			+ "left join medical.md_beneficiary_mst mb on mb.id = mr.beneficiary_id\r\n"
			+ "LEFT JOIN medical.md_beneficiary_mst parent_mb ON parent_mb.id = mb.parent_benficiary_id\r\n"
			+ "LEFT JOIN medical.md_general_type_mst mg ON mg.type_id = COALESCE(mb.relation, parent_mb.relation)\r\n"
			+ "left join medical.md_beneficiary_request_details_audit mra on mr.request_number = mra.request_number\r\n"
			+ "left join medical.md_district_mst dm on dm.id = mb.district_id\r\n"
			+ "LEFT JOIN medical.md_step_role_mapping sm ON sm.step_role_id = mr.status\r\n"
			+ "where 1=1 and (mr.status in (17,18) or mra.status in (17,18)) and (:requestNumber is null or coalesce(mr.request_number,mra.request_number)= CAST(:requestNumber AS VARCHAR))"
			+ "and (:beneficiaryId is null or mb.beneficiary_id = CAST(:beneficiaryId AS CHAR))"
			+ "and (:crtOnStart is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')>=CAST(CAST(:crtOnStart AS TEXT) AS VARCHAR))"
			+ "and (:crtOnEnd is null or TO_CHAR(coalesce(mr.crt_on,mra.crt_on),'YYYY-MM-DD')<=CAST(CAST(:crtOnEnd AS TEXT) AS VARCHAR)) ORDER BY mr.crt_on ASC"
			, nativeQuery = true)
	List<DashboardTableResponse> getPharmacistRequestDeliveredDetails(
		    @Param("requestNumber") String requestNumber,
		    @Param("beneficiaryId") String beneficiaryId,
		    @Param("crtOnStart") String crtOnStart,
		    @Param("crtOnEnd") String crtOnEnd);
	
	
	@Query(value="select b.beneficiaryId from BeneficiaryRequestDetails b where b.requestNumber=:requestNumber")
	BeneficiaryMst findBene(@Param("requestNumber") String requestNumber);
	
	

	
}
