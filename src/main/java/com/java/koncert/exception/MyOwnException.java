package com.java.koncert.exception;

public class MyOwnException extends RuntimeException{
    public MyOwnException() {

    }

    public MyOwnException(String message) {
        super (message);
    }

    public MyOwnException(Throwable cause) {
        super (cause);
    }

    public MyOwnException(String message, Throwable cause) {
        super (message, cause);
    }
}
