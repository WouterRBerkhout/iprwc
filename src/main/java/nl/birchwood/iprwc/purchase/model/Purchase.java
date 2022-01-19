package nl.birchwood.iprwc.purchase.model;

import lombok.Getter;
import lombok.Setter;
import nl.birchwood.iprwc.purchaseditem.model.PurchasedItem;
import nl.birchwood.iprwc.user.model.AppUser;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
public class Purchase {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    @CreatedDate
    private Date createDate = new Date();

    @ManyToOne(cascade = CascadeType.DETACH)
    private AppUser user;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PurchasedItem> purchasedItems;
}
