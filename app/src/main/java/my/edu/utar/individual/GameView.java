package my.edu.utar.individual;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;


public class GameView extends View {

    static MediaPlayer bgm;
    MediaPlayer fireEffect;
    Bitmap background;
    Bitmap pause;
    Rect rectBackground;
    Rect button;
    static Context context;
    Handler handler;
    Runnable runnable;
    final long UPDATE_MILLIS = 250;
    Paint pointsRecord = new Paint(), currentStage = new Paint(),  remainingTime = new Paint();
    Paint paint = new Paint();
    Paint paintOverlay = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint continueText = new Paint();
    Paint yesButton = new Paint(), noButton = new Paint();
    Paint resumeButton = new Paint(), backButton = new Paint();
    float TEXT_SIZE = 100;
    int points = 0;
    int stage = 1;
    double count = 0;
    int remaining = 5;
    Boolean nextLevelControl = true;
    Boolean continueControl = true;
    static int dWidth, dHeight;
    ArrayList<Orb> orbs;
    int countdown[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 21, 22, 23, 24, 25, 26, 27, 28, 29};


    // Constructor matching super
    @RequiresApi(api = Build.VERSION_CODES.O)
    public GameView (Context context) {
        super(context);
        this.context = context;

        bgm = MediaPlayer.create(context, R.raw.the_icy_cave);
        bgm.start();
        bgm.setLooping(true);
        fireEffect = MediaPlayer.create(context, R.raw.fire_effect1);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background8);
        pause = BitmapFactory.decodeResource(getResources(), R.drawable.pause3);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();

        // get the x and y of the screen
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;

        // rectangle of background & pause button
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        button = new Rect(dWidth-350, 120, dWidth-30, 250);

