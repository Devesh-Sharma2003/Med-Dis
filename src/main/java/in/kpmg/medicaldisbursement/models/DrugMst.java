package in.kpmg.medicaldisbursement.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical.md_drugs_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrugMst {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "drug_id")
	private Integer drugId;
	
	@Column(name = "drug_name")
	private String drugName;
	
	@Column(name = "manufacturer_name")
	private String manufacturerName;

}
