
package com.bernardomg.darksouls.explorer.initialize.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public final class TalismanAdjustmentBatchData {

    @NonNull
    private Integer adjustment   = 0;

    @NonNull
    private Integer faith        = 0;

    @NonNull
    private Integer intelligence = 0;

    @NonNull
    private String  name         = "";

}
