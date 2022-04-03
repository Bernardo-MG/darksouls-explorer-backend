
package com.bernardomg.darksouls.explorer.response.model;

import lombok.Data;
import lombok.NonNull;

@Data
public final class DefaultPaginatedResponse<T> implements PaginatedResponse<T> {

    /**
     * Response content.
     */
    @NonNull
    private T       content;

    private Boolean first;

    private Boolean last;

    private Integer pageNumber;

    private Integer elementsInPage;

    private Integer size;

    private Long    totalElements;

    private Integer totalPages;

    /**
     * Default constructor.
     */
    public DefaultPaginatedResponse() {
        super();
    }

    /**
     * Constructs a response with the specified content.
     *
     * @param cont
     *            content
     */
    public DefaultPaginatedResponse(@NonNull final T cont) {
        super();

        content = cont;
    }

}
