package datastructure;

import algorithms.InsertionSort;
import algorithms.MergeSort;
import algorithms.QuickSort;
import model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Verwaltungsklasse für Studenten.
 * Speichert alle Studenten in einer doppelt verketteten Liste
 * und stellt Sortier- und Suchfunktionen bereit.
 */
public class StudentenVerwaltung {

    private final DoublyLinkedList list;
    private int nextMatrikelnummer;

    public StudentenVerwaltung() {
        this.list = new DoublyLinkedList();
        this.nextMatrikelnummer = 12300001;
    }

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    public Student addStudent(String vorname, String nachname, double note, String studiengang) {
        Student s = new Student(nextMatrikelnummer++, vorname, nachname, note, studiengang);
        list.add(s);
        return s;
    }

    public boolean removeStudent(int matrikelnummer) {
        return list.remove(matrikelnummer);
    }

    public Student findByMatrikelnummer(int matrikelnummer) {
        return list.findByMatrikelnummer(matrikelnummer);
    }

    /** Lineare Suche nach Name (Teilstring, case-insensitiv). */
    public List<Student> searchByName(String term) {
        List<Student> results = new ArrayList<>();
        String lower = term.toLowerCase();
        DoublyLinkedList.Node node = list.getHead();
        while (node != null) {
            Student s = node.data;
            if (s.getVorname().toLowerCase().contains(lower) ||
                s.getNachname().toLowerCase().contains(lower)) {
                results.add(s);
            }
            node = node.next;
        }
        return results;
    }

    // -------------------------------------------------------------------------
    // Sortierung – jeder Algorithmus mit klarem Anwendungsfall
    // -------------------------------------------------------------------------

    /**
     * Insertion Sort nach Nachname A-Z.
     * O(n) best-case – ideal wenn nach dem Hinzufügen eines Studenten neu sortiert wird.
     */
    public void sortByNachnameInsertionSort() {
        Student[] arr = list.toArray();
        InsertionSort.sort(arr, InsertionSort.SortKey.NACHNAME);
        list.fromArray(arr);
    }

    /**
     * Merge Sort nach Matrikelnummer aufsteigend.
     * Garantiert O(n log n), stabil – für große Datensätze zuverlässig.
     */
    public void sortByMatrikelnummerMergeSort() {
        Student[] arr = list.toArray();
        MergeSort.sort(arr, MergeSort.SortKey.MATRIKELNUMMER);
        list.fromArray(arr);
    }

    /**
     * Merge Sort nach Note aufsteigend (beste Note zuerst).
     */
    public void sortByNoteMergeSort() {
        Student[] arr = list.toArray();
        MergeSort.sort(arr, MergeSort.SortKey.NOTE);
        list.fromArray(arr);
    }

    /**
     * Quick Sort nach Note aufsteigend.
     * O(n log n) average – in der Praxis schnellstes Verfahren.
     */
    public void sortByNoteQuickSort() {
        Student[] arr = list.toArray();
        QuickSort.sort(arr, QuickSort.SortKey.NOTE_ASC);
        list.fromArray(arr);
    }

    // -------------------------------------------------------------------------
    // Statistiken
    // -------------------------------------------------------------------------

    public double getDurchschnittsnote() {
        if (list.isEmpty()) return 0;
        double sum = 0;
        DoublyLinkedList.Node node = list.getHead();
        while (node != null) {
            sum += node.data.getNote();
            node = node.next;
        }
        return sum / list.size();
    }

    public int getAnzahlPositiv() {
        int count = 0;
        DoublyLinkedList.Node node = list.getHead();
        while (node != null) {
            if (node.data.getNote() <= 4.0) count++;
            node = node.next;
        }
        return count;
    }

    public int getAnzahlNegativ() {
        return list.size() - getAnzahlPositiv();
    }

    public List<Student> getAllStudents() {
        List<Student> result = new ArrayList<>();
        DoublyLinkedList.Node node = list.getHead();
        while (node != null) {
            result.add(node.data);
            node = node.next;
        }
        return result;
    }

    public int size() { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }
    public DoublyLinkedList getList() { return list; }
}
