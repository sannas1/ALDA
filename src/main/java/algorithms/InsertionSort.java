package algorithms;

import model.Student;

/**
 * Insertion Sort – Sortieren durch Einfügen (vgl. Vorlesung Kapitel 04).
 *
 * Idee: Das Array wird von links nach rechts durchlaufen. Das aktuelle Element
 * wird durch Verschieben an die richtige Stelle im bereits sortierten linken Teil eingefügt.
 *
 * Invariante: Nach dem k-ten Durchlauf liegen die ersten k Elemente sortiert vor.
 *
 * Laufzeit:
 *   Best-Case:  O(n)   – bereits sortierte Folge (nur n Vergleiche)
 *   Worst-Case: O(n²)  – umgekehrt sortierte Folge
 *
 * Einsatz: Sortierung nach Name – effizient wenn Liste bereits fast sortiert ist
 * (z.B. nach Hinzufügen eines einzelnen Studenten).
 */
public class InsertionSort {

    public enum SortKey { NACHNAME, VORNAME }

    /** Sortiert das Student-Array aufsteigend nach dem gewählten Schlüssel. In-place. */
    public static void sort(Student[] arr, SortKey key) {
        int n = arr.length;
        for (int k = 1; k < n; k++) {
            Student current = arr[k];
            int j = k - 1;
            while (j >= 0 && compare(arr[j], current, key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = current;
        }
    }

    private static int compare(Student a, Student b, SortKey key) {
        return switch (key) {
            case NACHNAME -> a.getNachname().compareToIgnoreCase(b.getNachname());
            case VORNAME  -> a.getVorname().compareToIgnoreCase(b.getVorname());
        };
    }
}
