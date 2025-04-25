package strings;

import java.util.ArrayList;

/**
 *
 */
public class MorseCode {

    private ArrayList<MorseNotation> literals = new ArrayList<>();

    public MorseCode(String message) {
        if (message == null) {
            System.out.println("\u001B[31m String object 'message' must not be null\u001B[0m");
            return;
        }

        for (char c : message.toCharArray()) {
            String toStr = Character.toString(c);
            MorseNotation mn = MorseNotation.search(toStr, true);

            if (!MorseNotation.isValid(mn)) {
                System.out.println(STR."\u001B[33m No valid Morse code available for character '\{c}'\u001B[0m");
            }
            literals.add(mn);
        }
    }

    /**
     * Returns the stored Morse code message as clear text.
     * @return String as clear text.
     */
    public String getClearText() {
        StringBuilder sb = new StringBuilder();
        for (MorseNotation mn : literals) {
            sb.append(mn.literal);
        }
        return sb.toString();
    }

    /**
     * Decodes a given {@code morseCode} String and will return a clear text as a String.
     * @param   morseCode The String that is decoded. Has to be in Morse code or Morse Signal notation.
     * @return  String as clear text.
     */
    public static String decode(String morseCode){
        if (morseCode == null) {
            System.out.println("\u001B[31m String object 'morseCode' must not be null\u001B[0m");
            return "";
        }

        String firstChar = Character.toString(morseCode.trim().charAt(0));
        // check if the message is an actual Morse code
        String regEx =  STR."\{MorseNotation.SIGNAL_MARK}|\{MorseNotation.SIGNAL_GAP}|" +
                        STR."\{MorseNotation.SHORT_MARK}|\{MorseNotation.LONG_MARK}";
        boolean isMorseCode = firstChar.matches(regEx);

        if(!isMorseCode){
            System.out.println("\u001B[33m String object 'morseCode' is not a valid Morse Code\u001B[0m");
            return "";
        }

        // check if it's in signal or morse notation
        regEx = STR."\{MorseNotation.SIGNAL_MARK}|\{MorseNotation.SIGNAL_GAP}";
        boolean isSignalSequence = firstChar.matches(regEx);

        return isSignalSequence ? decodeSignalString(morseCode) : decodeMorseString(morseCode);
    }

