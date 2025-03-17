package in.kpmg.medicaldisbursement.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "medical.md_menu_mst")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuMst {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private Integer menuId;

	@Column(name = "link")
	private String link;

	@Column(name = "icon")
	private String icon;

	@Column(name = "menu_name")
	private String name;

	@Column(name = "menu_telugu_name")
	private String nameTelugu;
	
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "parent_menu_id",referencedColumnName = "menu_id")
	@Column(name = "parent_menu_id")
	private Integer parentMenuId;

	@Column(name = "is_default")
	private Boolean isDefault;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crt_by", referencedColumnName = "user_id")
	private UserMst createdBy;

	@Column(name = "crt_on", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	@CreationTimestamp
	private Timestamp createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upd_by", referencedColumnName = "user_id")
	private UserMst updatedBy;
	
	@Column(name = "upd_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm aa", timezone = "Asia/Kolkata")
	// @UpdateTimestamp
	private Timestamp updatedOn;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "display_order")
	private Integer displayOrder;

//	@OneToOne
//	@JoinColumn(name = "parent_menu_id", nullable = true, insertable = false, updatable = false)
//	private MenuMst mn;

}
