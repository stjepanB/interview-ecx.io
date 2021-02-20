package interview.repositories;

import interview.InterviewApplication;
import interview.models.Book;
import interview.repositories.impl.BookXmlRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = InterviewApplication.class)
@ExtendWith(SpringExtension.class)
public class BookRepositoryTest {

    private List<Book> books;
    private BookRepository bookRepository;
    private Book first;

    @BeforeEach
    void setUp(@Value("${test.xml.books}") String fileLocation, @Value("${util.stop.word}") String stopWordLocation) {
        bookRepository = new BookXmlRepository(fileLocation, 0.05F, stopWordLocation);
        this.books = bookRepository.findAll();
        this.first = bookRepository.findById("bk101");
    }

    @Test
    @DisplayName("First book in document has right name.")
    public void firstBook_hasRightTitle() {
        Assertions.assertEquals("XML Developer's Guide", first.getTitle());
    }

    @Test
    @DisplayName("List of books contains first one.")
    public void firstBook_isInList() {
        var book = books.stream()
                .filter(e -> e.getId().equals("bk101"))
                .findFirst().get();
        Assertions.assertNotNull(book);
    }

    @Test
    @DisplayName("Publish date")
    public void firstBook_hasRightDate() {
        var book = books.stream()
                .filter(e -> e.getId().equals("bk101"))
                .findFirst().get();
        Assertions.assertEquals("2000-10-01", book.getPublish_date());
    }

    @Test
    @DisplayName("Find books by author test")
    public void correctName_authorTest() {
        var b = bookRepository.findByAuthor("Gambardella");
        Assertions.assertEquals("Gambardella, Matthew", b.get(0).getAuthor());
    }

    @Test
    @DisplayName("Find multiple books by author test")
    public void allBooksForFirstName_authorTest() {
        var b = bookRepository.findByAuthor("Eva");
        Assertions.assertEquals(3, b.size());
    }

    @Test
    @DisplayName("Find multiple books by upper case author test")
    public void allBooksUpperCase_authorTest() {
        var b = bookRepository.findByAuthor("EVA");
        Assertions.assertEquals(3, b.size());
    }

    @Test
    @DisplayName("Find all books that have similar description")
    public void allBooksWithDesc_descriptionTest() {
        List<Book> books = bookRepository.findByDescription("two daughters fight for power in England");
        Assertions.assertEquals("bk105", books.get(0).getId());
    }

    @Test
    @DisplayName("All books publish on same date")
    public void booksWith_samePublishDate() {
        List<Book> books = bookRepository.findByPublishedDate(LocalDate.of(2001, 9, 10));
        Book tmp = bookRepository.findById("bk105");
        Assertions.assertTrue(books.contains(tmp));
    }

}
