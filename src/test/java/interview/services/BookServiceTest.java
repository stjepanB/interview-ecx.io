package interview.services;

import interview.InterviewApplication;
import interview.forms.BookLoanForm;
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
    private BookLoanForm loanForm;

    private Loan newLoan;
    private Loan existingLoan;

    private Loan l;

    @BeforeEach
    void setUp() {
        newLoan = new Loan();
        newLoan.setBook_id("bk111");
        newLoan.setReturned(false);
        newLoan.setUser("Slavko Miškić");
        newLoan.setStart_date(LocalDate.now().toString());
        newLoan.setStart_date(LocalDate.now().plusDays(30L).toString());

        existingLoan = new Loan();
        existingLoan.setBook_id("bk223");


        Book b_existing = new Book();
        b_existing.setId("bk223");

        Book b_newLoan = new Book();
        b_newLoan.setId("bk111");


        doReturn(b_existing).when(bookRepository).findById("bk223");
        doReturn(b_newLoan).when(bookRepository).findById("bk111");


        doReturn(null).when(loanRepository).findActiveLoanForBook(b_newLoan);
        doReturn(existingLoan).when(loanRepository).findActiveLoanForBook(b_existing);
        doReturn(newLoan).when(loanRepository).save(newLoan);
        bookService = new BookServiceImpl(loanRepository, bookRepository, 30L);

    }

    @Test
    @DisplayName("Get false for check book when book is checked")
    public void checkUpTrue_onNewLoanTest() {
        doReturn(newLoan).when(loanForm).getLoan(30L);
        Assertions.assertTrue(bookService.checkBook(loanForm));
    }

    @Test
    @DisplayName("Get false for check book when book is checked")
    public void checkUpFalse_onBorrowedBookTest() {
        doReturn(existingLoan).when(loanForm).getLoan(30L);
        Assertions.assertFalse(bookService.checkBook(loanForm));
    }
}
