package nl.birchwood.iprwc.purchaseditem;

import nl.birchwood.iprwc.purchase.model.Purchase;
import nl.birchwood.iprwc.purchaseditem.model.PurchasedItem;
import nl.birchwood.iprwc.user.model.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PurchasedItemRepository extends PagingAndSortingRepository<PurchasedItem, UUID> {
    Page<PurchasedItem> findAllByPurchase(Purchase purchase, Pageable pageable);
}
