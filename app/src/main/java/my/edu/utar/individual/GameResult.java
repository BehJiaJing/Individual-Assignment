package my.edu.utar.individual;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;


public class GameResult extends AppCompatActivity implements player_name_dialog.ExampleDialogListener {

    // global variable
    MediaPlayer bgm;
    ImageButton exitButton;
    ImageButton replayButton;
    TextView tvPoints;
    TextView tvHighest;
    static final int maxRow = 25;
    int highest;
    static int points;
    ImageView ivNewHighest;
    static TableRow ranking[] = new TableRow[maxRow];
    static TextView rankingName[] = new TextView[maxRow];
    static TextView rankingPoint[] = new TextView[maxRow];
    static SharedPreferences sharedPreferences;
    public static Context context;
    public static int rankingPointList [] = new int[25];
    public static String rankingNameList [] = new String[25];
    static ArrayList<Integer> rankingPointList_finalize = new ArrayList<Integer>();
    static ArrayList<String> rankingNameList_finalize = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        context = this;
        initialize();

        // play the bgm
        bgm = MediaPlayer.create(getApplicationContext(), R.raw.the_final_of_the_fantasy);
        bgm.start();
        bgm.setLooping(true);

        // ask player to input the name and update the ranking
        openDialog();

        // get the points data from GameView
        points = getIntent().getExtras().getInt("points");

        // Initialize the sharedPreferences
        sharedPreferences = getSharedPreferences("my_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        setFieldsFromSavedData();

        // Update the highest marks
        tvPoints.setText("" + points);
        if (points > highest) {

            // make the trophy visible
            ivNewHighest.setVisibility(View.VISIBLE);
            highest = points;

            editor.putInt("highest", highest);
            editor.commit();
        }
        tvHighest.setText("" + highest);

        replayButton.setOnClickListener(replayOnClick);
        exitButton.setOnClickListener(exitOnClick);
    }

    protected View.OnClickListener replayOnClick = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            GameView gameView = new GameView(GameResult.this);
            setContentView(gameView);
            bgm.stop();
            bgm.release();
        }
    };

    protected View.OnClickListener exitOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            System.exit(0);
        }
    };

    public void openDialog() {
        player_name_dialog playerNameDialog = new player_name_dialog();
        playerNameDialog.show(getSupportFragmentManager(), "dialog");
    }

    public void initialize() {
        exitButton = findViewById(R.id.exitButton);
        replayButton = findViewById(R.id.replayButton);
        tvPoints = findViewById(R.id.tvPoints);
        tvHighest = findViewById(R.id.tvHighest);
        ivNewHighest = findViewById(R.id.ivNewHighest);

        for (int i = 0; i < 25; i++) {
            Resources res = getResources();
            int rankingID = res.getIdentifier("ranking" + (i+1), "id", getApplicationContext().getPackageName());
            int rankingNameID = res.getIdentifier("ranking" + (i+1) + "Name", "id", getApplicationContext().getPackageName());
            int rankingPointID = res.getIdentifier("ranking" + (i+1) + "Point", "id", getApplicationContext().getPackageName());
            ranking[i] = findViewById(rankingID);
            rankingName[i] = findViewById(rankingNameID);
            rankingPoint[i] = findViewById(rankingPointID);
        }

    }

    public void setFieldsFromSavedData() {
        highest = sharedPreferences.getInt("highest", 0);

        // get the point of rankingList
        // get the name of rankingList
        rankingNameList_finalize.clear();
        rankingPointList_finalize.clear();

        // retrieve the data from sharedPreferences
        for (int i = 0; i < 25; i++) {
            rankingPointList[i] = sharedPreferences.getInt("ranking" + (i+1) + "Point", 0);
            rankingNameList[i] = sharedPreferences.getString("ranking" + (i+1) + "Name", "" );

            if (rankingPointList[i] != 0) {
                rankingPointList_finalize.add(rankingPointList[i]);
            }
            if (!rankingNameList[i].equals("")) {
                rankingNameList_finalize.add(rankingNameList[i]);
            }
        }
    }

    public static void sorting() {

        // temp of Point and Name
        int tempPoint;
        int i = rankingPointList_finalize.size() - 1;
        String tempName;

        if (rankingPointList_finalize.size() != 1) {
            for (int j = rankingPointList_finalize.size() - 2; j >= 0; j--) {
                if (rankingPointList_finalize.get(i) > rankingPointList_finalize.get(j)) {
                    tempPoint = rankingPointList_finalize.get(j);
                    rankingPointList_finalize.set(j, rankingPointList_finalize.get(i)) ;
                    rankingPointList_finalize.set(i, tempPoint);

                    tempName = rankingNameList_finalize.get(j);
                    rankingNameList_finalize.set(j, rankingNameList_finalize.get(i));
                    rankingNameList_finalize.set(i, tempName);

                    i--;
                }
            }


            while (rankingPointList_finalize.size() > 25) {
                rankingPointList_finalize.remove(rankingPointList_finalize.size()-1);
                rankingNameList_finalize.remove(rankingNameList_finalize.size()-1);
            }
        }

    }

    public static void updateRanking(Context context) {

        sharedPreferences = context.getSharedPreferences("my_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i <rankingPointList_finalize.size(); i++) {
            ranking[i].setVisibility(View.VISIBLE);
            rankingName[i].setText("" + rankingNameList_finalize.get(i));
            rankingPoint[i].setText("" + rankingPointList_finalize.get(i));

            editor.putString("ranking" + (i+1) + "Name", rankingNameList_finalize.get(i));
            editor.putInt("ranking" + (i+1) + "Point", rankingPointList_finalize.get(i));
        }
        editor.commit();
    }
}