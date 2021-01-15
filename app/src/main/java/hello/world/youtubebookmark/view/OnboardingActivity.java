package hello.world.youtubebookmark.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import hello.world.youtubebookmark.R;
import hello.world.youtubebookmark.adapter.CustomAdapter;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.CirclePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.FullscreenPromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.ImmersiveModeCompatPromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.CirclePromptFocal;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class OnboardingActivity extends AppCompatActivity {
    private CardView card;
    private LinearLayout settingView, addMenu, mainYoutube, contentYoutube,  shareApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolBar);

        card = findViewById(R.id.card);
        settingView = findViewById(R.id.setting_view);
        addMenu = findViewById(R.id.add_menu);
        mainYoutube = findViewById(R.id.main_youtube);
        contentYoutube = findViewById(R.id.content_youtube);
        shareApp = findViewById(R.id.share_app);

        showAddListButton();
    }

    private void StartActivity() {
        Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showAddListButton() {
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.add_menu)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.add_btn)
                .setSecondaryText(R.string.show_add_list_btn)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new CirclePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            addMenu.setVisibility(View.GONE);
                            mainYoutube.setVisibility(View.VISIBLE);
                            showMainYoutube();
                        }
                    }
                })
                .show();
    }

    private void showMainYoutube() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.main_youtube)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_main_youtube_youtube)
                .setSecondaryText(R.string.show_main_youtube)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            contentYoutube.setVisibility(View.VISIBLE);
                            mainYoutube.setVisibility(View.GONE);
                            showContentYoutube();
                        }
                    }
                })
                .show();
    }

    private void showContentYoutube() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.content_youtube)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_content_view_youtube_pr)
                .setSecondaryText(R.string.show_content_youtube_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            showShareButton();
                        }
                    }
                })
                .show();
    }

    private void showShareButton() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.select_share)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_share_button_pr)
                .setSecondaryText(R.string.show_share_button_se)
                .setPromptBackground(new CirclePromptBackground())
                .setPromptFocal(new CirclePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            contentYoutube.setVisibility(View.GONE);
                            shareApp.setVisibility(View.VISIBLE);
                            showShareApp();
                        }
                    }
                })
                .show();
    }

    private void showShareApp() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.share_app)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_share_app_pr)
                .setSecondaryText(R.string.show_share_app_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            shareApp.setVisibility(View.GONE);
                            settingView.setVisibility(View.VISIBLE);
                            showSettingView();
                        }
                    }
                })
                .show();
    }

    private void showSettingView() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.setting_view)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_setting_view_pr)
                .setSecondaryText(R.string.show_setting_view_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            showThumbAlert();
                        }
                    }
                })
                .show();
    }

    private void showThumbAlert() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.alert_img)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_thumb_alert_pr)
                .setSecondaryText(R.string.show_thumb_alert_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            alertTitle();
                        }
                    }
                })
                .show();
    }

    private void alertTitle() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.et_title)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.alert_title_pr)
                .setSecondaryText(R.string.alert_title_sec)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            etHour();
                        }
                    }
                })
                .show();
    }

    private void etHour() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.et_hour)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.et_hour_pr)
                .setSecondaryText(R.string.et_hour_se)
                .setPromptBackground(new CirclePromptBackground())
                .setPromptFocal(new CirclePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            etMin();
                        }
                    }
                })
                .show();
    }

    private void etMin() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.et_min)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.et_min_pr)
                .setSecondaryText(R.string.et_min_se)
                .setPromptBackground(new CirclePromptBackground())
                .setPromptFocal(new CirclePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            etSec();
                        }
                    }
                })
                .show();
    }

    private void etSec() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.et_sec)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.et_sec_pr)
                .setSecondaryText(R.string.et_sec_se)
                .setPromptBackground(new CirclePromptBackground())
                .setPromptFocal(new CirclePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            addListBtn();
                        }
                    }
                })
                .show();
    }

    private void addListBtn() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.add_list_btn)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.add_btn)
                .setSecondaryText(R.string.add_list_btn_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            settingView.setVisibility(View.GONE);
                            card.setVisibility(View.VISIBLE);
                            showCard();
                        }
                    }
                })
                .show();
    }

    private void showCard() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.card)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_card_pr)
                .setSecondaryText(R.string.show_card_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            showButton();
                        }
                    }
                })
                .show();
    }

        private void showButton() {
            new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.thumb_nail)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_button_pr)
                .setSecondaryText(R.string.show_button_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            showTitle();
                        }
                    }
                })
                .show();
    }

    private void showTitle() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.list_title)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_title_pr)
                .setSecondaryText(R.string.show_title_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            showTime();
                        }
                    }
                })
                .show();
    }

    private void showTime() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.save_time)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_time_pr)
                .setSecondaryText(R.string.show_time_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            showStartButton();
                        }
                    }
                })
                .show();
    }

    private void showStartButton() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.start_link)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_start_button_pr)
                .setSecondaryText(R.string.show_start_button_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            showLike();
                        }
                    }
                })
                .show();
    }

    private void showLike() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.list_like)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_like_pr)
                .setSecondaryText(R.string.show_like_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            showRemove();
                        }
                    }
                })
                .show();
    }

    private void showRemove() {
        new MaterialTapTargetPrompt.Builder(this).setTarget(R.id.remove)
                .setBackgroundColour(getResources().getColor(R.color.purple_500))
                .setPrimaryText(R.string.show_remove_pr)
                .setSecondaryText(R.string.show_remove_se)
                .setPromptBackground(new RectanglePromptBackground().setCornerRadius(33.0f, 33.0f))
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            card.setVisibility(View.GONE);
                            settingView.setVisibility(View.VISIBLE);
                            StartActivity();
                            finish();
                        }
                    }
                })
                .show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}