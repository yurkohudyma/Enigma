package Enigma;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;


public class Enigma {
    static final String ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ", ROTOR_I = "EKMFLGDQVZNTOWYHXUSPAIBRCJ",
            ROTOR_II = "AJDKSIRUXBLHWTMCQGZNPYFVOE", ROTOR_III = "BDFHJLCPRTXVZNYEIWGAKMUSQO",
            ROTOR_IV = "ESOVPZJAYQUIRHXLNFTGKDCMWB";
    static final String REFLECTOR = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
    static final Map<Character, Character> plugboardCableMap = new HashMap<>(
            Map.of('A', 'Z', 'Z', 'A'));
    public static final String ROTOR4_LIST = "rotor4List";
    public static final String ROTOR3_LIST = "rotor3List";
    public static final String ROTOR2_LIST = "rotor2List";
    public static final String ROTOR1_LIST = "rotor1List";

    static final Map<String, Character> notchMap = getRotorsNotchMap();

    static int indexInRotor;

    static Map<String, Deque<Character>> rotorListMap = getRotorListMap();

    static Map<String, Deque<Character>> reversedRotorListMap = getReversedRotorListMap();

    static List<Character> refList = getListFromString(REFLECTOR);

    static char charr;

    /**
     * 1) механічно машина сконструйована так, що в момент натискання клавіші літери провертається правий ротор<br>
     * - і замикається коло контактів уже провернутого ротора. У роторів є виїмка на певній позиції, яка провертає <br>
     * - одразу і наступний ротор<br>
     * 2) вхідна літера йде на перевірку у <b>plugboardCableMap</b>, якщо літера по key визначена, тоді <i>override</i><br>
     * 3) далі йде на кодування на ротор <b>rotorListMap</b>, знаходимо - міняємо. Заміна тут відбувається по індексу,<br>
     * - тобто ми визначаємо індекс поточної літери у АБЦ і знаходимо<br>
     * 4) йдемо на рефлектор, він статичний, але міняє літеру симетрично на вході і на виході<br>
     * 6) повертаємось назад через <b>reversedRotorListMap</b>. У мапах строго поступальний і зворотній порядок обробки, як і в реальних роторах<br>
     * 7) повторний захід на <b>plugboardCableMap</b>
     */
    public static void main(String[] args) {
        out.println(encryptPhrase("ZAAEI QSWZP"));
    }

    private static String encryptPhrase(String phrase) {
        var arr = phrase.toCharArray();
        var sb = new StringBuilder();
        for (Character character : arr) {
            if (character == ' ') {
                sb.append('\0');
            } else {
                charr = character;
                sb.append(encryptChar());
            }
        }
        return sb.toString();
    }

    private static char encryptChar() {
        advanceRotorList();
        checkPlugboard();
        proceedWithRotors();
        proceedWithReflector();
        reverseRotors();
        checkPlugboard();
        return charr;
    }

    private static void proceedWithReflector() {
        charr = refList.get(ABC.indexOf(charr));
    }

    private static void advanceRotorList() {
        //todo notches not engaged
        var rotor1List = rotorListMap.get(ROTOR1_LIST);
        var lastChar = rotor1List.removeLast();
        rotor1List.addFirst(lastChar);
        rotorListMap.put(ROTOR1_LIST, rotor1List);
        reversedRotorListMap.put(ROTOR1_LIST, rotor1List);
    }

    private static void checkPlugboard() {
        var newChar = plugboardCableMap.get(charr);
        charr = newChar == null ? charr : newChar;
    }

    private static List<Character> getListFromString(String reflector) {
        return reflector
                .chars()
                .mapToObj(c -> (char) c)
                .toList();
    }

    private static Deque<Character> getDequeFromString(String str) {
        return str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private static void proceedWithRotors() {
        rotorListMap
                .values()
                .forEach(value ->
                        charr = ((LinkedList<Character>) value).get(ABC.indexOf(charr)));
    }

    private static void reverseRotors() {
        reversedRotorListMap.values().forEach(value -> {
            indexInRotor = ((LinkedList<Character>) value).indexOf(charr);
            charr = ABC.charAt(indexInRotor);
        });
    }

    private static Map<String, Deque<Character>> getRotorListMap() {
        var map = new LinkedHashMap<String, Deque<Character>>();
        map.put(ROTOR1_LIST, getDequeFromString(ROTOR_I));
        map.put(ROTOR2_LIST, getDequeFromString(ROTOR_II));
        map.put(ROTOR3_LIST, getDequeFromString(ROTOR_III));
        map.put(ROTOR4_LIST, getDequeFromString(ROTOR_IV));
        return map;
    }

    private static Map<String, Deque<Character>> getReversedRotorListMap() {
        var map = new LinkedHashMap<String, Deque<Character>>();
        map.put(ROTOR4_LIST, getDequeFromString(ROTOR_IV));
        map.put(ROTOR3_LIST, getDequeFromString(ROTOR_III));
        map.put(ROTOR2_LIST, getDequeFromString(ROTOR_II));
        map.put(ROTOR1_LIST, getDequeFromString(ROTOR_I));
        return map;
    }

    private static Map<String, Character> getRotorsNotchMap() {
        return new HashMap<>(Map.of(
                ROTOR1_LIST, 'Q',
                ROTOR2_LIST, 'E',
                ROTOR3_LIST, 'V',
                ROTOR_IV, 'J')
        );
    }
}
