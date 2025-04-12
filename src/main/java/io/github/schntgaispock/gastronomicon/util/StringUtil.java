package io.github.schntgaispock.gastronomicon.util;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {

    @Nonnull
    public static String formatColors(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String waterUsed(int mB, String suffix) {
        return "&8⇨ &9🪣 &7" + mB + " mB" + suffix;
    }

    public static String kebabCase(String str) {
        return str.replace("_", "-").replace(" ", "-").toLowerCase();
    }

    public static boolean isBlank(String string) {
        // Is default blank if null or length = 0
        if (string == null || string.length() == 0) {
            return true;
        }

        // Otherwise loop through the chars to see if any are NOT whitespace
        for (char chr : string.toCharArray()) {
            if (!Character.isWhitespace(chr)) {
                return false;
            }
        }

        // No non-whitespace chars, is blank
        return true;
    }
}
