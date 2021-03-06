
package com.bernardomg.darksouls.explorer.item.weapon.domain;

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
@Entity(name = "Weapon")
@Table(name = "weapons")
public final class PersistentWeapon {

    @NonNull
    @Column(name = "critical_damage", nullable = false)
    private Integer criticalDamage     = 0;

    @NonNull
    @Column(name = "description", nullable = false)
    private String  description;

    @NonNull
    @Column(name = "dexterity_requirement", nullable = false)
    private Integer dexterity;

    @NonNull
    @Column(name = "dexterity_bonus", nullable = false)
    private String  dexterityBonus     = "";

    @NonNull
    @Column(name = "durability", nullable = false)
    private Integer durability         = 0;

    @NonNull
    @Column(name = "faith_requirement", nullable = false)
    private Integer faith;

    @NonNull
    @Column(name = "faith_bonus", nullable = false)
    private String  faithBonus         = "";

    @NonNull
    @Column(name = "fire_damage", nullable = false)
    private Integer fireDamage         = 0;

    @NonNull
    @Column(name = "fire_reduction", nullable = false)
    private Float   fireReduction      = 0f;

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long    id;

    @NonNull
    @Column(name = "intelligence_requirement", nullable = false)
    private Integer intelligence;

    @NonNull
    @Column(name = "intelligence_bonus", nullable = false)
    private String  intelligenceBonus  = "";

    @NonNull
    @Column(name = "lightning_damage", nullable = false)
    private Integer lightningDamage    = 0;

    @NonNull
    @Column(name = "lightning_reduction", nullable = false)
    private Float   lightningReduction = 0f;

    @NonNull
    @Column(name = "magic_damage", nullable = false)
    private Integer magicDamage        = 0;

    @NonNull
    @Column(name = "magic_reduction", nullable = false)
    private Float   magicReduction     = 0f;

    @NonNull
    @Column(name = "name", nullable = false)
    private String  name;

    @NonNull
    @Column(name = "physical_damage", nullable = false)
    private Integer physicalDamage     = 0;

    @NonNull
    @Column(name = "physical_reduction", nullable = false)
    private Float   physicalReduction  = 0f;

    @NonNull
    @Column(name = "stability", nullable = false)
    private Integer stability          = 0;

    @NonNull
    @Column(name = "strength_requirement", nullable = false)
    private Integer strength;

    @NonNull
    @Column(name = "strength_bonus", nullable = false)
    private String  strengthBonus      = "";

    @NonNull
    @Column(name = "subtype", nullable = false)
    private String  subtype;

    @NonNull
    @Column(name = "type", nullable = false)
    private String  type;

    @NonNull
    @Column(name = "weight", nullable = false)
    private Long    weight             = 0l;

}
