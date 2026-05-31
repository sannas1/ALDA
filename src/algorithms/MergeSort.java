package algorithms;

import model.Song;

/**
 * Merge Sort – Sortieren nach dem Prinzip "Teile und Herrsche" (vgl. Vorlesung Kapitel 04).
 *
 * Idee (vgl. Pseudocode aus der Vorlesung):
 *   MergeSort(A, l, r):
 *     falls l < r:
 *       m = (l+r)/2
 *       MergeSort(A, l, m)
 *       MergeSort(A, m+1, r)
 *       Merge(A, l, m, r)
 *
 * Die Merge-Funktion verschmilzt zwei bereits sortierte Teilfolgen zu einer sortierten Gesamtfolge.
 *
 * Laufzeit: O(n log n) für alle Fälle – optimal für das Sortierproblem.
 *
 * Einsatz im Playlist Manager: Sortierung großer Playlists nach Künstler,
 * da Merge Sort stabile O(n log n) Laufzeit für alle Eingaben garantiert.
 */
public class MergeSort {

    public enum SortKey { ARTIST, DURATION, PLAY_COUNT }

    /** Öffentlicher Einstiegspunkt. Sortiert das gesamte Array. */
    public static void sort(Song[] arr, SortKey key) {
        if (arr == null || arr.length <= 1) return;
        mergeSort(arr, 0, arr.length - 1, key);
    }

    /** Rekursive Merge Sort Funktion (vgl. MergeSort(A,l,r) aus der Vorlesung). */
    private static void mergeSort(Song[] arr, int l, int r, SortKey key) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m, key);
            mergeSort(arr, m + 1, r, key);
            merge(arr, l, m, r, key);
        }
    }

    /**
     * Verschmilzt A[l..m] und A[m+1..r] zu einer sortierten Folge in A[l..r].
     * Verwendet ein Hilfsarray (vgl. Vorlesung: B[l..r] = Kopie aus A).
     */
    private static void merge(Song[] arr, int l, int m, int r, SortKey key) {
        // Kopiere beide Teilarrays ins Hilfsarray B
        Song[] B = new Song[r - l + 1];
        for (int i = 0; i < B.length; i++) B[i] = arr[l + i];

        int p = 0;           // Zeiger auf linke Hälfte in B
        int q = m - l + 1;  // Zeiger auf rechte Hälfte in B
        int rightEnd = r - l;

        for (int i = l; i <= r; i++) {
            if (p > m - l) {
                // Linke Hälfte erschöpft
                arr[i] = B[q++];
            } else if (q > rightEnd) {
                // Rechte Hälfte erschöpft
                arr[i] = B[p++];
            } else if (compare(B[p], B[q], key) <= 0) {
                arr[i] = B[p++];
            } else {
                arr[i] = B[q++];
            }
        }
    }

    private static int compare(Song a, Song b, SortKey key) {
        return switch (key) {
            case ARTIST     -> a.getArtist().compareToIgnoreCase(b.getArtist());
            case DURATION   -> Integer.compare(a.getDurationSeconds(), b.getDurationSeconds());
            case PLAY_COUNT -> Integer.compare(b.getPlayCount(), a.getPlayCount()); // absteigend
        };
    }
}
