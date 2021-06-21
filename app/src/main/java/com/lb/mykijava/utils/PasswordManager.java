package com.lb.mykijava.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PasswordManager {

    public enum complexity {LOW, MEDIUM, HIGH}

    public static String generatePassword(complexity c, int length) {

        String lowerAtoZ = "qwertyuioplkjhgfdsazxcvbnm";
        String upperAtoZ = "QWERTYUIOPLKJHGFDDSAZXCVBNM";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()_+~<>?:{}";
        List<String> chars = new ArrayList<>();

        switch (c) {
            case LOW:
                chars = Arrays.asList(lowerAtoZ, upperAtoZ);
                break;
            case MEDIUM:
                chars = Arrays.asList(lowerAtoZ, upperAtoZ, numbers);
                break;
            case HIGH:
                chars = Arrays.asList(lowerAtoZ, upperAtoZ, numbers, specialChars);
                break;
        }
        StringBuilder s = new StringBuilder();
        Random rnd = new Random();
        while (s.length() < length) {
            for (String characters : chars) {
                if (s.length() == length) continue;
                s.append(characters.charAt(rnd.nextInt(characters.length() - 1)));

            }
        }

        return s.toString();
    }

    public static boolean isValid(String pass) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+~<>?:{}]).{8,}";
        return pass.matches(pattern);

    }
}
