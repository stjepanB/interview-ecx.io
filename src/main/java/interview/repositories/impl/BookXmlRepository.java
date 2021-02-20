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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BookXmlRepository implements BookRepository {

    private final float SIMILARITY;
    private String fileLocation;
    private String stopWordsLocation;
    private List<Book> books;
    private List<String> stopWords;

    public BookXmlRepository(@Value("${xml.book}") String fileLocation,
                             @Value("${util.book.similarity}") Float similarity,
                             @Value("${util.stop.words}") String stopWordsLocation) {
        this.fileLocation = fileLocation;
        this.SIMILARITY = similarity;
        this.stopWordsLocation = stopWordsLocation;
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

    @Override
    public List<Book> findByAuthor(String author) {
        String[] tmp = author.split(" ");
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = tmp[i].trim();
        }

        return this.books.stream()
                .filter(e -> isSimilarToQuery(e.getAuthor(), tmp))
                .collect(Collectors.toList());
    }

    private boolean isSimilarToQuery(String author, String[] query) {
        String[] auths = author.split(" |,");

        for (String s : query) {
            for (String auth : auths) {

                if (auth.toLowerCase(Locale.ROOT).equals(s.toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Book> findByDescription(String description) {
        List<String> meaningWords = getMeaningWords(description);
        return this.books.stream()
                .filter(e -> areSimilarDesc(meaningWords, getMeaningWords(e.getDescription())))
                .collect(Collectors.toList());
    }

    private boolean areSimilarDesc(List<String> input1, List<String> input2) {
        Set<String> input1Set = new HashSet<>(input1);
        int containedWordsCount = 0;

        for (String s1 : input1) {
            if (input2.contains(s1)) {
                containedWordsCount++;
            }
        }
        if (input1Set.size() == 0) {
            return false;
        }
        float similarity = containedWordsCount * 1.0F / input1Set.size();

        return similarity > this.SIMILARITY;
    }

    private List<String> getMeaningWords(String input) {
        String[] desc = input.toLowerCase().split(" ");
        List<String> meaningWords = new ArrayList<>();

        for (String s : desc) {
            if (!stopWords.contains(s)) {
                meaningWords.add(s);
            }
        }
        return meaningWords;
    }

    @Override
    public List<Book> findByPublishedDate(LocalDate publishDate) {
        return this.books.stream().filter(e -> e.getPublish_date()
                .equals(publishDate.toString()))
                .collect(Collectors.toList());
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

        try {
            stopWords = Files.readAllLines(Paths.get(stopWordsLocation));
        } catch (IOException e) {
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
