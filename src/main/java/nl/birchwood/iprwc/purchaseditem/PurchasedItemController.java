package nl.birchwood.iprwc.purchaseditem;

import nl.birchwood.iprwc.purchaseditem.model.PurchasedItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/order/{purchaseId}/items")
@RestController
public class PurchasedItemController {

    private final PurchasedItemService purchasedItemService;

    @Autowired
    public PurchasedItemController(PurchasedItemService purchasedItemService) {
        this.purchasedItemService = purchasedItemService;
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping()
    public Page<PurchasedItemResponse> getPurchasedItemsByPurchase(
            @PathVariable UUID purchaseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ) {
        return purchasedItemService.findAll(purchaseId, page, size, sortBy);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping()
    public @ResponseBody
    PurchasedItemResponse addItemToPurchase(
            @PathVariable UUID purchaseId,
            @RequestParam String itemId,
            @RequestParam int quantity
            ) {
        return purchasedItemService.create(purchaseId, UUID.fromString(itemId), quantity);
    }


}
