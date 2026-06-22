package ui;

/**
 * ALDA Projekt – Studenten-Notenverwaltung
 *
 * Datenstruktur:
 *   DoublyLinkedList (doppelt verkettete Liste, Kapitel 03)
 *   – Speichert alle Studenten mit next- und prev-Zeigern
 *   – Ermöglicht effizientes Einfügen (O(1)) und Entfernen (O(n))
 *
 * Algorithmen:
 *   1. InsertionSort  (Kapitel 04) – sortiert nach Nachname A-Z
 *      O(n²) worst-case, O(n) best-case bei fast-sortierter Liste
 *
 *   2. MergeSort      (Kapitel 04) – sortiert nach Matrikelnummer / Note
 *      O(n log n) für alle Fälle, stabil, Divide & Conquer
 *
 *   3. QuickSort      (Kapitel 04) – sortiert nach Note
 *      O(n log n) average, O(n²) worst-case, in der Praxis schnellstes Verfahren
 */

import datastructure.StudentenVerwaltung;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Student;

import java.util.List;

/**
 * JavaFX Hauptanwendung – Studenten-Notenverwaltung.
 */
public class MainApp extends Application {

    private final StudentenVerwaltung verwaltung = new StudentenVerwaltung();
    private TableView<Student> tableView;
    private Label statusLabel;
    private Label statsLabel;

    @Override
    public void start(Stage stage) {
        loadSampleData();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f6f9;");

        root.setTop(buildHeader());
        root.setCenter(buildCenter());
        root.setBottom(buildStatusBar());

        Scene scene = new Scene(root, 1000, 680);
        stage.setTitle("ALDA – Studenten-Notenverwaltung");
        stage.setScene(scene);
        stage.setMinWidth(850);
        stage.setMinHeight(550);
        stage.show();

        refreshTable();
        updateStats();
    }

    // -------------------------------------------------------------------------
    // UI-Aufbau
    // -------------------------------------------------------------------------

    private VBox buildHeader() {
        VBox header = new VBox(4);
        header.setPadding(new Insets(18, 24, 14, 24));
        header.setStyle("-fx-background-color: #2c3e50;");

        Label title = new Label("Studenten-Notenverwaltung");
        title.setFont(Font.font("System", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Datenstruktur: Doppelt verkettete Liste  |  Algorithmen: InsertionSort · MergeSort · QuickSort");
        subtitle.setFont(Font.font("System", 12));
        subtitle.setTextFill(Color.web("#95a5a6"));

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private HBox buildCenter() {
        HBox center = new HBox(16);
        center.setPadding(new Insets(16));

        // Linke Seite: Tabelle + Suchleiste
        VBox tableSection = new VBox(10);
        HBox.setHgrow(tableSection, Priority.ALWAYS);

        HBox searchBar = buildSearchBar();
        tableView = buildTable();
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableSection.getChildren().addAll(searchBar, tableView);

        // Rechte Seite: Panels
        VBox sidePanel = new VBox(12);
        sidePanel.setMinWidth(230);
        sidePanel.setMaxWidth(230);
        sidePanel.getChildren().addAll(
                buildStatsPanel(),
                buildSortPanel(),
                buildAddPanel(),
                buildRemovePanel()
        );

        center.getChildren().addAll(tableSection, sidePanel);
        return center;
    }

    private HBox buildSearchBar() {
        HBox bar = new HBox(8);
        bar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Nach Name suchen...");
        searchField.setPrefHeight(34);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchBtn = styledButton("Suchen", "#3498db");
        Button resetBtn  = styledButton("Alle anzeigen", "#7f8c8d");

        searchBtn.setOnAction(e -> {
            String term = searchField.getText().trim();
            if (term.isEmpty()) { refreshTable(); return; }
            List<Student> results = verwaltung.searchByName(term);
            tableView.getItems().setAll(results);
            setStatus(results.size() + " Ergebnis(se) für \"" + term + "\"");
        });

        resetBtn.setOnAction(e -> { searchField.clear(); refreshTable(); });
        searchField.setOnAction(e -> searchBtn.fire());

        bar.getChildren().addAll(searchField, searchBtn, resetBtn);
        return bar;
    }

    @SuppressWarnings("unchecked")
    private TableView<Student> buildTable() {
        TableView<Student> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 6;");

        TableColumn<Student, Integer> colMnr = new TableColumn<>("Matrikelnr.");
        colMnr.setCellValueFactory(new PropertyValueFactory<>("matrikelnummer"));
        colMnr.setMaxWidth(110);

        TableColumn<Student, String> colNachname = new TableColumn<>("Nachname");
        colNachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));

        TableColumn<Student, String> colVorname = new TableColumn<>("Vorname");
        colVorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));

