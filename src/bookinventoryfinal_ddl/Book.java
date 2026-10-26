package bookinventoryfinal_ddl;

public class Book {
    private int id;
    private String title;
    private String author;
    private int quantity;
    private String category;
    private String isbn;
    private String publisher;
    private String year;
    private String coverPath;

    public Book(int id, String title, String author, int quantity, String category) {
        this(id, title, author, quantity, category, "", "", "", "");
    }

    public Book(int id, String title, String author, int quantity, String category,
                String isbn, String publisher, String year, String coverPath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.category = category;
        this.isbn = isbn;
        this.publisher = publisher;
        this.year = year;
        this.coverPath = coverPath;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getCoverPath() { return coverPath; }
    public void setCoverPath(String coverPath) { this.coverPath = coverPath; }
}
