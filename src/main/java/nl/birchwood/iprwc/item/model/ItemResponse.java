package nl.birchwood.iprwc.item.model;

import lombok.Value;

import java.util.UUID;

@Value
public class ItemResponse {

    UUID id;
    String name;
    Float price;
    Integer stock;

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.stock = item.getStock();
    }
}
