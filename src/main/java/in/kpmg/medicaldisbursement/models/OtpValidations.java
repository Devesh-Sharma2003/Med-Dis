package in.kpmg.medicaldisbursement.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical.md_otp_validations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpValidations {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "otp_id")
	private Integer otpId;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName = "user_id")
	private UserMst user;
	
	@Column(name="otp")
	private Integer otp;
	
	@Column(name = "otp_generated_at")
	@CreationTimestamp
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	private Timestamp otpGeneratedAt;
	
	@Column(name="validation_type")
	private Integer validationType;
	
	@Column(name="failed_attempts")
	private Integer failedAttempts;
	
	@Column(name="is_validated")
	private Boolean isValidated;
}
