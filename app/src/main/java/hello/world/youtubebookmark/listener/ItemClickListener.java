package hello.world.youtubebookmark.listener;

import hello.world.youtubebookmark.db.list.adapter.BookEntity;

public interface ItemClickListener {
    void OnItemClickListener(int position, BookEntity bookEntity);
}
