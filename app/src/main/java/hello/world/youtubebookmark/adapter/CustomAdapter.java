package hello.world.youtubebookmark.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hello.world.youtubebookmark.R;
import hello.world.youtubebookmark.db.database.BookDatabase;
import hello.world.youtubebookmark.db.list.adapter.BookEntity;
import hello.world.youtubebookmark.db.list.like.BookEntityLike;
import hello.world.youtubebookmark.listener.ItemClickListener;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private List<BookEntity> mListAdapter = new ArrayList<>();
    private List<BookEntityLike> mListLike = new ArrayList<>();
    private final BookDatabase db;
    private int idx;
    ItemClickListener itemClickListener;
    Context context;
    int x, y;
    private int positionCheck = -1;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTitle;
        protected TextView mTime;
        protected TextView mRemove;
        protected ImageView mThumb;
        protected Button mLinkBtn;
        protected ToggleButton mToggleBtn;
        protected CardView cardView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mTitle = itemView.findViewById(R.id.list_title);
            this.mTime = itemView.findViewById(R.id.save_time);
            this.mRemove = itemView.findViewById(R.id.remove);
            this.mThumb = itemView.findViewById(R.id.thumb_nail);
            this.mLinkBtn = itemView.findViewById(R.id.start_link);
            this.mToggleBtn = itemView.findViewById(R.id.list_like);
            this.cardView = itemView.findViewById(R.id.card);
        }
    }

    public CustomAdapter(BookDatabase db, Context context) {
        this.db = db;
        this.context = context;
    }

    // Create item
    public void setItemAdapterList(List<BookEntity> data) {
        mListAdapter = data;
        notifyDataSetChanged();
    }

    public void setItemLikeList(List<BookEntityLike> data) {
        mListLike = data;
        notifyDataSetChanged();
    }

    // Update item
    public void updateItem(BookEntity dictionaryEntity) {
        new Thread(() -> {
            mListAdapter.get(idx).setTitle(dictionaryEntity.getTitle());
            mListAdapter.get(idx).setTime(dictionaryEntity.getTime());
            mListAdapter.get(idx).setShared_text(dictionaryEntity.getShared_text());
            mListAdapter.get(idx).setImg_url(dictionaryEntity.getImg_url());

            db.bookDao().update(mListAdapter.get(idx));
        }).start();
    }

    public void insert(BookEntity bookEntity) {
        new Thread(() -> {
            db.bookDao().insert(bookEntity);
            mListAdapter.add(bookEntity);
        }).start();
    }

    // Delete all items
    public void deleteAll() {
        new Thread(() -> {
            mListAdapter.clear();
            mListLike.clear();
            db.bookDao().deleteAll();
            db.bookLikeDao().deleteAll();
        }).start();
    }

    public void getXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void insertLike(BookEntityLike bookEntityLike) {
        new Thread(() -> {
            mListLike.add(bookEntityLike);
            db.bookLikeDao().insert(bookEntityLike);
        }).start();
    }

    public void insertDESC(BookEntity bookEntity) {

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_shape, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        boolean isAnimChecked = true;

        holder.mTitle.setText(mListAdapter.get(position).getTitle());
        holder.mTime.setText(mListAdapter.get(position).getTime());
        Picasso.get()
                .load(mListAdapter.get(position).getThumb_url())
                .placeholder(R.drawable.ic_baseline_photo_24)
                .error(R.drawable.ic_baseline_error_outline_24)
                .fit()
                .into(holder.mThumb);

        final BookEntity bookEntity = mListAdapter.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.OnItemClickListener(position, bookEntity);
                idx = position;
            }
        });

        holder.mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, mListAdapter.get(position).getTitle()+context.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                new Thread(() -> {
                    BookEntity dict = mListAdapter.remove(holder.getAdapterPosition());
                    BookEntityLike bookEntityLike = mListLike.remove(holder.getAdapterPosition());
                    db.bookDao().delete(dict);
                    db.bookLikeDao().delete(bookEntityLike);
                }).start();
            }
        });

        holder.mLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, mListAdapter.get(position).getTitle()+context.getString(R.string.go_youtube_recycle), Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(mListAdapter.get(position).getImg_url());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        holder.mToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.mToggleBtn.isChecked()) {
                    new Thread(() -> {
                        mListLike.get(position).setIs_like(true);
                        db.bookLikeDao().update(mListLike.get(position));
                    }).start();

                } else {
                    new Thread(() -> {
                        mListLike.get(position).setIs_like(false);
                        db.bookLikeDao().update(mListLike.get(position));
                    }).start();
                }
            }
        });
        holder.mToggleBtn.setChecked(mListLike.get(position).isIs_like());

        int tempTime = Integer.parseInt(mListAdapter.get(position).getTime());
        Log.d("temp_time", Integer.toString(tempTime));
        int min = tempTime / 60;
        int hour = min / 60;
        int sec = tempTime % 60;
        min %= 60;
        String time = hour + context.getString(R.string.hour_text)+" " + min + context.getString(R.string.min)+" "
                + sec + context.getString(R.string.sec);
        holder.mTime.setText(time);

        if(holder.getAdapterPosition() > positionCheck) {
            holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.recycler_anim));
            positionCheck = holder.getAdapterPosition();
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return mListAdapter.size();
    }
}
