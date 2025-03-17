package in.kpmg.medicaldisbursement.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "medical.md_workflow_mst")
public class WorkflowMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workflow_id")
    private Long workflowId;

    @Column(name = "workflow_name")
    private String workflowName;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "scheme_name")
    private String schemeName;
}
