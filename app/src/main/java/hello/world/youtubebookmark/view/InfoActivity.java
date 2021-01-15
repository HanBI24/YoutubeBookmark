package hello.world.youtubebookmark.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense21;
import de.psdev.licensesdialog.licenses.License;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;
import hello.world.youtubebookmark.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView mLicense = (TextView)findViewById(R.id.license);
        TextView mIcon = (TextView)findViewById(R.id.use_icon);

        mLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Notices notices = new Notices();
                notices.addNotice(new Notice("Picasso", "https://github.com/square/picasso", "Copyright 2013 Square, Inc.", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("CircleImageView", "https://github.com/hdodenhof/CircleImageView", "Copyright 2014 - 2020 Henning Dodenhof", new ApacheSoftwareLicense20()));
                new LicensesDialog.Builder(InfoActivity.this)
                        .setNotices(notices)
                        .setIncludeOwnLicense(true)
                        .build()
                        .show();
            }
        });

        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
                View infoView = LayoutInflater.from(InfoActivity.this).inflate(R.layout.icon_font_info, null, false);
                builder.setTitle("정보").setView(infoView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}