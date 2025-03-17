package in.kpmg.medicaldisbursement.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="medical.md_general_type_mst")
@AllArgsConstructor
@NoArgsConstructor
public class GeneralTypeMst {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="type_id")
	private Integer typeId;

	@Column(name="type_name")
	private String typeName;
	
	@Column(name="type_desc")
	private String typeDesc;

	@Column(name="is_active")
    private Boolean isActive;

	@Column(name = "type_order")
	private Integer typeOrder;
	
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "parent_type_id",referencedColumnName = "type_id")
	@Column(name = "parent_type_id")
	private Integer parentTypeId;
}
