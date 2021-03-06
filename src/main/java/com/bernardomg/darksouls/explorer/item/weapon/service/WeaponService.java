
package com.bernardomg.darksouls.explorer.item.weapon.service;

import java.util.Optional;

import com.bernardomg.darksouls.explorer.domain.Summary;
import com.bernardomg.darksouls.explorer.item.weapon.domain.Weapon;
import com.bernardomg.darksouls.explorer.item.weapon.domain.adjustment.WeaponAdjustment;
import com.bernardomg.darksouls.explorer.item.weapon.domain.path.WeaponProgression;
import com.bernardomg.pagination.model.Pagination;
import com.bernardomg.pagination.model.Sort;

public interface WeaponService {

    public Optional<WeaponAdjustment> getAdjustment(final Long id);

    public Iterable<Summary> getAll(final String type, final Pagination pagination, final Sort sort);

    public Optional<Weapon> getOne(final Long id);

    public Optional<WeaponProgression> getProgression(final Long id);

}
