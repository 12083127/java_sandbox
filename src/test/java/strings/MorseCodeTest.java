package strings;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MorseCodeTest {

    Map<String, String> clearChars = new HashMap<String, String>();
    Map<String, String> sequences = new HashMap<String, String>();
    Map<String, String> signalSeq = new HashMap<String, String>();

    public MorseCodeTest(){
        clearChars.put(" "," "); clearChars.put("!","!"); clearChars.put("\"","\""); clearChars.put("#","");
        clearChars.put("$",""); clearChars.put("%",""); clearChars.put("&",""); clearChars.put("'","'");
        clearChars.put("(","("); clearChars.put(")",")"); clearChars.put("*",""); clearChars.put("+","+");
        clearChars.put(",",","); clearChars.put("-","-"); clearChars.put(".","."); clearChars.put("/","/");
        clearChars.put("0","0"); clearChars.put("1","1"); clearChars.put("2","2"); clearChars.put("3","3");
        clearChars.put("4","4"); clearChars.put("5","5"); clearChars.put("6","6"); clearChars.put("7","7");
        clearChars.put("8","8"); clearChars.put("9","9"); clearChars.put(":",":"); clearChars.put(";",";");
        clearChars.put("<",""); clearChars.put("=","="); clearChars.put(">",""); clearChars.put("?","?");
        clearChars.put("@","@"); clearChars.put("A","A"); clearChars.put("B","B"); clearChars.put("C","C");
        clearChars.put("D","D"); clearChars.put("E","E"); clearChars.put("F","F"); clearChars.put("G","G");
        clearChars.put("H","H"); clearChars.put("I","I"); clearChars.put("J","J"); clearChars.put("K","K");
        clearChars.put("L","L"); clearChars.put("M","M"); clearChars.put("N","N"); clearChars.put("O","O");
        clearChars.put("P","P"); clearChars.put("Q","Q"); clearChars.put("R","R"); clearChars.put("S","S");
        clearChars.put("T","T"); clearChars.put("U","U"); clearChars.put("V","V"); clearChars.put("W","W");
        clearChars.put("X","X"); clearChars.put("Y","Y"); clearChars.put("Z","Z"); clearChars.put("[","");
        clearChars.put("\\",""); clearChars.put("]",""); clearChars.put("^",""); clearChars.put("_","_");
        clearChars.put("`",""); clearChars.put("a","A"); clearChars.put("b","B"); clearChars.put("c","C");
        clearChars.put("d","D"); clearChars.put("e","E"); clearChars.put("f","F"); clearChars.put("g","G");
        clearChars.put("h","H"); clearChars.put("i","I"); clearChars.put("j","J"); clearChars.put("k","K");
        clearChars.put("l","L"); clearChars.put("m","M"); clearChars.put("n","N"); clearChars.put("o","O");
        clearChars.put("p","P"); clearChars.put("q","Q"); clearChars.put("r","R"); clearChars.put("s","S");
        clearChars.put("t","T"); clearChars.put("u","U"); clearChars.put("v","V"); clearChars.put("w","W");
        clearChars.put("x","X"); clearChars.put("y","Y"); clearChars.put("z","Z"); clearChars.put("{","");
        clearChars.put("|",""); clearChars.put("}",""); clearChars.put("~",""); clearChars.put("\r", System.lineSeparator());
        clearChars.put("\n", System.lineSeparator()); clearChars.put("\t", "\t");

        sequences.put(" ","   "); sequences.put("!","–·–·––"); sequences.put("\"","·–··–·"); sequences.put("#","");
        sequences.put("$",""); sequences.put("%",""); sequences.put("&",""); sequences.put("'","·––––·");
        sequences.put("(","–·––·"); sequences.put(")","–·––·–"); sequences.put("*",""); sequences.put("+","·–·–·");
        sequences.put(",","––··––"); sequences.put("-","–····–"); sequences.put(".","·–·–·–"); sequences.put("/","–··–·");
        sequences.put("0","–––––"); sequences.put("1","·––––"); sequences.put("2","··–––"); sequences.put("3","···––");
        sequences.put("4","····–"); sequences.put("5","·····"); sequences.put("6","–····"); sequences.put("7","––···");
        sequences.put("8","–––··"); sequences.put("9","––––·"); sequences.put(":","–––···"); sequences.put(";","–·–·–·");
        sequences.put("<",""); sequences.put("=","–···–"); sequences.put(">",""); sequences.put("?","··––··");
        sequences.put("@","·––·–·"); sequences.put("A","·–"); sequences.put("B","–···"); sequences.put("C","–·–·");
        sequences.put("D","–··"); sequences.put("E","·"); sequences.put("F","··–·"); sequences.put("G","––·");
        sequences.put("H","····"); sequences.put("I","··"); sequences.put("J","·–––"); sequences.put("K","–·–");
        sequences.put("L","·–··"); sequences.put("M","––"); sequences.put("N","–·"); sequences.put("O","–––");
        sequences.put("P","·––·"); sequences.put("Q","––·–"); sequences.put("R","·–·"); sequences.put("S","···");
        sequences.put("T","–"); sequences.put("U","··–"); sequences.put("V","···–"); sequences.put("W","·––");
        sequences.put("X","–··–"); sequences.put("Y","–·––"); sequences.put("Z","––··"); sequences.put("[","");
        sequences.put("\\",""); sequences.put("]",""); sequences.put("^",""); sequences.put("_","··––·–");
        sequences.put("`",""); sequences.put("a","·–"); sequences.put("b","–···"); sequences.put("c","–·–·");
        sequences.put("d","–··"); sequences.put("e","·"); sequences.put("f","··–·"); sequences.put("g","––·");
        sequences.put("h","····"); sequences.put("i","··"); sequences.put("j","·–––"); sequences.put("k","–·–");
        sequences.put("l","·–··"); sequences.put("m","––"); sequences.put("n","–·"); sequences.put("o","–––");
        sequences.put("p","·––·"); sequences.put("q","––·–"); sequences.put("r","·–·"); sequences.put("s","···");
        sequences.put("t","–"); sequences.put("u","··–"); sequences.put("v","···–"); sequences.put("w","·––");
        sequences.put("x","–··–"); sequences.put("y","–·––"); sequences.put("z","––··"); sequences.put("{","");
        sequences.put("|",""); sequences.put("}",""); sequences.put("~",""); sequences.put("\r", System.lineSeparator());
        sequences.put("\n", System.lineSeparator()); sequences.put("\t", "         ");

        signalSeq.put(" ","˽˽˽˽˽˽˽"); signalSeq.put("!","▓▓▓˽▓˽▓▓▓˽▓˽▓▓▓˽▓▓▓"); signalSeq.put("\"","▓˽▓▓▓˽▓˽▓˽▓▓▓˽▓");
        signalSeq.put("#",""); signalSeq.put("$",""); signalSeq.put("%",""); signalSeq.put("&","");
        signalSeq.put("'","▓˽▓▓▓˽▓▓▓˽▓▓▓˽▓▓▓˽▓"); signalSeq.put("(","▓▓▓˽▓˽▓▓▓˽▓▓▓˽▓");
        signalSeq.put(")","▓▓▓˽▓˽▓▓▓˽▓▓▓˽▓˽▓▓▓"); signalSeq.put("*",""); signalSeq.put("+","▓˽▓▓▓˽▓˽▓▓▓˽▓");
        signalSeq.put(",","▓▓▓˽▓▓▓˽▓˽▓˽▓▓▓˽▓▓▓"); signalSeq.put("-","▓▓▓˽▓˽▓˽▓˽▓˽▓▓▓");
        signalSeq.put(".","▓˽▓▓▓˽▓˽▓▓▓˽▓˽▓▓▓"); signalSeq.put("/","▓▓▓˽▓˽▓˽▓▓▓˽▓");
        signalSeq.put("0","▓▓▓˽▓▓▓˽▓▓▓˽▓▓▓˽▓▓▓"); signalSeq.put("1","▓˽▓▓▓˽▓▓▓˽▓▓▓˽▓▓▓");
        signalSeq.put("2","▓˽▓˽▓▓▓˽▓▓▓˽▓▓▓"); signalSeq.put("3","▓˽▓˽▓˽▓▓▓˽▓▓▓");
        signalSeq.put("4","▓˽▓˽▓˽▓˽▓▓▓"); signalSeq.put("5","▓˽▓˽▓˽▓˽▓"); signalSeq.put("6","▓▓▓˽▓˽▓˽▓˽▓");
        signalSeq.put("7","▓▓▓˽▓▓▓˽▓˽▓˽▓"); signalSeq.put("8","▓▓▓˽▓▓▓˽▓▓▓˽▓˽▓"); signalSeq.put("9","▓▓▓˽▓▓▓˽▓▓▓˽▓▓▓˽▓");
        signalSeq.put(":","▓▓▓˽▓▓▓˽▓▓▓˽▓˽▓˽▓"); signalSeq.put(";","▓▓▓˽▓˽▓▓▓˽▓˽▓▓▓˽▓");
        signalSeq.put("<",""); signalSeq.put("=","▓▓▓˽▓˽▓˽▓˽▓▓▓"); signalSeq.put(">","");
        signalSeq.put("?","▓˽▓˽▓▓▓˽▓▓▓˽▓˽▓"); signalSeq.put("@","▓˽▓▓▓˽▓▓▓˽▓˽▓▓▓˽▓"); signalSeq.put("A","▓˽▓▓▓");
        signalSeq.put("B","▓▓▓˽▓˽▓˽▓"); signalSeq.put("C","▓▓▓˽▓˽▓▓▓˽▓"); signalSeq.put("D","▓▓▓˽▓˽▓");
        signalSeq.put("E","▓"); signalSeq.put("F","▓˽▓˽▓▓▓˽▓"); signalSeq.put("G","▓▓▓˽▓▓▓˽▓");
        signalSeq.put("H","▓˽▓˽▓˽▓"); signalSeq.put("I","▓˽▓"); signalSeq.put("J","▓˽▓▓▓˽▓▓▓˽▓▓▓");
        signalSeq.put("K","▓▓▓˽▓˽▓▓▓"); signalSeq.put("L","▓˽▓▓▓˽▓˽▓"); signalSeq.put("M","▓▓▓˽▓▓▓");
        signalSeq.put("N","▓▓▓˽▓"); signalSeq.put("O","▓▓▓˽▓▓▓˽▓▓▓"); signalSeq.put("P","▓˽▓▓▓˽▓▓▓˽▓");
        signalSeq.put("Q","▓▓▓˽▓▓▓˽▓˽▓▓▓"); signalSeq.put("R","▓˽▓▓▓˽▓"); signalSeq.put("S","▓˽▓˽▓");
        signalSeq.put("T","▓▓▓"); signalSeq.put("U","▓˽▓˽▓▓▓"); signalSeq.put("V","▓˽▓˽▓˽▓▓▓");
        signalSeq.put("W","▓˽▓▓▓˽▓▓▓"); signalSeq.put("X","▓▓▓˽▓˽▓˽▓▓▓"); signalSeq.put("Y","▓▓▓˽▓˽▓▓▓˽▓▓▓");
        signalSeq.put("Z","▓▓▓˽▓▓▓˽▓˽▓"); signalSeq.put("[",""); signalSeq.put("\\",""); signalSeq.put("]","");
        signalSeq.put("^",""); signalSeq.put("_","▓˽▓˽▓▓▓˽▓▓▓˽▓˽▓▓▓"); signalSeq.put("`",""); signalSeq.put("a","▓˽▓▓▓");
        signalSeq.put("b","▓▓▓˽▓˽▓˽▓"); signalSeq.put("c","▓▓▓˽▓˽▓▓▓˽▓"); signalSeq.put("d","▓▓▓˽▓˽▓");
        signalSeq.put("e","▓"); signalSeq.put("f","▓˽▓˽▓▓▓˽▓"); signalSeq.put("g","▓▓▓˽▓▓▓˽▓"); signalSeq.put("h","▓˽▓˽▓˽▓");
        signalSeq.put("i","▓˽▓"); signalSeq.put("j","▓˽▓▓▓˽▓▓▓˽▓▓▓"); signalSeq.put("k","▓▓▓˽▓˽▓▓▓");
        signalSeq.put("l","▓˽▓▓▓˽▓˽▓"); signalSeq.put("m","▓▓▓˽▓▓▓"); signalSeq.put("n","▓▓▓˽▓");
        signalSeq.put("o","▓▓▓˽▓▓▓˽▓▓▓"); signalSeq.put("p","▓˽▓▓▓˽▓▓▓˽▓"); signalSeq.put("q","▓▓▓˽▓▓▓˽▓˽▓▓▓");
        signalSeq.put("r","▓˽▓▓▓˽▓"); signalSeq.put("s","▓˽▓˽▓"); signalSeq.put("t","▓▓▓"); signalSeq.put("u","▓˽▓˽▓▓▓");
        signalSeq.put("v","▓˽▓˽▓˽▓▓▓"); signalSeq.put("w","▓˽▓▓▓˽▓▓▓"); signalSeq.put("x","▓▓▓˽▓˽▓˽▓▓▓");
        signalSeq.put("y","▓▓▓˽▓˽▓▓▓˽▓▓▓"); signalSeq.put("z","▓▓▓˽▓▓▓˽▓˽▓"); signalSeq.put("{","");
        signalSeq.put("|",""); signalSeq.put("}",""); signalSeq.put("~",""); signalSeq.put("\r", System.lineSeparator());
        signalSeq.put("\n", System.lineSeparator()); signalSeq.put("\t", "˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽˽");

    }

    @org.junit.jupiter.api.Test
    void getClearTextOfSingleChars() {
        for(Map.Entry<String, String> e : clearChars.entrySet()){
            MorseCode mc = new MorseCode(e.getKey());
            assertEquals(e.getValue(), mc.getClearText());
            System.out.print(STR."\{e.getKey()}, ");
        }
    }

    @org.junit.jupiter.api.Test
    void decodeMorseSequenceOfSingleChars() {
        for(Map.Entry<String, String> e : sequences.entrySet()){
            assertEquals(e.getKey(), MorseCode.decode(e.getValue()));
            System.out.print(STR."\{e.getKey()}, ");
        }
    }

    @org.junit.jupiter.api.Test
    void getMorseSequenceOfSingleChars() {
        for(Map.Entry<String, String> e : sequences.entrySet()){
            MorseCode mc = new MorseCode(e.getKey());
            assertEquals(e.getValue(), mc.getNotation());
            System.out.print(STR."\{e.getKey()}, ");
        }
    }

    @org.junit.jupiter.api.Test
    void getSignalSequenceOfSingleChars() {
        for(Map.Entry<String, String> e : signalSeq.entrySet()){
            MorseCode mc = new MorseCode(e.getKey());
            assertEquals(e.getValue(), mc.getNotation(true));
            System.out.print(STR."\{e.getKey()}, ");
        }
    }

    @org.junit.jupiter.api.Test
    void getStackedNotation() {
    }

    @org.junit.jupiter.api.Test
    void getSpokenNotation() {
    }
}