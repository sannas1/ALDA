package ui;

import javafx.application.Application;

/**
 * Classpath-safe launcher for the JavaFX application.
 */
public final class MainApp {

    private MainApp() {
    }

    public static void main(String[] args) {
        Application.launch(MainFxApp.class, args);
    }
}
