package in.kpmg.medicaldisbursement.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "medical.md_step_mst")
public class StepMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "step_id")
    private Integer stepId;

    @Column(name = "step_name")
    private String stepName;

    @ManyToOne
    @JoinColumn(name = "workflow_id", referencedColumnName = "workflow_id")
    private WorkflowMaster workflowId;
}
