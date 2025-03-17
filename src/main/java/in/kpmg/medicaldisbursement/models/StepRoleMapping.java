package in.kpmg.medicaldisbursement.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "medical.md_step_role_mapping")
public class StepRoleMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "step_role_id")
    private Long stepRoleId;

    @OneToOne
    @JoinColumn(name = "step_id", referencedColumnName = "step_id")
    private StepMaster stepId;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private RoleMst roleId;

    @Column(name = "case_status_name")
    private String caseStatusName;
}
