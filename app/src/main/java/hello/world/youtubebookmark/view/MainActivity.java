package hello.world.youtubebookmark.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import hello.world.youtubebookmark.R;
import hello.world.youtubebookmark.adapter.CustomAdapter;
import hello.world.youtubebookmark.db.database.BookDatabase;
import hello.world.youtubebookmark.db.list.adapter.BookEntity;
import hello.world.youtubebookmark.db.list.like.BookEntityLike;
import hello.world.youtubebookmark.listener.ItemClickListener;

public class MainActivity extends AppCompatActivity implements androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private CustomAdapter mAdapter;
    private BookDatabase db;
    private Button addListBtn, cancelBtn;
    private EditText a_etMin, a_etSec, a_etTitle, a_etHour;
    private ImageView a_alertImg;
    private String titleList;
    private String timeUrl;
    private int time;
    private String sharedText;
    private BookEntity bookEntity;
    private BookEntityLike bookEntityLike;
    private SharedPreferences sp;
    public boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolBar);

        // Change to Preference Manager in androidx.
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        isChecked = sp.getBoolean("save", false);

        if(!isChecked) {
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish();
        }

        RecyclerView mRecyclerView = findViewById(R.id.book_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        db = BookDatabase.getDatabase(MainActivity.this);
        mAdapter = new CustomAdapter(db, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        db.bookDao().getAll().observe(this, bookEntities -> mAdapter.setItemAdapterList(bookEntities));
        db.bookLikeDao().getAll().observe(this, bookEntityLikes -> mAdapter.setItemLikeList(bookEntityLikes));
        mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClickListener(int position, BookEntity bookEntity) {
                showModifiedList(position, bookEntity);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            showAddAlert();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("save", true);
        editor.apply();
    }

    private void showAddAlert() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.items_setting, null, false);
            builder.setTitle(R.string.add_btn).setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            Window window = dialog.getWindow();
            int x = (int) (size.x * 1.0f);
            int y = (int) (size.y * 0.6f);
            window.setLayout(x, y);
            mAdapter.getXY(x, y);

            a_alertImg = view.findViewById(R.id.alert_img);
            a_etMin = view.findViewById(R.id.et_min);
            a_etSec = view.findViewById(R.id.et_sec);
            a_etHour = view.findViewById(R.id.et_hour);
            a_etTitle = view.findViewById(R.id.et_title);

            sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            String[] thumbKeyword = sharedText.split("/");
            String thumbNailImgLink_temp = "https://img.youtube.com/vi/";
            String thumbNailLink = thumbNailImgLink_temp + thumbKeyword[thumbKeyword.length - 1] + "/mqdefault.jpg";
            showThumbNail(thumbNailLink, a_alertImg);
            Log.d("thumb_nail_onclick", thumbNailLink);

            a_etSec.setImeOptions(EditorInfo.IME_ACTION_DONE);
            a_etSec.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    setTime();

                    if (a_etTitle.getText().toString().trim().length() <= 0) {
                        titleList = getString(R.string.video);
                    } else {
                        titleList = a_etTitle.getText().toString();
                    }
                    bookEntity = new BookEntity(titleList, Integer.toString(time), timeUrl, thumbNailLink, sharedText);
                    bookEntityLike = new BookEntityLike(false);

                    mAdapter.insert(bookEntity);
                    mAdapter.insertLike(bookEntityLike);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    dialog.dismiss();
                    return false;
                }
            });

            addListBtn = view.findViewById(R.id.add_list_btn);
            addListBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTime();

                    if (a_etTitle.getText().toString().trim().length() <= 0) {
                        titleList = getString(R.string.video);
                    } else {
                        titleList = a_etTitle.getText().toString();
                    }
                    bookEntity = new BookEntity(titleList, Integer.toString(time), timeUrl, thumbNailLink, sharedText);
                    bookEntityLike = new BookEntityLike(false);

                    // Create test.
                    new Thread(()-> {
                    for(int i=0; i<1000; i++) {
                        mAdapter.insert(bookEntity);
                        mAdapter.insertLike(bookEntityLike);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    }).start();

//                    mAdapter.insert(bookEntity);
//                    mAdapter.insertLike(bookEntityLike);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    dialog.dismiss();
                }
            });

            cancelBtn = view.findViewById(R.id.cancel_btn);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    dialog.dismiss();
                }
            });
        }
    }

    private void showModifiedList(int position, final BookEntity bookEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.items_setting, null, false);
        builder.setTitle(R.string.modified).setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Window window = dialog.getWindow();
        int x = (int) (size.x * 1.0f);
        int y = (int) (size.y * 0.6f);
        window.setLayout(x, y);

        a_alertImg = view.findViewById(R.id.alert_img);
        a_etMin = view.findViewById(R.id.et_min);
        a_etSec = view.findViewById(R.id.et_sec);
        a_etHour = view.findViewById(R.id.et_hour);
        a_etTitle = view.findViewById(R.id.et_title);

        Picasso.get()
                .load(bookEntity.getThumb_url())
                .placeholder(R.drawable.ic_baseline_photo_24)
                .error(R.drawable.ic_baseline_error_outline_24)
                .resize(1200, 900)
                .onlyScaleDown()
                .into(a_alertImg);

        a_etTitle.setText(bookEntity.getTitle());

        sharedText = bookEntity.getShared_text();

        a_etSec.setImeOptions(EditorInfo.IME_ACTION_DONE);
        a_etSec.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                setTime();

                if (a_etTitle.getText().toString().trim().length() <= 0) {
                    titleList = getString(R.string.video);
                } else {
                    titleList = a_etTitle.getText().toString();
                }
                Log.d("Update_time", timeUrl);
                BookEntity newBookEntity = new BookEntity(titleList, Integer.toString(time), timeUrl, bookEntity.getThumb_url(), sharedText);
