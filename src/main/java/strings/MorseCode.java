package strings;

/**
 * The {@code MorseCode} enum contains most characters of the Morse alphabet, some Morse prosigns and punctuation
 * characters. Every entry holds a representation of the character as a literal and the corresponding Morse code
 * sequence. The {@code generate()} method can be used to convert a {@code String} to Morse code notation.
 *
 */
public enum MorseCode{
    // literals
    A("A", "._"), B("B", "_..."), C("C", "_._."), D("D", "_.."), E("E", "."), F("F", ".._."), G("G", "__."),
    H("H", "...."), I("I", ".."), J("J", ".___"), K("K",  "_._"), L("L",  "._.."), M("M", "__"), N("N",  "_."),
    O("O",  "___"), P("P", ".__."), Q("Q",  "__._"), R("R",  "._."), S("S", "..."), T("T",  "_"), U("U",  ".._"),
    V("V", "..._"), W("W",  ".__"), X("X",  "_.._"), Y("Y", "_.__"), Z("Z",  "__.."), _0("0",  "_____"),
    _1("1",  ".____"), _2("2",  "..___"), _3("3",  "...__"), _4("4",  "...._"), _5("5",  "....."), _6("6",  "_...."),
    _7("7",  "__..."), _8("8",  "___.."), _9("9",  "____."), AE("Ä",  "._._") , OE("Ö",  "___."), UE("Ü",  "..__"),
    AA("Å",  ".__._"), EE("É",  ".._.."), SE("È",  "._.._"), SZ("ß",  "...__.."), CH("CH",  "____"), NY("Ñ",  "__.__"),
    // punctuation
    AAA(".",  "._._._"), MIM(",",  "__..__"), OS(":",  "___..."), NNN(";",  "_._._."), UD("?",  "..__.."),
    EM("!",  "_._.__"), BA("-",  "_...._"), UK("_",  "..__._"), KN("(",  "_.__."), KK(")",  "_.__._"),
    JN("'",  ".____."), BT("=",  "_..._"), AR("+",  "._._."), DN("/",  "_.._."), QM("\"",  "._.._."), AC("@",  ".__._."),
    // Morse code prosigns
    KA("",  "_._._"), VE("",  "..._."), SK("",  "..._._"), SOS("",  "...___..."), CQD("",  "_._. __._ _.."),
    HH("",  "........"),
    // NONE object for validation
    NONE("","");

    private static final char SHORT_MARK = '·';
    private static final char LONG_MARK = '–';
    private static final char GAP = ' ';
    private static final char SIGNAL_MARK = '▓';
    private static final char SIGNAL_GAP = '˽';
    
    private static final String SHORT_GAP = STR."\{GAP}";
    private static final String LONG_GAP = STR."\{GAP}\{GAP}\{GAP}";
    private static final String SIGNAL_SHORT_MARK = STR."\{SIGNAL_MARK}";
    private static final String SIGNAL_LONG_MARK = STR."\{SIGNAL_MARK}\{SIGNAL_MARK}\{SIGNAL_MARK}";
    private static final String SIGNAL_LETTER_GAP = STR."\{SIGNAL_GAP}\{SIGNAL_GAP}\{SIGNAL_GAP}";
    private static final String SIGNAL_WORD_GAP =
            STR."\{SIGNAL_GAP}\{SIGNAL_GAP}\{SIGNAL_GAP}\{SIGNAL_GAP}\{SIGNAL_GAP}\{SIGNAL_GAP}\{SIGNAL_GAP}";

    private final String literal;
    private final String sequence;

    MorseCode(String literal, String sequence){
        this.literal = literal;
        this.sequence = sequence;
    }

    public String getSequence() { return sequence;}

    public String getLiteral() { return literal; }

