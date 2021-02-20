package interview.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDate;

@XmlRootElement(name = "loan")
@XmlType(propOrder = {"user", "book_id", "start_date", "end_date", "returned"})
public class Loan {

    private String id;
    private String start_date;
    private String end_date;
    private String user;
    private String book_id;
    private boolean returned;

    public String getStart_date() {
        return start_date;
    }

    @XmlElement(name = "start_date")
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    @XmlElement(name = "end_date")
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getUser() {
        return user;
    }

    @XmlElement(name = "user")
    public void setUser(String user) {
        this.user = user;
    }

    public String getBook_id() {
        return book_id;
    }

    @XmlElement(name = "book_id")
    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "returned")
    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public LocalDate getStartLocalDate() {
        return LocalDate.parse(this.start_date);
    }

    public LocalDate getEndLocalDate() {
        return LocalDate.parse(this.end_date);
    }
}
