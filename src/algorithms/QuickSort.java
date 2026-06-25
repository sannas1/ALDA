package algorithms;

import model.Student;

/**
 * Quick Sort - Divide and Conquer nach C.A.R. Hoare (vgl. Vorlesung Kapitel 04).
 *
 * QuickSort(A, l, r):
 *   falls l < r:
 *     x = A[r].key          (Pivot = letztes Element)
 *     p = Partition(A, l, r, x)
 *     QuickSort(A, l, p-1)
 *     QuickSort(A, p+1, r)
 *
 * Partition bringt alle Elemente kleiner als Pivot nach links,
 * alle groesseren nach rechts. Pivot sitzt danach an seiner Endposition.
 *
 * Laufzeit:
 *   Average-Case: O(n log n) - Rekursionstiefe log(n), n Elemente pro Ebene
 *   Worst-Case:   O(n2)      - bei bereits sortierter Eingabe
 *
 * Einsatz: Sortierung nach Note - in der Praxis schnellstes Verfahren
 * (konstanter Faktor kleiner als Merge Sort, vgl. Vorlesung).
 */
public class QuickSort {

    public enum SortKey { NOTE, MATRIKELNUMMER }

    public static void sort(Student[] arr, SortKey key) {
        if (arr == null || arr.length <= 1) return;
        quickSort(arr, 0, arr.length - 1, key);
    }

    private static void quickSort(Student[] arr, int l, int r, SortKey key) {
        if (l < r) {
            int p = partition(arr, l, r, key);
            quickSort(arr, l, p - 1, key);
            quickSort(arr, p + 1, r, key);
        }
    }

    private static int partition(Student[] arr, int l, int r, SortKey key) {
        double pivot = getValue(arr[r], key);
        int i = l - 1;
        int j = r;

        do {
            do { i++; } while (getValue(arr[i], key) < pivot && i < r);
            do { j--; } while (getValue(arr[j], key) > pivot && j > l);
            if (i < j) swap(arr, i, j);
        } while (i < j);

        swap(arr, i, r);
        return i;
    }

    private static void swap(Student[] arr, int i, int j) {
        Student tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static double getValue(Student s, SortKey key) {
        return switch (key) {
            case NOTE           -> s.getNote();
            case MATRIKELNUMMER -> s.getMatrikelnummer();
        };
    }
}
