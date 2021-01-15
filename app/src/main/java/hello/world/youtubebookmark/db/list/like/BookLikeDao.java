package hello.world.youtubebookmark.db.list.like;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import hello.world.youtubebookmark.db.list.adapter.BookEntity;

@Dao
public interface BookLikeDao {

    @Query("SELECT * FROM book_like_table ORDER BY id1 DESC")
    LiveData<List<BookEntityLike>> getAll();

    @Insert
    void insert(BookEntityLike bookEntityLike);

    @Update
    void update(BookEntityLike bookEntityLike);

    @Delete
    void delete(BookEntityLike bookEntityLike);

    @Query("DELETE FROM book_like_table")
    void deleteAll();
}
