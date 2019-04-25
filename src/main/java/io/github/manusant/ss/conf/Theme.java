package io.github.manusant.ss.conf;

/**
 * @author manusant
 */
public enum Theme {

    FEELING_BLUE("theme-feeling-blue"),
    FLATTOP("theme-flattop"),
    MATERIAL("theme-material"),
    MONOKAI("theme-monokai"),
    MUTED("theme-muted"),
    NEWSPAPER("theme-newspaper"),
    OUTLINE("theme-outline");

    private String value;

    Theme(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Theme fromValue(String value) {
        if (value != null) {
            for (Theme theme : Theme.values()) {
                if (theme.name().equals(value) || theme.getValue().equals(value)) {
                    return theme;
                }
            }
        }
        return Theme.OUTLINE;
    }
}