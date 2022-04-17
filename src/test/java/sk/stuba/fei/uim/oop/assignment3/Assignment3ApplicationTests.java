package sk.stuba.fei.uim.oop.assignment3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.transaction.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    void testAddAuthor201() throws Exception {
        addAuthor("name", "surname", status().isCreated());
    }

    @Test
    void testAddBook() throws Exception {
        addBook();
    }

    @Test
    void testAddBook201() throws Exception {
        TestAuthorResponse author = addAuthor();
        addBook("name", "desc", 10, 5, 0, author.getId(), status().isCreated());
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
                    assert Objects.equals(response.getAuthor(), update3.getAuthor());
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
                    assert Objects.equals(response.getAuthor(), update3.getAuthor());
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

    @Test
    void deletedBookRemovedFromAuthor() throws Exception {
        TestAuthorResponse author = addAuthor();
        TestBookResponse book1 = addBook(author.getId());
        TestBookResponse book2 = addBook(author.getId());

        mockMvc.perform(get("/author/" + author.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestAuthorResponse authorToControl = stringToObject(mvcResult, TestAuthorResponse.class);
                    assert authorToControl.books.size() == 2;
                    assert authorToControl.books.contains(book1.getId());
                    assert authorToControl.books.contains(book2.getId());
                });

        mockMvc.perform(delete("/book/" + book1.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/author/" + author.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestAuthorResponse authorToControl = stringToObject(mvcResult, TestAuthorResponse.class);
                    assert authorToControl.books.size() == 1;
                    assert authorToControl.books.contains(book2.getId());
                });
    }

    @Test
    void deletingAuthorRemovesBooks() throws Exception {
        TestAuthorResponse author = addAuthor();
        TestBookResponse book1 = addBook(author.getId());
        TestBookResponse book2 = addBook(author.getId());

        mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            List<Map<String, Object>> list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 2;
            var ids = list.stream().map(b -> Long.valueOf((Integer) b.get("id"))).collect(Collectors.toSet());
            assert ids.contains(book1.getId());
            assert ids.contains(book2.getId());
        });

        mockMvc.perform(delete("/author/" + author.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/book/" + book1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/book/" + book2.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /////////////////////////////////////////////////////// LENDING LISTS

    @Test
    void testAddShoppingCart() throws Exception {
        addList();
    }

    @Test
    void testAddShoppingCart201() throws Exception {
        addList(status().isCreated());
    }

    @Test
    void testGetListById() throws Exception {
        TestListResponse list = addList();
        MvcResult mvcResult = mockMvc.perform(get("/list/" + list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        TestListResponse listResponse = stringToObject(mvcResult, TestListResponse.class);
        assert Objects.equals(listResponse.getId(), list.getId());
    }

    @Test
    void testGetMissingListById() throws Exception {
        TestListResponse list = addList();
        mockMvc.perform(get("/list/" + list.getId() + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void testGetAllLists() throws Exception {
        MvcResult result = mockMvc.perform(get("/list")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        addList();
        addList();
        mockMvc.perform(get("/list")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 2 + stringToObject(result, ArrayList.class).size();
        });
    }

    @Test
    void testDeleteListById() throws Exception {
        TestListResponse list = addList();
        mockMvc.perform(delete("/list/" + list.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockMvc.perform(get("/list/" + list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMissingListById() throws Exception {
        TestListResponse list = addList();
        mockMvc.perform(delete("/list/" + list.getId() + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void addBookToList() throws Exception {
        TestListResponse list = addList();
        TestBookResponse book = addBook();
        assert list.getLendingList().isEmpty();
        TestListResponse updatedList = addBookToList(book, list);
        assert updatedList.getLendingList().size() == 1;
        assert updatedList.getLendingList().get(0).getId() == book.getId();
    }

    @Test
    void addBookToListMissingBook() throws Exception {
        TestListResponse list = addList();
        TestBookResponse book = addBook();
        addBookToList(book.getId() + 1, list.getId(), status().isNotFound());
    }

    @Test
    void addBookToListMissingList() throws Exception {
        TestListResponse list = addList();
        TestBookResponse book = addBook();
        addBookToList(book.getId() , list.getId() + 1, status().isNotFound());
    }

    @Test
    void addBookToListExistingBook() throws Exception {
        TestListResponse list = addList();
        TestBookResponse book = addBook();
        addBookToList(book, list);
        addBookToList(book.getId() , list.getId() , status().isBadRequest());

        MvcResult mvcResult = mockMvc.perform(get("/list/" + list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        TestListResponse listResponse = stringToObject(mvcResult, TestListResponse.class);

        assert listResponse.getLendingList().size() == 1;
        assert listResponse.getLendingList().get(0).getId() == book.getId();
        assert listResponse.getLendingList().get(0).getLendCount() == book.getLendCount();
    }

    @Test
    void removeBookFromList() throws Exception {
        TestListResponse list = addList();
        TestBookResponse book = addBook();
        assert list.getLendingList().isEmpty();
        TestListResponse updatedList = addBookToList(book, list);
        assert updatedList.getLendingList().size() == 1;
        assert updatedList.getLendingList().get(0).getId() == book.getId();

        TestBookIdRequest bookID = new TestBookIdRequest();
        bookID.setId(book.getId());
        mockMvc.perform(delete("/list/" + list.getId() + "/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(bookID)))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/list/" + list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        TestListResponse listResponse = stringToObject(mvcResult, TestListResponse.class);

        assert listResponse.getLendingList().size() == 0;
    }

    @Test
    void testLendList() throws Exception {
        TestListResponse list = addList();
        TestBookResponse book = addBook();
        TestBookResponse book2 = addBook();
        addBookToList(book, list);
        addBookToList(book2, list);
        mockMvc.perform(get("/list/" + list.getId() + "/lend")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/list/" + list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        TestListResponse listResponse = stringToObject(mvcResult, TestListResponse.class);

        assert listResponse.getLendingList().size() == 2;
        assert listResponse.getLendingList().get(0).getId() == book.getId();
        assert listResponse.getLendingList().get(1).getId() == book2.getId();
        assert listResponse.getLendingList().get(0).getLendCount() == book.getLendCount() + 1;
        assert listResponse.getLendingList().get(1).getLendCount() == book2.getLendCount() + 1;
        assert listResponse.isLended();
    }

    @Test
    void testLendMissingList() throws Exception {
        TestListResponse list = addList();
        TestBookResponse book = addBook();
        addBookToList(book, list);
        mockMvc.perform(get("/list/" + (list.getId() + 1) + "/lend")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void testLendListTwice() throws Exception {
        TestListResponse list = addList();
        TestBookResponse book = addBook();
        addBookToList(book, list);
        mockMvc.perform(get("/list/" + list.getId() + "/lend")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockMvc.perform(get("/list/" + list.getId() + "/lend")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void addBookToLendedList() throws Exception {
        TestListResponse list = addList();
        TestBookResponse book = addBook();
        addBookToList(book, list);
        mockMvc.perform(get("/list/" + list.getId() + "/lend")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        TestBookResponse book2 = addBook();
        addBookToList(book2.getId() , list.getId() , status().isBadRequest());

        mockMvc.perform(get("/book/" + book.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestBookResponse bookToControl = stringToObject(mvcResult, TestBookResponse.class);
                    assert Objects.equals(bookToControl.getId(), book.getId());
                    assert bookToControl.getLendCount() == book.getLendCount() + 1;
                });
        mockMvc.perform(get("/book/" + book2.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestBookResponse bookToControl = stringToObject(mvcResult, TestBookResponse.class);
                    assert Objects.equals(bookToControl.getId(), book2.getId());
                    assert bookToControl.getLendCount() == book2.getLendCount();
                });
        MvcResult mvcResult = mockMvc.perform(get("/list/" + list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        TestListResponse listResponse = stringToObject(mvcResult, TestListResponse.class);

        assert listResponse.getLendingList().size() == 1;
    }

    @Test
    void testRemoveUnlendedList() throws Exception {
        TestListResponse list = addList();
        TestBookResponse book = addBook();
        TestBookResponse book2 = addBook();
        addBookToList(book, list);
        TestListResponse listResponse = addBookToList(book2, list);

        assert listResponse.getLendingList().size() == 2;
        assert listResponse.getLendingList().get(0).getId() == book.getId();
        assert listResponse.getLendingList().get(1).getId() == book2.getId();

        mockMvc.perform(delete("/list/" + list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        mockMvc.perform(get("/list/" + list.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void testRemoveLendedList() throws Exception {
        TestListResponse list = addList();
        TestBookResponse book = addBook();
        TestBookResponse book2 = addBook();
        addBookToList(book, list);
        addBookToList(book2, list);

        mockMvc.perform(get("/list/" + list.getId() + "/lend")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(get("/list/" + list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        TestListResponse listResponse = stringToObject(mvcResult, TestListResponse.class);

        assert listResponse.getLendingList().size() == 2;
        assert listResponse.getLendingList().get(0).getId() == book.getId();
        assert listResponse.getLendingList().get(1).getId() == book2.getId();
        assert listResponse.getLendingList().get(0).getLendCount() == book.getLendCount() + 1;
        assert listResponse.getLendingList().get(1).getLendCount() == book2.getLendCount() + 1;

        mockMvc.perform(delete("/list/" + list.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        mockMvc.perform(get("/book/" + book.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult2 -> {
                    TestBookResponse bookToControl = stringToObject(mvcResult2, TestBookResponse.class);
                    assert Objects.equals(bookToControl.getId(), book.getId());
                    assert bookToControl.getLendCount() == book.getLendCount();
                });
        mockMvc.perform(get("/book/" + book2.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult2 -> {
                    TestBookResponse bookToControl = stringToObject(mvcResult2, TestBookResponse.class);
                    assert Objects.equals(bookToControl.getId(), book2.getId());
                    assert bookToControl.getLendCount() == book2.getLendCount();
                });
    }

    ///////////////////////////////////////////////////////////

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
        return addBook(author.getId());
    }

    TestBookResponse addBook(long authorId) throws Exception {
        return addBook("name", "description", 100, 4, 2, authorId, status().is2xxSuccessful());
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
                    assert Objects.equals(book.getAuthor(), bookToControl.getAuthor());
                })
                .andReturn();
        return stringToObject(mvcResult, TestBookResponse.class);
    }

    TestListResponse addList() throws Exception {
        return addList(status().is2xxSuccessful());
    }

    TestListResponse addList(ResultMatcher statusMatcher) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(statusMatcher)
                .andReturn();
        return stringToObject(mvcResult, TestListResponse.class);
    }

    TestListResponse addBookToList(TestBookResponse book, TestListResponse list) throws Exception {
        return addBookToList(book, list, status().isOk());
    }

    TestListResponse addBookToList(TestBookResponse book, TestListResponse list,  ResultMatcher statusMatcher) throws Exception {
        return addBookToList(book.getId(), list.getId(), statusMatcher);
    }

    TestListResponse addBookToList(long bookId, long listId, ResultMatcher statusMatcher) throws Exception {
        TestBookIdRequest book = new TestBookIdRequest();
        book.setId(bookId);
        MvcResult mvcResult = mockMvc.perform(post("/list/" + listId + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(book)))
                .andExpect(statusMatcher)
                .andReturn();
        if (mvcResult.getResponse().getStatus() == HttpStatus.OK.value()) {
            return stringToObject(mvcResult, TestListResponse.class);
        }
        return null;
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
        protected long author;
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
        protected List<Long> books;
    }

    @Getter
    @Setter
    private static class TestListResponse {
        private long id;
        private List<TestBookResponse> lendingList;
        private boolean lended;
    }

    @Getter
    @Setter
    public static class TestBookIdRequest {
        private long id;
    }

}
