import java.nio.charset.StandardCharsets;

public final class StrSandbox {

    private static final short MAX_ROWS = 300;
    private static final short MAX_COLUMNS = 450;
    private static final byte PATTERN_MAX_LENGTH = 4;

    private static final char DEFAULT_GRID_SYMBOL = 'G';
    private static final char DEFAULT_SEPARATOR_SYMBOL = 'S';
    private static final String DEFAULT_PATTERN = STR."\{DEFAULT_SEPARATOR_SYMBOL}\{DEFAULT_GRID_SYMBOL}";
    private static final char DEFAULT_GRID_CHAR = 'O';
    private static final char DEFAULT_SEPARATOR_CHAR = ' ';

    /**
     * Draws a grid of '{@value DEFAULT_GRID_CHAR}' characters separated by '{@value DEFAULT_SEPARATOR_CHAR}' in the
     * terminal.
     *@param rows       number of rows. Clamps at {@value MAX_ROWS}
     *@param columns    number of columns. Clamps at {@value MAX_COLUMNS}
     */
    public static void drawGrid (int rows, int columns){
        drawGrid(rows, columns, DEFAULT_GRID_CHAR, DEFAULT_SEPARATOR_CHAR, DEFAULT_PATTERN, false);
    }
    /**
     * Draws a grid of user-defined characters in the terminal.
     *@param rows           number of rows. Clamps at {@value MAX_ROWS}
     *@param columns        number of columns. Clamps at {@value MAX_COLUMNS}
     *@param gridChar       character used to fill the grid
     *@param separator      character used as separator
     *@param pattern        Pattern with max length of {@value PATTERN_MAX_LENGTH}. Used to define custom arrangement of
     *                      grid ({@value DEFAULT_GRID_SYMBOL}) and separator ({@value DEFAULT_SEPARATOR_SYMBOL}) chars.
     *                      e.g. "SG", "SSG", "SGS"
     *@param useEvenOffset  used to offset the previously defined pattern in even rows
     */
    public static void drawGrid (int rows, int columns, char gridChar, char separator, String pattern, boolean useEvenOffset){
        if (pattern == null) {
            System.out.println("Pattern must not be Null!");
            return;
        }

        if (pattern.isEmpty()) pattern = DEFAULT_PATTERN;

        // sanitize pattern String by clamping it to defined max length and removing unwanted characters
        String sanitizedPattern = pattern.substring(0, Math.min(pattern.length(), PATTERN_MAX_LENGTH));
        sanitizedPattern = sanitizedPattern.replaceAll(STR."[^\{DEFAULT_GRID_SYMBOL}\{DEFAULT_SEPARATOR_SYMBOL}]", "");
        if (sanitizedPattern.isEmpty()) {
            System.out.println(STR."Pattern argument doesn't contain any valid characters. Only '\{DEFAULT_GRID_SYMBOL}'"
                    + STR." and '\{DEFAULT_SEPARATOR_SYMBOL}' allowed in pattern definition.");
            return;
        }

        // build the String used for printing the actual grid in terminal based on sanitized pattern String
        StringBuilder strBuilder = new StringBuilder();
        for(char c : sanitizedPattern.toCharArray()) {
            switch (c) {
                case DEFAULT_SEPARATOR_SYMBOL:
                    strBuilder.append(separator);
                    break;
                case DEFAULT_GRID_SYMBOL:
                    strBuilder.append(gridChar);
                    break;
                default:
                    System.out.println(STR."Invalid pattern char '\{c}' not allowed!");
            }
        }

        String gridStr = strBuilder.toString();
        if (gridStr.isEmpty()) {
            System.out.println("Couldn't build grid String. Check if pattern argument is valid!");
            return;
        }
        String gridStrOffset = gridStr.substring(1) + gridStr.charAt(0);

        // draw the grid using sout
        final int rowsClamped = Math.min(Math.abs(rows), MAX_ROWS);
        final int columnsClamped = Math.min(Math.abs(columns), MAX_COLUMNS);
        final int cells = rowsClamped * columnsClamped;
        for (int i = 0; i < cells; i++) {
            boolean isEvenColumn = (i + columnsClamped) / columnsClamped % 2 == 0;
            if (i % columnsClamped == 0) System.out.println();
            System.out.print(isEvenColumn && useEvenOffset ? gridStrOffset : gridStr);
        }
        System.out.println();
    }

    private static final int INDEX_OF_A = "A".toCharArray()[0];
    private static final int INDEX_OF_Z = "Z".toCharArray()[0];
    private static final int ALPHABET_LEN = INDEX_OF_Z - INDEX_OF_A;

    /**
     * Returns the Latin alphabet in either upper- or lower case.
     * @param   inUpperCase
     * @return  String
     */
    public static String getLatinLetters(boolean inUpperCase){
        final int lowerCaseOffset = 32;
        StringBuilder sb = new StringBuilder();
        for(int i = INDEX_OF_A; i < INDEX_OF_A + ALPHABET_LEN; i++){
            int charIndex = inUpperCase ? i : (i + lowerCaseOffset);
            sb.append((char) charIndex);
        }
        return sb.toString();
    }

    /**
     * Returns true of b is less than 'A' or bigger than 'Z'.
     * @param b
     * @return  boolean
     */
    private static boolean isUpperCaseByteCode(byte b){ return !(b < INDEX_OF_A || b > INDEX_OF_Z); }

    /**
     * Takes a String and will encode it with a key using the Caesar cipher.
     * @param message   Message that will be encoded
     * @param key       Key used to encode the message
     * @return          Encoded message as String
     */
    public static String encodeCaesarCipher(String message, int key){
        if (message == null) {
            System.out.println("Message must not be null!");
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for(byte b : message.toUpperCase().getBytes(StandardCharsets.UTF_8)){
            int shiftedByteCode = INDEX_OF_A + Math.floorMod(b - INDEX_OF_A + key, ALPHABET_LEN);
            int byteCode = StrSandbox.isUpperCaseByteCode(b) ? shiftedByteCode : b;
            sb.append((char) byteCode);
        }
        return sb.toString();
    }

    /**
     * Takes an encoded String and will decode it with a key using the Caesar cipher.
     * @param encodedMessage Encoded message that will be decoded
     * @param key            Key used to decode the message
     * @return               Decoded message as String
     */
    public static String decodeCaesarCipher(String encodedMessage, int key){
        return encodeCaesarCipher(encodedMessage, key * -1);
    }
}
