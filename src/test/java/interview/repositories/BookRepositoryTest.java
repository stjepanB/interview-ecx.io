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

import java.util.List;

@SpringBootTest(classes = InterviewApplication.class)
@ExtendWith(SpringExtension.class)
public class BookRepositoryTest {

    private List<Book> books;
    private BookRepository bookRepository;
    private Book first;

    @BeforeEach
    void setUp(@Value("${test.xml.books}") String fileLocation) {
        bookRepository = new BookXmlRepository(fileLocation);
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
}
