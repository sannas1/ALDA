package algorithms;

import model.Song;

/**
 * Insertion Sort – Sortieren durch Einfügen (vgl. Vorlesung Kapitel 04).
 *
 * Idee: Das Array wird von links nach rechts durchlaufen. Das aktuelle Element
 * wird durch Verschieben an die richtige Stelle im bereits sortierten linken Teil eingefügt.
 *
 * Laufzeit:
 *   Best-Case:  O(n)   – bereits sortierte Folge
 *   Worst-Case: O(n²)  – umgekehrt sortierte Folge
 *
 * Einsatz im Playlist Manager: Sortierung nach Titel für kleine Listen oder
 * wenn die Liste bereits fast sortiert ist (z.B. nach Hinzufügen eines Songs).
 */
public class InsertionSort {

    public enum SortKey { TITLE, ARTIST }

    /**
     * Sortiert das Song-Array aufsteigend nach dem gewählten Schlüssel.
     * In-place, kein zusätzlicher Speicher notwendig.
     */
    public static void sort(Song[] arr, SortKey key) {
        int n = arr.length;
        // k-ter Durchlauf: die ersten k Elemente sind sortiert
        for (int k = 1; k < n; k++) {
            Song current = arr[k];
            int j = k - 1;
            // Verschiebe Elemente, die größer als current sind, um eine Position nach rechts
            while (j >= 0 && compare(arr[j], current, key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = current;
        }
    }

    private static int compare(Song a, Song b, SortKey key) {
        return switch (key) {
            case TITLE  -> a.getTitle().compareToIgnoreCase(b.getTitle());
            case ARTIST -> a.getArtist().compareToIgnoreCase(b.getArtist());
        };
    }
}
