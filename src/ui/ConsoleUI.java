package ui;

import datastructure.DoublyLinkedList;
import datastructure.StudentenVerwaltung;
import model.Student;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final StudentenVerwaltung verwaltung;
    private final Scanner scanner;

    public ConsoleUI(StudentenVerwaltung verwaltung) {
        this.verwaltung = verwaltung;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            printMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> alleAnzeigen();
                case "2" -> studentHinzufuegen();
                case "3" -> studentEntfernen();
                case "4" -> sortieren();
                case "5" -> suchen();
                case "6" -> statistik();
                case "0" -> running = false;
                default  -> println("Ungueltige Eingabe.");
            }
        }
        println("Auf Wiedersehen!");
        scanner.close();
    }

    // -------------------------------------------------------------------------
    // Menue
    // -------------------------------------------------------------------------

    private void printMenu() {
        println("");
        println("============================================");
        println("  ALDA Studenten-Notenverwaltung");
        println("  Studenten gesamt: " + verwaltung.size());
        println("============================================");
        println("  1 - Alle Studenten anzeigen");
        println("  2 - Student hinzufuegen");
        println("  3 - Student entfernen");
        println("  4 - Sortieren");
        println("  5 - Suchen");
        println("  6 - Statistik");
        println("  0 - Beenden");
        println("============================================");
        System.out.print("Auswahl: ");
    }

    private void sortMenu() {
        println("");
        println("--- Sortieren ---");
        println("Insertion Sort  [O(n2) worst / O(n) best]:");
        println("  1 - Nach Nachname A-Z");
        println("Merge Sort      [O(n log n), stabil]:");
        println("  2 - Nach Matrikelnummer");
        println("  3 - Nach Note aufsteigend");
        println("Quick Sort      [O(n log n) average]:");
        println("  4 - Nach Note aufsteigend");
        println("  0 - Zurueck");
        System.out.print("Auswahl: ");
    }

    // -------------------------------------------------------------------------
    // Aktionen
    // -------------------------------------------------------------------------

    private void alleAnzeigen() {
        if (verwaltung.isEmpty()) { println("Keine Studenten vorhanden."); return; }
        println("");
        println(String.format("%-12s %-20s %-20s %-6s %-18s %s",
                "Matrikelnr.", "Vorname", "Nachname", "Note", "Beurteilung", "Studiengang"));
        println("-".repeat(90));
        DoublyLinkedList.Node node = verwaltung.getList().getHead();
        while (node != null) {
            Student s = node.data;
            println(String.format("%-12d %-20s %-20s %-6.1f %-18s %s",
                    s.getMatrikelnummer(), s.getVorname(), s.getNachname(),
                    s.getNote(), s.getNoteText(), s.getStudiengang()));
            node = node.next;
        }
        println("-".repeat(90));
        println("Gesamt: " + verwaltung.size() + " Studenten");
    }

    private void studentHinzufuegen() {
        println("\n--- Student hinzufuegen ---");
        try {
            System.out.print("Vorname: ");
            String vn = scanner.nextLine().trim();
            System.out.print("Nachname: ");
            String nn = scanner.nextLine().trim();
            System.out.print("Studiengang: ");
            String sg = scanner.nextLine().trim();
            System.out.print("Note (1.0 - 5.0): ");
            double note = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));

            if (vn.isEmpty() || nn.isEmpty() || sg.isEmpty()) {
                println("Fehler: Alle Felder muessen ausgefuellt werden.");
                return;
            }
            Student s = verwaltung.addStudent(vn, nn, note, sg);
            println("Hinzugefuegt: " + s.getFullName() + " (Mnr. " + s.getMatrikelnummer() + ")");
        } catch (NumberFormatException e) {
            println("Ungueltige Note - bitte z.B. 2.5 eingeben.");
        } catch (IllegalArgumentException e) {
            println("Fehler: " + e.getMessage());
        }
    }

    private void studentEntfernen() {
        if (verwaltung.isEmpty()) { println("Keine Studenten vorhanden."); return; }
        System.out.print("\nMatrikelnummer eingeben: ");
        try {
            int mnr = Integer.parseInt(scanner.nextLine().trim());
            Student s = verwaltung.findByMatrikelnummer(mnr);
            if (s == null) { println("Matrikelnummer " + mnr + " nicht gefunden."); return; }
            verwaltung.removeStudent(mnr);
            println("Entfernt: " + s.getFullName());
        } catch (NumberFormatException e) {
            println("Ungueltige Matrikelnummer.");
        }
    }

    private void sortieren() {
        if (verwaltung.isEmpty()) { println("Keine Studenten vorhanden."); return; }
        sortMenu();
        String choice = scanner.nextLine().trim();
        long start = System.nanoTime();
        switch (choice) {
            case "1" -> { verwaltung.sortByNachnameInsertionSort();    printSortResult("Insertion Sort", "Nachname A-Z", start); }
            case "2" -> { verwaltung.sortByMatrikelnummerMergeSort();  printSortResult("Merge Sort",     "Matrikelnummer", start); }
            case "3" -> { verwaltung.sortByNoteMergeSort();            printSortResult("Merge Sort",     "Note aufsteigend", start); }
            case "4" -> { verwaltung.sortByNoteQuickSort();            printSortResult("Quick Sort",     "Note aufsteigend", start); }
            case "0" -> {}
            default  -> println("Ungueltige Auswahl.");
        }
    }

    private void suchen() {
        System.out.print("\nSuchbegriff (Vor- oder Nachname): ");
        String term = scanner.nextLine().trim();
        if (term.isEmpty()) { println("Suchbegriff darf nicht leer sein."); return; }
        List<Student> results = verwaltung.searchByName(term);
        if (results.isEmpty()) {
            println("Kein Student gefunden fuer \"" + term + "\".");
        } else {
            println(results.size() + " Ergebnis(se):");
            for (Student s : results) {
                println(String.format("  [%d] %s - Note: %.1f (%s)",
                        s.getMatrikelnummer(), s.getFullName(), s.getNote(), s.getNoteText()));
            }
        }
    }

    private void statistik() {
        if (verwaltung.isEmpty()) { println("Keine Studenten vorhanden."); return; }
        int positiv = verwaltung.getAnzahlPositiv();
        int negativ = verwaltung.size() - positiv;
        println("\n--- Statistik ---");
        println("Gesamt:              " + verwaltung.size());
        println("Positiv (Note <= 4): " + positiv);
        println("Negativ (Note  > 4): " + negativ);
        println(String.format("Durchschnittsnote:   %.2f", verwaltung.getDurchschnittsnote()));
    }

    // -------------------------------------------------------------------------
    // Hilfsmethoden
    // -------------------------------------------------------------------------

    private void printSortResult(String algo, String criterion, long startNano) {
        long ms = (System.nanoTime() - startNano) / 1_000_000;
        println("\n" + algo + " angewendet - Sortiert nach: " + criterion +
                " (" + verwaltung.size() + " Studenten, " + ms + " ms)");
        alleAnzeigen();
    }

    private void println(String msg) { System.out.println(msg); }

    private void printBanner() {
        println("============================================");
        println("    ALDA - Studenten-Notenverwaltung");
        println("    Datenstruktur: Doppelt verk. Liste");
        println("    Algorithmen:   InsertionSort");
        println("                   MergeSort");
        println("                   QuickSort");
        println("============================================");
    }
}
