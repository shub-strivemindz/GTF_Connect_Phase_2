package com.gtfconnect.exceptions;

import androidx.annotation.Nullable;

public class ServerException extends Throwable{

    public ServerException(@Nullable String message) {
        super(message);
    }
}
