package io.skyshard.exceptions;

public class UnsupportedSystemException extends RuntimeException {

    public UnsupportedSystemException() {
        super("Attempting to run on an unsupported system. Exiting...");
    }

}
