package interview.repositories;

import interview.models.Book;
import interview.models.Loan;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository {

    List<Loan> findAll();

    Loan save(Loan loan);

    Loan findById(String id);

    boolean delete(Loan loan);

    List<Loan> findLoansByEndDateAfter(LocalDate endDate);

    List<Loan> findLoansByReturned(boolean returned);

    Loan findActiveLoanForBook(Book book);
}
