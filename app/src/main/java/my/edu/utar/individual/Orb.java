package my.edu.utar.individual;


// Java Class to Manage the Orb

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Orb {
    Bitmap orb[] = new Bitmap[3];
    Bitmap a[] = new Bitmap[1];
    int orbFrame = 0;
    int orbX, orbY;
    boolean light = false;
    Random random;

    // Constructor for orb class
    public Orb (Context context) {
        orb[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.purpleorb1);
        orb[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.purpleorb2);
        orb[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.purpleorb4);
        random = new Random();
        appear();
    }

    public Bitmap getOrb (int orbFrame) {
        return orb[orbFrame];
    }

    public int getOrbWidth () {
        int Highest = 0;
        for (int i = 0; i < 3; i++) {
            if (orb[i].getWidth() > Highest)
                Highest = orb[i].getWidth();
        }
        return Highest;
    }

    public int getOrbHeight () {
        int Highest = 0;
        for (int i = 0; i < 3; i++) {
            if (orb[i].getHeight() > Highest)
                Highest = orb[i].getHeight();
        }
        return Highest;
    }

    public void highlight (Context context) {
        orb[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blueorb1);
        orb[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blueorb2);
        orb[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blueorb4);
        light = true;
    }

    public void unhighlight (Context context) {
        orb[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.purpleorb1);
        orb[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.purpleorb2);
        orb[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.purpleorb4);
        light = false;
    }

    public void appear () {
        orbX = random.nextInt(GameView.dWidth - getOrbWidth());
        orbY = random.nextInt(GameView.dHeight - getOrbHeight() - 300) + 300;
    }
}
