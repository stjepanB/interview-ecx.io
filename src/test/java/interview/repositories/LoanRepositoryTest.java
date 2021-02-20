package interview.repositories;

import interview.InterviewApplication;
import interview.models.Book;
import interview.models.Loan;
import interview.repositories.impl.BookXmlRepository;
import interview.repositories.impl.LoanXmlRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = InterviewApplication.class)
@ExtendWith(SpringExtension.class)
public class LoanRepositoryTest {

    private List<Book> books;
    private LoanRepository loanRepository;
    private Book first;

    @BeforeEach
    void setUp(@Value("${test.xml.loans}") String fileLocation) {
        loanRepository = new LoanXmlRepository(fileLocation);
        Loan l = new Loan();
        l.setBook_id("bk106");
        l.setStart_date(LocalDate.of(2021, 1, 25).toString());
        l.setEnd_date(LocalDate.of(2021, 2, 25).toString());
        l.setUser("Netko nepoznat");
        l.setId("test1234");
        l.setReturned(false);
        loanRepository.save(l);
    }

    @Test
    @DisplayName("Save loan.")
    public void saveLoan() {
        Loan l = new Loan();
        l.setBook_id("bk101");
        l.setStart_date(LocalDate.of(2021, 1, 25).toString());
        l.setEnd_date(LocalDate.of(2021, 2, 25).toString());
        l.setUser("Stipe");
        l.setId("asdasd21321");
        l.setReturned(true);
        loanRepository.save(l);

        Assertions.assertNotNull(loanRepository.findById("asdasd21321"));
    }

    @Test
    @DisplayName("Get loan by id")
    public void findLoanById_test() {
        Loan l = loanRepository.findById("ln2234");
        Assertions.assertNotNull(l);
    }

    @Test
    @DisplayName("Find lonas with end date before")
    public void findLoansWithEndDate_beforeTest() {
        List<Loan> ls = loanRepository.findLoansByEndDateAfter(LocalDate.of(2021, 2, 2));
        Loan tmp = ls.stream().filter(e -> e.getBook_id().equals("bk106")).findFirst().orElse(null);
        Assertions.assertNotNull(tmp);
    }

    @Test
    @DisplayName("Find books that aren't returned")
    public void findLoans_notReturnedTest() {
        List<Loan> ls = loanRepository.findLoansByReturned(false);
        Loan tmp = ls.stream().filter(e -> e.getId().equals("test1234")).findFirst().orElse(null);
        Assertions.assertNotNull(tmp);
    }

    @Test
    @DisplayName("Find active loan for book")
    public void findActiveLoan_ForBookTest() {
        Book b = new Book();
        b.setId("bk101");
        Loan l = loanRepository.findActiveLoanForBook(b);
        Assertions.assertNotNull(l);
    }

    @Test
    @DisplayName("Don't find active loan for book")
    public void dontFindActiveLoan_ForBookTest() {
        Book b = new Book();
        b.setId("bk121");
        Loan l = loanRepository.findActiveLoanForBook(b);
        Assertions.assertNull(l);
    }

    @AfterEach
    void cleanUp() {
        Loan l = loanRepository.findById("asdasd21321");
        loanRepository.delete(l);
    }
}
