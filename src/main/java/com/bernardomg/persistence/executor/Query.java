
package com.bernardomg.persistence.executor;

import java.util.Map;

public interface Query<T> {

    public T getOutput(final Map<String, Object> record);

    public String getStatement(final Map<String, Object> params);

}
