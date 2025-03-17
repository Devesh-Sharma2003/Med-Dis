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
@Table(name = "medical.md_village_mst")
@NoArgsConstructor
@AllArgsConstructor
public class VillageMaster {

    @Id
    @Column(name = "id")
    private Integer villageId;

    @Column(name = "village_code")
    private String villageCode;

    @Column(name = "village_name")
    private String villageName;

    @ManyToOne
    @JoinColumn(name="mandal_id", referencedColumnName = "id")
    private MandalMaster mandal;

    @Column(name = "is_active")
    private Boolean isActive;
}
