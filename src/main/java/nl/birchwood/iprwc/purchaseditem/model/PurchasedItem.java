package nl.birchwood.iprwc.purchaseditem.model;

import lombok.Getter;
import lombok.Setter;
import nl.birchwood.iprwc.purchase.model.Purchase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter @Setter
public class PurchasedItem {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Purchase purchase;

    private String name;
    private int quantity;
    private Float price;
}
