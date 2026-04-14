package com.repopulse.common.cli;

import java.util.Scanner;

public class CliUtils {

    private static final Scanner sc = new Scanner(System.in);

    public static void clearScreen() {

    }

    public static int getIntInput(String prompt) {
        System.out.print(prompt);
        while(!sc.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            sc.next();
            System.out.print(prompt);
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    public static long getLongInput(String prompt) {
        System.out.print(prompt);
        while(!sc.hasNextLong()) {
            System.out.println("Please enter a valid number.");
            sc.next();
            System.out.print(prompt);
        }
        long val = sc.nextLong();
        sc.nextLine();
        return val;
    }

    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    public static void waitForEnter() {
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }
}