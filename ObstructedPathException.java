package me.potts.robots;

public class ObstructedPathException extends Exception {

    private static final long serialVersionUID = 3L;

    ObstructedPathException(String message) {
        super(message);
    }

}
