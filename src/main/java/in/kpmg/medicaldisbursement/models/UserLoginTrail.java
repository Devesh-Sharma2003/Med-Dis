package in.kpmg.medicaldisbursement.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "medical.md_user_login_trail")
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginTrail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_login_id")
    private Integer userLoginId;

    @OneToOne
    @JoinColumn(name = "user_role_id", referencedColumnName = "mapping_id")
    private UserRoleMappingMst userRoleId;

    @Column(name = "login_time")
    @CreationTimestamp
    private Timestamp loginTime;

    @Column(name = "logout_time")
    private Timestamp logoutTime;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "api_token")
    private String apiToken;

    @Column(name = "ip_address")
    private String ipAddress;

}