    /**
     * Converts the {@code message} given as parameter into Morse code notation. Morse code 'dahs' are represented by
     * '{@value LONG_MARK}' and 'dits' by the '{@value SHORT_MARK}' character. The signal notation uses
     * '{@value SIGNAL_MARK}' for 'signal on' and '{@value SIGNAL_GAP}' for 'signal off'. Escaped white space characters
     * are kept intact. Regular white spaces will be converted according to notation. Unknown characters are converted
     * to {@code NONE} and a notification about the failed conversion is sent to the console.
     * @param message           Message to convert to Morse code
     * @param isSignalSequence  If true, will return the message in Morse code signal notation
     * @return String in Morse code notation or Morse signal notation
     */
    public static String generate(String message, boolean isSignalSequence){
        if (message == null){
            System.out.println("String object 'message' must not be null");
            return "";
        }

        final String shortGap = isSignalSequence ? SIGNAL_LETTER_GAP : SHORT_GAP;
        final String longGap = isSignalSequence ? SIGNAL_WORD_GAP : LONG_GAP;
        final char[] chars = message.toCharArray();

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < chars.length; i++){
            String toStr = Character.toString(chars[i]);
            MorseCode mc = MorseCode.find(toStr);
            boolean isSingleWhiteSpace = toStr.equals(" ");

            // handle escaped white space
            if(toStr.matches("\\s") && !isSingleWhiteSpace) sb.append(toStr);
            // handle single white space
            else if(isSingleWhiteSpace) sb.append(longGap);
            // handle Strings.MorseCode specific values
            else if (MorseCode.isValid(mc)) {
                String sequence = generateSequence(mc.sequence, isSignalSequence);
                boolean isLastChar = i == chars.length - 1;
                if(!isLastChar) {
                    String nextChar = Character.toString(chars[i + 1]);
                    boolean isLastCharInWord = nextChar.equals(" ");
                    sb.append(isLastCharInWord ? sequence : STR."\{sequence}\{shortGap}");
                }
                else sb.append(sequence);
            }
            // handle exceptions
            else {
                System.out.println(STR."No valid Morse code available for character '\{chars[i]}'");
                sb.append(NONE.sequence);
            }
        }
        return sb.toString();
    }

    /**
     * Converts the {@code message} given as parameter into Morse code notation. Morse code 'dahs' are represented by
     * '{@value LONG_MARK}' and 'dits' by the '{@value SHORT_MARK}' character. The signal notation uses
     * '{@value SIGNAL_MARK}' for 'signal on' and '{@value SIGNAL_GAP}' for 'signal off'. Escaped white space characters
     * are kept intact. Regular white spaces will be converted according to notation. Unknown characters are converted
     * to {@code NONE} and a notification about the failed conversion is sent to the console.
     * @param   message   Message to convert to Morse code
     * @return  String in Morse code notation
     * @see #generate(String, boolean)
     */
    public static String generate(String message){ return generate(message, false); }

    public static String decode(String morseCode){
        if (morseCode == null){
            System.out.println("String object 'morseCode' must not be null");
            return "";
        }

        return "";
    }

    private static boolean isValid(MorseCode mc) { return mc != NONE; }

    private static MorseCode find(String str){
        for(MorseCode mc : MorseCode.values()){
            if(str.toUpperCase().equals(mc.literal) || str.toUpperCase().equals(mc.sequence)) return mc;
        }
        return NONE;
    }

    private static String generateSequence(String pattern, boolean isSignalSequence) {
        final String shortGap = isSignalSequence ? SIGNAL_LETTER_GAP : SHORT_GAP;
        final String shortMark = isSignalSequence ? SIGNAL_SHORT_MARK : STR."\{SHORT_MARK}";
        final String longMark = isSignalSequence ? SIGNAL_LONG_MARK : STR."\{LONG_MARK}";

        StringBuilder sb = new StringBuilder();
        char[] chars = pattern.toCharArray();
        for(int i = 0; i < chars.length; i++ ){
            String tmp = "";
            String toStr = Character.toString(chars[i]);
            boolean isLastChar = i == chars.length - 1;
            switch (toStr) {
                case "_" -> {
                    if (isSignalSequence) tmp = isLastChar ? longMark : longMark + SIGNAL_GAP;
                    else tmp = longMark;
                }
                case "." -> {
                    if (isSignalSequence) tmp = isLastChar ? shortMark : shortMark + SIGNAL_GAP;
                    else tmp = shortMark;
                }
                case " " -> tmp = shortGap;
                default -> System.out.println("Only use literals '_' or '.' to define pattern");
            }
            sb.append(tmp);
        }
        return sb.toString();
    }
}
