
package com.bernardomg.darksouls.explorer.item.weapon.controller;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bernardomg.darksouls.explorer.item.domain.ImmutableWeaponProgression;
import com.bernardomg.darksouls.explorer.item.domain.WeaponProgression;
import com.bernardomg.darksouls.explorer.item.weapon.domain.DtoWeapon;
import com.bernardomg.darksouls.explorer.item.weapon.domain.Weapon;
import com.bernardomg.darksouls.explorer.item.weapon.domain.request.DefaultWeaponRequest;
import com.bernardomg.darksouls.explorer.item.weapon.service.WeaponService;
import com.bernardomg.darksouls.explorer.persistence.model.Pagination;
import com.bernardomg.darksouls.explorer.persistence.model.Sort;

@RestController
@RequestMapping("/weapons")
public class WeaponController {

    private final WeaponService service;

    public WeaponController(final WeaponService srvc) {
        super();

        service = Objects.requireNonNull(srvc);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<? extends Weapon> read(
            @RequestParam(name = "name", defaultValue = "") final String name,
            @RequestParam(name = "selectors",
                    defaultValue = "") final Collection<String> selectors,
            final Pagination pagination, final Sort sort) {
        final DefaultWeaponRequest request;

        request = new DefaultWeaponRequest();
        request.setName(name);

        return service.getAll(request, pagination, sort);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Weapon readOne(@PathVariable("id") final Long id) {
        final Optional<? extends Weapon> read;
        final Weapon result;

        read = service.getOne(id);
        if (read.isPresent()) {
            result = read.get();
        } else {
            result = new DtoWeapon();
        }

        return result;
    }

    @GetMapping(path = "/{id}/progression",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WeaponProgression
            readProgressions(@PathVariable("id") final Long id) {
        return service.getProgression(id)
            .orElse(new ImmutableWeaponProgression());
    }

}
