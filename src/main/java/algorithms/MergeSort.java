package algorithms;

import model.Student;

/**
 * Merge Sort – Divide & Conquer (vgl. Vorlesung Kapitel 04).
 *
 * MergeSort(A, l, r):
 *   falls l < r:
 *     m = (l+r)/2
 *     MergeSort(A, l, m)
 *     MergeSort(A, m+1, r)
 *     Merge(A, l, m, r)
 *
 * Laufzeit: O(n log n) für alle Fälle – optimal und stabil.
 *
 * Einsatz: Sortierung nach Matrikelnummer – garantiert stabile O(n log n) Laufzeit,
 * ideal wenn die Reihenfolge gleicher Noten erhalten bleiben soll.
 */
public class MergeSort {

    public enum SortKey { MATRIKELNUMMER, NOTE }

    public static void sort(Student[] arr, SortKey key) {
        if (arr == null || arr.length <= 1) return;
        mergeSort(arr, 0, arr.length - 1, key);
    }

    private static void mergeSort(Student[] arr, int l, int r, SortKey key) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m, key);
            mergeSort(arr, m + 1, r, key);
            merge(arr, l, m, r, key);
        }
    }

    /** Verschmilzt A[l..m] und A[m+1..r] zu einer sortierten Folge. Verwendet Hilfsarray B. */
    private static void merge(Student[] arr, int l, int m, int r, SortKey key) {
        Student[] B = new Student[r - l + 1];
        for (int i = 0; i < B.length; i++) B[i] = arr[l + i];

        int p = 0;
        int q = m - l + 1;
        int rightEnd = r - l;

        for (int i = l; i <= r; i++) {
            if (p > m - l) {
                arr[i] = B[q++];
            } else if (q > rightEnd) {
                arr[i] = B[p++];
            } else if (compare(B[p], B[q], key) <= 0) {
                arr[i] = B[p++];
            } else {
                arr[i] = B[q++];
            }
        }
    }

    private static int compare(Student a, Student b, SortKey key) {
        return switch (key) {
            case MATRIKELNUMMER -> Integer.compare(a.getMatrikelnummer(), b.getMatrikelnummer());
            case NOTE           -> Double.compare(a.getNote(), b.getNote());
        };
    }
}
