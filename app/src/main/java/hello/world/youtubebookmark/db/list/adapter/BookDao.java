package hello.world.youtubebookmark.db.list.adapter;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {

    @Query("SELECT * FROM book_table ORDER BY id DESC")
    LiveData<List<BookEntity>> getAll();

    @Insert
    void insert(BookEntity bookEntity);

    @Update
    void update(BookEntity bookEntity);

    @Delete
    void delete(BookEntity bookEntity);

    @Query("DELETE FROM book_table")
    void deleteAll();

    @Query("SELECT * FROM book_table WHERE title LIKE :searchQuery")
    LiveData<List<BookEntity>> searchDb(String searchQuery);

//    @Query("SELECT * FROM book_table ORDER BY id DESC")
//    LiveData<List<BookEntity>> setSortDescId();

}
