package model;

/**
 * Repräsentiert einen Song in der Playlist.
 */
public class Song {

    private final int id;
    private String title;
    private String artist;
    private int durationSeconds;
    private int playCount;
    private int rating; // 1-5

    public Song(int id, String title, String artist, int durationSeconds, int rating) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.durationSeconds = durationSeconds;
        this.playCount = 0;
        this.rating = rating;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getDurationSeconds() { return durationSeconds; }
    public int getPlayCount() { return playCount; }
    public int getRating() { return rating; }

    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
    public void incrementPlayCount() { this.playCount++; }
    public void setRating(int rating) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Bewertung muss zwischen 1 und 5 liegen.");
        this.rating = rating;
    }

    public String formatDuration() {
        int min = durationSeconds / 60;
        int sec = durationSeconds % 60;
        return String.format("%d:%02d", min, sec);
    }

    @Override
    public String toString() {
        return String.format("[%d] %-30s %-20s %s  Bewertung: %d/5  Gespielt: %dx",
                id, title, artist, formatDuration(), rating, playCount);
    }
}
