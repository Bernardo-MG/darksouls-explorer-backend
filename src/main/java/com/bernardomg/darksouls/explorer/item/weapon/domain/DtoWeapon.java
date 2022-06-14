
package com.bernardomg.darksouls.explorer.item.weapon.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public final class DtoWeapon implements Weapon {

    @NonNull
    private String             description  = "";

    @NonNull
    private Integer            durability   = 0;

    @NonNull
    private Long               id           = -1l;

    @NonNull
    private String             name         = "";

    @NonNull
    private WeaponRequirements requirements = new DtoWeaponRequirements();

    @NonNull
    private Long               weight       = 0l;

}
