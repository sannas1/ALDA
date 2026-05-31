package datastructure;

import model.Song;
import algorithms.InsertionSort;
import algorithms.MergeSort;
import algorithms.QuickSort;

/**
 * Playlist-Klasse: verwaltet Songs in einer doppelt verketteten Liste
 * und stellt Sortier- und Navigationsfunktionen bereit.
 */
public class Playlist {

    private final DoublyLinkedList list;
    private DoublyLinkedList.Node currentNode; // aktuell abgespielter Song
    private int nextId;
    private final String name;

    public Playlist(String name) {
        this.name = name;
        this.list = new DoublyLinkedList();
        this.currentNode = null;
        this.nextId = 1;
    }

    // -------------------------------------------------------------------------
    // Song-Verwaltung
    // -------------------------------------------------------------------------

    public Song addSong(String title, String artist, int durationSeconds, int rating) {
        Song song = new Song(nextId++, title, artist, durationSeconds, rating);
        list.add(song);
        if (currentNode == null) currentNode = list.getHead();
        return song;
    }

    public boolean removeSong(int id) {
        if (currentNode != null && currentNode.data.getId() == id) {
            // Vor dem Löschen zum nächsten Song wechseln
            if (currentNode.next != null) currentNode = currentNode.next;
            else if (currentNode.prev != null) currentNode = currentNode.prev;
            else currentNode = null;
        }
        return list.remove(id);
    }

    public Song findById(int id) {
        return list.findById(id);
    }

    // -------------------------------------------------------------------------
    // Navigation (nutzt prev/next-Zeiger der doppelt verketteten Liste)
    // -------------------------------------------------------------------------

    public Song getCurrentSong() {
        return currentNode != null ? currentNode.data : null;
    }

    /** Wechselt zum nächsten Song (next-Zeiger). */
    public Song next() {
        if (currentNode == null) return null;
        if (currentNode.next != null) {
            currentNode = currentNode.next;
            currentNode.data.incrementPlayCount();
        }
        return currentNode.data;
    }

    /** Wechselt zum vorherigen Song (prev-Zeiger). */
    public Song previous() {
        if (currentNode == null) return null;
        if (currentNode.prev != null) {
            currentNode = currentNode.prev;
            currentNode.data.incrementPlayCount();
        }
        return currentNode.data;
    }

    /** Spielt den aktuellen Song ab (erhöht Play Count). */
    public Song play() {
        if (currentNode == null) return null;
        currentNode.data.incrementPlayCount();
        return currentNode.data;
    }

    /** Springt direkt zu einem Song per ID. */
    public Song jumpTo(int id) {
        DoublyLinkedList.Node node = list.getHead();
        while (node != null) {
            if (node.data.getId() == id) {
                currentNode = node;
                currentNode.data.incrementPlayCount();
                return currentNode.data;
            }
            node = node.next;
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Sortierung – jeder Algorithmus hat einen klaren Anwendungsfall
    // -------------------------------------------------------------------------

    /**
     * Insertion Sort nach Titel (A-Z).
     * Geeignet für kleine Playlists oder fast-sortierte Listen (Best-Case O(n)).
     */
    public void sortByTitleInsertionSort() {
        Song[] arr = list.toArray();
        InsertionSort.sort(arr, InsertionSort.SortKey.TITLE);
        list.fromArray(arr);
        currentNode = list.getHead();
    }

    /**
     * Insertion Sort nach Künstler (A-Z).
     */
    public void sortByArtistInsertionSort() {
        Song[] arr = list.toArray();
        InsertionSort.sort(arr, InsertionSort.SortKey.ARTIST);
        list.fromArray(arr);
        currentNode = list.getHead();
    }

    /**
     * Merge Sort nach Künstler – garantiert O(n log n) für alle Fälle.
     * Ideal für große Playlists, da stabile Laufzeit.
     */
    public void sortByArtistMergeSort() {
        Song[] arr = list.toArray();
        MergeSort.sort(arr, MergeSort.SortKey.ARTIST);
        list.fromArray(arr);
        currentNode = list.getHead();
    }

    /**
     * Merge Sort nach Dauer (kürzeste zuerst).
     */
    public void sortByDurationMergeSort() {
        Song[] arr = list.toArray();
        MergeSort.sort(arr, MergeSort.SortKey.DURATION);
        list.fromArray(arr);
        currentNode = list.getHead();
    }

    /**
     * Merge Sort nach Wiedergabeanzahl (meistgespielt zuerst).
     */
    public void sortByPlayCountMergeSort() {
        Song[] arr = list.toArray();
        MergeSort.sort(arr, MergeSort.SortKey.PLAY_COUNT);
        list.fromArray(arr);
        currentNode = list.getHead();
    }

    /**
     * Quick Sort nach Bewertung (höchste zuerst).
     * In der Praxis das schnellste Verfahren (vgl. Vorlesung).
     */
    public void sortByRatingQuickSort() {
        Song[] arr = list.toArray();
        QuickSort.sort(arr, QuickSort.SortKey.RATING);
        list.fromArray(arr);
        // Absteigend: höchste Bewertung zuerst – Array umkehren
        reverseList();
        currentNode = list.getHead();
    }

    private void reverseList() {
        Song[] arr = list.toArray();
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            Song tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
        }
        list.fromArray(arr);
    }

    // -------------------------------------------------------------------------
    // Hilfsmethoden
    // -------------------------------------------------------------------------

    public DoublyLinkedList getList() { return list; }
    public String getName() { return name; }
    public int size() { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }

    public int getTotalDurationSeconds() {
        int total = 0;
        DoublyLinkedList.Node node = list.getHead();
        while (node != null) {
            total += node.data.getDurationSeconds();
            node = node.next;
        }
        return total;
    }

    public String formatTotalDuration() {
        int total = getTotalDurationSeconds();
        int h = total / 3600;
        int m = (total % 3600) / 60;
        int s = total % 60;
        return h > 0 ? String.format("%d:%02d:%02d", h, m, s) : String.format("%d:%02d", m, s);
    }
}
