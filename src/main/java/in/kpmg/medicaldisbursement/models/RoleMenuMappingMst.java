package in.kpmg.medicaldisbursement.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "medical.md_role_menu_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuMappingMst {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mapping_id")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "role_id",referencedColumnName = "role_id", nullable = false)
	private RoleMst roleDtls;

	@OneToOne
	@JoinColumn(name = "menu_id", referencedColumnName = "menu_id", nullable = false)
	private MenuMst menuDtl;

	@Column(name = "is_viewable")
	private Boolean isView;

	@Column(name = "is_editable")
	private Boolean isEdit;


	@Column(name = "is_downloadable")
	private Boolean isDownload;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crt_by", referencedColumnName = "user_id")
	private UserMst createdBy;

	@Column(name = "crt_on", updatable = false)
	@CreationTimestamp
	private Timestamp createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upd_by", referencedColumnName = "user_id")
	private UserMst updatedBy;

	@Column(name = "upd_on")
	private Timestamp updatedOn;

	@Column(name = "is_active")
	private Boolean isActive;



}
