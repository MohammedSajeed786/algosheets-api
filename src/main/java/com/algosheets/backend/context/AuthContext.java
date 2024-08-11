package com.algosheets.backend.context;


import java.util.UUID;

public class AuthContext {

    public static final ThreadLocal<UUID> context=new ThreadLocal<>();

    public static void setContext(UUID userId) {
        context.set(userId);
    }

    public static UUID getContext() {
        return context.get();
    }

    public static void clearContext() {
        context.remove();
    }
}
