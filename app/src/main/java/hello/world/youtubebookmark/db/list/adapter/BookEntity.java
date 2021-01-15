package hello.world.youtubebookmark.db.list.adapter;

import android.widget.ImageView;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book_table")
public class BookEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String time;
    private String img_url;
    private String thumb_url;
    private String shared_text;

    public BookEntity(String title, String time, String img_url, String thumb_url, String shared_text) {
        this.title = title;
        this.time = time;
        this.img_url = img_url;
        this.thumb_url = thumb_url;
        this.shared_text = shared_text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getShared_text() {
        return shared_text;
    }

    public void setShared_text(String shared_text) {
        this.shared_text = shared_text;
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
