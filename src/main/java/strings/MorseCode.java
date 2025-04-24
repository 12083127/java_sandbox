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

    public String getClearText() {
        StringBuilder sb = new StringBuilder();
        for (MorseNotation mn : literals) {
            sb.append(mn.literal);
        }
        return sb.toString();
    }

    public static String decode(String morseCode){
        return "";
    }

    public String getNotation(){ return getNotation(false); }
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