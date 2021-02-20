package interview.services;

import interview.InterviewApplication;
import interview.forms.BookForm;
import interview.models.Book;
import interview.models.Loan;
import interview.repositories.BookRepository;
import interview.repositories.LoanRepository;
import interview.services.impl.BookServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = InterviewApplication.class)
@ExtendWith(SpringExtension.class)
public class BookServiceTest {

    @MockBean
    private LoanRepository loanRepository;
    @MockBean
    private BookRepository bookRepository;

    private BookService bookService;

    @MockBean
    private BookForm bookForm;

    private Book nullBook;
    private Book someBook;

    private Loan l;

    @BeforeEach
    void setUp() {
        nullBook = new Book();
        nullBook.setId("bk123");
        someBook = new Book();
        someBook.setId("bk223");

        doReturn(nullBook).when(bookForm).getBook();
        l = new Loan();
        l.setBook_id("nullBook");
        l.setStart_date(LocalDate.now().toString());
        l.setEnd_date(LocalDate.now().toString());
        doReturn(l).when(bookForm).getLoan();
        doReturn(null).when(loanRepository).findActiveLoanForBook(nullBook);
        doReturn(l).when(loanRepository).findActiveLoanForBook(someBook);
        doReturn(l).when(loanRepository).save(l);
        bookService = new BookServiceImpl(loanRepository, bookRepository);

    }

    @Test
    @DisplayName("Get false for check book when book is checked")
    public void checkUpTrue_onNewLoanTest() {
        Assertions.assertTrue(bookService.checkBook(bookForm));
    }

    @Test
    @DisplayName("Get false for check book when book is checked")
    public void checkUpFalse_onBorrowedBookTest() {
        doReturn(someBook).when(bookForm).getBook();
        Assertions.assertFalse(bookService.checkBook(bookForm));
    }
}
