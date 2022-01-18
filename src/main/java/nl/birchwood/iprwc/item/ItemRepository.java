package nl.birchwood.iprwc.item;

import nl.birchwood.iprwc.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, UUID> {


    Optional<Item> findByName(@Param("name") String name);
}
