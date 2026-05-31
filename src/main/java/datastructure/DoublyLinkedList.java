package datastructure;

import model.Student;

/**
 * Doppelt verkettete Liste zur Speicherung der Studenten.
 *
 * Jeder Knoten hat einen next- und prev-Zeiger (vgl. Vorlesung Kapitel 03).
 *
 * Laufzeiten:
 *   add:      O(1)  – Einfügen am Ende über tail-Zeiger
 *   remove:   O(n)  – Suche nach Matrikelnummer
 *   findById: O(n)  – Lineare Suche
 *   size:     O(1)
 */
public class DoublyLinkedList {

    /**
     * Knoten der doppelt verketteten Liste.
     * Enthält den Studenten sowie next- und prev-Zeiger.
     */
    public static class Node {
        public Student data;
        public Node next;
        public Node prev;

        public Node(Student data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /** Fügt einen Studenten am Ende ein. O(1) dank tail-Zeiger. */
    public void add(Student student) {
        Node newNode = new Node(student);
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

    /** Entfernt den Studenten mit der gegebenen Matrikelnummer. O(n). */
    public boolean remove(int matrikelnummer) {
        Node current = head;
        while (current != null) {
            if (current.data.getMatrikelnummer() == matrikelnummer) {
                if (current.prev != null) current.prev.next = current.next;
                else head = current.next;
                if (current.next != null) current.next.prev = current.prev;
                else tail = current.prev;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Sucht einen Studenten nach Matrikelnummer. O(n). */
    public Student findByMatrikelnummer(int matrikelnummer) {
        Node current = head;
        while (current != null) {
            if (current.data.getMatrikelnummer() == matrikelnummer) return current.data;
            current = current.next;
        }
        return null;
    }

    /** Gibt alle Studenten als Array zurück – für Sortieralgorithmen. */
    public Student[] toArray() {
        Student[] arr = new Student[size];
        Node current = head;
        for (int i = 0; i < size; i++) {
            arr[i] = current.data;
            current = current.next;
        }
        return arr;
    }

    /** Baut die Liste aus einem (sortierten) Array neu auf. */
    public void fromArray(Student[] arr) {
        head = null;
        tail = null;
        size = 0;
        for (Student s : arr) add(s);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public Node getHead() { return head; }
    public Node getTail() { return tail; }
}
