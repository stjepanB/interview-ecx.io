package interview.repositories.impl;

import interview.models.Book;
import interview.models.Catalog;
import interview.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookXmlRepository implements BookRepository {

    private String fileLocation;
    private List<Book> books;

    public BookXmlRepository(@Value("${xml.book}") String fileLocation) {
        this.fileLocation = fileLocation;
        prepare();
    }

    @Override
    public List<Book> findAll() {
        return this.books;
    }

    @Override
    public Book findById(String id) {
        return this.books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return this.books.stream()
                .filter(b -> b.getTitle().equals(title))
                .collect(Collectors.toList());
    }

    @Override
    public Book save(Book book) {

        this.books.add(book);
        Catalog c = new Catalog();
        c.setBooks(this.books);

        if (persist(c)) {
            return book;
        }
        this.books.remove(book);
        return null;
    }

    private void prepare() {

        Path path = Paths.get(fileLocation);

        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
                Files.writeString(path, "<?xml version=\"1.0\"?>");
                Files.writeString(path, "<loans>");
                Files.writeString(path, "</loans>");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.books = new ArrayList<>();
            return;
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Catalog.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            this.books = ((Catalog) jaxbUnmarshaller.unmarshal(
                    new FileInputStream(fileLocation)))
                    .getBooks();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }

        if (this.books == null) {
            this.books = new ArrayList<>();
        }
    }

    private boolean persist(Catalog c) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Catalog.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(c, new FileOutputStream(fileLocation));

        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
