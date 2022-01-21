package nl.birchwood.iprwc.purchaseditem;

import nl.birchwood.iprwc.exceptions.EntityNotFoundException;
import nl.birchwood.iprwc.item.ItemRepository;
import nl.birchwood.iprwc.item.model.Item;
import nl.birchwood.iprwc.purchase.PurchaseRepository;
import nl.birchwood.iprwc.purchase.model.Purchase;
import nl.birchwood.iprwc.purchaseditem.model.PurchasedItem;
import nl.birchwood.iprwc.purchaseditem.model.PurchasedItemResponse;
import nl.birchwood.iprwc.user.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PurchasedItemService {
    private final PurchasedItemRepository purchasedItemRepository;
    private final PurchaseRepository purchaseRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public PurchasedItemService(ItemRepository itemRepository, PurchasedItemRepository purchasedItemRepository, PurchaseRepository purchaseRepository) {
        this.purchasedItemRepository = purchasedItemRepository;
        this.purchaseRepository = purchaseRepository;
        this.itemRepository = itemRepository;
    }

    public Page<PurchasedItemResponse> findAll(UUID purchaseId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException(Purchase.class));
        Page<PurchasedItem> purchasedItems = purchasedItemRepository.findAllByPurchase(purchase, pageable);
        List<PurchasedItemResponse> purchasedItemResponseList = new ArrayList<>();
        purchasedItems.forEach(purchasedItem -> {
            if (purchasedItem.getPurchase().getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                purchasedItemResponseList.add(new PurchasedItemResponse(purchasedItem));
            }
        });
        return new PageImpl<>(purchasedItemResponseList, pageable, purchasedItems.getTotalElements());
    }

    public PurchasedItemResponse create(UUID purchaseId, UUID itemId, int quantity) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new EntityNotFoundException(Purchase.class));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(Item.class));

        PurchasedItem purchasedItem = new PurchasedItem();
        purchasedItem.setPurchase(purchase);
        purchasedItem.setName(item.getName());
        purchasedItem.setQuantity(quantity);
        item.setStock(item.getStock() - quantity);
        purchasedItem.setPrice(item.getPrice());
        purchasedItemRepository.save(purchasedItem);
        return new PurchasedItemResponse(purchasedItem);
    }
}
