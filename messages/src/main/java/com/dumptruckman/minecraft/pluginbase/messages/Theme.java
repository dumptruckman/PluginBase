package com.dumptruckman.minecraft.pluginbase.messages;

import org.jetbrains.annotations.NotNull;
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
    PLAIN('.', ChatColor.RESET),
    IMPORTANT('!', ChatColor.BOLD),
    HELP('h', ChatColor.WHITE),
    INFO('i', ChatColor.YELLOW),
    VALUE('v', ChatColor.DARK_GREEN),
    ;

    @NotNull
    private Character tag;
    @NotNull
    private ChatColor color;

    private Theme(@NotNull final Character tag, @NotNull final ChatColor color) {
        this.tag = tag;
        this.color = color;
    }

    @NotNull
    public Character getTag() {
        return tag;
    }

    @NotNull
    @Override
    public String toString() {
        return color.toString();
    }

    private static final Map<Character, ChatColor> tagMap = new HashMap<Character, ChatColor>();

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

            // We have to have found a color and tag to care about the theme in the xml
            if (color != null && tag != null) {
                tagMap.put(tag, color);
                if (applicableTheme != null) {
                    themeSet.remove(applicableTheme);
                    applicableTheme.tag = tag;
                    applicableTheme.color = color;
                }
            }
        }

        // Now we iterate over any of the remaining enum elements to add them as defaults.
        for (final Theme theme : themeSet) {
            tagMap.put(theme.tag, theme.color);
        }
    }

    private static final char THEME_MARKER = '$';

    @NotNull
    static String parseMessage(@NotNull final String message) {
        final char[] chars = message.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == THEME_MARKER) {
                final ChatColor color = tagMap.get(chars[i + 1]);
                if (color != null) {
                    chars[i] = ChatColor.COLOR_CHAR;
                    chars[i + 1] = color.getChar();
                }
            }
        }
        return new String(chars);
    }
}
