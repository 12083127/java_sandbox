package strings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

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
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false,  value = {"null,''", "'\n',''", "\r,''", "\t,''", "A,''"},
                                                            nullValues = "null")
    @CsvFileSource(resources = "/testdata_decode_sequence_of_single_letters.csv")
    void decodeMorseSequenceOfSingleLetters(String key, String value) {
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
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false, value = {"'\n', ", "\r, ", "\t,         "})
    @CsvFileSource(resources = "/testdata_get_sequence_of_single_chars.csv")
    void getMorseSequenceOfSingleChars(String key, String value) {
        MorseCode mc = new MorseCode(key);
        assertEquals(key.matches("[\r\n]") ? System.lineSeparator() : value, mc.getNotation());
    }

    @ParameterizedTest
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false, value = {"'\n', ", "\r, ", "\t,˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽"})
    @CsvFileSource(resources = "/testdata_get_signal_sequence_of_single_chars.csv")
    void getSignalSequenceOfSingleChars(String key, String value) {
        MorseCode mc = new MorseCode(key);
        assertEquals(key.matches("[\r\n]") ? System.lineSeparator() : value, mc.getNotation(true));
    }

    @Test
    void getStackedNotation() {
    }

    @ParameterizedTest
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false, value = {"'\n','\n'", "'\r','\r'", "\t,\t"})
    @CsvFileSource(resources = "/testdata_get_spoken_notation_of_single_chars.csv")
    void getSpokenNotationOfSingleChars(String key, String value) {
        MorseCode mc = new MorseCode(key);
        assertEquals(key.matches("[\r\n]") ? System.lineSeparator() : value, mc.getSpokenNotation());
    }
}