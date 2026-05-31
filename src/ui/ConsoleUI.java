package ui;

import datastructure.DoublyLinkedList;
import datastructure.Playlist;
import model.Song;

import java.util.Scanner;

/**
 * Konsolen-Benutzeroberfläche für den Playlist Manager.
 */
public class ConsoleUI {

    private final Playlist playlist;
    private final Scanner scanner;

    public ConsoleUI(Playlist playlist) {
        this.playlist = playlist;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> showAllSongs();
                case "2" -> addSong();
                case "3" -> removeSong();
                case "4" -> playerMenu();
                case "5" -> sortMenu();
                case "6" -> searchSong();
                case "0" -> running = false;
                default  -> print("Ungültige Eingabe. Bitte eine Zahl zwischen 0 und 6 eingeben.");
            }
        }
        print("\nAuf Wiedersehen!");
        scanner.close();
    }

    // -------------------------------------------------------------------------
    // Menüs
    // -------------------------------------------------------------------------

    private void printMainMenu() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.printf( "║  Playlist: %-30s║%n", playlist.getName());
        System.out.printf( "║  Songs: %-5d  Gesamtdauer: %-13s║%n", playlist.size(), playlist.formatTotalDuration());
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1 – Alle Songs anzeigen                 ║");
        System.out.println("║  2 – Song hinzufügen                     ║");
        System.out.println("║  3 – Song entfernen                      ║");
        System.out.println("║  4 – Player (abspielen / navigieren)     ║");
        System.out.println("║  5 – Sortieren                           ║");
        System.out.println("║  6 – Song suchen                         ║");
        System.out.println("║  0 – Beenden                             ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print("Auswahl: ");
    }

    private void sortMenu() {
        System.out.println("\n--- Sortieren ---");
        System.out.println("Insertion Sort  (O(n²) / O(n) best-case):");
        System.out.println("  1 – Nach Titel      (A-Z)");
        System.out.println("  2 – Nach Künstler   (A-Z)");
        System.out.println("Merge Sort      (O(n log n), stabil):");
        System.out.println("  3 – Nach Künstler   (A-Z)");
        System.out.println("  4 – Nach Dauer      (kürzeste zuerst)");
        System.out.println("  5 – Nach Wiedergaben (meistgespielt zuerst)");
        System.out.println("Quick Sort      (O(n log n) avg, O(n²) worst):");
        System.out.println("  6 – Nach Bewertung  (höchste zuerst)");
        System.out.println("  0 – Zurück");
        System.out.print("Auswahl: ");

        String choice = scanner.nextLine().trim();
        if (playlist.isEmpty()) { print("Die Playlist ist leer."); return; }

        long start = System.nanoTime();
        switch (choice) {
            case "1" -> { playlist.sortByTitleInsertionSort();   printSortResult("Insertion Sort", "Titel (A-Z)", start); }
            case "2" -> { playlist.sortByArtistInsertionSort();  printSortResult("Insertion Sort", "Künstler (A-Z)", start); }
            case "3" -> { playlist.sortByArtistMergeSort();      printSortResult("Merge Sort", "Künstler (A-Z)", start); }
            case "4" -> { playlist.sortByDurationMergeSort();    printSortResult("Merge Sort", "Dauer", start); }
            case "5" -> { playlist.sortByPlayCountMergeSort();   printSortResult("Merge Sort", "Wiedergaben", start); }
            case "6" -> { playlist.sortByRatingQuickSort();      printSortResult("Quick Sort", "Bewertung", start); }
            case "0" -> {}
            default  -> print("Ungültige Auswahl.");
        }
    }

    private void playerMenu() {
        boolean inPlayer = true;
        while (inPlayer) {
            Song current = playlist.getCurrentSong();
            System.out.println("\n--- Player ---");
            if (current != null) {
                System.out.println("  ▶  " + current);
            } else {
                System.out.println("  Keine Songs in der Playlist.");
            }
            System.out.println("  n – Nächster Song   |  p – Vorheriger Song");
            System.out.println("  j – Zu Song springen");
            System.out.println("  0 – Zurück");
            System.out.print("Auswahl: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "n" -> {
                    Song s = playlist.next();
                    if (s != null) print("Weiter: " + s.getTitle() + " – " + s.getArtist());
                    else print("Kein nächster Song.");
                }
                case "p" -> {
                    Song s = playlist.previous();
                    if (s != null) print("Zurück: " + s.getTitle() + " – " + s.getArtist());
                    else print("Kein vorheriger Song.");
                }
                case "j" -> {
                    System.out.print("Song-ID eingeben: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        Song s = playlist.jumpTo(id);
                        if (s != null) print("Spiele: " + s);
                        else print("Song mit ID " + id + " nicht gefunden.");
                    } catch (NumberFormatException e) {
                        print("Ungültige ID.");
                    }
                }
                case "0" -> inPlayer = false;
                default  -> print("Ungültige Eingabe.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Song-Verwaltung
    // -------------------------------------------------------------------------

    private void showAllSongs() {
        if (playlist.isEmpty()) { print("Die Playlist ist leer."); return; }
        System.out.println("\n--- Songs in der Playlist (Doppelt verkettete Liste) ---");
        System.out.printf("%-5s %-30s %-20s %-6s %-10s %-8s%n",
                "ID", "Titel", "Künstler", "Dauer", "Bewertung", "Gespielt");
        System.out.println("-".repeat(85));
        DoublyLinkedList.Node node = playlist.getList().getHead();
        while (node != null) {
            Song s = node.data;
            System.out.printf("%-5d %-30s %-20s %-6s %-10s %-8d%n",
                    s.getId(), s.getTitle(), s.getArtist(), s.formatDuration(),
                    "★".repeat(s.getRating()) + "☆".repeat(5 - s.getRating()), s.getPlayCount());
            node = node.next;
        }
        System.out.println("-".repeat(85));
        System.out.printf("Gesamt: %d Songs  |  Dauer: %s%n", playlist.size(), playlist.formatTotalDuration());
    }

    private void addSong() {
        System.out.println("\n--- Song hinzufügen ---");
        try {
            System.out.print("Titel: ");
            String title = scanner.nextLine().trim();
            if (title.isEmpty()) { print("Titel darf nicht leer sein."); return; }

            System.out.print("Künstler: ");
            String artist = scanner.nextLine().trim();
            if (artist.isEmpty()) { print("Künstler darf nicht leer sein."); return; }

            System.out.print("Dauer (Minuten:Sekunden, z.B. 3:45): ");
            int duration = parseDuration(scanner.nextLine().trim());

            System.out.print("Bewertung (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine().trim());

            Song added = playlist.addSong(title, artist, duration, rating);
            print("Hinzugefügt: " + added);
        } catch (NumberFormatException e) {
            print("Ungültige Eingabe – bitte Zahlen korrekt eingeben.");
        } catch (IllegalArgumentException e) {
            print("Fehler: " + e.getMessage());
        }
    }

    private void removeSong() {
        if (playlist.isEmpty()) { print("Die Playlist ist leer."); return; }
        System.out.print("\nSong-ID zum Entfernen: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Song s = playlist.findById(id);
            if (s == null) { print("Song mit ID " + id + " nicht gefunden."); return; }
            playlist.removeSong(id);
            print("Entfernt: " + s.getTitle() + " – " + s.getArtist());
        } catch (NumberFormatException e) {
            print("Ungültige ID.");
        }
    }

    private void searchSong() {
        System.out.print("\nSuchbegriff (Titel oder Künstler): ");
        String term = scanner.nextLine().trim().toLowerCase();
        if (term.isEmpty()) { print("Suchbegriff darf nicht leer sein."); return; }

        System.out.println("Suchergebnisse:");
        boolean found = false;
        DoublyLinkedList.Node node = playlist.getList().getHead();
        while (node != null) {
            Song s = node.data;
            if (s.getTitle().toLowerCase().contains(term) || s.getArtist().toLowerCase().contains(term)) {
                System.out.println("  " + s);
                found = true;
            }
            node = node.next;
        }
        if (!found) print("Kein Song gefunden für \"" + term + "\".");
    }

    // -------------------------------------------------------------------------
    // Hilfsmethoden
    // -------------------------------------------------------------------------

    private void printSortResult(String algorithm, String criterion, long startNano) {
        long ms = (System.nanoTime() - startNano) / 1_000_000;
        System.out.printf("%n✓ %s angewendet – Sortiert nach: %s (%d Songs, %d ms)%n",
                algorithm, criterion, playlist.size(), ms);
        showAllSongs();
    }

    private int parseDuration(String input) {
        String[] parts = input.split(":");
        if (parts.length != 2) throw new NumberFormatException("Format: M:SS");
        return Integer.parseInt(parts[0].trim()) * 60 + Integer.parseInt(parts[1].trim());
    }

    private void print(String msg) {
        System.out.println(msg);
    }

    private void printBanner() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         ALDA Playlist Manager            ║");
        System.out.println("║  Datenstruktur: Doppelt verkettete Liste ║");
        System.out.println("║  Algorithmen:   InsertionSort            ║");
        System.out.println("║                 MergeSort                ║");
        System.out.println("║                 QuickSort                ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }
}
