package com.es.core.model.phone.exception;

public class InvalidIdException extends RuntimeException{
    public InvalidIdException(Class<?> objClass, Long id) {
        super(String.format("Invalid ID of the object:\nclass %s\nID: %s", objClass.getName(), id));
    }
}
