
package com.bernardomg.darksouls.explorer.item.shield.controller;

import java.util.Collection;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.darksouls.explorer.item.shield.domain.Shield;
import com.bernardomg.darksouls.explorer.item.shield.domain.request.DefaultShieldRequest;
import com.bernardomg.darksouls.explorer.item.shield.service.ShieldService;
import com.bernardomg.darksouls.explorer.persistence.model.Pagination;
import com.bernardomg.darksouls.explorer.persistence.model.Sort;

@RestController
@RequestMapping("/shields")
public class ShieldController {

    private final ShieldService service;

    public ShieldController(final ShieldService srvc) {
        super();

        service = Objects.requireNonNull(srvc);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<? extends Shield> read(
            @RequestParam(name = "name", defaultValue = "") final String name,
            @RequestParam(name = "selectors",
                    defaultValue = "") final Collection<String> selectors,
            final Pagination pagination, final Sort sort) {
        final DefaultShieldRequest request;

        request = new DefaultShieldRequest();
        request.setName(name);

        return service.getAll(request, pagination, sort);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Shield readOne(@PathVariable("id") final Long id) {
        return service.getOne(id)
            .orElse(null);
    }

}
