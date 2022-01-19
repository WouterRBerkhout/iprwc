package nl.birchwood.iprwc.purchase.model;

import lombok.Value;

import java.util.Date;
import java.util.UUID;

@Value
public class PurchaseResponse {

    UUID id;
    Date createDate;
    String ownerName;

    public PurchaseResponse(Purchase purchase) {
        this.id = purchase.getId();
        this.createDate = purchase.getCreateDate();
        this.ownerName = purchase.getUser().getUsername();
    }

}
