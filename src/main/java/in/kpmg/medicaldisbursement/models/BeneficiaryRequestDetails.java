package in.kpmg.medicaldisbursement.models;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical.md_beneficiary_request_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryRequestDetails {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_number")
	private String requestNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "beneficiary_id",referencedColumnName = "id")
//	@Column(name = "beneficiary_id")
	private BeneficiaryMst beneficiaryId;
	
	@Column(name="doctor_name")
//	@Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", message = "Doctor Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
	private String doctorName;
	
	@Column(name="doctor_mobile_num")
	@NotNull(message = "Contact Number must not be blank!")
//    @Pattern(regexp = "^[1-9][0-9]{9}$", message = "Mobile number must be 10 digits and should not start with 0")
	private Long doctorMobileNo;
	
	@Column(name="emp_alt_contact")
//	@Pattern(regexp = "^[1-9][0-9]{9}$", message = "Mobile number must be 10 digits and should not start with 0")
	private Long empAltContact;
	
	@Column(name="remarks")
//	@NotBlank(message = "Remarks must not be blank!")
//	@Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$")
	private String remarks;
	
	@OneToOne
	@JoinColumn(name = "status",referencedColumnName = "step_role_id")
	private StepRoleMapping status;
	
	@ManyToOne
	@JoinColumn(name = "crt_by", referencedColumnName = "user_id")
	private UserMst createdBy;

	@Column(name = "crt_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	@CreationTimestamp
	private Timestamp createdOn;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "upd_by", referencedColumnName = "user_id")
	private UserMst updatedBy;

	@Column(name = "upd_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	@UpdateTimestamp
	private Timestamp updatedOn;
	
	@Column(name="shipment_type")
	private Integer shipmentType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="requested_person_type", referencedColumnName = "type_id")
//	@Column(name="requested_person_type")
	private GeneralTypeMst requestedPersonType;
	
	@Column(name="authorised_person_name")
//	@Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", messa@ge = "Authorized Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
	private String authorizedPersonName; 
	
	@Column(name="authorised_person_mob")
//	@Pattern(regexp = "^[1-9][0-9]{9}$", message = "Mobile number must be 10 digits and should not start with 0")
	private Long authorizedPersonNo;

	@Column(name="pickup_person_name")
//	@Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", message = "Authorized Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
	private String pickupPersonName;
	
	@Column(name="pickup_contact_number")
//	@Pattern(regexp = "^[1-9][0-9]{9}$", message = "Mobile number must be 10 digits and should not start with 0")
	private Long pickupContactNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="pickup_relation", referencedColumnName = "type_id")
	private GeneralTypeMst pickupRelation;
	
	@Column(name="pickup_relation_name")
//	@Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", message = "Authorized Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
	private String pickupRelationName;
}
