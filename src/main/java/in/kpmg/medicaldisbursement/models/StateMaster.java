package in.kpmg.medicaldisbursement.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "medical.md_state_mst")
@NoArgsConstructor
@AllArgsConstructor
public class StateMaster {

    @Id
    @Column(name = "id")
    private Integer stateId;

    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "is_active")
    private Boolean isStateActive;
 

}
