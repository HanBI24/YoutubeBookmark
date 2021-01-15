package hello.world.youtubebookmark.db.list.like;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import hello.world.youtubebookmark.db.list.adapter.BookEntity;

@Entity(tableName = "book_like_table")
public class BookEntityLike {
    @PrimaryKey(autoGenerate = true)
    private int id1;
    private boolean is_like;

    public BookEntityLike(boolean is_like) {
        this.is_like = is_like;
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public boolean isIs_like() {
        return is_like;
    }

    public void setIs_like(boolean is_like) {
        this.is_like = is_like;
    }

    @Override
    public String toString() {
        return "BookEntityLike{" +
                "id=" + id1 +
                ", is_like=" + is_like +
                '}';
    }
}
