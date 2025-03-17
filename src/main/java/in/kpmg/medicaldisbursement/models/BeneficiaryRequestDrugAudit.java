package in.kpmg.medicaldisbursement.models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical.md_beneficiary_request_drug_details_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryRequestDrugAudit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audit_id")
	private Integer auditId;
	
	@Column(name = "audited_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	private Timestamp auditedOn;
	
	@Column(name = "id")
	private Integer id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_number", referencedColumnName = "request_number")
	private BeneficiaryRequestDetails requestNumber;
	
	@Column(name = "drug_name")
	private String drugName;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name="remarks")
	private String remarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status",referencedColumnName = "step_role_id")
	private StepRoleMapping status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crt_by", referencedColumnName = "user_id")
	private UserMst createdBy;

	@Column(name = "crt_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	@CreationTimestamp
	private Timestamp createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upd_by", referencedColumnName = "user_id")
	private UserMst updatedBy;

	@Column(name = "upd_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	private Timestamp updatedOn;
	
	@Column(name="drug_type")
	private Integer drugType;
	
	@Column(name="batch_no")
	private String batchNo;
	
	@Column(name="manufactured_date")
	private Date manufacturedDate;
	
	@Column(name="expiry_date")
	private Date expiryDate;
	
	@Column(name="mrp")
	private Integer mrp;
	
	@Column(name="is_shipped")
	private Boolean isShipped;
	
	@Column(name="is_acknowledged")
	private Boolean isAcknowledged;
	
	@Column(name="dispatch_id")
	private String dispatchId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "drug_id", referencedColumnName = "drug_id")
	private DrugMst drugs;

}
