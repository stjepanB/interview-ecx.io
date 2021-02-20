package interview.forms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import interview.models.Book;
import interview.models.Loan;

import java.util.List;

public class BookForm {

    private String title;
    private String book_id;
    private String loan_id;
    private String user;
    private String author;
    private String genre;
    private Double price;
    private String publish_date;
    private String description;
    private String start_date;
    private String end_date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(String loan_id) {
        this.loan_id = loan_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    @JsonIgnore
    public Book getBook() {
        Book b = new Book();
        b.setId(this.book_id);
        b.setAuthor(this.author);
        b.setDescription(this.description);
        b.setGenre(this.genre);
        b.setPrice(this.price);
        b.setPublish_date(this.publish_date);
        b.setTitle(this.title);
        return b;
    }

    @JsonIgnore
    public Loan getLoan() {
        if(this.user == null){
            return null;
        }
        Loan l = new Loan();
        l.setBook_id(this.book_id);
        l.setUser(this.user);
        l.setStart_date(this.start_date);
        l.setEnd_date(this.end_date);
        return l;
    }

    public static BookForm createFormBook(Book b, Loan l) {
        BookForm f = new BookForm();
        f.setAuthor(b.getAuthor());
        f.setBook_id(b.getId());
        f.setDescription(b.getDescription());
        f.setGenre(b.getGenre());
        f.setPrice(b.getPrice());
        f.setTitle(b.getTitle());
        f.setPublish_date(b.getPublish_date());

        if (l != null) {
            f.setUser(l.getUser());
            f.setEnd_date(l.getEnd_date());
            f.setEnd_date(l.getStart_date());
            f.setLoan_id(l.getId());
        }
        return f;
    }
}
