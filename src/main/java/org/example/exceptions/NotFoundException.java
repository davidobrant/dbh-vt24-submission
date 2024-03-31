package org.example.exceptions;

import org.example.utils.Utilities;

public class NotFoundException extends Exception {

    Utilities u = new Utilities();
    public <T> NotFoundException(Class<T> entityClass) {
        super("No entities found...");
        String tableName = u.getTableName(entityClass);
    }
}
