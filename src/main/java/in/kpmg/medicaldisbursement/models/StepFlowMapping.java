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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical.md_step_flow_mapping")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StepFlowMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "flow_id")
	private Integer id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="initial_step_role_id", referencedColumnName = "step_role_id")
	private StepRoleMapping initialRoleId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="next_step_role_id", referencedColumnName = "step_role_id")
	private StepRoleMapping nextRoleId;
	
	@Column(name="remarks")
	private String remarks;
	
	 @Column(name = "is_active")
	 private Boolean isActive;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "condtn_id", referencedColumnName = "condtn_id")
	 private ConditionMst condtnId;
	 
	 @Column(name = "version_id")
	 private Integer versionId;
	 
	 @Column(name = "sla")
	 private Integer sla;
	 
	 @Column(name = "added_on")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	 private Timestamp addedOn;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "action_id", referencedColumnName = "action_id")
	 private ActionMst actionId;

}
