package interview.controllers;

import interview.forms.BookForm;
import interview.forms.BookLoanForm;
import interview.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/")
    public List<BookForm> getBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping(value = "/available")
    public List<BookForm> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    @GetMapping(value = "/available", params = "title")
    public List<BookForm> getAvailableBooksByTitle(@RequestParam String title) {
        return bookService.getAvailableBooksByTitle(title);
    }

    @GetMapping(value = "/available", params = "author")
    public List<BookForm> getAvailableBooksByAuthor(@RequestParam String author) {
        return bookService.getAvailableBooksByAuthor(author);
    }

    @GetMapping(value = "/available", params = "description")
    public List<BookForm> getAvailableBooksByDescription(@RequestParam String description) {
        return bookService.getAvailableBooksByDescription(description);
    }

    @PostMapping(value = "/loan")
    public HttpStatus saveLoan(@RequestBody BookLoanForm form) {
        return bookService.checkBook(form) ? HttpStatus.OK : HttpStatus.FORBIDDEN;
    }
}
