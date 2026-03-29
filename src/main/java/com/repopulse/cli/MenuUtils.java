package com.repopulse.cli;

import java.util.Scanner;

public class MenuUtil {
    private static final Scanner sc = new Scanner(System.in);

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            sc.next();
            System.out.print(prompt);
        }
        int val = sc.nextInt();
        sc.nextLine(); // consume newline
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
