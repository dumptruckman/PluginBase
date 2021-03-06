/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages

import java.util.HashMap

/**
 * All supported color values for chat.
 */
enum class ChatColor constructor(
        /**
         * Gets the char value associated with this color.
         *
         * @return A char value of this color code.
         */
        val char: Char,
        private val intCode: Int,
        /**
         * Checks if this code is a format code as opposed to a color code.
         */
        val isFormat: Boolean = false) {
    /**
     * Represents black.
     */
    BLACK('0', 0x00),
    /**
     * Represents dark blue.
     */
    DARK_BLUE('1', 0x1),
    /**
     * Represents dark green.
     */
    DARK_GREEN('2', 0x2),
    /**
     * Represents dark blue (aqua).
     */
    DARK_AQUA('3', 0x3),
    /**
     * Represents dark red.
     */
    DARK_RED('4', 0x4),
    /**
     * Represents dark purple.
     */
    DARK_PURPLE('5', 0x5),
    /**
     * Represents gold.
     */
    GOLD('6', 0x6),
    /**
     * Represents gray.
     */
    GRAY('7', 0x7),
    /**
     * Represents dark gray.
     */
    DARK_GRAY('8', 0x8),
    /**
     * Represents blue.
     */
    BLUE('9', 0x9),
    /**
     * Represents green.
     */
    GREEN('a', 0xA),
    /**
     * Represents aqua.
     */
    AQUA('b', 0xB),
    /**
     * Represents red.
     */
    RED('c', 0xC),
    /**
     * Represents light purple.
     */
    LIGHT_PURPLE('d', 0xD),
    /**
     * Represents yellow.
     */
    YELLOW('e', 0xE),
    /**
     * Represents white.
     */
    WHITE('f', 0xF),
    /**
     * Represents magical characters that change around randomly.
     */
    MAGIC('k', 0x10, true),
    /**
     * Makes the text bold.
     */
    BOLD('l', 0x11, true),
    /**
     * Makes a line appear through the text.
     */
    STRIKETHROUGH('m', 0x12, true),
    /**
     * Makes the text appear underlined.
     */
    UNDERLINE('n', 0x13, true),
    /**
     * Makes the text italic.
     */
    ITALIC('o', 0x14, true),
    /**
     * Resets all previous chat colors or formats.
     */
    RESET('r', 0x15);

    private val stringForm by lazy { "$COLOR_CHAR$char" }

    override fun toString(): String {
        return stringForm
    }

    /**
     * Checks if this code is a color code as opposed to a format code.
     */
    val isColor: Boolean
        get() = !isFormat && this != RESET

    companion object {

        /**
         * The special character which prefixes all chat colour codes.
         *
         * Use this if you need to dynamically convert colour codes from your custom format.
         */
        const val COLOR_CHAR = '\u00A7'

        private val STRIP_COLOR_PATTERN = "(?i)$COLOR_CHAR[0-9A-FK-OR]".toPattern()
        private val BY_ID = HashMap<Int, ChatColor>()
        private val BY_CHAR = HashMap<Char, ChatColor>()

        /**
         * Gets the color represented by the specified color code.
         *
         * @param code Code to check.
         * @return Associated [ChatColor] with the given code, or null if it doesn't exist.
         */
        @JvmStatic
        fun getByChar(code: Char): ChatColor? {
            return BY_CHAR[code]
        }

        /**
         * Gets the color represented by the specified color code.
         *
         * @param code Code to check.
         * @return Associated [ChatColor] with the given code, or null if it doesn't exist.
         */
        @JvmStatic
        fun getByChar(code: String): ChatColor? {
            return BY_CHAR[code[0]]
        }

        /**
         * Strips the given message of all color codes.
         *
         * @param input String to strip of color.
         * @return A copy of the input string, without any coloring.
         */
        @JvmStatic
        fun stripColor(input: String?): String? {
            if (input == null) {
                return null
            }

            return STRIP_COLOR_PATTERN.matcher(input).replaceAll("")
        }

        /**
         * Translates a string using an alternate color code character into a string that uses the internal
         * ChatColor.COLOR_CODE color code character.
         *
         * The alternate color code character will only be replaced if it is immediately followed by 0-9, A-F, or a-f.
         *
         * @param altColorChar The alternate color code character to replace. Ex: &.
         * @param textToTranslate Text containing the alternate color code character.
         * @return Text containing the ChatColor.COLOR_CODE color code character.
         */
        @JvmStatic
        fun translateAlternateColorCodes(altColorChar: Char, textToTranslate: String): String {
            val b = textToTranslate.toCharArray()
            for (i in 0..(b.size - 2)) {
                if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                    b[i] = COLOR_CHAR
                    b[i + 1] = Character.toLowerCase(b[i + 1])
                }
            }
            return String(b)
        }

        /**
         * Gets the ChatColors used at the end of the given input string.
         *
         * @param input Input string to retrieve the colors from.
         * @return Any remaining ChatColors to pass onto the next line.
         */
        @JvmStatic
        fun getLastColors(input: String): String {
            var result = ""
            val length = input.length

            // Search backwards from the end as it is faster
            for (index in length - 1 downTo 0) {
                val section = input[index]
                if (section == COLOR_CHAR && index < length - 1) {
                    val c = input[index + 1]
                    val color = getByChar(c)

                    if (color != null) {
                        result = color.toString() + result

                        // Once we find a color or reset we can stop searching
                        if (color.isColor || color == RESET) {
                            break
                        }
                    }
                }
            }

            return result
        }

        init {
            for (color in values()) {
                BY_ID.put(color.intCode, color)
                BY_CHAR.put(color.char, color)
            }
        }
    }
}
