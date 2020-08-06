package com.genpact.libraryApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genpact.libraryApp.controller.LibraryController;
import com.genpact.libraryApp.domain.Library;
import com.genpact.libraryApp.repository.BookRepository;
import com.genpact.libraryApp.repository.LibraryRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.BDDMockito.given;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryRepository libraryRepository;

    @MockBean
    private BookRepository bookRepository;

    private static final ObjectMapper mapper = new ObjectMapper();


    List<Library> getMockData() {
        List<Library> libraries = new ArrayList<>();
        libraries.add(new Library(1, "The District Central Library", "Coimbatore", "Yes"));
        libraries.add(new Library(2, "Yaal Library", "Coimbatore", "Yes"));
        libraries.add(new Library(1, "Coimbatore Central Library", "Coimbatore", "Yes"));
        return libraries;
    }


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new LibraryController(this.libraryRepository)).build();
    }


    @Test
    public void shouldFetchAllLibrary() throws Exception {
        Mockito.when(this.libraryRepository.findAll()).thenReturn(this.getMockData());
        mockMvc.perform(MockMvcRequestBuilders.get("/library"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(this.getMockData().size())))
                .andReturn();
        Mockito.verify(this.libraryRepository).findAll();
    }

    @Test
    public void shouldSaveLibrary() throws Exception {
        Library library = new Library(1, "The District Central Library", "Coimbatore", "Yes");
        Mockito.when(this.libraryRepository.save(ArgumentMatchers.any())).thenReturn(library);
        String json = mapper.writeValueAsString(library);
        mockMvc.perform(MockMvcRequestBuilders.post("/library").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.equalTo("The District Central Library")))
                .andExpect(jsonPath("$.location", Matchers.equalTo("Coimbatore")))
                .andExpect(jsonPath("$.isOpened", Matchers.equalTo("Yes")));
    }

    @Test
    public void shouldUpdateLibrary() throws Exception {
        Library library = new Library(2, "The District Central Library", "Coimbatore", "Yes");
        Mockito.when(this.libraryRepository.saveAndFlush(ArgumentMatchers.any())).thenReturn(library);
        String json = mapper.writeValueAsString(library);
        mockMvc.perform(MockMvcRequestBuilders.put("/library").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(2)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("The District Central Library")))
                .andExpect(jsonPath("$.location", Matchers.equalTo("Coimbatore")))
                .andExpect(jsonPath("$.isOpened", Matchers.equalTo("Yes")));
    }

    @Test
    public void shouldDeleteLibrary() throws Exception {
        final Long libraryId = 1L;
        Library library = new Library(1L, "The District Central Library", "Coimbatore", "Yes");
        given(this.libraryRepository.findById(libraryId)).willReturn(Optional.of(library));
        doNothing().when(this.libraryRepository).deleteById(library.getId());
        mockMvc.perform(MockMvcRequestBuilders.delete("/library/{id}", library.getId()))
                .andExpect(status().isOk());
    }

}
