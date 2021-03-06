
package com.bernardomg.darksouls.explorer.item.weapon.domain.path;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity(name = "WeaponLevel")
@Table(name = "weapon_levels")
public final class PersistentWeaponLevel {

    @NonNull
    @Column(name = "critical_damage", nullable = false)
    private Integer critical;

    @NonNull
    @Column(name = "dexterity_bonus", nullable = false)
    private String  dexterityBonus;

    @NonNull
    @Column(name = "faith_bonus", nullable = false)
    private String  faithBonus;

    @NonNull
    @Column(name = "fire_damage", nullable = false)
    private Integer fireDamage;

    @NonNull
    @Column(name = "fire_reduction", nullable = false)
    private Float   fireReduction;

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long    id;

    @NonNull
    @Column(name = "intelligence_bonus", nullable = false)
    private String  intelligenceBonus;

    @NonNull
    @Column(name = "level", nullable = false)
    private Integer level;

    @NonNull
    @Column(name = "lightning_damage", nullable = false)
    private Integer lightningDamage;

    @NonNull
    @Column(name = "lightning_reduction", nullable = false)
    private Float   lightningReduction;

    @NonNull
    @Column(name = "magic_damage", nullable = false)
    private Integer magicDamage;

    @NonNull
    @Column(name = "magic_reduction", nullable = false)
    private Float   magicReduction;

    @NonNull
    @Column(name = "name", nullable = false)
    private String  name;

    @NonNull
    @Column(name = "path", nullable = false)
    private String  path;

    @NonNull
    @Column(name = "physical_damage", nullable = false)
    private Integer physicalDamage;

    @NonNull
    @Column(name = "physical_reduction", nullable = false)
    private Float   physicalReduction;

    @NonNull
    @Column(name = "stability", nullable = false)
    private Integer stability;

    @NonNull
    @Column(name = "strength_bonus", nullable = false)
    private String  strengthBonus;

}
