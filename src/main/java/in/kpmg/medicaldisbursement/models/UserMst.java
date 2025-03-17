package in.kpmg.medicaldisbursement.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "medical.md_user_mst")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMst {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "user_name")
	private String userName;

	@Column(name = "login_name")
	private String loginName;

	@Column(name = "pswd")
	private String password;
	
	@Column(name = "email_id")
	private String email;

	@Column(name = "mobile_no")
	private Long mobileNo;

	@Column(name = "hospital_name")
	private String hospitalName;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "crt_by", referencedColumnName = "user_id")
	@Column(name = "crt_by")
	private Integer createdBy;

	@Column(name = "crt_on", updatable = false)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	@CreationTimestamp
	private Timestamp createdOn;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "upd_by", referencedColumnName = "user_id")
	@Column(name = "upd_by")
	private Integer updatedBy;

	@Column(name = "upd_on")
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	@UpdateTimestamp
	private Timestamp updatedOn;

	@Column(name = "is_active")
	private Boolean isActive;
	
	@OneToOne
	@JoinColumn(name = "reporting_person_id",referencedColumnName = "user_id")
	private UserMst managerId;

	@Column(name = "hosp_id")
	private String hospId;

	@Column(name = "hosp_dist_id")
	private String hospDistId;
	
	@Column(name = "hosp_dist_name")
	private String hospDistName;

	@Column(name = "hosp_mandal_id")
	private String hospMandalId;
	
	@Column(name = "hosp_mandal_name")
	private String hospMandalName;



}