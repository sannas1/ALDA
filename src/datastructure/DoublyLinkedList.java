package datastructure;

import model.Song;

/**
 * Doppelt verkettete Liste zur Speicherung der Playlist.
 *
 * Jeder Knoten hat einen next- und einen prev-Zeiger (vgl. Vorlesung Kapitel 03).
 * Dadurch kann die Playlist vorwärts (next) und rückwärts (prev) durchlaufen werden –
 * ideal für die Navigation zwischen Songs.
 *
 * Laufzeiten:
 *   add(song):          O(1) – Einfügen am Ende über tail-Zeiger
 *   remove(id):         O(n) – Suche erforderlich
 *   get(index):         O(n) – Sequenzieller Zugriff
 *   size():             O(1)
 */
public class DoublyLinkedList {

    /**
     * Innerer Knoten der doppelt verketteten Liste.
     * Enthält Daten sowie next- und prev-Zeiger.
     */
    public static class Node {
        public Song data;
        public Node next;
        public Node prev;

        public Node(Song data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /** Fügt einen Song am Ende der Liste ein. O(1) dank tail-Zeiger. */
    public void add(Song song) {
        Node newNode = new Node(song);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /** Fügt einen Song an einer bestimmten Position ein (0-basiert). O(n). */
    public void addAt(int index, Song song) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Ungültiger Index: " + index);
        if (index == size) { add(song); return; }
        Node newNode = new Node(song);
        if (index == 0) {
            newNode.next = head;
            if (head != null) head.prev = newNode;
            head = newNode;
            if (tail == null) tail = newNode;
        } else {
            Node current = getNode(index);
            Node prevNode = current.prev;
            prevNode.next = newNode;
            newNode.prev = prevNode;
            newNode.next = current;
            current.prev = newNode;
        }
        size++;
    }

    /** Entfernt den Song mit der gegebenen ID. Gibt true zurück bei Erfolg. O(n). */
    public boolean remove(int songId) {
        Node current = head;
        while (current != null) {
            if (current.data.getId() == songId) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    head = current.next;
                }
                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    tail = current.prev;
                }
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Gibt den Song an Position index zurück (0-basiert). O(n). */
    public Song get(int index) {
        return getNode(index).data;
    }

    /** Sucht einen Song anhand seiner ID. Gibt null zurück, wenn nicht gefunden. O(n). */
    public Song findById(int id) {
        Node current = head;
        while (current != null) {
            if (current.data.getId() == id) return current.data;
            current = current.next;
        }
        return null;
    }

    /** Gibt alle Songs als Array zurück – wird von den Sortieralgorithmen benötigt. */
    public Song[] toArray() {
        Song[] arr = new Song[size];
        Node current = head;
        for (int i = 0; i < size; i++) {
            arr[i] = current.data;
            current = current.next;
        }
        return arr;
    }

    /**
     * Ersetzt den Listeninhalt durch ein sortiertes Array.
     * Wird nach einem Sortiervorgang aufgerufen, um die Liste neu aufzubauen.
     */
    public void fromArray(Song[] arr) {
        head = null;
        tail = null;
        size = 0;
        for (Song s : arr) add(s);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public Node getHead() { return head; }
    public Node getTail() { return tail; }

    private Node getNode(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Ungültiger Index: " + index);
        Node current;
        // Von hinten oder vorne traversieren – je nachdem was kürzer ist
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) current = current.prev;
        }
        return current;
    }
}
