package cz.cuni.mff.java.projects.posapp.core;

import java.awt.*;
import java.util.HashMap;

/**
 * Factory providing color schemes fo the UI.
 * Color themes are validated to contain at least the defined required fields.
 */
public class ColorThemeFactory {

    private static final String[] requiredColors = {
            "primary", "secondary", "tertiary", "button", "background", "button-text"
    };

    /**
     * Make the default dark theme colour map
     * @return the theme
     */
    public static HashMap<String, Color> makeDarkTheme() {
        HashMap<String, Color> theme = new HashMap<>();
        theme.put("primary", new Color(50, 50, 50));
        theme.put("secondary", new Color(80, 80, 80));
        theme.put("tertiary",new Color(175, 175, 175));
        theme.put("button", new Color(90, 90, 90));
        theme.put("button-text", new Color(240, 240, 240));
        theme.put("background", new Color(60, 60, 60));
        return validateTheme(theme);
    }

    /**
     * Validates the theme against the requirement, checking that the required colour
     * definitions are present and not null.
     * @param theme to validate
     * @throws IllegalArgumentException if theme is invalid
     * @return the validated theme
     */
    public static HashMap<String, Color> validateTheme(HashMap<String, Color> theme) {
        boolean valid = true;
        for(String color: requiredColors) {
            valid &= theme.containsKey(color) & theme.get(color) != null;
        }
        if(valid) return theme;
        throw new IllegalArgumentException("Provided theme does not support all required colours!");
    }
}
