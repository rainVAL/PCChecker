package com.pcchecker.utils;

import java.util.Locale;

public class CurrencyUtils {
    public static final double USD_TO_PHP = 56.0;

    public static String formatPHP(double priceUsd) {
        double pricePhp = priceUsd * USD_TO_PHP;
        return String.format(Locale.US, "₱%,.2f", pricePhp);
    }
}