    private static String decodeMorseString(String morseCode){
        String splitRegEx = " |\r|\n";
        String[] splits = morseCode.splitWithDelimiters(splitRegEx, -1);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < splits.length; i++) {
            String current = splits[i];

            if(current.isEmpty()) continue;

            if(current.equals("\r") || current.equals("\n")){
                sb.append(current);
                continue;
            }

            if(current.equals(" ")){
                // check for triple white space sequence to convert to one white space,
                // otherwise just append an empty String to ignore the current array entry
                if (i + 5 < splits.length){
                    // checks the next four entries in array for sequence ["", " ", "", " "]
                    boolean isTripleWS = true;
                    for(int j = 1; j < 5; j++){
                        isTripleWS &= j % 2 == 0 ? splits[i+j].equals(" ") : splits[i+j].isEmpty();
                    }

                    if (isTripleWS) {
                        sb.append(" ");
                        i += 4;
                    }
                }
                continue;
            }

            current = current.replace(STR."\{MorseNotation.SHORT_MARK}", ".");
            current = current.replace(STR."\{MorseNotation.LONG_MARK}", "_");
            MorseNotation mc = MorseNotation.search(current, false);
            if (MorseNotation.isValid(mc)) {
                sb.append(mc.literal);
            } else {
                System.out.println(STR."\u001B[33m No valid character conversion found for String '\{current}'\u001B[0m");
            }
        }
        return sb.toString();
    }

    private static String decodeSignalString(String morseCode){
        String splitRegEx = STR."\{MorseNotation.SIGNAL_WORD_GAP}|\{MorseNotation.SIGNAL_LETTER_GAP}|\r|\n";
        String[] splits = morseCode.splitWithDelimiters(splitRegEx, -1);
        StringBuilder sb = new StringBuilder();
        for(String string : splits){
            if(string.equals(MorseNotation.SIGNAL_LETTER_GAP) || string.isEmpty()) continue;

            if(string.equals(MorseNotation.SIGNAL_WORD_GAP)){
                sb.append(" ");
                continue;
            }

            if(string.matches("\r|\n")){
                sb.append(string);
                continue;
            }

            string = string.replaceAll(MorseNotation.SIGNAL_LONG_MARK, "_");
            string = string.replaceAll(MorseNotation.SIGNAL_SHORT_MARK, ".");
            string = string.replaceAll(Character.toString(MorseNotation.SIGNAL_GAP), "");
            MorseNotation mc = MorseNotation.search(string, false);
            if (MorseNotation.isValid(mc)) {
                sb.append(mc.literal);
            } else {
                System.out.println(STR."\u001B[33m No valid character conversion found for String '\{string}'\u001B[0m");
            }
        }
        return sb.toString();
    }

    /**
     * Returns the stored message in Morse code notation.
     * @return  String
     */
    public String getNotation(){ return getNotation(false); }

    /**
     * Returns the stored message in Morse code notation or signal notation.
     * @param   asSignalSequence    if true, returns the string in signal notation
     * @return  String
     */
    public String getNotation(boolean asSignalSequence){
        final String shortGap = asSignalSequence ? MorseNotation.SIGNAL_LETTER_GAP : MorseNotation.SHORT_GAP;
        final String longGap = asSignalSequence ? MorseNotation.SIGNAL_WORD_GAP : MorseNotation.LONG_GAP;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < literals.size(); i++){
            MorseNotation current = literals.get(i);
            switch (current){
                case WHITE_SPACE:
                    sb.append(longGap);
                    break;
                case TAB:
                    sb.append(longGap).append(longGap).append(longGap);
                    break;
                default:
                    if (MorseNotation.isWhiteSpace(current)){
                        sb.append(current.sequence);
                        continue;
                    }
                    String sequence = MorseNotation.generateSequence(current.sequence, asSignalSequence);
                    boolean isLastChar = i == literals.size() - 1;
                    if (!isLastChar) {
                        boolean isLastCharInWord = MorseNotation.isWhiteSpace(literals.get(i + 1));
                        sb.append(isLastCharInWord ? sequence : STR."\{sequence}\{shortGap}");
                    }
                    else
                        sb.append(sequence);
            }
        }
        return sb.toString();
    }

    /**
     * Returns a String of the Morse code using the phrases 'Dit' and 'Dah' for short and long signals respectively.
     * @return String of dits and dahs
     */
    public String getSpokenNotation(){
        final String shortBark = "Di";
        final String longBark = "Dah";
        boolean isNewWord = true;
        StringBuilder sb = new StringBuilder();
        for(MorseNotation mn : literals){
            if(MorseNotation.isWhiteSpace(mn)) {
                String gap = mn == MorseNotation.WHITE_SPACE ? MorseNotation.LONG_GAP : mn.sequence;
                sb.append(gap);
                isNewWord = true;
                continue;
            }
            char[] chars = mn.sequence.toCharArray();
            for(int i = 0; i < chars.length; i++){
                char current = chars[i];
                boolean isLastChar = i == chars.length - 1;
                if (current == '.') {
                    String diDit = isLastChar ? STR."\{shortBark.toLowerCase()}t" : shortBark.toLowerCase();
                    sb.append(isNewWord ? shortBark : diDit);
                }
                if (current == '_') sb.append(isNewWord ? longBark : longBark.toLowerCase());
                isNewWord = false;
                sb.append(isLastChar? "" : "-");
            }
            sb.append("  ");
        }
        return sb.toString();
    }

    @Override
    public String toString(){ return literals.toString();  }

    /**
     * The '{@code Notation}' enum inside the '{@code MorseCode}' class contains most characters of the Morse alphabet,
     * some Morse prosigns and punctuation characters. Every entry holds a representation of the character itself and
     * the corresponding Morse code sequence. The enum also includes some entries for white space characters to
     * distinguish between them more easily.
     */
    private enum MorseNotation {
        // literals
        A("A", "._"), B("B", "_..."), C("C", "_._."), D("D", "_.."), E("E", "."), F("F", ".._."), G("G", "__."),
        H("H", "...."), I("I", ".."), J("J", ".___"), K("K", "_._"), L("L", "._.."), M("M", "__"), N("N", "_."),
        O("O", "___"), P("P", ".__."), Q("Q", "__._"), R("R", "._."), S("S", "..."), T("T", "_"), U("U", ".._"),
        V("V", "..._"), W("W", ".__"), X("X", "_.._"), Y("Y", "_.__"), Z("Z", "__.."), _0("0", "_____"),
        _1("1", ".____"), _2("2", "..___"), _3("3", "...__"), _4("4", "...._"), _5("5", "....."), _6("6", "_...."),
        _7("7", "__..."), _8("8", "___.."), _9("9", "____."), AE("Ä", "._._"), OE("Ö", "___."), UE("Ü", "..__"),
        AA("Å", ".__._"), EE("É", ".._.."), SE("È", "._.._"), SZ("ß", "...__.."), CH("CH", "____"), NY("Ñ", "__.__"),
        // punctuation
        AAA(".", "._._._"), MIM(",", "__..__"), OS(":", "___..."), NNN(";", "_._._."), UD("?", "..__.."),
        EM("!", "_._.__"), BA("-", "_...._"), UK("_", "..__._"), KN("(", "_.__."), KK(")", "_.__._"),
        JN("'", ".____."), BT("=", "_..._"), AR("+", "._._."), DN("/", "_.._."), QM("\"", "._.._."), AC("@", ".__._."),
        // Morse code prosigns
        KA("KA", "_._._"), VE("VE", "..._."), SK("SK", "..._._"), SOS("SOS", "...___..."), HH("HH", "........"),
        // white space entries
        WHITE_SPACE(" ", " "), RETURN("\r", "\r"), NEWLINE("\n", "\n"), TAB("\t","\t"),
        // NONE entry for validation
        NONE("", "");

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

        MorseNotation(String literal, String sequence) {
            this.literal = literal;
            this.sequence = sequence;
        }

        private static boolean isValid(MorseNotation mc) {
            return mc != NONE;
        }

        private static boolean isWhiteSpace(MorseNotation mc){
            return  mc == MorseNotation.WHITE_SPACE || mc == MorseNotation.RETURN || mc == MorseNotation.NEWLINE ||
                    mc == MorseNotation.TAB;
        }

        private static MorseNotation search(String searchStr, boolean lookForLiteral) {
            for (MorseNotation mn : MorseNotation.values()) {
                boolean isValidNotation = lookForLiteral ?  searchStr.toUpperCase().equals(mn.literal):
                                                            searchStr.equals(mn.sequence);
                if (isValidNotation) return mn;
            }
            return NONE;
        }

        private static String generateSequence(String pattern, boolean isSignalSequence) {
            final String shortGap = isSignalSequence ? SIGNAL_LETTER_GAP : SHORT_GAP;
            final String shortMark = isSignalSequence ? SIGNAL_SHORT_MARK : STR."\{SHORT_MARK}";
            final String longMark = isSignalSequence ? SIGNAL_LONG_MARK : STR."\{LONG_MARK}";

            StringBuilder sb = new StringBuilder();
            char[] chars = pattern.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                String tmp = "";
                String toStr = Character.toString(chars[i]);
                boolean isLastChar = i == chars.length - 1;
                switch (toStr) {
                    case "\r", "\t", "\n" -> { continue; }
                    case "_" -> {
                        if (isSignalSequence) tmp = isLastChar ? longMark : longMark + SIGNAL_GAP;
                        else tmp = longMark;
                    }
                    case "." -> {
                        if (isSignalSequence) tmp = isLastChar ? shortMark : shortMark + SIGNAL_GAP;
                        else tmp = shortMark;
                    }
                    case " " -> tmp = shortGap;
                    default -> System.out.println("\u001B[31m Only use literals '_' or '.' to define pattern\u001B[0m");
                }
                sb.append(tmp);
            }
            return sb.toString();
        }
    }
}