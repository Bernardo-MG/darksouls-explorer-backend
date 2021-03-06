
package com.bernardomg.darksouls.explorer.item.weapon.domain.path;

import java.util.Collections;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public final class DtoWeaponProgression implements WeaponProgression {

    @NonNull
    private String                          name  = "";

    @NonNull
    private Iterable<WeaponProgressionPath> paths = Collections.emptyList();

}