        TableColumn<Student, String> colStudiengang = new TableColumn<>("Studiengang");
        colStudiengang.setCellValueFactory(new PropertyValueFactory<>("studiengang"));

        TableColumn<Student, Double> colNote = new TableColumn<>("Note");
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colNote.setMaxWidth(70);
        colNote.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double note, boolean empty) {
                super.updateItem(note, empty);
                if (empty || note == null) { setText(null); setStyle(""); return; }
                setText(String.format("%.1f", note));
                if (note <= 1.5)      setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                else if (note <= 2.5) setStyle("-fx-text-fill: #2ecc71;");
                else if (note <= 3.5) setStyle("-fx-text-fill: #f39c12;");
                else if (note <= 4.0) setStyle("-fx-text-fill: #e67e22;");
                else                  setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            }
        });

        TableColumn<Student, String> colNoteText = new TableColumn<>("Beurteilung");
        colNoteText.setCellValueFactory(new PropertyValueFactory<>("noteText"));
        colNoteText.setMaxWidth(140);

        table.getColumns().addAll(colMnr, colNachname, colVorname, colStudiengang, colNote, colNoteText);
        return table;
    }

    private VBox buildStatsPanel() {
        VBox panel = sideCard("Statistik");
        statsLabel = new Label();
        statsLabel.setWrapText(true);
        statsLabel.setFont(Font.font("System", 12));
        statsLabel.setStyle("-fx-text-fill: #555;");
        panel.getChildren().add(statsLabel);
        return panel;
    }

    private VBox buildSortPanel() {
        VBox panel = sideCard("Sortieren");

        Button btnInsert = styledButton("Insertion Sort – Nachname A-Z", "#8e44ad");
        Button btnMerge1 = styledButton("Merge Sort – Matrikelnummer ↑", "#2980b9");
        Button btnMerge2 = styledButton("Merge Sort – Note ↑", "#2980b9");
        Button btnQuick  = styledButton("Quick Sort – Note ↑", "#16a085");

        btnInsert.setMaxWidth(Double.MAX_VALUE);
        btnMerge1.setMaxWidth(Double.MAX_VALUE);
        btnMerge2.setMaxWidth(Double.MAX_VALUE);
        btnQuick.setMaxWidth(Double.MAX_VALUE);

        btnInsert.setOnAction(e -> {
            long t = System.nanoTime();
            verwaltung.sortByNachnameInsertionSort();
            refreshTable();
            setStatus(String.format("Insertion Sort – Nachname A-Z  (%d ms)", (System.nanoTime()-t)/1_000_000));
        });
        btnMerge1.setOnAction(e -> {
            long t = System.nanoTime();
            verwaltung.sortByMatrikelnummerMergeSort();
            refreshTable();
            setStatus(String.format("Merge Sort – Matrikelnummer  (%d ms)", (System.nanoTime()-t)/1_000_000));
        });
        btnMerge2.setOnAction(e -> {
            long t = System.nanoTime();
            verwaltung.sortByNoteMergeSort();
            refreshTable();
            setStatus(String.format("Merge Sort – Note  (%d ms)", (System.nanoTime()-t)/1_000_000));
        });
        btnQuick.setOnAction(e -> {
            long t = System.nanoTime();
            verwaltung.sortByNoteQuickSort();
            refreshTable();
            setStatus(String.format("Quick Sort – Note  (%d ms)", (System.nanoTime()-t)/1_000_000));
        });

        panel.getChildren().addAll(
                sortLabel("O(n²) / O(n) best"), btnInsert,
                sortLabel("O(n log n), stabil"), btnMerge1, btnMerge2,
                sortLabel("O(n log n) avg"), btnQuick
        );
        return panel;
    }

    private VBox buildAddPanel() {
        VBox panel = sideCard("Student hinzufügen");

        TextField tfVorname     = smallField("Vorname");
        TextField tfNachname    = smallField("Nachname");
        TextField tfStudiengang = smallField("Studiengang");
        TextField tfNote        = smallField("Note (1.0 – 5.0)");

        Button btnAdd = styledButton("Hinzufügen", "#27ae60");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setOnAction(e -> {
            try {
                String vn = tfVorname.getText().trim();
                String nn = tfNachname.getText().trim();
                String sg = tfStudiengang.getText().trim();
                double note = Double.parseDouble(tfNote.getText().trim().replace(",", "."));
                if (vn.isEmpty() || nn.isEmpty() || sg.isEmpty()) {
                    setStatus("Bitte alle Felder ausfüllen.");
                    return;
                }
                Student s = verwaltung.addStudent(vn, nn, note, sg);
                tfVorname.clear(); tfNachname.clear(); tfStudiengang.clear(); tfNote.clear();
                refreshTable();
                updateStats();
                setStatus("Hinzugefügt: " + s.getFullName() + " (Mnr. " + s.getMatrikelnummer() + ")");
            } catch (NumberFormatException ex) {
                setStatus("Ungültige Note – bitte z.B. 2.5 eingeben.");
            } catch (IllegalArgumentException ex) {
                setStatus(ex.getMessage());
            }
        });

        panel.getChildren().addAll(tfVorname, tfNachname, tfStudiengang, tfNote, btnAdd);
        return panel;
    }

    private VBox buildRemovePanel() {
        VBox panel = sideCard("Student entfernen");
        TextField tfMnr = smallField("Matrikelnummer");
        Button btnRemove = styledButton("Entfernen", "#e74c3c");
        btnRemove.setMaxWidth(Double.MAX_VALUE);
        btnRemove.setOnAction(e -> {
            try {
                int mnr = Integer.parseInt(tfMnr.getText().trim());
                Student s = verwaltung.findByMatrikelnummer(mnr);
                if (s == null) { setStatus("Matrikelnummer " + mnr + " nicht gefunden."); return; }
                verwaltung.removeStudent(mnr);
                tfMnr.clear();
                refreshTable();
                updateStats();
                setStatus("Entfernt: " + s.getFullName());
            } catch (NumberFormatException ex) {
                setStatus("Bitte eine gültige Matrikelnummer eingeben.");
            }
        });
        panel.getChildren().addAll(tfMnr, btnRemove);
        return panel;
    }

    private HBox buildStatusBar() {
        HBox bar = new HBox();
        bar.setPadding(new Insets(6, 16, 6, 16));
        bar.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 1 0 0 0;");
        statusLabel = new Label("Bereit.");
        statusLabel.setFont(Font.font("System", 12));
        statusLabel.setTextFill(Color.web("#555"));
        bar.getChildren().add(statusLabel);
        return bar;
    }

    // -------------------------------------------------------------------------
    // Hilfsmethoden
    // -------------------------------------------------------------------------

    private void refreshTable() {
        tableView.getItems().setAll(verwaltung.getAllStudents());
    }

    private void updateStats() {
        if (verwaltung.isEmpty()) {
            statsLabel.setText("Keine Studenten.");
            return;
        }
        statsLabel.setText(String.format(
                "Gesamt:       %d Studenten%nPositiv (≤4): %d%nNegativ (5):  %d%nDurchschnitt: %.2f",
                verwaltung.size(),
                verwaltung.getAnzahlPositiv(),
                verwaltung.getAnzahlNegativ(),
                verwaltung.getDurchschnittsnote()
        ));
    }

    private void setStatus(String msg) {
        statusLabel.setText(msg);
        updateStats();
    }

    private VBox sideCard(String title) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 6; -fx-background-radius: 6;");
        Label lbl = new Label(title);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        lbl.setTextFill(Color.web("#2c3e50"));
        card.getChildren().add(lbl);
        return card;
    }

    private Label sortLabel(String complexity) {
        Label lbl = new Label(complexity);
        lbl.setFont(Font.font("System", 10));
        lbl.setTextFill(Color.web("#999"));
        lbl.setPadding(new Insets(4, 0, 0, 0));
        return lbl;
    }

    private Button styledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setFont(Font.font("System", 12));
        btn.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;", color));
        btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
        return btn;
    }

    private TextField smallField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(30);
        tf.setFont(Font.font("System", 12));
        return tf;
    }

    private void loadSampleData() {
        verwaltung.addStudent("Anna",     "Müller",     1.0, "Computer Science");
        verwaltung.addStudent("Thomas",   "Bauer",      2.0, "Computer Science");
        verwaltung.addStudent("Sarah",    "Wagner",     1.5, "Digital Communications");
        verwaltung.addStudent("Lukas",    "Fischer",    3.0, "Computer Science");
        verwaltung.addStudent("Julia",    "Schneider",  2.5, "Digital Communications");
        verwaltung.addStudent("Markus",   "Hoffmann",   4.0, "Computer Science");
        verwaltung.addStudent("Laura",    "Weber",      1.0, "Digital Communications");
        verwaltung.addStudent("David",    "Schmitt",    5.0, "Computer Science");
        verwaltung.addStudent("Sophie",   "Koch",       3.5, "Digital Communications");
        verwaltung.addStudent("Felix",    "Richter",    2.0, "Computer Science");
        verwaltung.addStudent("Hannah",   "Klein",      1.5, "Digital Communications");
        verwaltung.addStudent("Niklas",   "Wolf",       4.5, "Computer Science");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
