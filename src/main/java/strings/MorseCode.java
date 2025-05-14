package strings;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.CharBuffer;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 */
public class MorseCode {

    private final static int SAMPLE_RATE = 16 * 1024;
    private final static String LINE_SEPARATOR = System.lineSeparator();
    private final static AudioFormat audioFormat = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);;

    private ArrayList<MorseNotation> literals = new ArrayList<>();

    public MorseCode(String message) {
        if (message == null) {
            throw new IllegalArgumentException("\u001B[31m'message' must not be null\u001B[0m");
        }

        message = normalizeLineSeparators(message);
        // TODO handle proper recognition of 'CH' and 'ß'
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

        // sanitize string for decoding
        morseCode = normalizeLineSeparators(morseCode).trim();
        if(morseCode.isEmpty()){
            return "";
        }

        // check if the message is an actual Morse code
        String firstChar = Character.toString(morseCode.charAt(0));
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
        String splitRegEx = STR." |\{LINE_SEPARATOR}";
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
        String splitRegEx = STR."\{MorseNotation.SIGNAL_WORD_GAP}|\{MorseNotation.SIGNAL_LETTER_GAP}|\{LINE_SEPARATOR}";
        String[] splits = morseCode.splitWithDelimiters(splitRegEx, -1);
        StringBuilder sb = new StringBuilder();
        for(String string : splits){
            if(string.equals(MorseNotation.SIGNAL_LETTER_GAP) || string.isEmpty()) continue;

            if(string.equals(MorseNotation.SIGNAL_WORD_GAP)){
                sb.append(" ");
                continue;
            }

            if(string.matches("[\r\n]")){
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

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < literals.size(); i++){
            MorseNotation current = literals.get(i);
            String sequence = getMorseString(current, false, asSignalSequence);

            if (i == literals.size() - 1) {
                sb.append(sequence);
                break;
            }

            boolean isWhiteSpace = MorseNotation.isWhiteSpace(current);
            boolean isLastCharInWord = MorseNotation.isWhiteSpace(literals.get(i + 1));
            sb.append(isLastCharInWord || isWhiteSpace ? sequence : STR."\{sequence}\{shortGap}");
        }
        return sb.toString();
    }

    /**
     * Returns the stored message in both Morse code/signal notation and the clear Latin character above each Morse
     * symbol.
     * @return String
     */
    public String getStackedNotation() { return getStackedNotation(false); }

    /**
     * Returns the stored message in both Morse code/signal notation and the clear Latin character above each Morse
     * symbol.
     * @param asSignalSequence      if true, returns the Morse code string in signal notation
     * @return  String
     */
    public String getStackedNotation(boolean asSignalSequence){
        final String shortGap = asSignalSequence ? MorseNotation.SIGNAL_LETTER_GAP : MorseNotation.SHORT_GAP;
        final String clearGap = asSignalSequence ? MorseNotation.LONG_GAP : shortGap;
        final char separator = '—';

        StringBuilder stacked = new StringBuilder();
        StringBuilder morse = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < literals.size(); i++){
            MorseNotation current = literals.get(i);
            String sequence = getMorseString(current, false, asSignalSequence);
            String character = getMorseString(current, true);
            character = current == MorseNotation.TAB || current == MorseNotation.WHITE_SPACE ? " " : character;

            int insertionIndex = (sequence.length() + character.length()) / 2;
            for(int j = 0; j < sequence.length(); j++){
                sb.append(j == insertionIndex - 1 ? character : Character.toString(separator));
            }

            if(i == literals.size() - 1){
                morse.append(sequence);
                stacked.append(sb);
                break;
            }

            switch(current){
                // TODO check potential Windows line separator issues
                case RETURN -> { continue; }
                case NEWLINE -> {
                    stacked.append(character).append(morse).append(LINE_SEPARATOR);
                    morse.delete(0, morse.length());
                    sb.delete(0, sb.length());
                    continue;
                }
                case TAB, WHITE_SPACE -> character = sb.toString().replaceAll("\\S", " ");
                default -> character = sb.toString();
            }

            boolean isWhiteSpace = MorseNotation.isWhiteSpace(current);
            boolean isLastCharInWord = MorseNotation.isWhiteSpace(literals.get(i + 1));
            morse.append(isLastCharInWord || isWhiteSpace ? sequence : STR."\{sequence}\{shortGap}");
            stacked.append(isLastCharInWord || isWhiteSpace ? character : STR."\{character}\{clearGap}");

            sb.delete(0, sb.length());
        }
        return stacked.append(LINE_SEPARATOR).append(morse).toString();
    }

    private String getMorseString(MorseNotation character, boolean getCharacter) {
        return getMorseString(character, getCharacter, false);
    }

    private String getMorseString(MorseNotation character, boolean getCharacter, boolean asSignalSequence){
        final String longGap = asSignalSequence ? MorseNotation.SIGNAL_WORD_GAP : MorseNotation.LONG_GAP;
        final String sequence = MorseNotation.generateSequence(character.sequence, asSignalSequence);
        final String str = getCharacter ? character.literal : sequence;
        return switch(character){
            case NEWLINE, RETURN -> character.literal;
            case WHITE_SPACE -> longGap;
            case TAB -> STR."\{longGap}\{longGap}\{longGap}";
            default -> str;
        };
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
        for(int i = 0; i < literals.size(); i++){
            MorseNotation mn = literals.get(i);
            if(MorseNotation.isWhiteSpace(mn)) {
                String gap = mn == MorseNotation.WHITE_SPACE ? MorseNotation.LONG_GAP : mn.sequence;
                sb.append(gap);
                isNewWord = true;
                continue;
            }
            char[] chars = mn.sequence.toCharArray();
            for(int j = 0; j < chars.length; j++){
                char current = chars[j];
                boolean isLastChar = j == chars.length - 1;
                if (current == '.') {
                    String diDit = isLastChar ? STR."\{shortBark.toLowerCase()}t" : shortBark.toLowerCase();
                    sb.append(isNewWord ? shortBark : diDit);
                }
                if (current == '_') sb.append(isNewWord ? longBark : longBark.toLowerCase());
                isNewWord = false;
                sb.append(isLastChar? "" : "-");
            }
            sb.append(i == literals.size() - 1? "" : "   ");
        }
        return sb.toString();
    }

    private ArrayList<byte[]> getAudioData(int wordsPerMinute , int beepFrequency){
        ArrayList<byte[]> audioData = new ArrayList<>();

        final int defaultDitLength = 1200;
        final int wpm = Math.max(1, Math.min(wordsPerMinute, 50));
        final int bFreq = Math.max(50, Math.min(beepFrequency, 20000));
        final int ditLength = defaultDitLength / wpm;
        final int maxBufferSize = Integer.MAX_VALUE / ditLength / SAMPLE_RATE;
        final double period = (double) SAMPLE_RATE / bFreq;

        String signalStr = this.getNotation(true);
        signalStr = signalStr.replaceAll(LINE_SEPARATOR, MorseNotation.SIGNAL_WORD_GAP);

//        System.out.println(STR."Expected signal length: \{(float) signalStr.length() * ditLength / 1000} seconds.");
//        System.out.println(STR."Buffer Size: \{maxBufferSize}");

        CharBuffer charBuffer = CharBuffer.allocate(maxBufferSize);
        char[] signalChars = signalStr.toCharArray();
        for (int i = 0; i < signalChars.length; i++) {
            // write buffer
            charBuffer.put(signalChars[i]);
            if (charBuffer.hasRemaining() && i != signalChars.length - 1) continue;

            // read buffer
            charBuffer.flip();
            char[] chars = new char[charBuffer.limit()];
            charBuffer.get(chars, 0, charBuffer.limit());

            int length = chars.length * ditLength;
            int samples = (length * SAMPLE_RATE) / 1000;

            // generate waveform using signal string
            byte[] buffer = new byte[samples];
            for (int j = 0; j < buffer.length; j++) {
                float charIndex = (float) j / samples * chars.length;
                char current = chars[(int) charIndex];
                double angle = 2.0 * Math.PI * j / period;
                int volume = current == MorseNotation.SIGNAL_MARK ? 1 : 0;
                buffer[j] = (byte) (Math.sin(angle) * 127f * volume);
            }
            audioData.add(buffer);
            charBuffer.clear();
        }
        return audioData;
    }

    /**
     * Generates a waveform of the Morse code signal and will try to play it back using the computers sound system.
     * This is a blocking method, so execution of the thread and any subsequent code will be halted until playback
     * is finished.
     * @param wordsPerMinute    determines signal playback speed. One word is equal to 5 signal letters. Clamped between
     *                          1 and 50.
     */
    public void playSignal(int wordsPerMinute) { playSignal(wordsPerMinute, 800); }

    /**
     * Generates a waveform of the Morse code signal and will try to play it back using the computers sound system.
     * This is a blocking method, so execution of the thread and any subsequent code will be halted until playback
     * is finished.
     * @param wordsPerMinute    determines signal playback speed. One word is equal to 5 signal letters. Clamped between
     *                          1 and 50.
     * @param beepFrequency     determines the frequency of the beeping sound in hertz. Lower values get a lower
     *                          bass like sound and higher values will generate a higher pitched beeping noise.
     *                          Clamped between 50 and 20000 Hz
     */
    public void playSignal(int wordsPerMinute , int beepFrequency){
        SourceDataLine line;
        try {
            // prepare to play audio
            line = AudioSystem.getSourceDataLine(audioFormat);
            line.open(audioFormat, SAMPLE_RATE);
            line.start();
        } catch (LineUnavailableException e) {
            System.out.println(e.getLocalizedMessage());
            return;
        }

        for(byte[] buffer : getAudioData(wordsPerMinute, beepFrequency)){
            line.write(buffer, 0, buffer.length);
        }
        // wait until the remaining bytes have been drained and close line
        line.drain();
        line.close();
    }

    public void saveSignal(Path filePath, AudioFileFormat.Type fileFormat, int wordsPerMinute,
                           int beepFrequency) throws IOException {
        File file = filePath.toFile();
        if (file.isDirectory()) {
            System.out.println(STR."\u001B[31mCan not write file:\{file.toString()}\u001B[0m");
            return;
        }

        ArrayList<byte[]> audioData = getAudioData(wordsPerMinute, beepFrequency);
        int arraySize = audioData.stream().map(bytes -> bytes.length).reduce(0, Integer::sum);

        int i = 0;
        byte[] buffer = new byte[arraySize];
        while(i < arraySize) {
            for (byte[] b : audioData) {
                for (byte value : b) {
                    buffer[i++] = value;
                }
            }
        }

        final ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        final AudioInputStream ais = new AudioInputStream(bais, audioFormat, buffer.length);

        AudioSystem.write(ais, fileFormat, file);

        bais.close();
        ais.close();
    }

    @Override
    public String toString(){ return literals.toString();  }

    private static String normalizeLineSeparators(String str){ return str.replaceAll("\\r\\n|\\r|\\n", LINE_SEPARATOR); }

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