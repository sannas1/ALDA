package algorithms;

import model.Student;

/**
 * Insertion Sort - Sortieren durch Einfuegen (vgl. Vorlesung Kapitel 04).
 *
 * Idee: Das Array wird von links nach rechts durchlaufen. Das aktuelle Element
 * wird durch Verschieben an die richtige Stelle im sortierten linken Teil eingefuegt.
 * Invariante: Nach dem k-ten Durchlauf liegen die ersten k Elemente sortiert vor.
 *
 * Laufzeit:
 *   Best-Case:  O(n)  - bereits sortierte Folge (nur n Vergleiche noetig)
 *   Worst-Case: O(n2) - umgekehrt sortierte Folge
 *
 * Einsatz: Sortierung nach Nachname - effizient wenn Liste fast sortiert ist,
 * z.B. nach dem Hinzufuegen eines einzelnen Studenten.
 */
public class InsertionSort {

    public enum SortKey { NACHNAME, VORNAME }

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
