package termproject.cas.controller;

import org.springframework.web.bind.annotation.*;
import termproject.cas.model.Item;
import termproject.cas.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final ItemService service;

    public AppointmentController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<Item> getItems() {
        return service.getAllItems();
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return service.addItem(item);
    }
}
