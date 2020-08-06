package com.genpact.libraryApp.config;

import com.genpact.libraryApp.domain.Book;
import com.genpact.libraryApp.domain.Library;
import com.genpact.libraryApp.repository.BookRepository;
import com.genpact.libraryApp.repository.LibraryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineRunner.class);

    private final LibraryRepository libraryRepository;

    private final BookRepository bookRepository;

    public CommandLineRunner(LibraryRepository libraryRepository, BookRepository bookRepository) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {
        LOGGER.debug("Library Mock Data Loading...");

        Library library = new Library(1, "The District Central Library", "Coimbatore", "Yes");
        this.libraryRepository.save(library);

        Book book = new Book();
        book.setId(1);
        book.setIsbn("9781593275846");
        book.setTitle("Eloquent JavaScript, Second Edition");
        book.setSubtitle("A Modern Introduction to Programming");
        book.setAuthor("Marijn Haverbeke");
        book.setPublisher("publisher");
        book.setPages(472L);
        book.setLibrary(library);

        this.bookRepository.save(book);

        Book book1 = new Book();
        book1.setId(2);
        book1.setIsbn("9781491904244");
        book1.setTitle("You Don't Know JS");
        book1.setSubtitle("ES6 & Beyond");
        book1.setAuthor("Kyle Simpson");
        book1.setPublisher("O'Reilly Media");
        book1.setPages(278L);
        book1.setLibrary(library);

        this.bookRepository.save(book1);

        this.libraryRepository.save(new Library(2, "Yaal Library", "Coimbatore", "Yes"));
        this.libraryRepository.save(new Library(3, "Coimbatore Central Library", "Coimbatore", "Yes"));
        this.libraryRepository.save(new Library(4, "City Central Library, South Zone", "Bengaluru", "Yes"));
        this.libraryRepository.save(new Library(5, "Eloor Lending Libraries", "Bengaluru", "No"));

    }
}