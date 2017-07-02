package pluginbase.messages

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

import java.util.EnumSet
import java.util.HashMap

enum class Theme constructor(tag: Char, private var color: ChatColor?, private var style: ChatColor? = null) {
    SUCCESS('+', ChatColor.GREEN),
    ERROR('-', ChatColor.RED),
    INVALID('^', ChatColor.RED),
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
    QUESTION('?', ChatColor.DARK_PURPLE),

    CMD_USAGE('c', ChatColor.WHITE),
    CMD_FLAG('f', ChatColor.GOLD, ChatColor.ITALIC),
    OPT_ARG('o', ChatColor.GOLD),
    REQ_ARG('r', ChatColor.GREEN),
    CMD_HIGHLIGHT('C', null, ChatColor.BOLD),

    HEADER('=', ChatColor.LIGHT_PURPLE),
    LIST_ODD(':', ChatColor.WHITE),
    LIST_EVEN(';', ChatColor.YELLOW);

    var tag: Char = tag
        private set

    override fun toString(): String {
        return getColor(color, style)
    }

    companion object {

        private val tagMap = HashMap<Char, String>()

        val themeResource = "theme.xml"

        fun loadTheme(document: Document) {
            tagMap.clear()

            // Create a set of all the enum Theme elements so we can tell what wasn't handled in the file
            val themeSet = EnumSet.allOf(Theme::class.java)

            document.documentElement.normalize()
            // Grab the main set of nodes from the document node and iterate over them
            val nodes = document.documentElement.childNodes
            for (i in 0..nodes.length - 1) {
                var node: Node? = nodes.item(i)
                if (node!!.nodeType != Node.ELEMENT_NODE) {
                    continue
                }
                val element = node as Element?

                // Check to see if the node name represents a Theme enum
                var applicableTheme: Theme? = null
                try {
                    applicableTheme = Theme.valueOf(element!!.nodeName.toUpperCase())
                } catch (ignore: IllegalArgumentException) {
                }

                // Some references for the data we pull out of each theme node
                var tag: Char? = null
                var color: ChatColor? = null
                var style: ChatColor? = null

                // Grab the tag value
                node = element!!.getElementsByTagName("tag").item(0)
                if (node != null) {
                    val value = node.textContent
                    if (value != null && !value.isEmpty()) {
                        tag = value[0]
                    }
                }

                // Grab the color value
                node = element.getElementsByTagName("color").item(0)
                if (node != null) {
                    val value = node.textContent
                    if (value != null && !value.isEmpty()) {
                        try {
                            color = ChatColor.valueOf(value.toUpperCase())
                        } catch (ignore: IllegalArgumentException) {
                        }

                    }
                }

                // Grab the style value
                node = element.getElementsByTagName("style").item(0)
                if (node != null) {
                    val value = node.textContent
                    if (value != null && !value.isEmpty()) {
                        try {
                            style = ChatColor.valueOf(value.toUpperCase())
                        } catch (ignore: IllegalArgumentException) {
                        }

                    }
                }

                // We have to have found a color and tag to care about the theme in the xml
                if ((color != null || style != null) && tag != null && !tagMap.containsKey(tag)) {
                    tagMap.put(tag, getColor(color, style))
                    if (applicableTheme != null) {
                        themeSet.remove(applicableTheme)
                        applicableTheme.tag = tag
                        applicableTheme.color = color
                        applicableTheme.style = style
                    }
                }
            }

            // Now we iterate over any of the remaining enum elements to add them as defaults.
            for (theme in themeSet) {
                tagMap.putIfAbsent(theme.tag, getColor(theme.color, theme.style))
            }
        }

        private fun getColor(color: ChatColor?, style: ChatColor?): String {
            val result = (color?.toString() ?: "") + (style?.toString() ?: "")
            return if (result.isEmpty()) ChatColor.RESET.toString() else result
        }

        /**
         * Gets the Minecraft color string associated with the given theme tag, if any.
         *
         * @param themeTag the theme tag to convert to color
         * @return the Minecraft color code string or null if themeTag is not valid.
         */
        fun getColorByTag(themeTag: Char): String? {
            return tagMap[themeTag]
        }

        /**
         * The character that indicates a theme tag is being used.
         */
        val THEME_MARKER = '$'
        /**
         * The character that represents the following [.THEME_MARKER] is not actually a theme.
         */
        val THEME_ESCAPE_CHAR = '\\'

        internal fun parseMessage(message: String): String {
            val buffer = StringBuilder(message.length + 10)
            var previousChar = ' '
            var i = 0
            while (i < message.length - 1) {
                val currentChar = message[i]
                if (currentChar == THEME_MARKER && previousChar != THEME_ESCAPE_CHAR) {
                    val color = tagMap[message[i + 1]]
                    if (color != null) {
                        buffer.append(color)
                        i++
                    } else {
                        buffer.append(currentChar)
                    }
                } else {
                    buffer.append(currentChar)
                }
                previousChar = currentChar
                i++
            }
            if (!message.isEmpty()) {
                buffer.append(message[message.length - 1])
            }
            return buffer.toString()
        }
    }
}
