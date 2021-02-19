package interview.repositories.impl;

import interview.models.Book;
import interview.models.Catalog;
import interview.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class XmlBookRepository implements BookRepository {

    private String fileLocation;
    private List<Book> books;

    public XmlBookRepository(@Value("${xml.book}") String fileLocation) {
        this.fileLocation = fileLocation;
        try {
            prepare();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    private String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    private void prepare() throws IOException, JAXBException {
        ClassLoader classLoader = getClass().getClassLoader();
        classLoader.getResourceAsStream(fileLocation);
        JAXBContext jaxbContext = JAXBContext.newInstance(Catalog.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        InputStream stream = classLoader.getResourceAsStream(fileLocation);


        this.books = ((Catalog) jaxbUnmarshaller.unmarshal(new FileInputStream("D:/Code/interview-ecx.io/src/main/resources/books.xml"))).getBooks();
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
}
