package Enigma;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;


public class Enigma {
    static final String ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String ROTOR = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
    static final String REFLECTOR = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
    static final Map<Character, Character> plugboardCableMap = new HashMap<>(
            Map.of('A', 'Z','Z', 'A'));
    static Deque<Character> rotorList = getDequeFromString(ROTOR);
    static List<Character> refList = getListFromString(REFLECTOR);
    static char charr;

    /** 1) механічно машина сконструйована так, що в момент натискання клавіші літери провертається правий ротор<br>
     *    - і замикається коло контактів уже провернутого ротора. У роторів є виїмка на певній позиції, яка провертає <br>
     *    - одразу і наступний ротор
     * 2) вхідна літера йде на перевірку у <b>plugboardCableMap</b>, якщо літера по key визначена, тоді <i>override</i><br>
     * 3) далі йде на кодування на ротор, знаходимо - міняємо. Заміна тут відбувається по індексу, тобто ми визначаємо індекс поточної літери у АБЦ і знаходимо<br>
     * 4) в цій програмі передбачено 1 ротор<br>
     * 5) йдемо на рефлектор, він статичний, але міняє літеру симетрично на вході і на виході<br>
     * 6) повертаємось назад через ротор<br>
     * 7) повторний захід на <b>plugboardCableMap</b> */
    public static void main(String[] args) {
        out.println(encryptPhrase("VRIHT"));
    }

    private static String encryptPhrase(String phrase) {
        var arr = phrase.toCharArray();
        var sb = new StringBuilder();
        for (Character character: arr){
            charr = character;
            sb.append(encrypt());
        }
        return sb.toString();
    }

    private static char encrypt() {
        advanceRotorList();
        checkPlugboard();
        proceedWithRotorList();
        proceedWithReflector();
        reverseThroughRotorList();
        checkPlugboard();
        return charr;
    }

    private static void proceedWithReflector() {
        charr = refList.get(ABC.indexOf(charr));
    }

    private static void advanceRotorList() {
        var lastChar = rotorList.removeLast();
        rotorList.addFirst(lastChar);
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

    private static void proceedWithRotorList() {
        charr = ((LinkedList<Character>) rotorList).get(ABC.indexOf(charr));
    }

    private static void  reverseThroughRotorList() {
        int indexInRotor = ((LinkedList<Character>) rotorList).indexOf(charr);
        charr = ABC.charAt(indexInRotor);
    }
}
