package my.edu.utar.individual;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class Rule extends AppCompatActivity {

    MediaPlayer bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        bgm = MediaPlayer.create(getApplicationContext(), R.raw.title_theme);
        bgm.start();
        bgm.setLooping(true);
    }

    public void backMainMenu (View view) {
        Intent intent = new Intent(this, MainActivity.class);
        bgm.stop();
        bgm.release();
        this.startActivity(intent);
        ((Activity) this).finish();
    }
}