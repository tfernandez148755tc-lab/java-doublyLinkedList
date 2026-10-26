package bookinventoryfinal_ddl;

public class DoublyLinkedList {

    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // Add book to the end
    public void add(Book book) {

        Node newNode = new Node(book);

        if (head == null) {

            head = tail = newNode;

        } else {

            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;

        }

        size++;
    }

    // Delete by Book ID
    public boolean delete(int id) {

        Node current = head;

        while (current != null) {

            if (current.book.getId() == id) {

                if (current == head) {
                    head = current.next;
                }

                if (current == tail) {
                    tail = current.prev;
                }

                if (current.prev != null) {
                    current.prev.next = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                }

                size--;
                return true;
            }

            current = current.next;
        }

        return false;
    }

    // Search by ID
    public Node search(int id) {

        Node current = head;

        while (current != null) {

            if (current.book.getId() == id) {
                return current;
            }

            current = current.next;
        }

        return null;
    }

    // Get node by index
    public Node getNodeAt(int index) {

        if (index < 0 || index >= size)
            return null;

        Node current = head;

        int i = 0;

        while (current != null) {

            if (i == index)
                return current;

            current = current.next;

            i++;
        }

        return null;
    }

    // Get index by Book ID
    public int indexOf(int id) {

        Node current = head;

        int index = 0;

        while (current != null) {

            if (current.book.getId() == id)
                return index;

            current = current.next;

            index++;
        }

        return -1;
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {

        head = null;
        tail = null;
        size = 0;

    }

}