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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical.md_beneficiary_request_drug_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryRequestDrug {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_number", referencedColumnName = "request_number")
	private BeneficiaryRequestDetails requestNumber;
	
	@Column(name = "drug_name")
//	@Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", message = "Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
	private String drugName;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name="remarks")
//	@NotBlank(message = "Remarks must not be blank!")
//	@Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$")
	private String remarks;
	
	@Column(name="strength")
	private String strength;
	
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
//	@Column(name = "upd_by")
	private UserMst updatedBy;

	@Column(name = "upd_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	@UpdateTimestamp
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
	
	@Column(name="postal_tracking_id")
	private String postalTrackingId;
	
	@Column(name="dispatch_date")
	private Date dispatchDate;
	
	@Column(name="brand_name")
//	@Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", message = "Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
	private String brandName;
	
	@Column(name="discounted_price")
	private Integer discountedPrice;
	
	@Column(name="unit")
	private Integer unit;
	
	@Column(name="unit_text")
	private String unitText;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "drug_id", referencedColumnName = "drug_id")
	private DrugMst drugs;
}