//                bookEntity = new BookEntity(titleList, Integer.toString(time), timeUrl, thumbNailLink);
                mAdapter.updateItem(newBookEntity);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                dialog.dismiss();
                return false;
            }
        });


        addListBtn = view.findViewById(R.id.add_list_btn);
        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();

                if (a_etTitle.getText().toString().trim().length() <= 0) {
                    titleList = getString(R.string.video);
                } else {
                    titleList = a_etTitle.getText().toString();
                }
                Log.d("Update_time", timeUrl);
                BookEntity newBookEntity = new BookEntity(titleList, Integer.toString(time), timeUrl, bookEntity.getThumb_url(), sharedText);
//                bookEntity = new BookEntity(titleList, Integer.toString(time), timeUrl, thumbNailLink);
                mAdapter.updateItem(newBookEntity);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                dialog.dismiss();
            }
        });

        cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            }
        });
    }

    private void setTime() {
        String stringTime;
        int min;
        int sec;
        int hour;
        if (a_etMin.getText().toString().trim().length() <= 0 &&
                a_etSec.getText().toString().trim().length() <= 0 &&
                a_etHour.getText().toString().trim().length() <= 0) {
            hour = 0;
            min = 0;
            sec = 0;
            time = hour + min + sec;
            stringTime = Integer.toString(time);
            timeUrl = sharedText + "?t=" + stringTime;
            Toast.makeText(getApplicationContext(),
                    R.string.dont_any_input,
                    Toast.LENGTH_SHORT).show();
        } else if (a_etMin.getText().toString().trim().length() <= 0 && a_etHour.getText().toString().trim().length() <= 0) {
            hour = 0;
            min = 0;
            sec = Integer.parseInt(a_etSec.getText().toString());
            time = hour + min + sec;
            stringTime = Integer.toString(time);
            timeUrl = sharedText + "?t=" + stringTime;
            Toast.makeText(getApplicationContext(),
                    R.string.dont_one,
                    Toast.LENGTH_SHORT).show();
        } else if (a_etSec.getText().toString().trim().length() <= 0 && a_etHour.getText().toString().trim().length() <= 0) {
            hour = 0;
            min = Integer.parseInt(a_etMin.getText().toString());
            sec = 0;
            min *= 60;
            time = hour + min + sec;
            stringTime = Integer.toString(time);
            timeUrl = sharedText + "?t=" + stringTime;
            Toast.makeText(getApplicationContext(),
                    R.string.dont_one,
                    Toast.LENGTH_SHORT).show();
        } else if (a_etMin.getText().toString().trim().length() <= 0 && a_etSec.getText().toString().trim().length() <= 0) {
            hour = Integer.parseInt(a_etHour.getText().toString());
            min = 0;
            sec = 0;
            hour *= 3600;
            time = hour + min + sec;
            stringTime = Integer.toString(time);
            timeUrl = sharedText + "?t=" + stringTime;
            Toast.makeText(getApplicationContext(),
                    R.string.dont_one,
                    Toast.LENGTH_SHORT).show();
        } else if (a_etSec.getText().toString().trim().length() <= 0) {
            hour = Integer.parseInt(a_etHour.getText().toString());
            min = Integer.parseInt(a_etMin.getText().toString());
            sec = 0;
            min *= 60;
            hour *= 3600;
            time = hour + min + sec;
            stringTime = Integer.toString(time);
            timeUrl = sharedText + "?t=" + stringTime;
            Toast.makeText(getApplicationContext(),
                    R.string.dont_one,
                    Toast.LENGTH_SHORT).show();
        } else if (a_etMin.getText().toString().trim().length() <= 0) {
            hour = Integer.parseInt(a_etHour.getText().toString());
            min = 0;
            sec = Integer.parseInt(a_etSec.getText().toString());
            hour *= 3600;
            time = hour + min + sec;
            stringTime = Integer.toString(time);
            timeUrl = sharedText + "?t=" + stringTime;
            Toast.makeText(getApplicationContext(),
                    R.string.dont_one,
                    Toast.LENGTH_SHORT).show();
        } else if (a_etHour.getText().toString().trim().length() <= 0) {
            hour = 0;
            min = Integer.parseInt(a_etMin.getText().toString());
            sec = Integer.parseInt(a_etSec.getText().toString());
            min *= 60;
            time = hour + min + sec;
            stringTime = Integer.toString(time);
            timeUrl = sharedText + "?t=" + stringTime;
            Toast.makeText(getApplicationContext(),
                    R.string.dont_one,
                    Toast.LENGTH_SHORT).show();
        } else {
            hour = Integer.parseInt(a_etHour.getText().toString());
            min = Integer.parseInt(a_etMin.getText().toString());
            sec = Integer.parseInt(a_etSec.getText().toString());
            hour *= 3600;
            min *= 60;
            time = hour + min + sec;
            stringTime = Integer.toString(time);
            timeUrl = sharedText + "?t=" + stringTime;
            Toast.makeText(getApplicationContext(),
                    (hour / 3600) + getString(R.string.hour_text)+" " + (min / 60) + getString(R.string.min)+" " + sec + getString(R.string.set_sec),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showThumbNail(String thumbNailLink, ImageView thumbView) {
        Log.d("thumb_nail_picasso", thumbNailLink);
        Picasso.get()
                .load(thumbNailLink)
                .placeholder(R.drawable.ic_baseline_photo_24)
                .error(R.drawable.ic_baseline_error_outline_24)
                .resize(1200, 900)
                .onlyScaleDown()
                .into(thumbView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_control, menu);

        MenuItem search = menu.findItem(R.id.search_list);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) search.getActionView();
        searchView.isSubmitButtonEnabled();
        searchView.setOnQueryTextListener((androidx.appcompat.widget.SearchView.OnQueryTextListener) this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_list_all) {
            mAdapter.deleteAll();
            Toast.makeText(this, R.string.deleted_all, Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.add_list) {
            Toast.makeText(this, R.string.go_yotube_menu, Toast.LENGTH_SHORT).show();
            Intent it = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
            startActivity(it);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        if (item.getItemId() == R.id.info_menu) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.re_dec) {
            Intent intent  = new Intent(this,OnboardingActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // RecyclerView search to result in real time.
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query != null) {
            searchDatabase(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (query != null) {
            searchDatabase(query);
        }
        return true;
    }

    private void searchDatabase(String query) {
        String searchQuery = "%" + query + "%";
        db.bookDao().searchDb(searchQuery).observe(this, bookEntities -> mAdapter.setItemAdapterList(bookEntities));

    }
}