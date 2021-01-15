package hello.world.youtubebookmark.db.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import hello.world.youtubebookmark.db.list.adapter.BookDao;
import hello.world.youtubebookmark.db.list.adapter.BookEntity;
import hello.world.youtubebookmark.db.list.like.BookEntityLike;
import hello.world.youtubebookmark.db.list.like.BookLikeDao;

@Database(entities = {BookEntity.class, BookEntityLike.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
    public abstract BookLikeDao bookLikeDao();

    private static volatile BookDatabase INSTANCE;

    public static BookDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BookDatabase.class,
                            "book_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
