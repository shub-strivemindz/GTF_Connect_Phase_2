package com.gtfconnect.exceptions;

import androidx.annotation.Nullable;


public class ForbiddenException extends Throwable{

    public ForbiddenException(@Nullable String message) {
        super(message);
    }
}
