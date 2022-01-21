package nl.birchwood.iprwc.purchase;

import nl.birchwood.iprwc.purchase.model.PurchaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/order")
@RestController
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping()
    public @ResponseBody
    PurchaseResponse addPurchase() {
        return purchaseService.create();
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping()
    public @ResponseBody
    Page<PurchaseResponse> getAllPurchases(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createDate") String sortBy
    ) {
        return purchaseService.findAll(page, size, sortBy);
    }

}
