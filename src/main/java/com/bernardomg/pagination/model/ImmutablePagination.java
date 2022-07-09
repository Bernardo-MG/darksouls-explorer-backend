
package com.bernardomg.pagination.model;

import lombok.Data;
import lombok.NonNull;

/**
 * Default implementation of the paginated data request.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Data
public final class ImmutablePagination implements Pagination {

    /**
     * Page index to read.
     */
    @NonNull
    private final Integer page;

    /**
     * Always paged.
     */
    @NonNull
    private final Boolean paged = true;

    /**
     * Number of elements to read per page.
     */
    @NonNull
    private final Integer size;

    /**
     * Builds a pagination request for the specified page and default size.
     *
     * @param pg
     *            page index
     */
    public ImmutablePagination(@NonNull final Integer pg) {
        super();

        page = pg;
        size = Pagination.DEFAULT_SIZE;
    }

    /**
     * Builds a pagination request with the specified data.
     *
     * @param pg
     *            page index
     * @param sz
     *            page size
     */
    public ImmutablePagination(@NonNull final Integer pg, @NonNull final Integer sz) {
        super();

        page = pg;
        size = sz;
    }

}