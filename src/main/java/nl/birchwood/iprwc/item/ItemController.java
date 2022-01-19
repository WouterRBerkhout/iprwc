package nl.birchwood.iprwc.item;

import nl.birchwood.iprwc.item.model.Item;
import nl.birchwood.iprwc.item.model.ItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PreAuthorize("hasAnyAuthority('SUPERUSER')")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    ItemResponse createItem(@RequestBody Item item) {
        return itemService.create(item);
    }

    @GetMapping()
    public @ResponseBody
    Page<ItemResponse> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return itemService.findAll(page, size, sortBy);
    }

    @GetMapping("/{id}")
    public @ResponseBody
    ItemResponse getItem(@PathVariable UUID id) {
        return itemService.findOne(id);
    }

    @PreAuthorize("hasAnyAuthority('SUPERUSER')")
    @PatchMapping("/{id}")
    public @ResponseBody
    ItemResponse updateItem(
            @PathVariable UUID id,
            @RequestBody Item item
    ) {
        return itemService.update(id, item);
    }

    @PreAuthorize("hasAnyAuthority('SUPERUSER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable UUID id) {
        itemService.delete(id);
    }
}
