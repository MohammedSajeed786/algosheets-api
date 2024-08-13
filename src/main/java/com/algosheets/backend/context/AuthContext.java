package com.algosheets.backend.context;


import java.util.UUID;

public class AuthContext {

    public static final ThreadLocal<String> context=new ThreadLocal<>();

    public static void setContext(String userId) {
        context.set(userId);
    }

    public static String getContext() {
        return context.get();
    }

    public static void clearContext() {
        context.remove();
    }
}
