package interview.models;

import org.apache.tomcat.jni.Local;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDate;

@XmlRootElement(name = "book")
@XmlType(propOrder = {"author", "title", "genre", "price", "publish_date", "description"})
public class Book {

    private String id;
    private String title;
    private String author;
    private String genre;
    private Double price;
    private String publish_date;
    private String description;

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @XmlElement(name = "title")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    @XmlElement(name = "author")
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    @XmlElement(name = "genre")
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getPrice() {
        return price;
    }

    @XmlElement(name = "price")
    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPublish_date() {
        return publish_date;
    }

    @XmlElement(name = "publish_date")
    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublishLocalDate() {
        return LocalDate.parse(this.publish_date);
    }
}
