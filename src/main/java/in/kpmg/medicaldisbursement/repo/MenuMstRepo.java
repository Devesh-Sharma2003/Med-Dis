package in.kpmg.medicaldisbursement.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.kpmg.medicaldisbursement.models.MenuMst;

@Repository
public interface MenuMstRepo extends JpaRepository<MenuMst,Integer> {

	Optional<MenuMst> findByLink(String endpoint);

}
