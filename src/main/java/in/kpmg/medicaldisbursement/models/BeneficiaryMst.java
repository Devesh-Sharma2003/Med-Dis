package in.kpmg.medicaldisbursement.models;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical.md_beneficiary_mst")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryMst {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="beneficiary_id")
	private String beneficiaryId;
	
	@Column(name="beneficiary_name")
	// @Pattern(regexp = "^[A-Za-z][A-Za-z. ]*$", message = "Name must start with an alphabet, can contain spaces, but cannot start with a space or have special characters")
	private String beneficiaryName;
	
	@Column(name="beneficiary_phone_no")
	// @NotBlank(message = "Contact Number must not be blank!")
//    @Pattern(regexp = "^[1-9][0-9]{9}$", message = "Mobile number must be 10 digits and should not start with 0")
	private Long beneficiaryPhoneNo;
	
	@Column(name="parent_benficiary_id")
	private Integer parentBeneficiaryId;
	
	@OneToOne
	@JoinColumn(name="benficiary_type", referencedColumnName = "type_id")
	private GeneralTypeMst beneficiaryType;
	
	@Column(name="benficiary_dob")
	// @NotBlank(message = "DOB must not be blank!")
	private LocalDate beneficiaryDob;
	
	@OneToOne
	@JoinColumn(name="gender", referencedColumnName = "type_id")
	// @NotBlank(message = "Gender must not be blank!")
	private GeneralTypeMst gender;
	
	@OneToOne
	@JoinColumn(name="relation", referencedColumnName = "type_id")
	// @NotBlank(message = "relation must not be blank!")
	private GeneralTypeMst relation;
	
	@Column(name="email")
	// @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email address")
	private String email;
	
	@Column(name="alternate_contact_number")
//	 @Pattern(regexp = "^[1-9][0-9]{9}$", message = "Mobile number must be 10 digits and should not start with 0")
	private Long alternateContactNumber;
	
	@Column(name="house_no")
	// @NotBlank(message = "House Number must not be blank!")
	// @Pattern(regexp = "^[A-Za-z0-9/,- ]$")
	private String houseNo;
	
	@Column(name="landmark")
	// @Pattern(regexp = "^[A-Za-z0-9/,-. ]$")
	private String landmark;
	
	@Column(name="locality")
	private String locality;
	
	@OneToOne
	@JoinColumn(name="district_id", referencedColumnName = "id")
	private DistrictMaster districtId;
	
	@OneToOne
	@JoinColumn(name="state_id", referencedColumnName = "id")
    // @NotBlank(message = "State must not be blank!")
	private StateMaster stateId;
	
	@OneToOne
	@JoinColumn(name="mandal_id", referencedColumnName = "id")
	// @NotBlank(message = "Mandal must not be blank!")
	private MandalMaster mandalId;
	
	@OneToOne
	@JoinColumn(name="village_id", referencedColumnName = "id")
	// @NotBlank(message = "Village must not be blank!")
	private VillageMaster villageId;
	
	@Column(name="pincode")
    // @NotBlank(message = "Pincode must not be blank!")
	private Long pincode;
	
	@Column(name="cfms_id")
	private String cfmsId;

	@Column(name="is_active")
	private Boolean isActive;

}
