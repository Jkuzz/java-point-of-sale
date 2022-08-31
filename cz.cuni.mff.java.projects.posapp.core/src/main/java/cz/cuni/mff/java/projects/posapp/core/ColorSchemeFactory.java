package cz.cuni.mff.java.projects.posapp.core;

import java.awt.*;
import java.util.HashMap;

public class ColorSchemeFactory {

    private static final String[] requiredColors = {
            "primary", "secondary", "tertiary", "button", "background", "button-text"
    };

    /**
     * Make the default dark scheme colour map
     * @return the scheme
     */
    public static HashMap<String, Color> makeDarkScheme() {
        HashMap<String, Color> scheme = new HashMap<>();
        scheme.put("primary", new Color(50, 50, 50));
        scheme.put("secondary", new Color(80, 80, 80));
        scheme.put("tertiary",new Color(175, 175, 175));
        scheme.put("button", new Color(90, 90, 90));
        scheme.put("button-text", new Color(240, 240, 240));
        scheme.put("background", new Color(60, 60, 60));
        return validateScheme(scheme);
    }

    /**
     * Validates the scheme against the requirement, checking that the required colour
     * definitions are present and not null.
     * @param scheme to validate
     * @throws IllegalArgumentException if scheme is invalid
     * @return the validated scheme
     */
    public static HashMap<String, Color> validateScheme(HashMap<String, Color> scheme) {
        boolean valid = true;
        for(String color: requiredColors) {
            valid &= scheme.containsKey(color) & scheme.get(color) != null;
        }
        if(valid) return scheme;
        throw new IllegalArgumentException("Provided scheme does not support all required colours!");
    }
}
