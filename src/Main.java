import datastructure.Playlist;
import ui.ConsoleUI;

/**
 * Einstiegspunkt der Anwendung.
 *
 * Datenstruktur: Doppelt verkettete Liste (DoublyLinkedList)
 * Algorithmen:
 *   - Insertion Sort  (O(n²) worst, O(n) best) – Sortierung nach Titel/Künstler
 *   - Merge Sort      (O(n log n), stabil)     – Sortierung nach Künstler/Dauer/Wiedergaben
 *   - Quick Sort      (O(n log n) avg)         – Sortierung nach Bewertung
 */
public class Main {

    public static void main(String[] args) {
        Playlist playlist = new Playlist("Meine Playlist");
        loadSampleData(playlist);
        ConsoleUI ui = new ConsoleUI(playlist);
        ui.start();
    }

    /** Lädt Beispieldaten damit die App sofort demonstrierbar ist. */
    private static void loadSampleData(Playlist playlist) {
        playlist.addSong("Bohemian Rhapsody", "Queen",          354, 5);
        playlist.addSong("Hotel California",  "Eagles",         391, 5);
        playlist.addSong("Stairway to Heaven","Led Zeppelin",   482, 5);
        playlist.addSong("Smells Like Teen Spirit", "Nirvana",  301, 4);
        playlist.addSong("Billie Jean",       "Michael Jackson",294, 4);
        playlist.addSong("Purple Rain",       "Prince",         520, 4);
        playlist.addSong("Like a Rolling Stone","Bob Dylan",    369, 3);
        playlist.addSong("Yesterday",         "The Beatles",    125, 5);
        playlist.addSong("Imagine",           "John Lennon",    187, 4);
        playlist.addSong("Johnny B. Goode",   "Chuck Berry",    162, 3);
    }
}
