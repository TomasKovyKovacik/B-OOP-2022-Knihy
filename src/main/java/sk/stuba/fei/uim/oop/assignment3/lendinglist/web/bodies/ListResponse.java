package sk.stuba.fei.uim.oop.assignment3.lendinglist.web.bodies;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.assignment3.book.data.Book;
import sk.stuba.fei.uim.oop.assignment3.lendinglist.data.LendingList;

import java.util.List;

@Getter
@Setter
public class ListResponse {
    private long id;
    private List<Book> lendingList;
    private boolean lended;

    public ListResponse(LendingList lendingList) {
        this.id = lendingList.getId();
        this.lended = lendingList.isLended();
        this.lendingList = lendingList.getList();
    }
}
