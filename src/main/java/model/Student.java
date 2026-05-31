package model;

/**
 * Repräsentiert einen Studenten mit Matrikelnummer, Name und Note.
 */
public class Student {

    private final int matrikelnummer;
    private String vorname;
    private String nachname;
    private double note; // 1.0 (sehr gut) bis 5.0 (nicht genügend)
    private String studiengang;

    public Student(int matrikelnummer, String vorname, String nachname, double note, String studiengang) {
        this.matrikelnummer = matrikelnummer;
        this.vorname = vorname;
        this.nachname = nachname;
        setNote(note);
        this.studiengang = studiengang;
    }

    public int getMatrikelnummer() { return matrikelnummer; }
    public String getVorname() { return vorname; }
    public String getNachname() { return nachname; }
    public double getNote() { return note; }
    public String getStudiengang() { return studiengang; }
    public String getFullName() { return nachname + " " + vorname; }

    public void setVorname(String vorname) { this.vorname = vorname; }
    public void setNachname(String nachname) { this.nachname = nachname; }
    public void setStudiengang(String studiengang) { this.studiengang = studiengang; }
    public void setNote(double note) {
        if (note < 1.0 || note > 5.0) throw new IllegalArgumentException("Note muss zwischen 1.0 und 5.0 liegen.");
        this.note = note;
    }

    public String getNoteText() {
        if (note <= 1.5) return "Sehr Gut";
        if (note <= 2.5) return "Gut";
        if (note <= 3.5) return "Befriedigend";
        if (note <= 4.5) return "Genügend";
        return "Nicht Genügend";
    }

    @Override
    public String toString() {
        return String.format("%d | %-20s | %.1f (%s) | %s",
                matrikelnummer, getFullName(), note, getNoteText(), studiengang);
    }
}
