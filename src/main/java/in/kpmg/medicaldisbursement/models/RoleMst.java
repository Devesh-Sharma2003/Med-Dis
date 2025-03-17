package in.kpmg.medicaldisbursement.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "medical.md_role_mst")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleMst implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "role_name")
    private String name;


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


    @Column(name = "role_desc")
    private String roledesc;

    @Column(name = "display_order")
    private Integer displayOrder;

}
