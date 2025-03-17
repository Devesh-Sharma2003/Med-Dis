package in.kpmg.medicaldisbursement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.dtos.responseDto.RemarksOutputDto;
import in.kpmg.medicaldisbursement.models.BeneficiaryRequestAudit;

@Repository
public interface AuditRepo extends JpaRepository<BeneficiaryRequestAudit, Integer>{
	
	@Query(nativeQuery=true,value="select * from (\r\n"
			+ "select (select  distinct yrm.role_name	from medical.md_step_flow_mapping asfm, \r\n"
			+ "			medical.md_step_role_mapping asrm1, medical.md_role_mst yrm where yrm.role_id = asrm1.role_id  \r\n"
			+ "			and  asfm.next_step_role_id  = dc.status\r\n"
			+ "			and asfm.initial_step_role_id = asrm1.step_role_id\r\n"
			+ "			group by asfm.initial_step_role_id, yrm.role_name\r\n"
			+ "			 )\r\n"
			+ "				 || ' , ' || user_name actionBy,coalesce(dc.upd_on,dc.crt_on) actionOn,sm.case_status_name AS status,dc.remarks\r\n"
			+ "from medical.md_beneficiary_request_details dc\r\n"
			+ "left join medical.md_user_mst yum on yum.user_id = coalesce(dc.upd_by,dc.crt_by)\r\n"
			+ "left join medical.md_step_role_mapping sm on sm.step_role_id = dc.status\r\n"
			+ "where dc.request_number=:requestNumber\r\n"
			+ "union all \r\n"
			+ "select (select  distinct yrm.role_name	from medical.md_step_flow_mapping asfm, \r\n"
			+ "			medical.md_step_role_mapping asrm1, medical.md_role_mst yrm where yrm.role_id = asrm1.role_id  \r\n"
			+ "			and  asfm.next_step_role_id  = dc.status\r\n"
			+ "			and asfm.initial_step_role_id = asrm1.step_role_id\r\n"
			+ "			group by asfm.initial_step_role_id, yrm.role_name\r\n"
			+ "			 )\r\n"
			+ "				 || ' , ' || user_name actionBy,coalesce(dc.upd_on,dc.crt_on) actionOn,sm.case_status_name AS status,dc.remarks\r\n"
			+ "from medical.md_beneficiary_request_details_audit dc\r\n"
			+ "left join medical.md_user_mst yum on yum.user_id = coalesce(dc.upd_by,dc.crt_by)\r\n"
			+ "left join medical.md_step_role_mapping sm on sm.step_role_id = dc.status\r\n"
			+ "where dc.request_number=:requestNumber ) as b\r\n"
			+ "order by b.actionOn")
	  List<RemarksOutputDto> getAudit(@Param("requestNumber") String requestNumber);

}
