package io.skyshard.exceptions;

public class MissingTemplateException extends RuntimeException {

    public MissingTemplateException() {
        super("Unable to find a template to match on. Exiting...");
    }

}
