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
    private static final int ALPHABET_LEN = INDEX_OF_Z - INDEX_OF_A + 1;
    private static final int LOWERCASE_OFFSET = 32;

    /**
     * Returns the Latin alphabet in either upper- or lower case.
     * @param   inUpperCase If true, prints alphabet in upper case
     * @return  String
     */
    public static String getLatinLetters(boolean inUpperCase){
        StringBuilder sb = new StringBuilder();
        for(int i = INDEX_OF_A; i < INDEX_OF_A + ALPHABET_LEN; i++){
            int charIndex = inUpperCase ? i : (i + LOWERCASE_OFFSET);
            sb.append((char) charIndex);
        }
        return sb.toString();
    }

    /**
     * Returns true if b is between 'A' and 'Z'.
     * @param b byte to check against
     * @return  boolean
     */
    public static boolean isUpperCaseLetter(byte b){ return !(b < INDEX_OF_A || b > INDEX_OF_Z); }

    /**
     * Returns true if c is between 'A' and 'Z'.
     * @param c char to check against
     * @return  boolean
     */
    public static boolean isUpperCaseLetter(char c) { return Character.toString(c).matches("[A-Z]"); }

    /**
     * Returns true if b is between 'a' and 'z'.
     * @param b byte to check against
     * @return  boolean
     */
    public static boolean isLowerCaseLetter(byte b){
        final int indexOfLowerCaseA = INDEX_OF_A + LOWERCASE_OFFSET;
        final int indexOfLowerCaseZ = INDEX_OF_Z + LOWERCASE_OFFSET;
        return !(b < indexOfLowerCaseA || b > indexOfLowerCaseZ);
    }

    /**
     * Returns true if c is between 'a' and 'z'.
     * @param c byte to check
     * @return  boolean
     */
    public static boolean isLowerCaseLetter(char c){ return Character.toString(c).matches("[a-z]"); }

    /**
     * Returns true if b is between either an upper- or lower case Latin letter.
     * @param  b byte to check against
     * @return   boolean
     */
    public static boolean isLatinLetter(byte b){ return (isUpperCaseLetter(b) || isLowerCaseLetter(b)); }

    /**
     * Returns true if c is between either an upper- or lower case Latin letter.
     * @param  c char to check against
     * @return   boolean
     */
    public static boolean isLatinLetter(char c){ return Character.toString(c).matches("[A-z]"); }

    /**
     * Returns true if c is between '0' and '9'.
     * @param  c char to check against
     * @return   boolean
     */
    public static boolean isDigit(char c){ return Character.toString(c).matches("[0-9]"); }

    /**
     * Recursive helper method for wrapAt().
     * @param str   String to wrap
     * @param sb    StringBuilder Object used to assemble the new String
     * @param index length at which the method tries to wrap the String
     * @see #wrapAt(String, int)
     */
    private static void wrapAtHelper(String str, StringBuilder sb, int index){
        int i = Math.abs(index);

        if(str == null || sb == null){
            System.out.println("Str must not be null");
            return;
        }
        if(i > str.length()){
            sb.append(str);
            return;
        }
        // parse str backwards starting at index position to check where the nearest white space is
        while( i != 0) {
            char currentChar = str.charAt(i);
            boolean isWhiteSpace = Character.toString(currentChar).matches("\\s");
            if(isWhiteSpace){
                sb.append(STR."\{str.substring(0, i + 1)}\n");
                break;
            }
            i--;
        }
        // try to append a whole word, if we were unable to find a white space
        boolean noWhiteSpaceFound = i == 0 && !Character.toString(str.charAt(i)).matches("\\s");
        if (noWhiteSpaceFound){
            String word = str.split("\\s")[0];
            sb.append(STR."\{word}\n");
            i = word.length();
            // exit if this seems to be the last piece of String available
            if (i >= str.substring(i).length()){
                return;
            }
        }
        // call recursively until there is no string left
        wrapAtHelper(str.substring(i + 1), sb, index);
    }

    /**
     * Wraps a String by inserting '\n' at the length given by the index parameter. If length specified is too short,
     * it will wrap after the next word.
     * @param str   String to wrap
     * @param index length at which the method tries to wrap the String
     * @return      Reformatted String
     */
    public static String wrapAt(String str, int index){
        StringBuilder sb = new StringBuilder();
        wrapAtHelper(str, sb, index);
        return sb.toString();
    }

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
            int byteCode = isUpperCaseLetter(b) ? shiftedByteCode : b;
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

    private static final char ENC_DECIMAL_SEPARATOR = '-';

    /**
     * The goal is to encode letters and digits in decimal separated by '{@value ENC_DECIMAL_SEPARATOR}'. Other chars are
     * kept unchanged. "A" starts out at index 1, "B" = 2 etc. Lower case indices start at index 27.
     * Numbers start at index 53 with digit '0'.
     * @param message           message to be encoded
     * @param includeLowerCase  encode lower case or treat every letter as upper case
     * @return                  Encoded String
     */
    public static String encodeDecimal(String message, boolean includeLowerCase){
        if (message == null) {
            System.out.println("Message must not be null!");
            return "";
        }

        StringBuilder sb = new StringBuilder();
        final String msg = includeLowerCase ? message : message.toUpperCase();
        final byte[] bmsg = msg.getBytes(StandardCharsets.UTF_8);
        for(int i = 0; i < bmsg.length; i++){
            String str = "";
            final char currentChar = (char) bmsg[i];
            // handle non letters/digits and exit iteration early
            if(!(isLatinLetter(currentChar) || isDigit(currentChar))){
                sb.append(Character.toString(currentChar));
                continue;
            }
            // handle letters/digits
            if (isLatinLetter(currentChar)){
                int initialOffset = currentChar - INDEX_OF_A + 1;
                int shiftedB = isUpperCaseLetter(currentChar) ? initialOffset : initialOffset - LOWERCASE_OFFSET + ALPHABET_LEN;
                str = shiftedB < 10 ? STR."0\{shiftedB}" : Integer.toString(shiftedB);
            } else if (isDigit(currentChar)) {
                int initialOffset = currentChar - 48 + 1 + ALPHABET_LEN * 2;
                str = STR."\{initialOffset + Integer.parseInt(Character.toString(currentChar))}";
            }
            // check if next char exists and append separator if necessary
            if (i < msg.length() - 1) {
                char nextChar = msg.charAt(i + 1);
                str = isLatinLetter(nextChar) || isDigit(nextChar) ? STR."\{str}\{ENC_DECIMAL_SEPARATOR}" : str;
            }

            sb.append(str);
        }
        return sb.toString();
    }
}
