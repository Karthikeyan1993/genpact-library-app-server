package com.genpact.libraryApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genpact.libraryApp.controller.LibraryController;
import com.genpact.libraryApp.domain.Book;
import com.genpact.libraryApp.repository.BookRepository;
import com.genpact.libraryApp.repository.LibraryRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryRepository libraryRepository;

    @MockBean
    private BookRepository bookRepository;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new LibraryController(this.libraryRepository)).build();
    }

    List<Book> getMockData(){

        List<Book> data = new ArrayList<>();

        Book book = new Book();
        book.setId(1);
        book.setIsbn("9781593275846");
        book.setTitle("Eloquent JavaScript, Second Edition");
        book.setSubtitle("A Modern Introduction to Programming");
        book.setAuthor("Marijn Haverbeke");
        book.setPublisher("publisher");
        book.setPages(472L);

        Book book1 = new Book();
        book1.setId(2);
        book1.setIsbn("9781491904244");
        book1.setTitle("You Don't Know JS");
        book1.setSubtitle("ES6 & Beyond");
        book1.setAuthor("Kyle Simpson");
        book1.setPublisher("O'Reilly Media");
        book1.setPages(278L);

        data.add(book);
        data.add(book1);
    return  data;
    }

    @Test
    public void shouldFetchAllLibrary() throws Exception {
        Mockito.when(this.bookRepository.findByLibraryId(1L)).thenReturn(this.getMockData());
        mockMvc.perform(MockMvcRequestBuilders.get("/libraries/{libraryId}/books",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(this.getMockData().size())))
                .andReturn();
        Mockito.verify(this.bookRepository).findByLibraryId(1L);
    }

}
