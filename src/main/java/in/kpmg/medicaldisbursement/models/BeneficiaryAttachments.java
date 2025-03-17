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

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical.md_beneficiary_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryAttachments {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_number",referencedColumnName = "request_number")
	private BeneficiaryRequestDetails requestNumber;
	
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "attachment_type",referencedColumnName = "type_id")
	@Column(name = "attachment_type")
	private Integer attachmentType;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "file_path")
	private String filePath;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "crt_by", referencedColumnName = "user_id")
//	@Column(name="crt_by")
	private UserMst createdBy;
	
	@Column(name = "crt_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	@CreationTimestamp
	private Timestamp createdOn;
}
