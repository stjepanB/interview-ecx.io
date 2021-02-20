package interview.services.impl;

import interview.forms.BookForm;
import interview.models.Book;
import interview.models.Loan;
import interview.repositories.BookRepository;
import interview.repositories.LoanRepository;
import interview.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookForm> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        return books.stream()
                .map(e -> BookForm.createFormBook(e, loanRepository.findActiveLoanForBook(e)))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkBook(BookForm bookForm) {
        Loan tmp = loanRepository.findActiveLoanForBook(bookForm.getBook());
        if (tmp == null) {
            Loan l = bookForm.getLoan();
            l.setReturned(false);
            l.setId(generateLoanId());
            Loan saved = loanRepository.save(l);
            return saved != null;
        }
        return false;
    }

    @Override
    public List<BookForm> getAvailableBooks() {
        List<Book> books = bookRepository.findAll();

        return books.stream()
                .map(e -> BookForm.createFormBook(e, loanRepository.findActiveLoanForBook(e)))
                .filter(e -> e.getLoan() == null)
                .collect(Collectors.toList());
    }

    private String generateLoanId() {
        Random r = new Random();
        int num = r.nextInt(999999);
        LocalDate l = LocalDate.now();
        StringBuilder s = new StringBuilder();
        s.append("ln")
                .append(num)
                .append(l.getDayOfMonth())
                .append(l.getMonthValue())
                .append(l.getYear());
        return s.toString();
    }

    @Override
    public List<BookForm> getAvailableBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        return books.stream()
                .map(e -> BookForm.createFormBook(e, loanRepository.findActiveLoanForBook(e)))
                .filter(e -> e.getLoan() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookForm> getAvailableBooksByDescription(String description) {
        List<Book> books = bookRepository.findByDescription(description);
        return books.stream()
                .map(e -> BookForm.createFormBook(e, loanRepository.findActiveLoanForBook(e)))
                .filter(e -> e.getLoan() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookForm> getAvailableBooksByPublishDate(LocalDate publishDate) {
        List<Book> books = bookRepository.findByPublishedDate(publishDate);
        return books.stream()
                .map(e -> BookForm.createFormBook(e, loanRepository.findActiveLoanForBook(e)))
                .filter(e -> e.getLoan() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookForm> getAvailableBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitle(title);
        return books.stream()
                .map(e -> BookForm.createFormBook(e, loanRepository.findActiveLoanForBook(e)))
                .filter(e -> e.getLoan() == null)
                .collect(Collectors.toList());
    }
}
