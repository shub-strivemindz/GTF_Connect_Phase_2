package com.gtfconnect.exceptions;

import androidx.annotation.Nullable;

public class AuthenticationException extends Throwable{
    public AuthenticationException(@Nullable String message) {
        super(message);
    }
}
