package in.kpmg.medicaldisbursement.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "medical.md_district_mst")
@NoArgsConstructor
@AllArgsConstructor
public class DistrictMaster {

    @Id
    @Column(name = "id")
    private Integer distId;

    @Column(name = "district_code")
    private String districtCode;

    @Column(name = "district_name")
    private String districtName;
    
    @ManyToOne
    @JoinColumn(name="state_id", referencedColumnName = "id")
    private StateMaster state;

    @Column(name = "is_active")
    private Boolean isDistrictActive;


}
