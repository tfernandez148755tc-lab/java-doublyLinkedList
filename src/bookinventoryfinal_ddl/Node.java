package bookinventoryfinal_ddl;

public class Node {

    Book book;
    Node next;
    Node prev;

    public Node(Book book) {
        this.book = book;
        this.next = null;
        this.prev = null;
    }

    public Book getBook() {
        return book;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}