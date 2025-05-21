package strings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MorseCodeTest {

    @ParameterizedTest
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false,
            value = {"null,null", "'\n', ", "\r, ", "\t,\t"}, nullValues = "null")
    @CsvFileSource(resources = "/testdata_get_cleartxt_of_single_chars.csv")
    void getClearTextOfSingleChars(String key, String value) {
        if(key == null) {
            assertThrows(IllegalArgumentException.class, () -> new MorseCode(key));
            return;
        }
        MorseCode mc = new MorseCode(key);
        assertEquals(key.matches("[\r\n]") ? System.lineSeparator() : value, mc.getClearText());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata_get_cleartxt_of_strings.csv", nullValues = "null")
    void getClearTextOfStrings(String key, String value) {
        if(key == null) {
            assertThrows(IllegalArgumentException.class, () -> new MorseCode(key));
            return;
        }
        MorseCode mc = new MorseCode(key);
        assertEquals(value, mc.getClearText());
    }

    @ParameterizedTest
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false,  value = {"null,''", "'\n',''", "\r,''", "\t,''", "A,''"},
                                                            nullValues = "null")
    @CsvFileSource(resources = "/testdata_decode_sequence_of_single_letters.csv")
    void decodeMorseSequenceOfSingleLetters(String key, String value) {
        assertEquals(value, MorseCode.decode(key));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata_decode_sequence_of_strings.csv", nullValues = "null")
    void decodeMorseSequenceOfStrings(String key, String value) {
        assertEquals(value, MorseCode.decode(key));
    }

    @ParameterizedTest
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false,  value = {"null,''", "'\n',''", "\r,''", "\t,''", "A,''"},
            nullValues = "null")
    @CsvFileSource(resources = "/testdata_decode_signal_sequence_of_single_letters.csv")
    void decodeSignalSequenceOfSingleLetters(String key, String value) {
        assertEquals(value, MorseCode.decode(key));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata_decode_signal_sequence_of_strings.csv", nullValues = "null")
    void decodeSignalSequenceOfStrings(String key, String value) {
        assertEquals(value, MorseCode.decode(key));
    }

    @ParameterizedTest
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false, value = {"'\n', ", "\r, ", "\t,         "})
    @CsvFileSource(resources = "/testdata_get_sequence_of_single_chars.csv")
    void getMorseSequenceOfSingleChars(String key, String value) {
        Locale language = key.matches("CH")? Locale.GERMAN : Locale.ENGLISH;
        MorseCode mc = new MorseCode(key, language);
        assertEquals(key.matches("[\r\n]") ? System.lineSeparator() : value, mc.getNotation());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata_get_sequence_of_strings.csv", nullValues = "null")
    void getMorseSequenceOfStrings(String key, String value) {
        if(key == null) {
            assertThrows(IllegalArgumentException.class, () -> new MorseCode(key));
            return;
        }
        MorseCode mc = new MorseCode(key);
        assertEquals(value, mc.getNotation());
    }

    @ParameterizedTest
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false, value = {"'\n', ", "\r, ", "\t,˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽"})
    @CsvFileSource(resources = "/testdata_get_signal_sequence_of_single_chars.csv")
    void getSignalSequenceOfSingleChars(String key, String value) {
        Locale language = key.matches("CH")? Locale.GERMAN : Locale.ENGLISH;
        MorseCode mc = new MorseCode(key, language);
        assertEquals(key.matches("[\r\n]") ? System.lineSeparator() : value, mc.getNotation(true));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata_get_signal_sequence_of_strings.csv", nullValues = "null")
    void getSignalSequenceOfStrings(String key, String value) {
        if(key == null) {
            assertThrows(IllegalArgumentException.class, () -> new MorseCode(key));
            return;
        }
        MorseCode mc = new MorseCode(key);
        assertEquals(value, mc.getNotation(true));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata_get_stacked_notation_of_strings.csv")
    void getStackedNotation(String key, String value) {
        MorseCode mc = new MorseCode(key);
        assertEquals(value, mc.getStackedNotation());
    }

    @ParameterizedTest
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false, value = {"'\n','\n'", "'\r','\r'", "\t,\t"})
    @CsvFileSource(resources = "/testdata_get_spoken_notation_of_single_chars.csv")
    void getSpokenNotationOfSingleChars(String key, String value) {
        Locale language = key.matches("CH")? Locale.GERMAN : Locale.ENGLISH;
        MorseCode mc = new MorseCode(key, language);
        assertEquals(key.matches("[\r\n]") ? System.lineSeparator() : value, mc.getSpokenNotation());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata_get_spoken_notation_of_strings.csv")
    void getSpokenNotationOfStrings(String key, String value) {
        MorseCode mc = new MorseCode(key);
        assertEquals(value, mc.getSpokenNotation());
    }
}