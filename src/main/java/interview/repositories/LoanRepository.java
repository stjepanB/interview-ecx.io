package interview.repositories;

import interview.models.Loan;

import java.util.List;

public interface LoanRepository {

    List<Loan> findAll();

    Loan save(Loan loan);

    Loan findById(String id);

    boolean delete(Loan loan);
}
