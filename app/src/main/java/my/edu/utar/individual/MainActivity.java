package my.edu.utar.individual;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    MediaPlayer bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Keep the screen open
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        bgm = MediaPlayer.create(getApplicationContext(), R.raw.title_theme);
        bgm.start();
        bgm.setLooping(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startGame(View view) {
        GameView gameView = new GameView(this);
        setContentView(gameView);
        bgm.stop();
        bgm.release();
    }

    public void checkRule (View view) {
        Intent intent = new Intent(this, Rule.class);
        bgm.stop();
        bgm.release();
        this.startActivity(intent);
        ((Activity) this).finish();
    }

    public void quitGame(View view) {
        bgm.stop();
        bgm.release();
        System.exit(0);
    }
}