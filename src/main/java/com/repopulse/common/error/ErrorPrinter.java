package com.repopulse.common.error;

import com.repopulse.infra.exception.AppException;

public class ErrorPrinter {
    private ErrorPrinter() {
    }

    public static void print(AppException e) {
        System.out.println();
        System.out.println("[Error] " + safe(e.getUserMessage()));
        System.out.println();
    }

    public static void printUnexpected(Exception e) {
        System.out.println();
        System.out.println("[Error] Something went wrong. Please try again.");
        System.out.println();
    }

    private static String safe(String s) {
        return (s == null || s.isBlank()) ? "Something went wrong. Please try again." : s;
    }
}
