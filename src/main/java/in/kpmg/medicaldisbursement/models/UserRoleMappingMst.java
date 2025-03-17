package in.kpmg.medicaldisbursement.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "medical.md_user_role_mapping")
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleMappingMst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    private Integer roleMapId;

    // @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id", nullable = false)
    @ToString.Exclude
    private UserMst user;

    @OneToOne
    @JoinColumn(name = "role_id",referencedColumnName = "role_id", nullable = false)
    @ToString.Exclude
    private RoleMst role;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = UserMst.class)
    @JoinColumn(name = "crt_by", nullable = false)
    @ToString.Exclude
    private UserMst crtBy;

    @Column(name = "crt_on")
    @CreationTimestamp
    private Timestamp createdOn;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = UserMst.class)
    @JoinColumn(name = "upd_by", referencedColumnName = "user_id")
    @ToString.Exclude
    private UserMst updBy;

    @UpdateTimestamp
    @Column(name = "upd_on")
    private Timestamp updatedOn;

    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name="is_primary_role")
    private Boolean isPrimaryRole;

}
