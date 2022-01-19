package nl.birchwood.iprwc.item;

import nl.birchwood.iprwc.exceptions.EntityNotFoundException;
import nl.birchwood.iprwc.item.model.Item;
import nl.birchwood.iprwc.item.model.ItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemResponse create(Item item) {
        itemRepository.save(item);
        return new ItemResponse(item);
    }

    public Page<ItemResponse> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Item> items = itemRepository.findAll(pageable);
        List<ItemResponse> itemResponseList = new ArrayList<>();
        items.forEach(item -> itemResponseList.add(new ItemResponse(item)));
        return new PageImpl<>(itemResponseList, pageable, items.getTotalElements());
    }

    public ItemResponse findOne(UUID id) {
        Item item = itemRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Item.class));
        return new ItemResponse(item);
    }

    public ItemResponse update(UUID id, Item item) {
        Item existingItem = itemRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Item.class));
        if (item.getName() != null) {
            existingItem.setName(item.getName());
        }
        if (item.getPrice() != null) {
            existingItem.setPrice(item.getPrice());
        }
        if (item.getStock() != null) {
            existingItem.setStock(item.getStock());
        }
        itemRepository.save(existingItem);
        return new ItemResponse(existingItem);
    }

    public void delete(UUID id ) {
        Item item = itemRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Item.class));
        itemRepository.delete(item);
    }

}
