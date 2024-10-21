package com.example.Inmopro.v1.Controller.Request;

import jakarta.mail.MessagingException;

import java.io.IOException;

@FunctionalInterface
public interface ThrowingSupplier<T> {
    T get() throws IOException, MessagingException;
}
