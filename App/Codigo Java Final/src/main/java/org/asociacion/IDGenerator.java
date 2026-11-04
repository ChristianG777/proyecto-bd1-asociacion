package org.asociacion;

import java.util.Random;

public class IDGenerator {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random R = new Random();

    public static String generarID(String prefijo) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) sb.append(CHARS.charAt(R.nextInt(CHARS.length())));
        return prefijo + "-" + sb.toString();
    }
}