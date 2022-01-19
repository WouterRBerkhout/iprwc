package nl.birchwood.iprwc.purchase;

import nl.birchwood.iprwc.purchase.model.Purchase;
import nl.birchwood.iprwc.user.model.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PurchaseRepository extends PagingAndSortingRepository<Purchase, UUID> {
    Page<Purchase> findAllByUser(AppUser appUser, Pageable pageable);
}
