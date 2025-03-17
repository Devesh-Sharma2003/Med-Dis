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
@Table(name = "medical.md_mandal_mst")
@NoArgsConstructor
@AllArgsConstructor
public class MandalMaster {

    @Id
    @Column(name = "id")
    private Integer mandalId;

    @Column(name = "mandal_code")
    private String mandalCode;

    @Column(name = "mandal_name")
    private String mandalName;

    @ManyToOne
    @JoinColumn(name="dist_id", referencedColumnName = "id")
    private DistrictMaster district;

    @Column(name = "is_active")
    private Boolean isActive;


}