        // handler and runnable
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                count += 0.25;
                countDown();
            }
        };

        // set the paint
        pointsRecord.setColor(Color.rgb(255, 225, 53));
        pointsRecord.setTextSize(TEXT_SIZE);
        pointsRecord.setTextAlign(Paint.Align.LEFT);
        pointsRecord.setTypeface(ResourcesCompat.getFont(context, R.font.kenney_pixel));
        currentStage.setColor(Color.rgb(255, 225, 53));
        currentStage.setTextSize(TEXT_SIZE + 10);
        currentStage.setTextAlign(Paint.Align.LEFT);
        currentStage.setTypeface(ResourcesCompat.getFont(context, R.font.kenney_pixel));
        remainingTime.setColor(Color.rgb(255, 26, 0));
        remainingTime.setTextSize(TEXT_SIZE * 3);
        remainingTime.setTextAlign(Paint.Align.CENTER);
        remainingTime.setTypeface(ResourcesCompat.getFont(context, R.font.kenney_pixel));
        paint.setColor(Color.parseColor("#336699CC"));
        paintOverlay.setColor(Color.BLACK);
        paintOverlay.setARGB(90, 0, 0, 0);

        // set the paint for the next level pause menu and pause menu
        continueText.setColor(Color.WHITE);
        continueText.setTextSize(TEXT_SIZE*2);
        continueText.setTextAlign(Paint.Align.CENTER);
        continueText.setTypeface(ResourcesCompat.getFont(context, R.font.kenney_pixel));
        yesButton.setColor(Color.WHITE);
        yesButton.setTextSize(TEXT_SIZE + 20);
        yesButton.setTypeface(ResourcesCompat.getFont(context,R.font.kenney_pixel));
        noButton.setColor(Color.RED);
        noButton.setTextSize(TEXT_SIZE + 20);
        noButton.setTypeface(ResourcesCompat.getFont(context,R.font.kenney_pixel));
        resumeButton.setColor(Color.WHITE);
        resumeButton.setTextSize(TEXT_SIZE + 30);
        resumeButton.setTypeface(ResourcesCompat.getFont(context,R.font.kenney_blocks));
        resumeButton.setTextAlign(Paint.Align.CENTER);
        backButton.setColor(Color.RED);
        backButton.setTextSize(TEXT_SIZE + 30);
        backButton.setTypeface(ResourcesCompat.getFont(context,R.font.kenney_blocks));
        backButton.setTextAlign(Paint.Align.CENTER);

        orbs = new ArrayList<>();
        upLevel(stage);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(pause, null, button, null);

        // generate the orb randomly with one highlight
        for (int i = 0; i < orbs.size() ; i++) {
            canvas.drawBitmap(orbs.get(i).getOrb(orbs.get(i).orbFrame),
                    orbs.get(i).orbX, orbs.get(i).orbY, null);
            orbs.get(i).orbFrame++;

            // Animation of orbFrame
            if(orbs.get(i).orbFrame > 2) {
                orbs.get(i).orbFrame = 0; }
        }

        // draw the text
        canvas.drawRect(0, 0, dWidth,300, paint);
        canvas.drawText("Points: " + points, 20, TEXT_SIZE + 20, pointsRecord);
        canvas.drawText("Stage: " + stage, 20, (TEXT_SIZE + 20) * 2, currentStage);
        canvas.drawText("" + remaining, dWidth/2, (TEXT_SIZE + 20) * 2 , remainingTime);


        // execute when every stage is finish
        if (nextLevelControl == false ) {
            canvas.drawRect(0, 0, dWidth, dHeight, paintOverlay);

            canvas.drawText("Continue?", (dWidth)/2, (dHeight)/2 -
                    (continueText.descent() + continueText.descent()/2), continueText);
            // distance from the baseline to center

            canvas.drawText("YES", dWidth/4, (dHeight)/1.5f -
                    (yesButton.descent() + yesButton.descent()/1.5f), yesButton);
            canvas.drawText("NO", dWidth/1.5f, (dHeight)/1.5f -
                    (noButton.descent() + noButton.descent()/1.5f), noButton);
            }
        // execute when the user select the pause button
        if (continueControl == false) {
            canvas.drawRect(0, 0, dWidth, dHeight, paintOverlay);

            canvas.drawText("RESUME", (dWidth)/2, (dHeight)/3 -
                    (resumeButton.descent() + resumeButton.descent()/3*2), resumeButton);
            canvas.drawText("QUIT", (dWidth)/2, (dHeight)/3*2 -
                    (backButton.descent() + backButton.descent()/3*2), backButton);
        }
            handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (button.contains((int)touchX, (int)touchY)) {
                pauseControl(2); }

            // custom onClickAction after user select want to continue or not
            if (nextLevelControl == false) {

                if( touchX >= dWidth/4 - 100
                        && touchX <= dWidth/4 + 100
                        && touchY >= (dHeight/1.5f - (yesButton.descent() + yesButton.descent()/1.5f) * 2) - 100
                        && touchY <= (dHeight/1.5f - (yesButton.descent() + yesButton.descent()/1.5f) * 2) + 100) {
                    nextLevelControl = true;
                    remaining = 5;
                    stage++;
                    upLevel(stage);
                    invalidate(); }
                else if ( touchX >= dWidth/1.5f - 100
                        && touchX <= dWidth/1.5f + 100
                        && touchY >= (dHeight/1.5f - (noButton.descent() + noButton.descent()/1.5f) * 2) - 100
                        && touchY <= (dHeight/1.5f - (noButton.descent() + noButton.descent()/1.5f) * 2) + 100) {
                    Intent intent = new Intent(context, MainActivity.class);
                    bgm.stop();
                    bgm.release();
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }

            // custom onClickAction after user make selection in pauseMenu
            if (continueControl == false) {

                if (touchX >= dWidth/2 - 150
                        && touchX <= dWidth/2 + 150
                        && touchY >= (dHeight/3 - (resumeButton.descent() + resumeButton.descent()/1.5f) /3) - 150
                        && touchY <= (dHeight/3 - (resumeButton.descent() + resumeButton.descent()/1.5f) /3) + 150) {
                    continueControl = true;
                    invalidate();
                }
                else if (touchX >= dWidth/2 - 150
                        && touchX <= dWidth/2 + 150
                        && touchY >= (dHeight/3*2 - (backButton.descent() + backButton.descent()/1.5f) /3*2) - 150
                        && touchY <= (dHeight/3*2 - (backButton.descent() + backButton.descent()/1.5f) /3*2) + 150) {
                    Intent intent = new Intent(context, MainActivity.class);
                    bgm.stop();
                    bgm.release();
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }

        // when touch the highlighted orb, user will get the point
        for (int i = 0; i < orbs.size() ; i++) {
            if (orbs.get(i).orbX <= touchX
                    && orbs.get(i).orbX + orbs.get(i).getOrbWidth() >= touchX
                    && orbs.get(i).orbY <= touchY
                    && orbs.get(i).orbY + orbs.get(i).getOrbHeight() >= touchY
                    && orbs.get(i).light == true
                    && (nextLevelControl == true && continueControl == true)
            ) {
                fireEffect.start();
                // highlight nextItem
                orbs.get(i).unhighlight(context);
                if (i + 1 != orbs.size()) {
                    orbs.get(i + 1).highlight(context);
                } else {
                    orbs.get(0).highlight(context);
                }
                // get the points
                points = points + 1;
            }
        }

        return true;
    }

    // increase the number of orbs according to the stage
    public void upLevel (int currentStage) {
        // remain the unselected highlight orb

        for (int i = 0; i < orbs.size(); i++) {
            if (orbs.get(i).light == false) {
                orbs.remove(i);
                i--;
            }
        }

        switch (currentStage)
        {
            case 1: {
                for (int i = 0; i < 4; i++) {
                    Orb orb = new Orb(context);
                    orbs.add(orb); }
                orbs.get(0).highlight(context);
            }
            break;
            case 2: {
                for (int i = 0; i < 8; i++) {
                    Orb orb = new Orb(context);
                    orbs.add(orb); }
            }
            break;
            case 3: {
                for (int i = 0; i < 15; i++) {
                    Orb orb = new Orb(context);
                    orbs.add(orb); }
            }
            break;
            case 4: {
                for (int i = 0; i < 24; i++) {
                    Orb orb = new Orb(context);
                    orbs.add(orb); }
            }
            break;
            case 5: {
                for (int i = 0; i < 35; i++) {
                    Orb orb = new Orb(context);
                    orbs.add(orb); }
            }
            break;
            default: {
            }
        }
    }

    public void countDown () {

        // reduce the second by 1 if match the result
        for (int i = 0; i < countdown.length; i++) {
            if (count == countdown[i]) {
                remaining--;
            }
        }

        // after level 5 finish, bring the result and go the result view
        if (count == 25 + 5) {
            bgm.stop();
            bgm.release();
            Intent intent = new Intent(context, GameResult.class);
            intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
        // after every 5 second,
        else if(count == 5 + 1
                || count == 10 + 2 || count == 15 + 3 || count == 20 + 4) {
            stage++;
            pauseControl(1);
        }

        if(nextLevelControl == true && continueControl == true) {
            invalidate();
        }
    }

    public void pauseControl (int i) {

        if (i == 1) {
            nextLevelControl = false;
            remaining = 0;
            stage--;
            invalidate();
        }
        if (i == 2) {
            continueControl = false;
            invalidate();
        }

    }
}