package interview.repositories;

import interview.models.Book;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository {

    List<Book> findAll();

    Book findById(String id);

    List<Book> findByTitle(String title);

    Book save(Book book);

    List<Book> findByAuthor(String author);

    List<Book> findByDescription(String description);

    List<Book> findByPublishedDate(LocalDate publishDate);
}
