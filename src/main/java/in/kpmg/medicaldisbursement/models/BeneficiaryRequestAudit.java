package in.kpmg.medicaldisbursement.models;

import java.sql.Timestamp;

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
@Table(name = "medical.md_beneficiary_request_details_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryRequestAudit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audit_id")
	private Integer auditId;
	
	@Column(name = "audited_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	private Timestamp auditedOn;
	
	@Column(name = "request_number")
	private String requestNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "beneficiary_id",referencedColumnName = "id")
	private BeneficiaryMst beneficiaryId;
	
	@Column(name="doctor_name")
	private String doctorName;
	
	@Column(name="doctor_mobile_num")
	private Long doctorMobileNo;
	
	@Column(name="emp_alt_contact")
	private Long empAltContact;
	
	@Column(name="postal_tracking_id")
	private String postalTrackingId;
	
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
	
	@Column(name="shipment_type")
	private Integer shipmentType;

}
