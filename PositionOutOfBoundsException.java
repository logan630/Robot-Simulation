package me.potts.robots;

public class PositionOutOfBoundsException extends Exception {

    private static final long serialVersionUID = 2L;

    PositionOutOfBoundsException(String message) {
        super(message);
    }

}
