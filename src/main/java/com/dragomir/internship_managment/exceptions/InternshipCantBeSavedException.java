package com.dragomir.internship_managment.exceptions;

public class InternshipCantBeSavedException extends RuntimeException {
    public InternshipCantBeSavedException(String message) {
        super(message);
    }
}
