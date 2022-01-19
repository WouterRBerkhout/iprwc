package nl.birchwood.iprwc.purchase;

import nl.birchwood.iprwc.exceptions.EntityNotFoundException;
import nl.birchwood.iprwc.purchase.model.Purchase;
import nl.birchwood.iprwc.purchase.model.PurchaseResponse;
import nl.birchwood.iprwc.user.UserRepository;
import nl.birchwood.iprwc.user.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, UserRepository userRepository) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
    }

    public Page<PurchaseResponse> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        AppUser user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EntityNotFoundException(AppUser.class));
        Page<Purchase> purchases = purchaseRepository.findAllByUser(user, pageable);
        List<PurchaseResponse> purchaseResponseList = new ArrayList<>();
        purchases.forEach(purchase -> purchaseResponseList.add(new PurchaseResponse(purchase)));
        return new PageImpl<>(purchaseResponseList, pageable, purchases.getTotalElements());
    }

    public PurchaseResponse create() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(AppUser.class));
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchaseRepository.save(purchase);
        return new PurchaseResponse(purchase);
    }

}

