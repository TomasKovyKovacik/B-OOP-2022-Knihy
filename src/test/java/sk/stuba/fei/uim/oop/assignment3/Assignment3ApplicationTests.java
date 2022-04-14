package sk.stuba.fei.uim.oop.assignment3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.transaction.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class Assignment3ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddAuthor() throws Exception {
        addAuthor();
    }

    @Test
    void testAddBook() throws Exception {
        addBook();
    }

    @Test
    void testGetAllBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        addBook();
        addBook();
        mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 2 + stringToObject(result, ArrayList.class).size();
        });
    }

    @Test
    void testGetAllAuthors() throws Exception {
        MvcResult result = mockMvc.perform(get("/author")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        addAuthor();
        addAuthor();
        mockMvc.perform(get("/author")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 2 + stringToObject(result, ArrayList.class).size();
        });
    }

    @Test
    void testGetBookById() throws Exception {
        TestBookResponse book = addBook();
        mockMvc.perform(get("/book/" + book.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestBookResponse bookToControl = stringToObject(mvcResult, TestBookResponse.class);
                    assert Objects.equals(bookToControl.getId(), book.getId());
                });
    }

    @Test
    void testGetAuthorById() throws Exception {
        TestAuthorResponse author = addAuthor();
        mockMvc.perform(get("/author/" + author.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestAuthorResponse authorToControl = stringToObject(mvcResult, TestAuthorResponse.class);
                    assert Objects.equals(authorToControl.getId(), author.getId());
                });
    }

    @Test
    void testGetMissingBookById() throws Exception {
        TestBookResponse book = addBook();
        mockMvc.perform(get("/book/" + (book.getId() + 1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetMissingAuthorById() throws Exception {
        TestAuthorResponse author = addAuthor();
        mockMvc.perform(get("/author/" + (author.getId() + 1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateBook() throws Exception {
        TestBookResponse book = addBook();
        TestBookRequest update1 = new TestBookRequest();
        update1.name = "updated name";
        TestBookRequest update2 = new TestBookRequest();
        update2.description = "updated description";
        TestBookRequest update3 = new TestBookRequest();
        TestAuthorResponse a = addAuthor();
        update3.author = a.getId();
        TestBookRequest update4 = new TestBookRequest();
        update4.pages = 150;
        mockMvc.perform(put("/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(update1)))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestBookResponse response = stringToObject(mvcResult, TestBookResponse.class);
                    assert Objects.equals(response.getName(), update1.getName());
                    assert Objects.equals(response.getDescription(), book.getDescription());
                    assert Objects.equals(response.getAuthor(), book.getAuthor());
                    assert Objects.equals(response.getPages(), book.getPages());
                });
        mockMvc.perform(put("/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(update2)))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestBookResponse response = stringToObject(mvcResult, TestBookResponse.class);
                    assert Objects.equals(response.getName(), update1.getName());
                    assert Objects.equals(response.getDescription(), update2.getDescription());
                    assert Objects.equals(response.getAuthor(), book.getAuthor());
                    assert Objects.equals(response.getPages(), book.getPages());
                });
        mockMvc.perform(put("/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(update3)))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestBookResponse response = stringToObject(mvcResult, TestBookResponse.class);
                    assert Objects.equals(response.getName(), update1.getName());
                    assert Objects.equals(response.getDescription(), update2.getDescription());
                    assert Objects.equals(response.getAuthor().getId(), update3.getAuthor());
                    assert Objects.equals(response.getPages(), book.getPages());
                });
        mockMvc.perform(put("/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(update4)))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestBookResponse response = stringToObject(mvcResult, TestBookResponse.class);
                    assert Objects.equals(response.getName(), update1.getName());
                    assert Objects.equals(response.getDescription(), update2.getDescription());
                    assert Objects.equals(response.getAuthor().getId(), update3.getAuthor());
                    assert Objects.equals(response.getPages(), update4.getPages());
                });
    }

    @Test
    void testUpdateAuthor() throws Exception {
        TestAuthorResponse author = addAuthor();
        TestAuthor update1 = new TestAuthor();
        update1.name = "updated name";
        TestAuthor update2 = new TestAuthor();
        update2.surname = "updated surname";
        mockMvc.perform(put("/author/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(update1)))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestAuthorResponse response = stringToObject(mvcResult, TestAuthorResponse.class);
                    assert Objects.equals(response.getName(), update1.getName());
                    assert Objects.equals(response.getSurname(), author.getSurname());
                });
        mockMvc.perform(put("/author/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(update2)))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestAuthorResponse response = stringToObject(mvcResult, TestAuthorResponse.class);
                    assert Objects.equals(response.getName(), update1.getName());
                    assert Objects.equals(response.getSurname(), update2.getSurname());
                });
    }

    @Test
    void testUpdateMissingBook() throws Exception {
        TestBookResponse product = addBook();
        mockMvc.perform(put("/book/" + (product.getId() + 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(new TestBookRequest())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateMissingAuthor() throws Exception {
        TestAuthorResponse product = addAuthor();
        mockMvc.perform(put("/author/" + (product.getId() + 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(new TestAuthor())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBook() throws Exception {
        TestBookResponse book = addBook();
        mockMvc.perform(delete("/book/" + book.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/book/" + book.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAuthor() throws Exception {
        TestAuthorResponse author = addAuthor();
        mockMvc.perform(delete("/author/" + author.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/author/" + author.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMissingBook() throws Exception {
        TestBookResponse book = addBook();
        mockMvc.perform(delete("/book/" + (book.getId() + 1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMissingAuthor() throws Exception {
        TestAuthorResponse author = addAuthor();
        mockMvc.perform(delete("/author/" + (author.getId() + 1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBookAmount() throws Exception {
        TestBookResponse book = addBook();
        mockMvc.perform(get("/book/" + book.getId() + "/amount")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    Amount response = stringToObject(mvcResult, Amount.class);
                    assert Objects.equals(response.getAmount(), book.getAmount());
                });
    }

    @Test
    void testGetMissingBookAmount() throws Exception {
        TestBookResponse book = addBook();
        mockMvc.perform(get("/book/" + (book.getId() + 1) + "/amount")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testIncrementBookAmount() throws Exception {
        TestBookResponse book = addBook();
        Amount request = new Amount();
        request.setAmount(10);
        mockMvc.perform(post("/book/" + book.getId() + "/amount")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToString(request)))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    Amount response = stringToObject(mvcResult, Amount.class);
                    assert Objects.equals(response.getAmount(), book.getAmount() + request.getAmount());
                });
        mockMvc.perform(get("/book/" + book.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    TestBookResponse response = stringToObject(mvcResult, TestBookResponse.class);
                    assert Objects.equals(response.getAmount(), book.getAmount() + request.getAmount());
                });
    }

    @Test
    void testIncrementMissingBookAmount() throws Exception {
        TestBookResponse book = addBook();
        Amount request = new Amount();
        request.setAmount(10);
        mockMvc.perform(post("/book/" + (book.getId() + 1) + "/amount")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBookLendCount() throws Exception {
        TestBookResponse book = addBook();
        mockMvc.perform(get("/book/" + book.getId() + "/lendCount")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    Amount response = stringToObject(mvcResult, Amount.class);
                    assert Objects.equals(response.getAmount(), book.getLendCount());
                });
    }

    @Test
    void testGetMissingBookLendCount() throws Exception {
        TestBookResponse book = addBook();
        mockMvc.perform(get("/book/" + (book.getId() + 1) + "/lendCount")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    /////////////////////////////////////////////////////// LENDING LISTS

    @Test
    void testAddShoppingCart() throws Exception {
//        addCart();
    }

    TestAuthorResponse addAuthor() throws Exception {
        return addAuthor("name", "surname", status().is2xxSuccessful());
    }

    TestAuthorResponse addAuthor(String name, String surname, ResultMatcher statusMatcher) throws Exception {
        TestAuthor author = new TestAuthor();
        author.setName(name);
        author.setSurname(surname);
        MvcResult mvcResult = mockMvc.perform(post("/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(author))
                ).andExpect(statusMatcher)
                .andDo(mvcResult1 -> {
                    TestAuthorResponse authorToControl = stringToObject(mvcResult1, TestAuthorResponse.class);
                    assert Objects.equals(author.getName(), authorToControl.getName());
                    assert Objects.equals(author.getSurname(), authorToControl.getSurname());
                })
                .andReturn();
        return stringToObject(mvcResult, TestAuthorResponse.class);
    }

    TestBookResponse addBook() throws Exception {
        TestAuthorResponse author = addAuthor();
        return addBook("name", "description", 100, 4, 2, author.getId(), status().is2xxSuccessful());
    }

    TestBookResponse addBook(String name, String description, int pages, int amount, int lendCount, long author, ResultMatcher statusMatcher) throws Exception {
        TestBookRequest book = new TestBookRequest();
        book.setName(name);
        book.setDescription(description);
        book.setPages(pages);
        book.setAmount(amount);
        book.setLendCount(lendCount);
        book.setAuthor(author);
        MvcResult mvcResult = mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(book))
                ).andExpect(statusMatcher)
                .andDo(mvcResult1 -> {
                    TestBookResponse bookToControl = stringToObject(mvcResult1, TestBookResponse.class);
                    assert Objects.equals(book.getName(), bookToControl.getName());
                    assert Objects.equals(book.getDescription(), bookToControl.getDescription());
                    assert Objects.equals(book.getPages(), bookToControl.getPages());
                    assert Objects.equals(book.getAmount(), bookToControl.getAmount());
                    assert Objects.equals(book.getLendCount(), bookToControl.getLendCount());
                    assert Objects.equals(book.getAuthor(), bookToControl.getAuthor().getId());
                })
                .andReturn();
        return stringToObject(mvcResult, TestBookResponse.class);
    }

    static String objectToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <K> K stringToObject(MvcResult object, Class<K> objectClass) throws UnsupportedEncodingException, JsonProcessingException {
        return new ObjectMapper().readValue(object.getResponse().getContentAsString(), objectClass);
    }

    @Getter
    @Setter
    private static class Amount {
        protected int amount;
    }

    @Getter
    @Setter
    private static class TestBookRequest extends Amount {
        protected String name;
        protected String description;
        protected long author;
        protected int pages;
        protected int lendCount;
    }

    @Getter
    @Setter
    private static class TestBookResponse extends Amount {
        protected TestAuthorResponse author;
        protected long id;
        protected String name;
        protected String description;
        protected int pages;
        protected int lendCount;
    }

    @Getter
    @Setter
    private static class TestAuthor {
        protected String name;
        protected String surname;
    }

    @Getter
    @Setter
    private static class TestAuthorResponse extends TestAuthor {
        protected long id;
        protected List<TestBookResponse> books;
    }
}
