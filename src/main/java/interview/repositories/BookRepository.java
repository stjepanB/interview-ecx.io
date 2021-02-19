package interview.repositories;

import interview.models.Book;

import java.util.List;

public interface BookRepository {

    List<Book> findAll();

    Book findById(String id);

    List<Book> findByTitle(String title);

    Book save(Book book);
}
