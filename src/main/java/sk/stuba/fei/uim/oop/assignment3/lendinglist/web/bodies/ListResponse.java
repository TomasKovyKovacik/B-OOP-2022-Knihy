package sk.stuba.fei.uim.oop.assignment3.lendinglist.web.bodies;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookResponse;
import sk.stuba.fei.uim.oop.assignment3.lendinglist.data.LendingList;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ListResponse {
    private long id;
    private List<BookResponse> lendingList;
    private boolean lended;

    public ListResponse(LendingList lendingList) {
        this.id = lendingList.getId();
        this.lended = lendingList.isLended();
        this.lendingList = lendingList.getList().stream().map(BookResponse::new).collect(Collectors.toList());
    }
}
