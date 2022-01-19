package nl.birchwood.iprwc.purchaseditem.model;

import lombok.Value;

import java.util.UUID;

@Value
public class PurchasedItemResponse {

    UUID id;
    String name;
    int quantity;
    Float price;


    public PurchasedItemResponse(PurchasedItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
    }
}
