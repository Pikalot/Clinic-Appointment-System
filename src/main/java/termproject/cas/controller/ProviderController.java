package termproject.cas.controller;

import org.springframework.web.bind.annotation.*;
import termproject.cas.model.Provider;
import termproject.cas.service.ProviderService;

import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {
    private final ProviderService service;

    public ProviderController(ProviderService service) {
        this.service = service;
    }

    @GetMapping
    public List<Provider> getAllProviders() {
        return service.getAllProviders();
    }

    @PostMapping
    public void createProvider(@RequestBody Provider provider) {
        service.addProvider(provider);
    }
}
