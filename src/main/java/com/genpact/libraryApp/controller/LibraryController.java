package com.genpact.libraryApp.controller;

import com.genpact.libraryApp.domain.Library;
import com.genpact.libraryApp.exception.ResourceNotFoundException;
import com.genpact.libraryApp.repository.LibraryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class LibraryController {

    private final LibraryRepository libraryRepository;

    public LibraryController(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    @GetMapping("library")
    ResponseEntity<List<Library>> getAllLibrary() {
        return new ResponseEntity<>(this.libraryRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("library")
    ResponseEntity<Library> saveLibrary(@RequestBody Library library) {
        return new ResponseEntity<>(this.libraryRepository.save(library), HttpStatus.CREATED);
    }

    @PutMapping("library")
    ResponseEntity<Library> updateLibrary(@RequestBody Library library) {
        return new ResponseEntity<>(this.libraryRepository.saveAndFlush(library), HttpStatus.OK);
    }

    @DeleteMapping("library/{id}")
    ResponseEntity<Boolean> deleteLibrary(@PathVariable Long id) {
        Optional<Library> library = this.libraryRepository.findById(id);
        if (library.isPresent()) {
            this.libraryRepository.deleteById(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Library Not Found");
        }
    }
}
