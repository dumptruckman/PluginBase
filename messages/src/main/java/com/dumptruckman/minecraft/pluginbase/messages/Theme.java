package com.dumptruckman.minecraft.pluginbase.messages;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Theme {
    SUCCESS('+', ChatColor.GREEN),
    ERROR('-', ChatColor.RED),
    FAILURE('^', ChatColor.RED),
    PLAIN('.', ChatColor.RESET),
    IMPORTANT('!', null, ChatColor.BOLD),
    IMPORTANT2('*', null, ChatColor.ITALIC),
    IMPORTANT3('_', null, ChatColor.UNDERLINE),
    HELP('h', ChatColor.WHITE),
    INFO('i', ChatColor.AQUA),
    SORRY('$', ChatColor.DARK_GRAY),
    RETRY(',', ChatColor.GRAY),
    DO_THIS('~', ChatColor.BLUE),
    VALUE('v', ChatColor.DARK_GREEN),
    TITLE('t', ChatColor.DARK_AQUA),
    PLEASE_WAIT('w', ChatColor.GRAY),

    CMD_USAGE('c', ChatColor.WHITE),
    CMD_FLAG('f', ChatColor.GOLD, ChatColor.ITALIC),
    OPT_ARG('o', ChatColor.GOLD),
    REQ_ARG('r', ChatColor.GREEN),
    CMD_HIGHLIGHT('C', null, ChatColor.BOLD),

    HEADER('=', ChatColor.LIGHT_PURPLE),
    LIST_ODD(':', ChatColor.WHITE),
    LIST_EVEN(';', ChatColor.YELLOW),

    ;

    @NotNull
    private Character tag;
    @Nullable
    private ChatColor color;
    @Nullable
    private ChatColor style;

    private Theme(@NotNull final Character tag, @NotNull final ChatColor color) {
        this(tag, color, null);
    }

    private Theme(@NotNull final Character tag, @Nullable final ChatColor color, @Nullable final ChatColor style) {
        this.tag = tag;
        this.color = color;
        this.style = style;
    }

    @NotNull
    public Character getTag() {
        return tag;
    }

    @NotNull
    @Override
    public String toString() {
        return getColor(color, style);
    }

    private static final Map<Character, String> tagMap = new HashMap<Character, String>();

    @NotNull
    private static String themeResource = "theme.xml";

    public static String getThemeResource() {
        return themeResource;
    }

    public static void loadTheme(@NotNull final Document document) {
        tagMap.clear();

        // Create a set of all the enum Theme elements so we can tell what wasn't handled in the file
        final EnumSet<Theme> themeSet = EnumSet.allOf(Theme.class);

        document.getDocumentElement().normalize();
        // Grab the main set of nodes from the document node and iterate over them
        final NodeList nodes = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            final Element element = (Element) node;

            // Check to see if the node name represents a Theme enum
            Theme applicableTheme = null;
            try {
                applicableTheme = Theme.valueOf(element.getNodeName().toUpperCase());
            } catch (IllegalArgumentException ignore) { }

            // Some references for the data we pull out of each theme node
            Character tag = null;
            ChatColor color = null;
            ChatColor style = null;

            // Grab the tag value
            node = element.getElementsByTagName("tag").item(0);
            if (node != null) {
                final String value = node.getTextContent();
                if (value != null && !value.isEmpty()) {
                    tag = value.charAt(0);
                }
            }

            // Grab the color value
            node = element.getElementsByTagName("color").item(0);
            if (node != null) {
                final String value = node.getTextContent();
                if (value != null && !value.isEmpty()) {
                    try {
                        color = ChatColor.valueOf(value.toUpperCase());
                    } catch (IllegalArgumentException ignore) { }
                }
            }

            // Grab the style value
            node = element.getElementsByTagName("style").item(0);
            if (node != null) {
                final String value = node.getTextContent();
                if (value != null && !value.isEmpty()) {
                    try {
                        style = ChatColor.valueOf(value.toUpperCase());
                    } catch (IllegalArgumentException ignore) { }
                }
            }

            // We have to have found a color and tag to care about the theme in the xml
            if ((color != null || style != null) && tag != null && !tagMap.containsKey(tag)) {
                tagMap.put(tag, getColor(color, style));
                if (applicableTheme != null) {
                    themeSet.remove(applicableTheme);
                    applicableTheme.tag = tag;
                    applicableTheme.color = color;
                }
            }
        }

        // Now we iterate over any of the remaining enum elements to add them as defaults.
        for (final Theme theme : themeSet) {
            if (!tagMap.containsKey(theme.tag)) {
                tagMap.put(theme.tag, getColor(theme.color, theme.style));
            }
        }
    }

    private static String getColor(@Nullable final ChatColor color, @Nullable final ChatColor style) {
        final String result = (color != null ? color.toString() : "") + (style != null ? style.toString() : "");
        return result.isEmpty() ? ChatColor.RESET.toString() : result;
    }

    /**
     * The character that indicates a theme tag is being used.
     */
    public static final char THEME_MARKER = '$';
    /**
     * The character that represents the following {@link #THEME_MARKER} is not actually a theme.
     */
    public static final char THEME_ESCAPE_CHAR = '\\';

    @NotNull
    static String parseMessage(@NotNull final String message) {
        final StringBuilder buffer = new StringBuilder(message.length() + 10);
        char previousChar = ' ';
        for (int i = 0; i < message.length() - 1; i++) {
            final char currentChar = message.charAt(i);
            if (currentChar == THEME_MARKER && previousChar != THEME_ESCAPE_CHAR) {
                final String color = tagMap.get(message.charAt(i + 1));
                if (color != null) {
                    buffer.append(color);
                    i++;
                } else {
                    buffer.append(currentChar);
                }
            } else {
                buffer.append(currentChar);
            }
            previousChar = currentChar;
        }
        if (!message.isEmpty()) {
            buffer.append(message.charAt(message.length() - 1));
        }
        return buffer.toString();
    }
}
