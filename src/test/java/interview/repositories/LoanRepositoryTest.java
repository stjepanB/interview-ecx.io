package interview.repositories;

import interview.InterviewApplication;
import interview.models.Book;
import interview.models.Loan;
import interview.repositories.impl.BookXmlRepository;
import interview.repositories.impl.LoanXmlRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    }

    @Test
    @DisplayName("Save loan.")
    public void saveLoan(){
        Loan l = new Loan();
        l.setBook_id("bk101");
        l.setEnd_date(LocalDate.of(2021,1,25).toString());
        l.setStart_date(LocalDate.of(2021,2,25).toString());
        l.setUser("Stipe");
        l.setId("asdasd21321");
        loanRepository.save(l);

        Assertions.assertNotNull(loanRepository.findById("asdasd21321"));
    }
}
