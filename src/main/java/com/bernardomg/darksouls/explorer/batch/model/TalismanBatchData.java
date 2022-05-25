
package com.bernardomg.darksouls.explorer.batch.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public final class TalismanBatchData {

    @NonNull
    private String  description  = "";

    @NonNull
    private Integer dexterity    = 0;

    @NonNull
    private Integer durability   = 0;

    @NonNull
    private Integer faith        = 0;

    @NonNull
    private Integer intelligence = 0;

    @NonNull
    private String  name         = "";

    @NonNull
    private Integer strength     = 0;

    @NonNull
    private Double  weight       = 0d;

}