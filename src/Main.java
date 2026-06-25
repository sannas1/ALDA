/**
 * ALDA Projekt - Studenten-Notenverwaltung
 *
 * Datenstruktur:
 *   DoublyLinkedList (doppelt verkettete Liste, Kapitel 03)
 *   - Jeder Knoten hat next- und prev-Zeiger
 *   - Einfuegen O(1), Entfernen O(n), Suche O(n)
 *
 * Algorithmen:
 *   1. InsertionSort  (Kapitel 04) - Nachname A-Z
 *      O(n2) worst-case, O(n) best-case bei fast-sortierter Liste
 *
 *   2. MergeSort      (Kapitel 04) - Matrikelnummer / Note
 *      O(n log n) alle Faelle, stabil, Divide and Conquer
 *
 *   3. QuickSort      (Kapitel 04) - Note
 *      O(n log n) average, O(n2) worst-case
 *
 * Starten:
 *   javac -d out -sourcepath src src/Main.java
 *   java -cp out Main
 */

import datastructure.StudentenVerwaltung;
import ui.ConsoleUI;

public class Main {

    public static void main(String[] args) {
        StudentenVerwaltung verwaltung = new StudentenVerwaltung();
        loadSampleData(verwaltung);
        ConsoleUI ui = new ConsoleUI(verwaltung);
        ui.start();
    }

    private static void loadSampleData(StudentenVerwaltung v) {
        v.addStudent("Anna",    "Mueller",    1.0, "Computer Science");
        v.addStudent("Thomas",  "Bauer",      2.0, "Computer Science");
        v.addStudent("Sarah",   "Wagner",     1.5, "Digital Communications");
        v.addStudent("Lukas",   "Fischer",    3.0, "Computer Science");
        v.addStudent("Julia",   "Schneider",  2.5, "Digital Communications");
        v.addStudent("Markus",  "Hoffmann",   4.0, "Computer Science");
        v.addStudent("Laura",   "Weber",      1.0, "Digital Communications");
        v.addStudent("David",   "Schmitt",    5.0, "Computer Science");
        v.addStudent("Sophie",  "Koch",       3.5, "Digital Communications");
        v.addStudent("Felix",   "Richter",    2.0, "Computer Science");
    }
}
