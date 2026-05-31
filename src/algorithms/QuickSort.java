package algorithms;

import model.Song;

/**
 * Quick Sort – schneller Sortieralgorithmus nach C.A.R. Hoare (vgl. Vorlesung Kapitel 04).
 *
 * Idee (vgl. Pseudocode aus der Vorlesung):
 *   QuickSort(A, l, r):
 *     falls l < r:
 *       x = A[r].key  (Pivotelement = letztes Element)
 *       p = Partition(A, l, r, x)
 *       QuickSort(A, l, p-1)
 *       QuickSort(A, p+1, r)
 *
 *   Partition(A, l, r, x):
 *     i = l-1; j = r
 *     wiederhole:
 *       i++ bis A[i].key >= x
 *       j-- bis A[j].key <= x
 *       falls i < j: vertausche A[i] und A[j]
 *     bis i >= j
 *     vertausche A[i] mit A[r]
 *     return i
 *
 * Laufzeit:
 *   Average-Case: O(n log n) – Rekursionstiefe log(n), n Elemente pro Ebene
 *   Worst-Case:   O(n²)      – bei bereits sortierter Eingabe und letztem Element als Pivot
 *
 * Einsatz im Playlist Manager: Sortierung nach Bewertung (Rating),
 * da Quick Sort in der Praxis das schnellste Verfahren ist (konstanter Faktor
 * kleiner als bei Merge Sort und Heap Sort, vgl. Vorlesung).
 */
public class QuickSort {

    public enum SortKey { RATING, ID }

    /** Öffentlicher Einstiegspunkt. Sortiert das gesamte Array. */
    public static void sort(Song[] arr, SortKey key) {
        if (arr == null || arr.length <= 1) return;
        quickSort(arr, 0, arr.length - 1, key);
    }

    /** Rekursive Quick Sort Funktion (vgl. QuickSort(A,l,r) aus der Vorlesung). */
    private static void quickSort(Song[] arr, int l, int r, SortKey key) {
        if (l < r) {
            int p = partition(arr, l, r, key);
            quickSort(arr, l, p - 1, key);
            quickSort(arr, p + 1, r, key);
        }
    }

    /**
     * Partition-Funktion: Bestimmt die endgültige Position des Pivotelements
     * und bringt alle kleineren Elemente nach links, alle größeren nach rechts.
     * Das letzte Element dient als Pivot (vgl. Vorlesung: x = A[r].key).
     */
    private static int partition(Song[] arr, int l, int r, SortKey key) {
        int pivotKey = getValue(arr[r], key); // Pivot = letztes Element
        int i = l - 1;
        int j = r;

        do {
            // i vorwärtsbewegen bis A[i] >= pivot
            do { i++; } while (getValue(arr[i], key) < pivotKey);
            // j rückwärtsbewegen bis A[j] <= pivot
            do { j--; } while (j >= l && getValue(arr[j], key) > pivotKey);
            if (i < j) {
                swap(arr, i, j);
            }
        } while (i < j);

        // Pivot an seine endgültige Position setzen
        swap(arr, i, r);
        return i;
    }

    private static void swap(Song[] arr, int i, int j) {
        Song tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static int getValue(Song song, SortKey key) {
        return switch (key) {
            case RATING -> song.getRating();
            case ID     -> song.getId();
        };
    }
}
