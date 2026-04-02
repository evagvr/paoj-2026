package com.pao.laboratory04.bonus.exceptions;

import com.pao.laboratory04.bonus.enums.Status;

public class InvalidTransitionException extends RuntimeException {
    private final Status fromStatus;
    private final Status toStatus;

    public InvalidTransitionException(Status fromStatus, Status toStatus) {
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }

    @Override
    public String getMessage() {
        return "Nu se poate trece din " + fromStatus + " în " + toStatus;
    }
}