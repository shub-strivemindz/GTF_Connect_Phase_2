package com.gtfconnect.exceptions;

import androidx.annotation.Nullable;

public class LaunchException extends Throwable{

    public LaunchException(@Nullable String message) {
        super(message);
    }
}
