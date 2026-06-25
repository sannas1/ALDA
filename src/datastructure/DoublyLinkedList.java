package datastructure;

import model.Student;

/**
 * Doppelt verkettete Liste (vgl. Vorlesung Kapitel 03).
 *
 * Jeder Knoten hat einen next- und prev-Zeiger.
 *
 * Laufzeiten:
 *   add:    O(1) - Einfuegen am Ende ueber tail-Zeiger
 *   remove: O(n) - Lineare Suche nach Matrikelnummer
 *   find:   O(n) - Lineare Suche
 *   size:   O(1)
 */
public class DoublyLinkedList {

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

    /** Fuegt einen Studenten am Ende ein. O(1) dank tail-Zeiger. */
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

    /** Gibt alle Studenten als Array zurueck - wird von den Sortieralgorithmen benoetigt. */
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

    public int size()      { return size; }
    public boolean isEmpty() { return size == 0; }
    public Node getHead()  { return head; }
}
