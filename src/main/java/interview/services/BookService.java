package interview.services;

import interview.forms.BookForm;
import interview.forms.BookLoanForm;
import interview.models.Book;

import java.time.LocalDate;
import java.util.List;

public interface BookService {

    List<BookForm> getAllBooks();

    boolean checkBook(BookLoanForm bookForm);

    List<BookForm> getAvailableBooks();

    List<BookForm> getAvailableBooksByAuthor(String author);

    List<BookForm> getAvailableBooksByDescription(String description);

    List<BookForm> getAvailableBooksByPublishDate(LocalDate publishDate);

    List<BookForm> getAvailableBooksByTitle(String title);

}
