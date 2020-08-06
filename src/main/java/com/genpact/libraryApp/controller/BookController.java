package com.genpact.libraryApp.controller;

import com.genpact.libraryApp.domain.Book;
import com.genpact.libraryApp.domain.Library;
import com.genpact.libraryApp.exception.ResourceNotFoundException;
import com.genpact.libraryApp.repository.BookRepository;
import com.genpact.libraryApp.repository.LibraryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class BookController {

    private final BookRepository bookRepository;

    private final LibraryRepository libraryRepository;

    public BookController(BookRepository bookRepository, LibraryRepository libraryRepository) {
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
    }

    @GetMapping("libraries/{libraryId}/books")
    ResponseEntity<List<Book>> getAllBooksByLibraryId(@PathVariable Long libraryId) {
        return new ResponseEntity<>(this.bookRepository.findByLibraryId(libraryId), HttpStatus.OK);
    }

    @PostMapping("libraries/{libraryId}/book")
    ResponseEntity<Book> saveBook(@PathVariable Long libraryId, @RequestBody Book book) {
        Library lib = this.libraryRepository.findById(libraryId)
                .orElseThrow(() -> new ResourceNotFoundException(""));
        book.setLibrary(lib);
        return new ResponseEntity<>(this.bookRepository.save(book), HttpStatus.OK);
    }


    @PutMapping("libraries/{libraryId}/book")
    ResponseEntity<Book> updateBook(@PathVariable Long libraryId, @RequestBody Book book) {
        if (!this.libraryRepository.existsById(libraryId)) {
            throw new ResourceNotFoundException("Library Not Found");
        }
        if (!this.bookRepository.existsById(book.getId())) {
            throw new ResourceNotFoundException("Book Not Found");
        }
        this.bookRepository.saveAndFlush(book);
        return new ResponseEntity<>(this.bookRepository.saveAndFlush(book), HttpStatus.OK);
    }

    @DeleteMapping("libraries/{libraryId}/book/{bookId}")
    ResponseEntity<Void> deleteBook(@PathVariable Long libraryId, @PathVariable Long bookId) {
        if (!this.libraryRepository.existsById(libraryId)) {
            throw new ResourceNotFoundException("Library Not Found");
        }
        Optional<Book> book = this.bookRepository.findById(bookId);
        if (book.isPresent()) {
            this.bookRepository.deleteById(bookId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("Book Not Found");
        }
    }

}
